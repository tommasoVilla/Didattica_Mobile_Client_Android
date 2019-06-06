package it.uniroma2.dicii.sdcc.didatticamobile.model;

/*
 * An User object represents an user registered to the service. The type can be "student" or "teacher"
 * */
public class User {

    private String name;
    private String surname;
    private String username;
    private String password;
    private String type;
    private String mail;

    public User() {}

    public User(String name, String surname, String username, String password, String type, String mail) {
        this.name = name;
        this.surname = surname;
        this.username = username;
        this.password = password;
        this.type = type;
        this.mail = mail;
    }

    public User(String name, String surname, String username, String password, String mail) {
        this.name = name;
        this.surname = surname;
        this.username = username;
        this.password = password;
        this.mail = mail;
    }

    public String getName() {
        return name;
    }

    public String getSurname() {
        return surname;
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }

    public String getType() { return type; }

    public void setName(String name) {
        this.name = name;
    }

    public void setSurname(String surname) {
        this.surname = surname;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getMail() { return mail; }

    public void setMail(String mail) { this.mail = mail; }
}
