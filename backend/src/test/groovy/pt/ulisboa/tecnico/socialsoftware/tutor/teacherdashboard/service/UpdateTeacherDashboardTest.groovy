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

import javax.swing.text.html.Option
import java.util.logging.Handler

@DataJpaTest
class UpdateTeacherDashboardTest extends SpockTest {
    def teacher

    def setup() {
        createExternalCourseAndExecution()

        teacher = new Teacher(USER_1_NAME, false)
        userRepository.save(teacher)
        teacher.addCourse(externalCourseExecution)
    }

    def createQuestion(available=true) {
        def question = new Question()
        question.setTitle("Question Title")
        question.setCourse(externalCourse)
        if (available == true){
            question.setStatus(Question.Status.AVAILABLE)
        }
        else{
            question.setStatus(Question.Status.SUBMITTED)
        }
        def questionDetails = new MultipleChoiceQuestion()
        questionDetails.setQuestion(question)
        question.setQuestionDetails(questionDetails)
        questionRepository.save(question)
        questionDetailsRepository.save(questionDetails)

        def option = new Option()
        option.setContent("Option Content")
        option.setCorrect(true)
        option.setSequence(0)
        option.setQuestionDetails(questionDetails)
        questionDetails.addOption(option)
        optionRepository.save(option)

        def optionKO = new Option()
        optionKO.setContent("Option Content")
        optionKO.setCorrect(false)
        optionKO.setSequence(1)
        optionKO.setQuestionDetails(questionDetails)
        questionDetails.addOption(optionKO)
        optionRepository.save(optionKO)

        return question;
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

    def "update all teacher dashboard"(){
        given: "three students"
        def student1 = new Student(USER_1_NAME, false)
        userRepository.save(student1)
        externalCourseExecution.addUser(student1)

        def student2 = new Student(USER_2_NAME, false)
        userRepository.save(student2)
        externalCourseExecution.addUser(student2)

        def student3 = new Student(USER_3_NAME, false)
        userRepository.save(student3)
        externalCourseExecution.addUser(student3)

        and: "three quizzes"
        def quiz1 = new Quiz(externalCourseExecution)
        quiz1.setStatus(Quiz.Status.AVAILABLE)
        quiz1.setTitle("Quiz 1")
        quiz1Repository.save(quiz1)

        def quiz2 = new Quiz(externalCourseExecution)
        quiz2.setStatus(Quiz.Status.AVAILABLE)
        quiz2.setTitle("Quiz 2")
        quiz2Repository.save(quiz2)

        def quiz3 = new Quiz(externalCourseExecution)
        quiz3.setStatus(Quiz.Status.AVAILABLE)
        quiz3.setTitle("Quiz 3")
        quiz3Repository.save(quiz3)

        and: "three questions for each quiz"
        def quizQuestion1 = new QuizQuestion(quiz1, createQuestion())
        quizQuestion1.setSequence(0)
        quizQuestionRepository.save(quizQuestion1)

        def quizQuestion2 = new QuizQuestion(quiz1, createQuestion())
        quizQuestion2.setSequence(1)
        quizQuestionRepository.save(quizQuestion2)

        def quizQuestion3 = new QuizQuestion(quiz1, createQuestion())
        quizQuestion3.setSequence(2)
        quizQuestionRepository.save(quizQuestion3)

        def quizQuestion4 = new QuizQuestion(quiz2, createQuestion())
        quizQuestion4.setSequence(0)
        quizQuestionRepository.save(quizQuestion4)

        def quizQuestion5 = new QuizQuestion(quiz2, createQuestion())
        quizQuestion5.setSequence(1)
        quizQuestionRepository.save(quizQuestion5)

        def quizQuestion6 = new QuizQuestion(quiz2, createQuestion())
        quizQuestion6.setSequence(2)
        quizQuestionRepository.save(quizQuestion6)

        def quizQuestion7 = new QuizQuestion(quiz3, createQuestion())
        quizQuestion7.setSequence(0)
        quizQuestionRepository.save(quizQuestion7)

        def quizQuestion8 = new QuizQuestion(quiz3, createQuestion())
        quizQuestion8.setSequence(1)

        and: "four questions"
        def q1 = createQuestion()
        def q2 = createQuestion()
        def q3 = createQuestion()
        def q4 = createQuestion()
        def q5 = createQuestion()
}

    @TestConfiguration
    static class LocalBeanConfiguration extends BeanConfiguration {}

}
