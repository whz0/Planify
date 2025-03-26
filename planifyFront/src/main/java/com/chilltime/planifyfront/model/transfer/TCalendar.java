package com.chilltime.planifyfront.model.transfer;

public class TCalendar {
    private Long id;
    private String name;
    private String description;
    private boolean active;
    private String type;
    private Long id_user;

    public TCalendar() {
    }

    public TCalendar(Long id, String name, String description, boolean active, String type, Long id_user) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.active = active;
        this.type = type;
        this.id_user = id_user;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Long getId_user() {
        return id_user;
    }

    public void setId_user(Long id_user) {
        this.id_user = id_user;
    }
}
