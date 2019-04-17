package piiksuma.database;

import piiksuma.exceptions.PiikDatabaseException;

import java.lang.reflect.Field;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.*;
import java.util.regex.Matcher;


/**
 * Database conection and data insertion wrapper. Performs insertions from
 * given objects without actually specifying SQL sentences.
 *
 * @param <E> Mapped class type. Used to check asigments when returning query
 *            results.
 * @author luastan
 * @author CardamaS99
 * @author danimf99
 * @author alvrogd
 * @author OswaldOswin1
 * @author Marcos-marpin
 */
public class InsertionMapper<E> extends Mapper<E> {
    private List<E> insertions;
    private String query;
    private ArrayList<String> columnas;
    private HashMap<String, Field> atributos;


    public InsertionMapper(Connection conexion) {
        super(conexion);
        insertions = new ArrayList<>();
        query = "";
        columnas = new ArrayList<>();
        atributos = new HashMap<>();
    }


    /**
     * Defines the Class of the insertions
     *
     * @param mappedClass Class corresponding the elements to be inserted
     * @return The IsertionMapper instance
     */
    @Override
    public InsertionMapper<E> defineClass(Class<? extends E> mappedClass) {
        super.defineClass(mappedClass);
        return this;
    }

    /**
     * Adds an object to be inserted
     *
     * @param objeto Object to be inserted
     * @return The current InsertionMapper instance
     */
    public InsertionMapper<E> add(E objeto) {
        insertions.add(objeto);
        return this;
    }


    /**
     * Adds multiple objects to the insertion tool
     *
     * @param objetos Objetos que se quieren insertar
     * @return El propio insertionMapper
     */
    public InsertionMapper<E> addAll(E... objetos) {
        insertions.addAll(Arrays.asList(objetos));
        return this;
    }


    /**
     * Extracts the atributes and fields to be inserted into the database and
     * generates the corresponding SQL sentence base for the insertions
     */
    private void prepareQuery() {
        String nombreColumna;
        StringBuilder queryBuilder = new StringBuilder("INSERT INTO ")
                .append(mappedClass.getAnnotation(MapperTable.class).nombre()).append("(");

        // Fetches all the fields to be mapped
        for (Field field : mappedClass.getDeclaredFields()) {
            field.setAccessible(true);
            if (field.isAnnotationPresent(MapperColumn.class) && !field.getAnnotation(MapperColumn.class).hasDefault()) {
                nombreColumna = field.getAnnotation(MapperColumn.class).columna();
                nombreColumna = nombreColumna.equals("") ? field.getName() : nombreColumna;
                queryBuilder.append(nombreColumna).append(",");
                this.columnas.add(nombreColumna);
                this.atributos.put(nombreColumna, field);
            }
        }
        queryBuilder.deleteCharAt(queryBuilder.length() - 1);   // Deletes last comma ","
        queryBuilder.append(") VALUES (?");
        for (int i = 0; i < this.atributos.size() - 1; i++) {   // as there's a ? before, it's necessary to decrement the
            // size by one
            queryBuilder.append(",?");
        }
        queryBuilder.append(");");
        query = queryBuilder.toString();
    }


    /**
     * Inserts all the given elements into the database
     */
    public void insert() throws PiikDatabaseException {
        /*
        PreparedStatement statement = null;
        // Pepares the query
        prepareQuery();
        try {
            for (E insercion : this.insertions) {
                statement = connection.prepareStatement(this.query);    // Instances the preparedStatement
                for (int i = 0; i < this.columnas.size(); i++) {        // Inserts the data
                    Object object = this.atributos.get(this.columnas.get(i)).get(insercion);
                    if (object != null && this.atributos.get(this.columnas.get(i)).get(insercion).getClass().
                            isAnnotationPresent(MapperTable.class)) {
                        statement.setObject(i + 1, fkValue(this.atributos.get(this.columnas.get(i)).get(insercion)));
                    } else {
                        statement.setObject(i + 1, this.atributos.get(this.columnas.get(i)).get(insercion));
                    }
                }
                // Ejecutar
                statement.execute();
            }
        } catch (SQLException e) {
            System.out.println("SQL movida");
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }

         */
        Map<String, Object> insertion;
        Class fieldClass;
        String columnName;
        Matcher matcher;
        Object atrib;
        String extColumn;
        try {
            for (E element : this.insertions) {
                insertion = new HashMap<>();
                for (Field field : this.mappedClass.getDeclaredFields()) {
                    if (field.isAnnotationPresent(MapperColumn.class)) {
                        field.setAccessible(true);
                        fieldClass = field.getAnnotation(MapperColumn.class).targetClass();
                        if (fieldClass == Object.class) {
                            // Normal field
                            columnName = field.getAnnotation(MapperColumn.class).columna();
                            columnName = columnName.equals("") ? field.getName() : columnName;
                            atrib = field.get(element);
                            if (field.getAnnotation(MapperColumn.class).hasDefault() && atrib == null) {
                                insertion.put(columnName, new Mapper.DEFAULT());
                            } else {
                                insertion.put(columnName, atrib);
                            }
                        } else {
                            // Foreign keys
                            if (field.get(element) != null) {
                                matcher = regexFKeys.matcher(field.getAnnotation(MapperColumn.class).fKeys());
                                while (matcher.find()) {
                                    for (Field extField : fieldClass.getDeclaredFields()) {
                                        if (extField.isAnnotationPresent(MapperColumn.class)) {
                                            extField.setAccessible(true);
                                            extColumn = extField.getAnnotation(MapperColumn.class).columna();
                                            extColumn = extColumn.equals("") ? extField.getName() : extColumn;
                                            if (extColumn.equals(matcher.group(2))) {
                                                System.out.println(matcher.group(0));
                                                insertion.put(matcher.group(1), extField.get(field.get(element)));
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
                this.customInsertion(insertion, mappedClass.getAnnotation(MapperTable.class).nombre());
            }
        } catch (IllegalAccessException ex) {
            throw new PiikDatabaseException("Unable to map the insertion");
        }

    }


    /**
     * Generates a Map to be used as a template in custom insertions performed
     * by the custom insertion mapper.
     *
     * @return Map template
     */
    public static Map<String, Object> customInsertionTemplate() {
        HashMap<String, Object> insertionTemplate = new HashMap<>();

        insertionTemplate.put("table", "");
        insertionTemplate.put("set", new ArrayList<Map<String, Object>>());
        insertionTemplate.put("where", new ArrayList<Map<String, Object>>());

        return insertionTemplate;
    }

    /**
     * Automatically inserts values indicated by a list containing maps with
     * the following structure:
     * <p>
     * [
     * {
     * "Column name" : "value"
     * },
     * {
     * "Column name that has an Integer" : 445
     * },
     * {
     * "Column name that has a default value" : new Mapper.DEFAULT()
     * }
     * ]
     *
     * @param insertions Map list to be used as a guide to perform the
     *                   insertions
     */
    public void customInsertion(List<Map<String, Object>> insertions, String table) throws PiikDatabaseException {
        for (Map<String, Object> insertion : insertions) {
            customInsertion(insertion, table);
        }
    }

    public void customInsertion(Map<String, Object> insertion, String table) throws PiikDatabaseException {
        /*
         *
         * INSERT INTO post (author, id, text, publicationDate, sugarDaddy, authorDaddy, multimedia)
         * values ('id2', 'post2', 'Respuesta', default, 'post2', 'id2', null)
         *
         * */

        ArrayList<Object> params = new ArrayList<>();
        StringBuilder insertionBuilder = new StringBuilder("INSERT INTO ");
        StringBuilder valueBuilder = new StringBuilder("values (");
        insertionBuilder.append(table).append(" (");

        insertion.forEach((s, o) -> {
            if (o instanceof Mapper.DEFAULT) {
                insertionBuilder.append(s).append(",");
                valueBuilder.append("default,");
            } else {
                insertionBuilder.append(s).append(",");
                valueBuilder.append("?,");
                params.add(o);
            }

        });
        insertionBuilder.delete(insertionBuilder.length() - 1, insertionBuilder.length()).append(") ");
        valueBuilder.delete(valueBuilder.length() - 1, valueBuilder.length()).append(")");

        try {
            PreparedStatement statement = connection.prepareStatement(insertionBuilder.toString() + valueBuilder.toString());
            for (int i = 0; i < params.size(); i++) {
                statement.setObject(i + 1, params.get(i));
            }
            statement.execute();
        } catch (SQLException sql) {
            sql.printStackTrace();
            throw new PiikDatabaseException(sql.getMessage());
        }

    }


}
