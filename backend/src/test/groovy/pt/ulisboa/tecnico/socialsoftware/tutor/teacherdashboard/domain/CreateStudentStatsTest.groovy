package pt.ulisboa.tecnico.socialsoftware.tutor.teacherdashboard.service;

import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest
import org.springframework.boot.test.context.TestConfiguration
import pt.ulisboa.tecnico.socialsoftware.tutor.BeanConfiguration
import pt.ulisboa.tecnico.socialsoftware.tutor.SpockTest
import pt.ulisboa.tecnico.socialsoftware.tutor.answer.domain.MultipleChoiceAnswer
import pt.ulisboa.tecnico.socialsoftware.tutor.answer.domain.QuestionAnswer
import pt.ulisboa.tecnico.socialsoftware.tutor.answer.domain.QuizAnswer
import pt.ulisboa.tecnico.socialsoftware.tutor.auth.domain.AuthUser
import pt.ulisboa.tecnico.socialsoftware.tutor.execution.dto.CourseExecutionDto
import pt.ulisboa.tecnico.socialsoftware.tutor.question.domain.MultipleChoiceQuestion
import pt.ulisboa.tecnico.socialsoftware.tutor.question.domain.Option
import pt.ulisboa.tecnico.socialsoftware.tutor.question.domain.Question
import pt.ulisboa.tecnico.socialsoftware.tutor.quiz.domain.Quiz
import pt.ulisboa.tecnico.socialsoftware.tutor.quiz.domain.QuizQuestion
import pt.ulisboa.tecnico.socialsoftware.tutor.user.domain.Student
import pt.ulisboa.tecnico.socialsoftware.tutor.utils.DateHandler
import spock.lang.Unroll
import pt.ulisboa.tecnico.socialsoftware.tutor.user.domain.Teacher
import pt.ulisboa.tecnico.socialsoftware.tutor.teacherdashboard.domain.TeacherDashboard
import pt.ulisboa.tecnico.socialsoftware.tutor.teacherdashboard.domain.StudentStats

@DataJpaTest
class CreateStudentStatsTest extends SpockTest {
    def student
    def teacher
    def question1
    def question2
    def question3
    def question4
    // def courseDto
    def optionOK
    def optionKO
    def quiz1
    def quiz2
    def quiz3
    def quiz1Question
    def quiz2Question
    def quiz3Question

    def setup() {
        createExternalCourseAndExecution()

        // courseDto = new CourseExecutionDto(externalCourseExecution)

        student = new Student(USER_1_NAME, USER_1_USERNAME, USER_1_EMAIL, false, AuthUser.Type.TECNICO)
        student.addCourse(externalCourseExecution)
        userRepository.save(student)
        
        teacher = new Teacher(USER_1_NAME, false)
        userRepository.save(teacher)

        question1 = new Question()
        question1.setKey(1)
        question1.setCourse(externalCourse)
        question1.setContent("Question Content")
        question1.setTitle("Question Title")
        questionRepository.save(question1)

        question2 = new Question()
        question2.setKey(2)
        question2.setCourse(externalCourse)
        question2.setContent("Question Content")
        question2.setTitle("Question Title")
        questionRepository.save(question2)

        question3 = new Question()
        question3.setKey(3)
        question3.setCourse(externalCourse)
        question3.setContent("Question Content")
        question3.setTitle("Question Title")
        questionRepository.save(question3)

        question4 = new Question()
        question4.setKey(4)
        question4.setCourse(externalCourse)
        question4.setContent("Question Content")
        question4.setTitle("Question Title")
        questionRepository.save(question4)

        def questionDetails = new MultipleChoiceQuestion();
        question1.setQuestionDetails(questionDetails);
        question2.setQuestionDetails(questionDetails);
        question3.setQuestionDetails(questionDetails);
        question4.setQuestionDetails(questionDetails);
        questionDetailsRepository.save(questionDetails);

        optionOK = new Option()
        optionOK.setContent("Option Content")
        optionOK.setCorrect(true)
        optionOK.setSequence(0)
        optionOK.setQuestionDetails(questionDetails)
        optionRepository.save(optionOK)

        optionKO = new Option()
        optionKO.setContent("Option Content")
        optionKO.setCorrect(false)
        optionKO.setSequence(0)
        optionKO.setQuestionDetails(questionDetails)
        optionRepository.save(optionKO)
    }

    def "create empty student statistics"() {

        when: "a student statistics is created"
        def teacherDashboard = new TeacherDashboard(externalCourseExecution, teacher)
        teacherDashboardRepository.save(teacherDashboard)
        def studentStats = new StudentStats(teacherDashboard)
        StudentStatsRepository.save(studentStats)

        then: "the new student statistics is correctly persisted"
        studentStatsRepository.count() == 1L
        studentStatsRepository.findAll().get(0).getId() != 0
        studentStatsRepository.findAll().get(0).getCourseExecution().getId() == externalCourseExecution.getId()

        and: "the teacher dashboard has a reference for the student statistics"
        teacherDashboard.getStudentStats().size() == 1
        teacherDashboard.getStudentStats().contains(studentStats)
    }

    def "get number of students"(){
        
        when:"a student statistics is created"
        def teacherDashboard = new TeacherDashboard(externalCourseExecution, teacher)
        teacherDashboardRepository.save(teacherDashboard)
        def studentStats = new StudentStats(teacherDashboard)
        StudentStatsRepository.save(studentStats)
        
        and: "a student is added to student statistics"
        studentStatsRepository.save(student)

        and: "updated"
        teacherDashboard.update()
        
        then: "the number of students is correct"
        studentStats.getNumStudents() == 1
        studentStats.toString() == "StudentStats{id=2, numStudents=1, numMore75CorrectQuestions=0, numAtLeast3Quizes=0}"
        
        studentStats.remove() == null
        teacherDashboard.getStudentStats().size() == 0
        studentStats.getCourseExecution() == null
        studentStats.getTeacherDashboard() == null

    }

    def createAndAnswerQuizz(Question questionN, boolean isCorrect){
        def quiz = new Quiz()
        quiz.setKey(1)
        quiz.setTitle(QUIZ_TITLE)
        quiz.setAvailableDate(LOCAL_DATE_BEFORE)
        quiz.setType(Quiz.QuizType.IN_CLASS.toString())
        quiz.setConclusionDate(LOCAL_DATE_TOMORROW)
        quiz.setResultsDate(null)
        quiz.setCourseExecution(externalCourseExecution)

        def quizQuestion = new QuizQuestion()
        quizQuestion.setSequence(1)
        quizQuestion.setQuiz(quiz)
        quizQuestion.setQuestion(questionN)

        def quizAnswer = new QuizAnswer()
        quizAnswer.setAnswerDate(DateHandler.now())
        quizAnswer.setCompleted(true)
        quizAnswer.setStudent(student)
        quizAnswer.setQuiz(quiz)

        def questionAnswer = new QuestionAnswer()
        questionAnswer.setSequence(0)
        questionAnswer.setQuizAnswer(quizAnswer)
        questionAnswer.setQuizQuestion(quizQuestion)
        def answerDetails = new MultipleChoiceAnswer(questionAnswer)
        if(isCorrect){
            answerDetails.setOption(optionOK);
        }else {
            answerDetails.setOption(optionKO);
        }
        questionAnswer.setAnswerDetails(answerDetails);

        quizRepository.save(quiz)
        quizAnswerRepository.save(quizAnswer)
        questionAnswerRepository.save(questionAnswer)
        answerDetailsRepository.save(answerDetails)

    }

    def "get number of students with at least 75% of correct questions"(){
        when:"a student statistics is created"
        def teacherDashboard = new TeacherDashboard(externalCourseExecution, teacher)
        teacherDashboardRepository.save(teacherDashboard)
        def studentStats = new StudentStats(teacherDashboard)
        StudentStatsRepository.save(studentStats)
        
        and: "a student is added to student statistics"
        studentStatsRepository.save(student)
        
        and: "four questions are  answered by a student"
        
        Question[] questions = new Question[4]
        questions[0] = question1
        questions[1] = question2
        questions[2] = question3
        questions[3] = question4

        for (int i = 0; i < numCorrectAnswers; i++) {
            createAndAnswerQuizz(questions[i], true)
        }
        for (int i = numCorrectAnswers; i < 4; i++) {
            createAndAnswerQuizz(questions[i], false)
        }

        // createAndAnswerQuizz(question1, true)
        // createAndAnswerQuizz(question2, true)
        // createAndAnswerQuizz(question3, true)
        // createAndAnswerQuizz(question4, true)

        teacherDashboard.update()


        then: "the number of students with at least 75% of correct questions is correct"
        if(numCorrectAnswers >= 3){
            studentStats.getNumMore75CorrectQuestions() == 1
        }else{
            studentStats.getNumMore75CorrectQuestions() == 0
        }

        where:
        numCorrectAnswers << [0,1,2,3,4]
    }

    @Unroll
    def "get number of students with at least 3 quizzes"(){

        when:"a student statistics is created"
        def teacherDashboard = new TeacherDashboard(externalCourseExecution, teacher)
        teacherDashboardRepository.save(teacherDashboard)
        def studentStats = new StudentStats(teacherDashboard)
        StudentStatsRepository.save(studentStats)
        
        and: "a student is added to student statistics"
        studentStatsRepository.save(student)


        and: "three quizzes are  answered by a student"
        quiz1 = new Quiz()
        quiz1.setKey(1)
        quiz1.setTitle(QUIZ_TITLE)
        quiz1.setAvailableDate(LOCAL_DATE_BEFORE)
        quiz1.setType(Quiz.QuizType.IN_CLASS.toString())
        quiz1.setConclusionDate(LOCAL_DATE_TOMORROW)
        quiz1.setResultsDate(null)
        quiz1.setCourseExecution(externalCourseExecution)

        quiz1Question = new QuizQuestion()
        quiz1Question.setSequence(1)
        quiz1Question.setQuiz(quiz1)
        quiz1Question.setQuestion(question1)

        def quiz1Answer = new QuizAnswer()
        quiz1Answer.setAnswerDate(DateHandler.now())
        quiz1Answer.setCompleted(true)
        quiz1Answer.setStudent(student)
        quiz1Answer.setQuiz(quiz1)

        def question1Answer = new QuestionAnswer()
        question1Answer.setSequence(0)
        question1Answer.setQuizAnswer(quiz1Answer)
        question1Answer.setQuizQuestion(quiz1Question)
        def answer1Details = new MultipleChoiceAnswer(question1Answer, optionOK);
        question1Answer.setAnswerDetails(answer1Details);

        quizRepository.save(quiz1)
        quizAnswerRepository.save(quiz1Answer)
        questionAnswerRepository.save(question1Answer)
        answerDetailsRepository.save(answer1Details)

        quiz2 = new Quiz()
        quiz2.setKey(2)
        quiz2.setTitle(QUIZ_TITLE)
        quiz2.setAvailableDate(LOCAL_DATE_BEFORE)
        quiz2.setType(Quiz.QuizType.IN_CLASS.toString())
        quiz2.setConclusionDate(LOCAL_DATE_TOMORROW)
        quiz2.setResultsDate(null)
        quiz2.setCourseExecution(externalCourseExecution)

        quiz2Question = new QuizQuestion()
        quiz2Question.setSequence(1)
        quiz2Question.setQuiz(quiz2)
        quiz2Question.setQuestion(question2)

        def quiz2Answer = new QuizAnswer()
        quiz2Answer.setAnswerDate(DateHandler.now())
        quiz2Answer.setCompleted(true)
        quiz2Answer.setStudent(student)
        quiz2Answer.setQuiz(quiz2)

        def question2Answer = new QuestionAnswer()
        question2Answer.setSequence(0)
        question2Answer.setQuizAnswer(quiz2Answer)
        question2Answer.setQuizQuestion(quiz2Question)
        def answer2Details = new MultipleChoiceAnswer(question2Answer, optionOK);
        question2Answer.setAnswerDetails(answer2Details);

        quizRepository.save(quiz2)
        quizAnswerRepository.save(quiz2Answer)
        questionAnswerRepository.save(question2Answer)
        answerDetailsRepository.save(answer2Details)

        quiz3 = new Quiz()
        quiz3.setKey(3)
        quiz3.setTitle(QUIZ_TITLE)
        quiz3.setAvailableDate(LOCAL_DATE_BEFORE)
        quiz3.setType(Quiz.QuizType.IN_CLASS.toString())
        quiz3.setConclusionDate(LOCAL_DATE_TOMORROW)
        quiz3.setResultsDate(null)
        quiz3.setCourseExecution(externalCourseExecution)

        quiz3Question = new QuizQuestion()
        quiz3Question.setSequence(1)
        quiz3Question.setQuiz(quiz3)
        quiz3Question.setQuestion(question3)

        def quiz3Answer = new QuizAnswer()
        quiz3Answer.setAnswerDate(DateHandler.now())
        quiz3Answer.setCompleted(true)
        quiz3Answer.setStudent(student)
        quiz3Answer.setQuiz(quiz3)

        def question3Answer = new QuestionAnswer()
        question3Answer.setSequence(0)
        question3Answer.setQuizAnswer(quiz3Answer)
        question3Answer.setQuizQuestion(quiz3Question)
        def answer3Details = new MultipleChoiceAnswer(question3Answer, optionOK);
        question3Answer.setAnswerDetails(answer3Details);

        quizRepository.save(quiz3)
        quizAnswerRepository.save(quiz3Answer)
        questionAnswerRepository.save(question3Answer)
        answerDetailsRepository.save(answer3Details)

        and: "and the teacher dashboard is updated"
        teacherDashboard.update()

        then: "the number of students who did at least 3 quizzes is correct"
        studentStats.getNumStudents() == 1
        studentStats.getNumAtLeast3Quizes() == 1
        for (Student user: studentStats.getCourseExecution().getStudents()) {
            for (QuizAnswer answer: user.getQuizAnswers()) {
                println("_____________________________________________________________________________")
                println("The Value is: ")
                println(answer.getNumberOfCorrectAnswers() / answer.getQuiz().getQuizQuestionsNumber())
                println("_____________________________________________________________________________")
            }
        }
    }


    @TestConfiguration
    static class LocalBeanConfiguration extends BeanConfiguration {}

}