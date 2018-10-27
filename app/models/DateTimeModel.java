package models;

import java.util.Date;

public class DateTimeModel {

    private int dow;
    private Date start, end;
    private String id;
    private boolean allDay;

    public DateTimeModel(int dow, Date start, Date end) {
        this.dow = dow;
        this.start = start;
        this.end = end;
    }

    public DateTimeModel(int dow, Date start, Date end, String id) {
        this.dow = dow;
        this.start = start;
        this.end = end;
        this.id = id;
    }

    public DateTimeModel(int dow, Date start, Date end, String id, boolean allDay) {
        this.dow = dow;
        this.start = start;
        this.end = end;
        this.allDay = allDay;
    }

    public void setDOW(int dow) {
        this.dow = dow;
    }

    public int getDOW() {
        return dow;
    }

    public void setStart(Date start) {
        this.start = start;
    }

    public Date getStart() {
        return start;
    }

    public void setEnd(Date end) {
        this.end = end;
    }

    public Date getEnd() {
        return start;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getId() {
        return id;
    }

    public boolean getAllDay() {
        return allDay;
    }

    public void setAllDay(boolean allDay) {
        this.allDay = allDay;
    }

}
