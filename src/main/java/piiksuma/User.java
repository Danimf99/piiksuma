package piiksuma;

import piiksuma.database.MapperColumn;
import piiksuma.database.MapperTable;

import java.sql.Timestamp;
import java.util.Objects;

@MapperTable(nombre = "piiUser")
public class User {
    @MapperColumn(pkey = true)
    private String email;
    @MapperColumn
    private String name;
    @MapperColumn
    private String id;
    @MapperColumn
    private String pass;
    @MapperColumn
    private String gender;
    @MapperColumn(columna = "description")
    private String bio;
    @MapperColumn(columna = "home")
    private String direction;
    @MapperColumn(columna = "postalCode")
    private String postCode;
    @MapperColumn(columna = "province")
    private String state;
    @MapperColumn
    private String country;
    @MapperColumn
    private String city;
    @MapperColumn(columna = "birthPlace")
    private String birthPlace;
    @MapperColumn(columna = "birthdate")
    private Timestamp birthday;
    @MapperColumn(columna = "registrationDate", hasDefault = true)
    private Timestamp registrationDate;
    @MapperColumn(columna = "deathdate")
    private Timestamp deadDate;
    @MapperColumn
    private String religion;
    @MapperColumn(columna = "emotionalSituation")
    private String loveStatus;
    @MapperColumn
    private String job;
    private UserType type;

    public User(String name, String id, String email) {
        this.name = name;
        this.id = id;
        this.email = email;
        this.type = UserType.user;
    }

    /**
     * Constructor for testing -> removeUser
     *
     * @param name
     * @param id
     * @param email
     * @param type  Test if user is of type Admin
     */
    public User(String id, String name, String email, String pass, UserType type) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.pass = pass;
        this.type = type;
        this.birthday = new Timestamp(System.currentTimeMillis());

    }

    /**
     * A empty constructor is needed to be created by reflexion
     */

    public User() {
    }


    public String getNombre() {
        return name;
    }

    public void setNombre(String name) {
        this.name = name;
    }

    public UserType getType() {
        return type;
    }

    public void setType(UserType type) {
        this.type = type;
    }

    public String getIdUsuario() {
        return id;
    }

    public void setIdUsuario(String id) {
        this.id = id;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getPass() {
        return pass;
    }

    public void setPass(String pass) {
        this.pass = pass;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getBio() {
        return bio;
    }

    public void setBio(String bio) {
        this.bio = bio;
    }

    public String getDirection() {
        return direction;
    }

    public void setDirection(String direction) {
        this.direction = direction;
    }

    public String getPostCode() {
        return postCode;
    }

    public void setPostCode(String postCode) {
        this.postCode = postCode;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getBirthPlace() {
        return birthPlace;
    }

    public void setBirthPlace(String birthPlace) {
        this.birthPlace = birthPlace;
    }

    public Timestamp getBirthday() {
        return birthday;
    }

    public void setBirthday(Timestamp birthday) {
        this.birthday = birthday;
    }

    public Timestamp getRegistrationDate() {
        return registrationDate;
    }

    public void setRegistrationDate(Timestamp registrationDate) {
        this.registrationDate = registrationDate;
    }

    public Timestamp getDeadDate() {
        return deadDate;
    }

    public void setDeadDate(Timestamp deadDate) {
        this.deadDate = deadDate;
    }

    public String getReligion() {
        return religion;
    }

    public void setReligion(String religion) {
        this.religion = religion;
    }

    public String getLoveStatus() {
        return loveStatus;
    }

    public void setLoveStatus(String loveStatus) {
        this.loveStatus = loveStatus;
    }

    public String getJob() {
        return job;
    }

    public void setJob(String job) {
        this.job = job;
    }

    /**
     * Function to check that the attributes with restriction 'not null' are not null
     *
     * @return the function return "true" if the attributes are not null, otherwise return "false"
     */
    public boolean checkNotNull() {
        // Check that the primary keys are not null
        if (!checkPrimaryKey()) {
            return false;
        }

        // Check the attributes with restriction 'not null'
        if (getPass() == null || getPass().isEmpty()) {
            return false;
        }

        if (getBirthday() == null) {
            return false;
        }

        return getRegistrationDate() != null;
    }

    /**
     * Function to check that the primary keys are not null
     *
     * @return the function return "true" if the primary keys are not null, otherwise return "false"
     */
    public boolean checkPrimaryKey() {
        // Check that the primary keys are not null
        return getEmail() != null && !getEmail().isEmpty();

    }

    @Override
    public String toString() {
        return "User{" +
                "id='" + id + '\'' +
                ", pass='" + pass + '\'' +
                ", type='" + this.type + '\'' +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        User user = (User) o;
        return email.equals(user.email);
    }

    @Override
    public int hashCode() {
        return Objects.hash(email);
    }
}
