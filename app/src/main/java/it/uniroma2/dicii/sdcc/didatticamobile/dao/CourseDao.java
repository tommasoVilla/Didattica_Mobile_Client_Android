package it.uniroma2.dicii.sdcc.didatticamobile.dao;

import java.util.List;
import it.uniroma2.dicii.sdcc.didatticamobile.error.BadRequestException;
import it.uniroma2.dicii.sdcc.didatticamobile.error.ConflictException;
import it.uniroma2.dicii.sdcc.didatticamobile.error.CourseDaoException;
import it.uniroma2.dicii.sdcc.didatticamobile.error.ExistentSubscriptionException;
import it.uniroma2.dicii.sdcc.didatticamobile.error.ExpiredTokenException;
import it.uniroma2.dicii.sdcc.didatticamobile.error.TemporaryUnavailableException;
import it.uniroma2.dicii.sdcc.didatticamobile.model.Course;

/*
 * UserDao interface encapsulates the method used to interact with the persistence layer for course management*/
public interface CourseDao {
    /*
     * add makes a insert request for the given course to the persistence layer.
     * The operation can succeed only with a valid token.
     */
    Course add(Course course, String token) throws CourseDaoException, ConflictException, TemporaryUnavailableException, ExpiredTokenException;

    /*
    * findBy retrieves the courses matching with the given pattern according to the provided criterion
    * E.g. if criterion is "name" and pattern is "a" the method retrieves all the courses with
    * name containing "a".
    * The operation can succeed only with a valid token.
    * */

    List<Course> findBy(String criterion, String pattern, String token) throws CourseDaoException, ExpiredTokenException, BadRequestException, TemporaryUnavailableException;

    /*
    * addCourseToStudent subscribe the student with the given username to the
    * course with the provided id.
    * The operation can succeed only with a valid token.
    * */
    void addCourseToStudent(String courseId, String studentUsername, String token) throws ExpiredTokenException, TemporaryUnavailableException, CourseDaoException, ExistentSubscriptionException;

    /*
    * findBySubscribedStudent retrieves the courses which the user with the given username is subscribed to.
    * If no courses are found, an empty list is returned.
    * */
    List<Course> findBySubscribedStudent(String studentUsername, String token) throws ExpiredTokenException, TemporaryUnavailableException, CourseDaoException;

    /*
    * removeStudentFromCourse unsubscribe the given student from the given course.
    * The operation can succeed only providing a valid token.
    * */
    void removeStudentFromCourse(String courseId, String studentUsername, String token) throws ExpiredTokenException, TemporaryUnavailableException, CourseDaoException;
}
