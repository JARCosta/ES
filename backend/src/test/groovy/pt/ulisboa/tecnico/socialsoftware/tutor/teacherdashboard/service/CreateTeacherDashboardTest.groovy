package pt.ulisboa.tecnico.socialsoftware.tutor.teacherdashboard.service

import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest
import org.springframework.boot.test.context.TestConfiguration
import pt.ulisboa.tecnico.socialsoftware.tutor.BeanConfiguration
import pt.ulisboa.tecnico.socialsoftware.tutor.SpockTest
import pt.ulisboa.tecnico.socialsoftware.tutor.exceptions.ErrorMessage
import pt.ulisboa.tecnico.socialsoftware.tutor.exceptions.TutorException
import pt.ulisboa.tecnico.socialsoftware.tutor.user.domain.Teacher
import pt.ulisboa.tecnico.socialsoftware.tutor.quiz.domain.Quiz
import pt.ulisboa.tecnico.socialsoftware.tutor.teacherdashboard.domain.QuizStats
import spock.lang.Unroll

import pt.ulisboa.tecnico.socialsoftware.tutor.execution.CourseExecutionService
import pt.ulisboa.tecnico.socialsoftware.tutor.execution.domain.CourseExecution
import pt.ulisboa.tecnico.socialsoftware.tutor.execution.repository.CourseExecutionRepository
import pt.ulisboa.tecnico.socialsoftware.tutor.execution.CourseExecutionService
import pt.ulisboa.tecnico.socialsoftware.tutor.execution.domain.CourseExecution
import pt.ulisboa.tecnico.socialsoftware.tutor.execution.repository.CourseExecutionRepository
import pt.ulisboa.tecnico.socialsoftware.tutor.question.domain.Course
import pt.ulisboa.tecnico.socialsoftware.tutor.teacherdashboard.dto.TeacherDashboardDto
import pt.ulisboa.tecnico.socialsoftware.tutor.teacherdashboard.dto.QuizStatsDto


@DataJpaTest
    class CreateTeacherDashboardTest extends SpockTest {
    def teacher

    def setup() {
        createExternalCourseAndExecution()

        teacher = new Teacher(USER_1_NAME, false)
        userRepository.save(teacher)
    }

    def "create an empty dashboard"() {
        given: "a teacher in a course execution"
        teacher.addCourse(externalCourseExecution)

        when: "a dashboard is created"
        teacherDashboardService.getTeacherDashboard(externalCourseExecution.getId(), teacher.getId())

        then: "an empty dashboard is created"
        teacherDashboardRepository.count() == 1L
        def result = teacherDashboardRepository.findAll().get(0)
        result.getId() != 0
        result.getCourseExecution().getId() == externalCourseExecution.getId()
        result.getTeacher().getId() == teacher.getId()

        and: "the teacher has a reference for the dashboard"
        teacher.getDashboards().size() == 1
        teacher.getDashboards().contains(result)
    }

    def "cannot create multiple dashboards for a teacher on a course execution"() {
        given: "a teacher in a course execution"
        teacher.addCourse(externalCourseExecution)

        and: "an empty dashboard for the teacher"
        teacherDashboardService.createTeacherDashboard(externalCourseExecution.getId(), teacher.getId())

        when: "a second dashboard is created"
        teacherDashboardService.createTeacherDashboard(externalCourseExecution.getId(), teacher.getId())

        then: "there is only one dashboard"
        teacherDashboardRepository.count() == 1L

        and: "exception is thrown"
        def exception = thrown(TutorException)
        exception.getErrorMessage() == ErrorMessage.TEACHER_ALREADY_HAS_DASHBOARD
    }

    def "cannot create a dashboard for a user that does not belong to the course execution"() {
        when: "a dashboard is created"
        teacherDashboardService.createTeacherDashboard(externalCourseExecution.getId(), teacher.getId())

        then: "exception is thrown"        
        def exception = thrown(TutorException)
        exception.getErrorMessage() == ErrorMessage.TEACHER_NO_COURSE_EXECUTION
    }

    @Unroll
    def "cannot create a dashboard with courseExecutionId=#courseExecutionId"() {
        when: "a dashboard is created"
        teacherDashboardService.createTeacherDashboard(courseExecutionId, teacher.getId())

        then: "an exception is thrown"
        def exception = thrown(TutorException)
        exception.getErrorMessage() == ErrorMessage.COURSE_EXECUTION_NOT_FOUND

        where:
        courseExecutionId << [0, 100]
    }

    @Unroll
    def "cannot create a dashboard with teacherId=#teacherId"() {
        when: "a dashboard is created"
        teacherDashboardService.createTeacherDashboard(externalCourseExecution.getId(), teacherId)

        then: "an exception is thrown"
        def exception = thrown(TutorException)
        exception.getErrorMessage() == ErrorMessage.USER_NOT_FOUND

        where:
        teacherId << [0, 100]
    }

    def "dashboard is created with student stats"(){
        given: "a teacher in a course execution"
        teacher.addCourse(externalCourseExecution)

        when: "a dashboard is created"
        teacherDashboardService.getTeacherDashboard(externalCourseExecution.getId(), teacher.getId())

        then: "an empty dashboard is created"
        
        teacherDashboardRepository.count() == 1L
        studentStatsRepository.count() != 0
    }

    @Unroll
    def "create dashboard that has 3 courseExecutions associated with it"() {
        given: "a teacher dashboard with quizStats"
        def externalCourse = new Course(COURSE_1_NAME, Course.Type.TECNICO)
        courseRepository.save(externalCourse)


        def externalCourseExecution1 = new CourseExecution(externalCourse, COURSE_2_ACRONYM, COURSE_2_ACADEMIC_TERM, Course.Type.TECNICO, LOCAL_DATE_TODAY)
        courseExecutionRepository.save(externalCourseExecution1)


        def externalCourseExecution2 = new CourseExecution(externalCourse, COURSE_3_ACRONYM, COURSE_3_ACADEMIC_TERM, Course.Type.TECNICO, LOCAL_DATE_YESTERDAY)
        courseExecutionRepository.save(externalCourseExecution2)

        def externalCourseExecution3 = new CourseExecution(externalCourse, COURSE_4_ACRONYM, COURSE_4_ACADEMIC_TERM, Course.Type.TECNICO, LOCAL_DATE_TOMORROW)
        courseExecutionRepository.save(externalCourseExecution3)

        teacher.addCourse(externalCourseExecution3)

        when: "a dashboard is created"
        teacherDashboardService.createTeacherDashboard(externalCourseExecution3.getId(), teacher.getId())



        then: "the dashboard is saved to the database with 3 quizStats"
        teacherDashboardRepository.findAll().size() == 1L
        teacher.getDashboards().size() == 1
        quizStatsRepository.count() == 3L
    }

    @Unroll
    def "create dashboard that has 3 courseExecutions associated with it but with 4 courseExecutions in course"() {
        given: "a teacher dashboard with quizStats"
        def externalCourse = new Course(COURSE_1_NAME, Course.Type.TECNICO)
        courseRepository.save(externalCourse)


        def externalCourseExecution1 = new CourseExecution(externalCourse, COURSE_2_ACRONYM, COURSE_2_ACADEMIC_TERM, Course.Type.TECNICO, LOCAL_DATE_TOMORROW)
        courseExecutionRepository.save(externalCourseExecution1)


        def externalCourseExecution2 = new CourseExecution(externalCourse, COURSE_3_ACRONYM, COURSE_3_ACADEMIC_TERM, Course.Type.TECNICO, LOCAL_DATE_TODAY)
        courseExecutionRepository.save(externalCourseExecution2)

        def externalCourseExecution3 = new CourseExecution(externalCourse, COURSE_4_ACRONYM, COURSE_4_ACADEMIC_TERM, Course.Type.TECNICO, LOCAL_DATE_BEFORE)
        courseExecutionRepository.save(externalCourseExecution3)

        def externalCourseExecution4 = new CourseExecution(externalCourse, COURSE_5_ACRONYM, COURSE_5_ACADEMIC_TERM, Course.Type.TECNICO, LOCAL_DATE_YESTERDAY)
        courseExecutionRepository.save(externalCourseExecution4)

        teacher.addCourse(externalCourseExecution1)

        when: "a dashboard is created"
        teacherDashboardService.createTeacherDashboard(externalCourseExecution1.getId(), teacher.getId())



        then: "the dashboard is saved to the database with 3 quizStats"
        teacherDashboardRepository.findAll().size() == 1L
        teacher.getDashboards().size() == 1
        quizStatsRepository.count() == 3L
        // check if the dashboard has the correct courseExecutions are in order
        def dashboard = teacherDashboardRepository.findAll().get(0)
        dashboard.getQuizStats().size() == 3;
        dashboard.getQuizStats().get(0).getCourseExecution().getAcronym() == COURSE_2_ACRONYM
        dashboard.getQuizStats().get(1).getCourseExecution().getAcronym() == COURSE_3_ACRONYM
        dashboard.getQuizStats().get(2).getCourseExecution().getAcronym() == COURSE_5_ACRONYM
    }

    @Unroll
    def "create dashboard that has 2 courseExecutions associated with"() {
        given: "a teacher dashboard with quizStats"
        def externalCourse = new Course(COURSE_1_NAME, Course.Type.TECNICO)
        courseRepository.save(externalCourse)


        def externalCourseExecution1 = new CourseExecution(externalCourse, COURSE_2_ACRONYM, COURSE_2_ACADEMIC_TERM, Course.Type.TECNICO, LOCAL_DATE_TODAY)
        courseExecutionRepository.save(externalCourseExecution1)


        def externalCourseExecution3 = new CourseExecution(externalCourse, COURSE_4_ACRONYM, COURSE_4_ACADEMIC_TERM, Course.Type.TECNICO, LOCAL_DATE_TOMORROW)
        courseExecutionRepository.save(externalCourseExecution3)

        teacher.addCourse(externalCourseExecution3)

        when: "a dashboard is created"
        teacherDashboardService.createTeacherDashboard(externalCourseExecution3.getId(), teacher.getId())

        then: "the dashboard is saved to the database with 3 quizStats"
        teacherDashboardRepository.findAll().size() == 1L
        teacher.getDashboards().size() == 1
        quizStatsRepository.count() == 2L
        // check if the dashboard has the correct courseExecutions are in order
        def dashboard = teacherDashboardRepository.findAll().get(0)
        dashboard.getQuizStats().get(0).getCourseExecution().getAcronym() == COURSE_4_ACRONYM
        dashboard.getQuizStats().get(1).getCourseExecution().getAcronym() == COURSE_2_ACRONYM
    }

    @Unroll
    def "create dashboard with empty quizzstats and check if the stats are zero"(){
        given: "a teacher in a course execution"
        teacher.addCourse(externalCourseExecution)

        and: "an empty dashboard for the teacher"
        teacherDashboardService.createTeacherDashboard(externalCourseExecution.getId(), teacher.getId())

        when: "the dashboard is retrieved"
        def dashboard = teacherDashboardService.getTeacherDashboard(externalCourseExecution.getId(), teacher.getId())

        then: "the stats in quizStats are zero"
        dashboard.getQuizStatsDto().get(0).getId() == quizStatsRepository.findAll().get(0).getId()
        dashboard.getQuizStatsDto().get(0).getNumQuizzes() == 0
        dashboard.getQuizStatsDto().get(0).getNumUniqueAnsweredQuizzes() == 0
        dashboard.getQuizStatsDto().get(0).getAverageQuizzesSolved() == 0
    }

    @Unroll
    def "create a teacher dashboard and set Id"(){
        given: "teacher dashboard dto"
        def teacherDashboardDto = new TeacherDashboardDto()

        when: "the dashboard id is seted"
        teacherDashboardDto.setId(1)

        then: "the is correct"
        teacherDashboardDto.getId() == 1
    }

     @Unroll
     def "create a teacher dashboard and set number of students"(){
        given: "teacher dashboard dto"
        def teacherDashboardDto = new TeacherDashboardDto()

        when: "the dashboard number of students is seted"
        teacherDashboardDto.setNumberOfStudents(1)

        then: "the is correct"
        teacherDashboardDto.getNumberOfStudents() == 1
    }

    @Unroll
    def "get a teacher dashboard for a teacher with no coruse executions"(){
        when: "a dashboard is retrieved"
        def dashboard = teacherDashboardService.getTeacherDashboard(externalCourseExecution.getId(), teacher.getId())

        then: "a exception is thrown"
        def exception = thrown(TutorException)
        exception.getErrorMessage() == ErrorMessage.TEACHER_NO_COURSE_EXECUTION

    }

    @Unroll
    def "test to string method on quizStatDto"(){
        given: "a quizStatDto"
        def quizStatDto = new QuizStatsDto()
        quizStatDto.setId(1)
        quizStatDto.setNumQuizzes(0)
        quizStatDto.setNumUniqueAnsweredQuizzes(0)
        quizStatDto.setAverageQuizzesSolved(0)
        
        when: "the to string method is called"
        def result = quizStatDto.toString()
            
        then: "the result is correct"
        result == "QuizStatsDto{" +
                "id=" + 1 +
                ", numQuizzes=" + 0 +
                ", numUniqueAnsweredQuizzes=" + 0 +
                ", averageQuizzesSolved=" + 0.0 +
                '}'
    }

    @Unroll
    def "test to string method on teacherDashboardDto"(){
        given: "a teacherDashboardDto"
        def dashboard = new TeacherDashboardDto()
        
        when: "the to string method is called"
        def result = dashboard.toString()
            
        then: "the result is correct"
        def tostr = "TeacherDashboardDto{" +
                "id=" + dashboard.getId() +
                ", numberOfStudents=" + dashboard.getNumberOfStudents() +
                ", numStudents=" + dashboard.getNumStudents() +
                ", numMore75CorrectQuestions=" + dashboard.getnumMore75CorrectQuestions() +
                ", numAtLeast3Quizes=" + dashboard.getnumAtLeast3Quizes() +
                '}'
        result == tostr
    }


    @Unroll
    @TestConfiguration
    static class LocalBeanConfiguration extends BeanConfiguration {}
}
