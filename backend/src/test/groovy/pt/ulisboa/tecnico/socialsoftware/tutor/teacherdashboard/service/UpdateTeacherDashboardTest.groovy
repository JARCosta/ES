package pt.ulisboa.tecnico.socialsoftware.tutor.teacherdashboard.service

import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest
import org.springframework.boot.test.context.TestConfiguration
import pt.ulisboa.tecnico.socialsoftware.tutor.BeanConfiguration
import pt.ulisboa.tecnico.socialsoftware.tutor.SpockTest
import pt.ulisboa.tecnico.socialsoftware.tutor.teacherdashboard.domain.TeacherDashboard
import pt.ulisboa.tecnico.socialsoftware.tutor.teacherdashboard.domain.StudentStats
import pt.ulisboa.tecnico.socialsoftware.tutor.exceptions.ErrorMessage
import pt.ulisboa.tecnico.socialsoftware.tutor.exceptions.TutorException
import pt.ulisboa.tecnico.socialsoftware.tutor.user.domain.Teacher
import pt.ulisboa.tecnico.socialsoftware.tutor.user.domain.Student
import spock.lang.Unroll

@DataJpaTest
class UpdateTeacherDashboardTest extends SpockTest {
    def teacher

    def setup() {
        createExternalCourseAndExecution()

        teacher = new Teacher(USER_1_NAME, false)
        userRepository.save(teacher)
        teacher.addCourse(externalCourseExecution)
    }

    def "update a dashboard"() {
        given: "an empty dashboard for the teacher"
        def teacherDashboardDto = teacherDashboardService.createTeacherDashboard(externalCourseExecution.getId(), teacher.getId())
        teacherDashboardDto.setNumberOfStudents(100)
        teacherDashboardDto.setId(99)

        and: "a change on the dashboard's stats"
        def teacherDashboard = teacherDashboardRepository.findAll().get(0)

        when: "a dashboard is updated"
        def student = new Student(USER_1_NAME, false)
        userRepository.save(student)
        externalCourseExecution.addUser(student)
        teacherDashboardService.updateTeacherDashboard(teacherDashboard.getId())
        teacherDashboardDto = teacherDashboardService.getTeacherDashboard(externalCourseExecution.getId(), teacher.getId())

        then: "the dashboard is updated for the true values"
        teacherDashboardRepository.count() == 1L
        def result = teacherDashboardRepository.findAll().get(0)
        result.getStudentStats().get(0).getNumStudents() == 1
        result.getStudentStats().get(0).getNumMore75CorrectQuestions() == 0
        result.getStudentStats().get(0).getNumAtLeast3Quizzes() == 0
        teacherDashboard.getStudentStats().size() == 1
        teacherDashboardDto.getId() == 1
        teacherDashboardDto.getNumberOfStudents() == 0
        teacherDashboardDto.getNumStudents() == [1,]
        teacherDashboardDto.getnumMore75CorrectQuestions() == [0,]
        teacherDashboardDto.getnumAtLeast3Quizes() == [0,]
    }

    @TestConfiguration
    static class LocalBeanConfiguration extends BeanConfiguration {}
}