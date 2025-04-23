package com.chilltime.planifyfront.model.transfer;

public class TPlanner {
    private Long id;
    private String username;
    private String password;
    private String role;
    private boolean active;

    public TPlanner(Long id, String username, String password, String role, boolean active) {
        this.id = id;
        this.username = username;
        this.password = password;
        this.role = role;
        this.active = active;
    }

    public TPlanner() {}

    public Long getId() {
        return this.id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getRole() {
        return role;
    }
}
