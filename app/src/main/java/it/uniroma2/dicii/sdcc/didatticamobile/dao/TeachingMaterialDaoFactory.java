package it.uniroma2.dicii.sdcc.didatticamobile.dao;

import java.io.IOException;

import it.uniroma2.dicii.sdcc.didatticamobile.config.AppConfiguration;
import it.uniroma2.dicii.sdcc.didatticamobile.error.InvalidConfigurationException;

/*
 * An TeachingMaterialDaoFactory object is responsible to build an object that implements TeachingMaterialDao interface.
 * The concrete class is read from the configuration parameters stored in AppConfiguration.
 * The class is implemented as a singleton.
 * */
public class TeachingMaterialDaoFactory {

    private static TeachingMaterialDaoFactory instance;

    private TeachingMaterialDaoFactory() {
    }

    public static synchronized TeachingMaterialDaoFactory getInstance() {
        if (instance == null) {
            instance = new TeachingMaterialDaoFactory();
        }
        return instance;
    }

    public TeachingMaterialDao createTeachingMaterialDao() throws InvalidConfigurationException {
        try {
            String className = AppConfiguration.getInstance().readProperty("teachingmaterialdao_type");
            Class<?> c = Class.forName(className);
            return (TeachingMaterialDao) c.newInstance();
        } catch (NullPointerException | IOException | ClassNotFoundException | InstantiationException | IllegalAccessException e) {
            throw new InvalidConfigurationException(e.getMessage(), e.getCause());
        }
    }
}