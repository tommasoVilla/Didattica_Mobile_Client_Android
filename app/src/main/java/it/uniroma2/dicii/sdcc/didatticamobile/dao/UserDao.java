package it.uniroma2.dicii.sdcc.didatticamobile.dao;

import it.uniroma2.dicii.sdcc.didatticamobile.error.LoginAuthorizationException;
import it.uniroma2.dicii.sdcc.didatticamobile.error.RegistrationAuthorizationException;
import it.uniroma2.dicii.sdcc.didatticamobile.error.UsernameConflictException;
import it.uniroma2.dicii.sdcc.didatticamobile.error.TemporaryUnavailableException;
import it.uniroma2.dicii.sdcc.didatticamobile.error.UserDaoException;
import it.uniroma2.dicii.sdcc.didatticamobile.model.User;

/*
 * UserDao interface encapsulates the method used to interact with the persistence layer for user management*/
public interface UserDao {
    /*
     * register makes a registration request for the given user to the persistence layer.
     * The registration can succeed only with a valid fiscalCode.
     */
    User register(User user, String fiscalCode) throws UsernameConflictException, RegistrationAuthorizationException, TemporaryUnavailableException, UserDaoException;
    /*
     * login makes a login request with given username and password. It returns the access token,
     * that will be used to authenticate the future requests.
     */
    String login(String username, String password) throws LoginAuthorizationException, TemporaryUnavailableException, UserDaoException;

}
