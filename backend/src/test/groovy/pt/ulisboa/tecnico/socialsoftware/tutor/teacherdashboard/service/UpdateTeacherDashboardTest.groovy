package pt.ulisboa.tecnico.socialsoftware.tutor.teacherdashboard.service

import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest
import org.springframework.boot.test.context.TestConfiguration
import pt.ulisboa.tecnico.socialsoftware.tutor.BeanConfiguration
import pt.ulisboa.tecnico.socialsoftware.tutor.SpockTest
import pt.ulisboa.tecnico.socialsoftware.tutor.teacherdashboard.domain.TeacherDashboard
import pt.ulisboa.tecnico.socialsoftware.tutor.teacherdashboard.domain.StudentStats
import pt.ulisboa.tecnico.socialsoftware.tutor.teacherdashboard.dto.TeacherDashboardDto
import pt.ulisboa.tecnico.socialsoftware.tutor.exceptions.ErrorMessage
import pt.ulisboa.tecnico.socialsoftware.tutor.exceptions.TutorException
import pt.ulisboa.tecnico.socialsoftware.tutor.user.domain.Teacher
import pt.ulisboa.tecnico.socialsoftware.tutor.quiz.domain.Quiz
import pt.ulisboa.tecnico.socialsoftware.tutor.teacherdashboard.domain.QuizStats
import pt.ulisboa.tecnico.socialsoftware.tutor.execution.CourseExecutionService
import pt.ulisboa.tecnico.socialsoftware.tutor.execution.domain.CourseExecution
import pt.ulisboa.tecnico.socialsoftware.tutor.execution.repository.CourseExecutionRepository
import pt.ulisboa.tecnico.socialsoftware.tutor.execution.CourseExecutionService
import pt.ulisboa.tecnico.socialsoftware.tutor.execution.domain.CourseExecution
import pt.ulisboa.tecnico.socialsoftware.tutor.execution.repository.CourseExecutionRepository
import pt.ulisboa.tecnico.socialsoftware.tutor.question.domain.Course
import pt.ulisboa.tecnico.socialsoftware.tutor.user.domain.Student
import pt.ulisboa.tecnico.socialsoftware.tutor.exceptions.ErrorMessage
import pt.ulisboa.tecnico.socialsoftware.tutor.exceptions.TutorException
import pt.ulisboa.tecnico.socialsoftware.tutor.answer.domain.QuizAnswer

import pt.ulisboa.tecnico.socialsoftware.tutor.answer.domain.MultipleChoiceAnswer
import pt.ulisboa.tecnico.socialsoftware.tutor.answer.domain.QuestionAnswer
import pt.ulisboa.tecnico.socialsoftware.tutor.answer.domain.QuizAnswer
import pt.ulisboa.tecnico.socialsoftware.tutor.question.domain.MultipleChoiceQuestion
import pt.ulisboa.tecnico.socialsoftware.tutor.question.domain.Option
import pt.ulisboa.tecnico.socialsoftware.tutor.question.domain.Question
import pt.ulisboa.tecnico.socialsoftware.tutor.quiz.domain.Quiz
import pt.ulisboa.tecnico.socialsoftware.tutor.quiz.domain.QuizQuestion
import pt.ulisboa.tecnico.socialsoftware.tutor.user.domain.Student
import pt.ulisboa.tecnico.socialsoftware.tutor.auth.domain.AuthUser
import pt.ulisboa.tecnico.socialsoftware.tutor.utils.DateHandler

import spock.lang.Unroll

@DataJpaTest
class UpdateTeacherDashboardTest extends SpockTest {
    def teacher
    def quizQuestion

    def setup() {
        createExternalCourseAndExecution()

        teacher = new Teacher(USER_1_NAME, false)
        userRepository.save(teacher)
        teacher.addCourse(externalCourseExecution)
    }

    def createTeacherDashboard(teacher){
        def teacherDashboard = new TeacherDashboard(externalCourseExecution, teacher)
        teacherDashboardRepository.save(teacherDashboard)

        return teacherDashboard
    }

    def createQuizStat() {
        def quizStat = new QuizStats(teacherDashboard, externalCourseExecution)
        quizStatsRepository.save(quizStat)
        return quizStat
    }

     def createQuiz(cex) {
        // Quiz
        def quiz = new Quiz()
        quiz.setTitle("Quiz Title")
        quiz.setType(Quiz.QuizType.PROPOSED.toString())
        quiz.setCourseExecution(cex)
        quiz.setCreationDate(DateHandler.now())
        quiz.setAvailableDate(DateHandler.now())
        quizRepository.save(quiz)

        // Add Question
        def question = createQuestion()
        quizQuestion = createQuizQuestion(quiz, question)

        return quiz
    }

    def createQuestion() {
        def newQuestion = new Question()
        newQuestion.setTitle(QUESTION_1_TITLE)
        newQuestion.setCourse(externalCourse)
        def questionDetails = new MultipleChoiceQuestion()
        newQuestion.setQuestionDetails(questionDetails)
        questionRepository.save(newQuestion)

        def option = new Option()
        option.setContent(OPTION_1_CONTENT)
        option.setCorrect(true)
        option.setSequence(0)
        option.setQuestionDetails(questionDetails)
        optionRepository.save(option)
        def optionKO = new Option()
        optionKO.setContent(OPTION_2_CONTENT)
        optionKO.setCorrect(false)
        optionKO.setSequence(1)
        optionKO.setQuestionDetails(questionDetails)
        optionRepository.save(optionKO)

        return newQuestion;
    }

    def createQuizQuestion(quiz, question) {
        def quizQuestion = new QuizQuestion(quiz, question, 0)
        quizQuestionRepository.save(quizQuestion)
        return quizQuestion
    }

    def answerQuiz(quizQuestion, quiz, student, completed = true, date = DateHandler.now()) {
        def quizAnswer = new QuizAnswer()
        quizAnswer.setCompleted(completed)
        quizAnswer.setCreationDate(date)
        quizAnswer.setAnswerDate(date)
        quizAnswer.setStudent(student)
        quizAnswer.setQuiz(quiz)
        quizAnswerRepository.save(quizAnswer)

        def questionAnswer = new QuestionAnswer()
        questionAnswer.setTimeTaken(1)
        questionAnswer.setQuizAnswer(quizAnswer)
        questionAnswer.setQuizQuestion(quizQuestion)
        questionAnswerRepository.save(questionAnswer)

        def answerDetails
        def correctOption = quizQuestion.getQuestion().getQuestionDetails().getCorrectOption()
        answerDetails = new MultipleChoiceAnswer(questionAnswer, correctOption)
        questionAnswer.setAnswerDetails(answerDetails)
        answerDetailsRepository.save(answerDetails)
        return questionAnswer
    }

    def createStudent(username) {
        def student = new Student(USER_1_USERNAME, username, USER_1_EMAIL, false, AuthUser.Type.TECNICO)
        student.addCourse(externalCourseExecution)
        userRepository.save(student)
        return student;
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

    @Unroll
    def "create dashboard that has 3 courseExecutions associated with it and calls update"() {
        given: "a teacher dashboard with quizStats"
        def externalCourse = new Course(COURSE_1_NAME, Course.Type.TECNICO)
        courseRepository.save(externalCourse)


        def externalCourseExecution1 = new CourseExecution(externalCourse, COURSE_2_ACRONYM, COURSE_2_ACADEMIC_TERM, Course.Type.TECNICO, LOCAL_DATE_TODAY)
        courseExecutionRepository.save(externalCourseExecution1)

        def externalCourseExecution2 = new CourseExecution(externalCourse, COURSE_3_ACRONYM, COURSE_3_ACADEMIC_TERM, Course.Type.TECNICO, LOCAL_DATE_YESTERDAY)
        courseExecutionRepository.save(externalCourseExecution2)

        teacher.addCourse(externalCourseExecution1)

        when: "a dashboard is created"
        def dashboard = teacherDashboardService.createTeacherDashboard(externalCourseExecution1.getId(), teacher.getId())

        and: "the dashboard is updated with 3 courseExecutions"
        teacherDashboardService.updateTeacherDashboard(dashboard.getId())

        then: "the dashboard is saved to the database with 3 quizStats"
        teacherDashboardRepository.findAll().size() == 1L
        teacher.getDashboards().size() == 1
        quizStatsRepository.count() == 2

        and: "the dashboard has 0 numQuizzes, 0 uniqueQuestionsAnswered, 0 averageQuizzesSolved"
        def result = teacherDashboardRepository.findAll().get(0)
        result.getQuizStats().get(0).getNumQuizzes() == 0
        result.getQuizStats().get(0).getNumUniqueAnsweredQuizzes() == 0
        result.getQuizStats().get(0).getAverageQuizzesSolved() == 0
    }

    @Unroll
    def "cannot update dashboard that does not exist"() {
        when: "a dashboard is updated"
        teacherDashboardService.updateTeacherDashboard(1)

        then: "a TutorException is thrown"
        def e = thrown(TutorException)
        e.getErrorMessage() == ErrorMessage.DASHBOARD_NOT_FOUND
    }

    @Unroll
    def "create dashboard, update it, and then check if stats correct"(){
        given: "a teacher in a course execution"
        teacher.addCourse(externalCourseExecution)

        when: "a dashboard is created"
        def teacherdashboard = createTeacherDashboard(teacher)

        and: "a quizzStats for that quiz are created and addded to the teacher dashboard"
        def quizStats = new QuizStats(teacherdashboard, externalCourseExecution)

        and: "get the teacher dashboard dto"
        def dashboard = teacherDashboardService.getTeacherDashboard(externalCourseExecution.getId(), teacher.getId())

        then: "stats are 0"
        dashboard.getQuizStatsDto().get(0).getNumQuizzes() == 0
        dashboard.getQuizStatsDto().get(0).getNumUniqueAnsweredQuizzes() == 0
        dashboard.getQuizStatsDto().get(0).getAverageQuizzesSolved() == 0

        when: "the dashboard is updated"
        teacherDashboardService.updateTeacherDashboard(dashboard.getId())
        dashboard = teacherDashboardService.getTeacherDashboard(externalCourseExecution.getId(), teacher.getId())

        then: "stats are still 0"
        dashboard.getQuizStatsDto().get(0).getNumQuizzes() == 0
        dashboard.getQuizStatsDto().get(0).getNumUniqueAnsweredQuizzes() == 0
        dashboard.getQuizStatsDto().get(0).getAverageQuizzesSolved() == 0

        when: "quiz is creted and answered"
        def quiz = createQuiz(externalCourseExecution)
        def student = createStudent(USER_1_USERNAME)
        def questionAnswer = answerQuiz(quizQuestion, quiz, student)    

        and: "the dashboard is updated"
        teacherDashboardService.updateTeacherDashboard(dashboard.getId())
        dashboard = teacherDashboardService.getTeacherDashboard(externalCourseExecution.getId(), teacher.getId())

        then: "stats are correct"
        dashboard.getQuizStatsDto().get(0).getNumQuizzes() == 1
        dashboard.getQuizStatsDto().get(0).getNumUniqueAnsweredQuizzes() == 1
        dashboard.getQuizStatsDto().get(0).getAverageQuizzesSolved() == 1
    }

    def "try to update an non existing dashbord"(){
        when: "we invoke update on a non existing dashboard"
        teacherDashboardService.updateTeacherDashboard(null)

        then: "an exception is thrown"
        def exception = thrown(TutorException)
        exception.getErrorMessage() == ErrorMessage.DASHBOARD_NOT_FOUND
    }


    @Unroll
    @TestConfiguration
    static class LocalBeanConfiguration extends BeanConfiguration {}
}