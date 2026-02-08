// Author: [REPLACE_WITH_STUDENT_NAME]
// NOTE: Please change the above line to your full name before submission.

package it.spaceschool.service;

import it.spaceschool.dao.StudentDAOMemory;
import it.spaceschool.dao.StudentDAO;
import it.spaceschool.model.Student;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class EnrollmentTest {

    @Test
    void enroll_course_updates_student_and_sends_email_mock() {
        StudentDAO dao = new StudentDAOMemory();
        StudentService service = new StudentService(dao);

        Student student = new Student("user", "Name", "mail@test.com", "pass123");
        student.setBirthDate("2000-01-01");
        student.setMotivationLetterPath("motivation.pdf");

        service.register(student);

        String courseId = "C-101";
        service.enrollCourse(student, courseId);

        Student stored = dao.findByEmail("mail@test.com").orElseThrow();
        assertTrue(stored.getEnrolledCourseIds().contains(courseId));
    }
}
