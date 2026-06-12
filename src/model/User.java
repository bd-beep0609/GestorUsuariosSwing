package model;

import java.io.Serializable;

public class User implements Serializable {
    private static final long serialVersionUID = 1L;
    private static int nextId = 1;
    private int id;
    private String name;
    private String email;
    private String password;
    private boolean active;
    private String role;

    public User(String name, String email, String password, boolean active, String role) {
        this.id = nextId++;
        this.name = name;
        this.email = email;
        this.password = password;
        this.active = active;
        this.role = role;
    }

    public User(int id, String name, String email, String password, boolean active, String role) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.password = password;
        this.active = active;
        this.role = role;
        if (id >= nextId) nextId = id + 1;
    }

    // Getters y Setters
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }

    public boolean isActive() { return active; }
    public void setActive(boolean active) { this.active = active; }

    public String getActiveText() { return active ? "Activo" : "Inactivo"; }

    public String getRole() { return role; }
    public void setRole(String role) { this.role = role; }

    public boolean isAdmin() { return "ADMIN".equals(role); }
    public boolean isUser() { return "USER".equals(role); }

    public static void setNextId(int value) { nextId = value; }
    public static int getNextId() { return nextId; }

    @Override
    public String toString() {
        return name + " (" + email + ") - " + role;
    }
}