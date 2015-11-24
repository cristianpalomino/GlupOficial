package pe.com.glup.models;

import java.util.Date;

public class DemoModel {
    private static int nextId = 0;
    private String label;
    private Date dateTime;
    private String pathToImage;
    private int id = ++nextId;

    public void setLabel(String label) {
        this.label = label;
    }

    public void setDateTime(Date dateTime) {
        this.dateTime = dateTime;
    }

    public static int getNextId() {
        return nextId;
    }

    public String getLabel() {
        return label;
    }

    public Date getDateTime() {
        return dateTime;
    }

    public String getPathToImage() {
        return pathToImage;
    }

    public int getId() {
        return id;
    }
}
