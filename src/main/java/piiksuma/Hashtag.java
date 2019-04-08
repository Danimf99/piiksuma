package piiksuma;

import piiksuma.database.MapperColumn;
import piiksuma.database.MapperTable;

@MapperTable
public class Hashtag {

    /* Attributes */
    @MapperColumn(pkey = true)
    private String name;


    /* Constructors */

    public Hashtag() {
    }

    public Hashtag(String name) {
        if (name != null) {
            this.name = name;
        } else {
            this.name = "";
        }
    }


    /* Getters and setters */

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}