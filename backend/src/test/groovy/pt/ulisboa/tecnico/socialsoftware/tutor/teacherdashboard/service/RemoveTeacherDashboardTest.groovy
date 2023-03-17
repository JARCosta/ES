package pt.ulisboa.tecnico.socialsoftware.tutor.teacherdashboard.service

import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest
import org.springframework.boot.test.context.TestConfiguration
import pt.ulisboa.tecnico.socialsoftware.tutor.BeanConfiguration
import pt.ulisboa.tecnico.socialsoftware.tutor.SpockTest
import pt.ulisboa.tecnico.socialsoftware.tutor.teacherdashboard.domain.TeacherDashboard
import pt.ulisboa.tecnico.socialsoftware.tutor.exceptions.ErrorMessage
import pt.ulisboa.tecnico.socialsoftware.tutor.exceptions.TutorException
import pt.ulisboa.tecnico.socialsoftware.tutor.user.domain.Teacher
import pt.ulisboa.tecnico.socialsoftware.tutor.teacherdashboard.domain.QuizStats
import spock.lang.Unroll

@DataJpaTest
class RemoveTeacherDashboardTest extends SpockTest {

    def teacher

    def setup() {
        createExternalCourseAndExecution()

        teacher = new Teacher(USER_1_NAME, false)
        userRepository.save(teacher)
    }

    def createTeacherDashboard() {
        def dashboard = new TeacherDashboard(externalCourseExecution, teacher)
        teacherDashboardRepository.save(dashboard)
        return dashboard
    }

    def "remove a dashboard"() {
        given: "a dashboard"
        def dashboard = createTeacherDashboard()

        when: "the user removes the dashboard"
        teacherDashboardService.removeTeacherDashboard(dashboard.getId())

        then: "the dashboard is removed"
        teacherDashboardRepository.findAll().size() == 0L
        teacher.getDashboards().size() == 0
    }

    def "cannot remove a dashboard twice"() {
        given: "a removed dashboard"
        def dashboard = createTeacherDashboard()
        teacherDashboardService.removeTeacherDashboard(dashboard.getId())

        when: "the dashboard is removed for the second time"
        teacherDashboardService.removeTeacherDashboard(dashboard.getId())

        then: "an exception is thrown"        
        def exception = thrown(TutorException)
        exception.getErrorMessage() == ErrorMessage.DASHBOARD_NOT_FOUND
    }

    @Unroll
    def "cannot remove a dashboard that doesn't exist with the dashboardId=#dashboardId"() {
        when: "an incorrect dashboard id is removed"
        teacherDashboardService.removeTeacherDashboard(dashboardId)

        then: "an exception is thrown"        
        def exception = thrown(TutorException)
        exception.getErrorMessage() == ErrorMessage.DASHBOARD_NOT_FOUND

        where:
        dashboardId << [null, 10, -1]
    }

    def "removes the statistics on teacher dashboard when its removed"(){
        given: "a dashboard"
        teacher.addCourse(externalCourseExecution)
        teacherDashboardService.getTeacherDashboard(externalCourseExecution.getId(),teacher.getId())

        when: "the user removes the dashboard"
        assert studentStatsRepository.count() == 1
        assert teacherDashboardRepository.count() == 1
        assert teacher.getDashboards().size() == 1
        assert teacherDashboardRepository.findAll().get(0).getStudentStats().size() == 1
        assert teacherDashboardRepository.findAll().get(0).getTeacher() == teacher
        teacherDashboardService.removeTeacherDashboard(teacherDashboardRepository.findAll().get(0).getId())

        then: "the statistics are removed"
        teacherDashboardRepository.count() == 0
        teacher.getDashboards().size() == 0
        studentStatsRepository.count() == 0
        
    }

    @Unroll
    def "remove dashboard that has 1 quizStat associated with it"() {
        given: "a teacher dashboard with quizStats"
        def dashboard = createTeacherDashboard()
        def quizStats = new QuizStats()
        quizStatsRepository.save(quizStats)
        dashboard.addQuizStats(quizStats)
        teacherDashboardRepository.save(dashboard)

        when: "the dashboard is removed"
        teacherDashboardService.removeTeacherDashboard(dashboard.getId())

        then: "the dashboard is removed"
        teacherDashboardRepository.findAll().size() == 0L
        teacher.getDashboards().size() == 0
        quizStatsRepository.count() == 0L
    }

    @Unroll
    def "remove dashboard that has 2 quizStats associated with it"() {
        given: "a teacher dashboard with quizStats"
        def dashboard = createTeacherDashboard()
        def quizStats1 = new QuizStats()
        quizStatsRepository.save(quizStats1)
        dashboard.addQuizStats(quizStats1)
        def quizStats2 = new QuizStats()
        quizStatsRepository.save(quizStats2)
        dashboard.addQuizStats(quizStats2)
        teacherDashboardRepository.save(dashboard)

        when: "the dashboard is removed"
        teacherDashboardService.removeTeacherDashboard(dashboard.getId())

        then: "the dashboard is removed"
        teacherDashboardRepository.findAll().size() == 0L
        teacher.getDashboards().size() == 0
        quizStatsRepository.count() == 0L
    }

    @Unroll
    def "remove dashboard that doesn't have any quizStats associated with it"() {
        given: "a teacher dashboard with quizStats"
        def dashboard = createTeacherDashboard()

        when: "the dashboard is removed"
        teacherDashboardService.removeTeacherDashboard(dashboard.getId())

        then: "the dashboard is removed"
        teacherDashboardRepository.findAll().size() == 0L
        teacher.getDashboards().size() == 0
        quizStatsRepository.count() == 0L
    }


    @TestConfiguration
    static class LocalBeanConfiguration extends BeanConfiguration {}
}
