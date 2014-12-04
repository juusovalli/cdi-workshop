package com.example.cdi_workshop;

import java.io.Serializable;

import javax.enterprise.context.SessionScoped;

@SessionScoped
public class BlackBoard implements Serializable {

    private User currentUser;

    public User getCurrentUser() {
        return currentUser;
    }

    public void setCurrentUser(User user) {
        this.currentUser = user;
    }
}
