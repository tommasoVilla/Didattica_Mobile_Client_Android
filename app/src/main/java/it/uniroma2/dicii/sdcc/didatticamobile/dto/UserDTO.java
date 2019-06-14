package it.uniroma2.dicii.sdcc.didatticamobile.dto;

import it.uniroma2.dicii.sdcc.didatticamobile.model.User;

/* An UserDTO is an object used to map the JSON returned from the backend service into an User object*/
public class UserDTO {

    private User user;

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }
}
