package it.uniroma2.dicii.sdcc.didatticamobile.dao;

import java.io.IOException;

import it.uniroma2.dicii.sdcc.didatticamobile.config.AppConfiguration;
import it.uniroma2.dicii.sdcc.didatticamobile.error.InvalidConfigurationException;
/*
 * An UserDaoFactory object is responsible to build an object that implements UserDao interface.
 * The concrete class is read from the configuration parameters stored in AppConfiguration.
 * The class is implemented as a singleton.
 * */
public class UserDaoFactory {

    private static UserDaoFactory instance;

    private UserDaoFactory() {
    }

    public static synchronized UserDaoFactory getInstance() {
        if (instance == null) {
            instance = new UserDaoFactory();
        }
        return instance;
    }

    public UserDao createUserDao() throws InvalidConfigurationException {
        try {
            String className = AppConfiguration.getInstance().readProperty("userdao_type");
            Class<?> c = Class.forName(className);
            return (UserDao) c.newInstance();
        } catch (NullPointerException | IOException | ClassNotFoundException | InstantiationException | IllegalAccessException e) {
            throw new InvalidConfigurationException(e.getMessage(), e.getCause());
        }
    }
}
