package pt.ulisboa.tecnico.socialsoftware.tutor.course.service

import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest
import org.springframework.boot.test.context.TestConfiguration
import pt.ulisboa.tecnico.socialsoftware.tutor.BeanConfiguration
import pt.ulisboa.tecnico.socialsoftware.tutor.SpockTest
import pt.ulisboa.tecnico.socialsoftware.tutor.course.domain.Course
import pt.ulisboa.tecnico.socialsoftware.tutor.course.domain.CourseExecution
import pt.ulisboa.tecnico.socialsoftware.tutor.exceptions.ErrorMessage
import pt.ulisboa.tecnico.socialsoftware.tutor.exceptions.TutorException
import pt.ulisboa.tecnico.socialsoftware.tutor.user.User
import pt.ulisboa.tecnico.socialsoftware.tutor.user.dto.ExternalUserDto

@DataJpaTest
class GetExternalUsersTest extends SpockTest {

    Course course1
    CourseExecution courseExecution1
    Course course2
    CourseExecution courseExecution2
    User user1
    User user2

    def setup() {
        course1 = new Course(COURSE_1_NAME, Course.Type.EXTERNAL)
        courseRepository.save(course1)
        courseExecution1 = new CourseExecution(course1, COURSE_1_ACRONYM, COURSE_1_ACADEMIC_TERM, Course.Type.EXTERNAL)
        courseExecutionRepository.save(courseExecution1)

        user1 = new User(USER_1_NAME, USER_1_USERNAME, User.Role.STUDENT)
        userRepository.save(user1)
        user1.addCourse(courseExecution1)
        courseExecution1.addUser(user1)

        course2 = new Course(COURSE_1_NAME, Course.Type.EXTERNAL)
        courseRepository.save(course2)
        courseExecution2 = new CourseExecution(course2, COURSE_2_ACRONYM, COURSE_2_ACADEMIC_TERM, Course.Type.EXTERNAL)
        courseExecutionRepository.save(courseExecution2)

        user2 = new User(USER_2_NAME, USER_2_USERNAME, User.Role.STUDENT)
        userRepository.save(user2)
        user2.addCourse(courseExecution2)
        courseExecution2.addUser(user2)
    }

    def "the course execution id is invalid"() {
        given: "a invalid execution id"
        def executionId = -1

        when:
        courseService.getExternalUsers(executionId)

        then: "an exception is thrown"
        def error = thrown(TutorException)
        error.getErrorMessage() == ErrorMessage.COURSE_EXECUTION_NOT_FOUND
    }

    def "the course execution is not external"() {
        given: "a non-external course execution id"
        def executionId = externalCourseExecution.getId()

        when:
        courseService.getExternalUsers(executionId)

        then: "an exception is thrown"
        def error = thrown(TutorException)
        error.getErrorMessage() == ErrorMessage.COURSE_EXECUTION_NOT_EXTERNAL
    }

    def "receives a course execution id, and returns the list of student belonging to that course execution"() {
        given: "a course execution id"
        def executionId = courseExecution1.getId()

        when:
        List<ExternalUserDto> result = courseService.getExternalUsers(executionId)

        then: "check if the list contains one user"
        result.size() == 1

        and: "it contains the correct user"
        result.get(0).getId() == user1.getId()

    }

    
    @TestConfiguration
    static class LocalTestConfiguration extends BeanConfiguration { }
}