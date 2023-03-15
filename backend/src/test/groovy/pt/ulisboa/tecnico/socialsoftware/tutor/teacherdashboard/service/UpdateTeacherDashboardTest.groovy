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
        // def studentStats = new StudentStats(teacherDashboard, externalCourseExecution)
        // studentStatsRepository.save(studentStats)
        // teacherDashboard.addStudentStats(studentStats)

        // teacherDashboard.addStudentStats(studentStats)
        // studentStatsRepository.save(studentStats)
        // def studentStats = teacherDashboard.getStudentStats().get(0)
        // studentStats.setNumStudents(3)
        // studentStats.setNumMore75CorrectQuestions(2)
        // studentStats.setNumAtLeast3Quizzes(1)
        // ((AuthTecnicoUser)student.authUser).setEnrolledCoursesAcronyms(externalCourseExecution.getAcronym())

        when: "a dashboard is updated"
        def student = new Student(USER_1_NAME, false)
        userRepository.save(student)
        externalCourseExecution.addUser(student)
        assert externalCourseExecution.getYear() == 2019
        // assert studentStats.getNumStudents() == 3
        // assert studentStats.getNumMore75CorrectQuestions() == 2
        // assert studentStats.getNumAtLeast3Quizzes() == 1
        // assert teacherDashboardDto.getNumStudents() == 3
        // assert teacherDashboard.getStudentStats().get(0).getNumStudents() == 0
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
        // result.getId() != 0
        // result.getCourseExecution().getId() == externalCourseExecution.getId()
        // result.getTeacher().getId() == teacher.getId()

        // and: "the teacher has a reference for the dashboard"
        // teacher.getDashboards().size() == 1
        // teacher.getDashboards().contains(result)
    }

    @TestConfiguration
    static class LocalBeanConfiguration extends BeanConfiguration {}
}