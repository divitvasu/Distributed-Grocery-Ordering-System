package com.GroceryOrderingSystem.model;

import javax.persistence.*;

@Entity
@Table(name="clients")
public class ClientAuth {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    private String email;
    private String password;

    public String getEmail() {
        return email;
    }
    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }
    public void setPassword(String password) {
        this.password = password;
    }

    @Override
    public String toString() {
        return "Client [email=" + email + ", password=" + password + "]";
    }

}
