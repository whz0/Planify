package com.chilltime.planifyfront.model.transfer;

public class TCalendarCode {
    private Long id;
    private String code;
    private boolean used;
    private Long calendarID;

    public TCalendarCode() {}

    public TCalendarCode(Long id, String code, boolean used, Long calendarID) {
        this.id = id;
        this.code = code;
        this.used = used;
        this.calendarID = calendarID;
    }

    public Long getId() { return id; }

    public void setId(Long id) {
        this.id = id;
    }

    public String getCode() { return code; }

    public void setCode(String code) { this.code = code; }

    public boolean isUsed() { return used; }

    public void setUsed(boolean used) { this.used = used; }

    public Long getCalendarID() { return calendarID; }

    public void setCalendarID(Long calendarID) { this.calendarID = calendarID; }

}
