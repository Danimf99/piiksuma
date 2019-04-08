package piiksuma;

import piiksuma.database.MapperColumn;
import piiksuma.database.MapperTable;

@MapperTable(nombre = "ticket")

public class Ticket extends Message {

    /* Attributes */
    @MapperColumn(pkey = true)
    private String id;
    @MapperColumn(columna = "deadline")
    private String closeDate;
    @MapperColumn(columna = "usr")
    private String user;
    @MapperColumn
    private String section;
    @MapperColumn(columna = "text")
    private String textProblem;
    @MapperColumn
    private String creationDate;
    @MapperColumn
    private String adminClosing;

    /* Constructors */

    public Ticket() {
    }

    public Ticket(String section) {

        // todo add message fields to constructor
        super();

        if (section != null) {
            this.section = section;
        } else {
            this.section = "";
        }

        closeDate = "";
    }


    /* Getters and setters */

    @Override
    public String getId() {
        return id;
    }

    @Override
    public void setId(String id) {
        this.id = id;
    }

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public String getCreationDate() {
        return creationDate;
    }

    public void setCreationDate(String creationDate) {
        this.creationDate = creationDate;
    }

    public String getAdminClosing() {
        return adminClosing;
    }

    public void setAdminClosing(String adminClosing) {
        this.adminClosing = adminClosing;
    }

    public String getCloseDate() {
        return closeDate;
    }

    public void setCloseDate(String closeDate) {
        this.closeDate = closeDate;
    }

    public String getSection() {
        return section;
    }

    public void setSection(String section) {
        this.section = section;
    }

    public String getTextProblem() {
        return textProblem;
    }

    public void setTextProblem(String textProblem) {
        this.textProblem = textProblem;
    }
}