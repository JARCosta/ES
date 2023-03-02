package pt.ulisboa.tecnico.socialsoftware.tutor.teacherdashboard.service

import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest
import org.springframework.boot.test.context.TestConfiguration
import pt.ulisboa.tecnico.socialsoftware.tutor.BeanConfiguration
import pt.ulisboa.tecnico.socialsoftware.tutor.SpockTest
import pt.ulisboa.tecnico.socialsoftware.tutor.teacherdashboard.domain.QuizStats
import pt.ulisboa.tecnico.socialsoftware.tutor.teacherdashboard.domain.TeacherDashboard
import pt.ulisboa.tecnico.socialsoftware.tutor.user.domain.Teacher
import pt.ulisboa.tecnico.socialsoftware.tutor.quiz.domain.Quiz
import pt.ulisboa.tecnico.socialsoftware.tutor.answer.domain.QuizAnswer
import pt.ulisboa.tecnico.socialsoftware.tutor.user.domain.Student
import pt.ulisboa.tecnico.socialsoftware.tutor.auth.domain.AuthUser


@DataJpaTest
class CreateQuizStatsTest extends SpockTest {

    def teacher
    def user
    def teacherdashboard
    def quizstats
    def newquiz
    def quizanswer


    def setup() {
        createExternalCourseAndExecution()

        teacher = new Teacher(USER_1_NAME, false)
        userRepository.save(teacher)

        user = new Student(USER_1_NAME, USER_1_USERNAME, USER_1_EMAIL, false, AuthUser.Type.TECNICO)
        user.addCourse(externalCourseExecution)
        userRepository.save(user)

        teacherdashboard = new TeacherDashboard(externalCourseExecution, teacher)
        
    }
    
    def "create quiz stats for a course execution"(){
        when: "quiz stat is created to a course execution"
        quizstats = new QuizStats(externalCourseExecution)

        then: "all stats are zero"
        quizstats.getNumberOfQuizzes() == 0
        quizstats.getNumberOfUniqueQuizzesSolved() == 0
        quizstats.getAverageQuizzesSolved() == 0
    }

    def "create quiz and update"(){
        given: "quiz stat for a course execution"
        quizstats = new QuizStats(externalCourseExecution)

        when: "a quiz is added"
        newquiz = new Quiz()
        externalCourseExecution.addQuiz(newquiz)

        and: "a student solves the quizz"
        quizanswer = new QuizAnswer(user, newquiz)
        quizanswer.setCompleted(true)

        and: "quizz stats are updated"
        quizstats.update();

        then:
        quizstats.getNumberOfQuizzes() == 1
        quizstats.getNumberOfUniqueQuizzesSolved() == 1
        quizstats.getAverageQuizzesSolved() == 1
    }

    def "get quiz states with teacher dashboard"(){
        given: "current course execution quizz stats"
        int cexIndex = teacherdashboard.getQuizStats().size() - 1
        quizstats = teacherdashboard.getQuizStats().get(cexIndex)

        when: "a quiz is added"
        newquiz = new Quiz()
        externalCourseExecution.addQuiz(newquiz)

        and: "a student solves the quizz"
        quizanswer = new QuizAnswer(user, newquiz)
        quizanswer.setCompleted(true)

        and: "quizz stats are updated"
        teacherdashboard.update()

        then:
        quizstats.getNumberOfQuizzes() == 1
        quizstats.getNumberOfUniqueQuizzesSolved() == 1
        quizstats.getAverageQuizzesSolved() == 1
    }
        
    @TestConfiguration
    static class LocalBeanConfiguration extends BeanConfiguration {}
}
