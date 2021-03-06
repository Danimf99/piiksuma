package piiksuma;

import piiksuma.api.MultimediaType;
import piiksuma.database.MapperColumn;
import piiksuma.database.MapperTable;

import java.util.Objects;

@MapperTable(nombre = "multimedia")
public class Multimedia extends PiikObject {

    /* Attributes */
    @MapperColumn(pkey = true)
    private String hash;
    @MapperColumn(notNull = true)
    private String resolution;
    @MapperColumn(notNull = true)
    private String uri;
    private MultimediaType type;


    /* Constructor */

    public Multimedia() {
    }

    public Multimedia(String hash, String resolution, String uri) {
        if (hash != null) {
            this.hash = hash;
        } else {
            this.hash = "";
        }

        if (resolution != null) {
            this.resolution = resolution;
        } else {
            this.resolution = "";
        }

        if (uri != null) {
            this.uri = uri;
        } else {
            this.uri = "";
        }
    }


    /* Getters and setters */

    public String getHash() {
        return hash;
    }

    public void setHash(String hash) {
        this.hash = hash;
    }

    public String getResolution() {
        return resolution;
    }

    public void setResolution(String resolution) {
        this.resolution = resolution;
    }

    public String getUri() {
        return uri;
    }

    public void setUri(String uri) {
        this.uri = uri;
    }

    public MultimediaType getType() {
        return type;
    }

    public void setType(MultimediaType type) {
        this.type = type;
    }

    /**
     * This function checks if the values of the primary keys are not null or are not empty
     *
     * @return the function return "true" if the primary keys are not null, otherwise return "false"
     */
    /*public boolean checkPrimaryKey() {
            // TODO Eliminar esto una vez se compruebe el funcionamiento del PiikObject
        return hash != null && !hash.isEmpty();
    }*/

    /*public boolean checkNotNull() {
            // TODO Eliminar esto una vez se compruebe el funcionamiento del PiikObject
        if (!checkPrimaryKey()) {
            return false;
        }
        return uri != null && !uri.isEmpty();

    }*/

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Multimedia that = (Multimedia) o;
        return hash.equals(that.hash);
    }

    @Override
    public int hashCode() {
        return Objects.hash(hash);
    }
}
