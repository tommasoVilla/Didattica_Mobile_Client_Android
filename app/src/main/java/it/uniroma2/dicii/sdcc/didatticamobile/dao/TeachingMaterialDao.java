package it.uniroma2.dicii.sdcc.didatticamobile.dao;

import java.util.ArrayList;

import it.uniroma2.dicii.sdcc.didatticamobile.error.ExpiredTokenException;
import it.uniroma2.dicii.sdcc.didatticamobile.error.TeachingMaterialDaoException;
import it.uniroma2.dicii.sdcc.didatticamobile.error.TemporaryUnavailableException;

/* TeachingMaterialDao interface encapsulates the method used to interact with the persistence layer
 * for teaching material management*/
public interface TeachingMaterialDao {

    /*
     * findTeachingMaterialByCourse makes a request for searching all teaching material in the
     * persistence layer belongs to a specific course.
     */
    ArrayList<String> findTeachingMaterialByCourse(String courseId, String token) throws ExpiredTokenException, TemporaryUnavailableException, TeachingMaterialDaoException;
    String getDownloadLink(String username, String courseId, String filename, String token) throws ExpiredTokenException, TeachingMaterialDaoException, TemporaryUnavailableException;
}
