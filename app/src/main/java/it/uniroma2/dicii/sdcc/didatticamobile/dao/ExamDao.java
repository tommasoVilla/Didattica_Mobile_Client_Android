package it.uniroma2.dicii.sdcc.didatticamobile.dao;

import java.util.List;

import it.uniroma2.dicii.sdcc.didatticamobile.error.BadRequestException;
import it.uniroma2.dicii.sdcc.didatticamobile.error.ConflictException;
import it.uniroma2.dicii.sdcc.didatticamobile.error.ExamDaoException;
import it.uniroma2.dicii.sdcc.didatticamobile.error.ExistentSubscriptionException;
import it.uniroma2.dicii.sdcc.didatticamobile.error.ExpiredTokenException;
import it.uniroma2.dicii.sdcc.didatticamobile.error.NotAllowedExamReservationException;
import it.uniroma2.dicii.sdcc.didatticamobile.error.TemporaryUnavailableException;
import it.uniroma2.dicii.sdcc.didatticamobile.model.Exam;

/*ExamDao interface encapsulates the method used to interact with the persistence layer for exam management*/
public interface ExamDao {
    /*
     * add makes a insert request for the given exam to the persistence layer.
     * The operation can succeed only with a valid token.
     */
    Exam add(Exam exam, String token) throws ConflictException, ExpiredTokenException, TemporaryUnavailableException, ExamDaoException;


    /*
     * findByCourse retrieves the exams matching with the given course id
     * E.g. if course is "4j9d3d"  the method retrieves all the exams related with the course with id "4j9d3d".
     * The operation can succeed only with a valid token.
     * */
    List<Exam> findByCourse(String courseId, String token) throws ExpiredTokenException, BadRequestException, TemporaryUnavailableException, ExamDaoException;

    /*
    * registerStudentToExam reserve the exam call with the provided examId for the student with the given username
    * The operation can succeed only with a valid token.
    * */
    void registerStudentToExam(String studentUsername, String examId, String token) throws ExpiredTokenException, BadRequestException, TemporaryUnavailableException, ExamDaoException, ExistentSubscriptionException, NotAllowedExamReservationException;

}
