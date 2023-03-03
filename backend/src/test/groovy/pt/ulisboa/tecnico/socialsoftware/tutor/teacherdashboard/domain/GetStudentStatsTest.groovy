// package pt.ulisboa.tecnico.socialsoftware.tutor.teacherdashboard.service

// import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest
// import org.springframework.boot.test.context.TestConfiguration
// import pt.ulisboa.tecnico.socialsoftware.tutor.BeanConfiguration
// import pt.ulisboa.tecnico.socialsoftware.tutor.SpockTest
// import pt.ulisboa.tecnico.socialsoftware.tutor.exceptions.ErrorMessage
// import pt.ulisboa.tecnico.socialsoftware.tutor.exceptions.TutorException
// import spock.lang.Unroll
// import pt.ulisboa.tecnico.socialsoftware.tutor.teacherdashboard.domain.StudentStats

// @DataJpaTest
// class GetStudentStatsTest extends SpockTest {
    
//     def authUserDto
//     def courseExecutionDto
//     def quiz

//     def setup() {
//         def courseExecution1 = createExternalCourseAndExecution();
//         def courseExecution2 = createExternalCourseAndExecution();
//         def courseExecution3 = createExternalCourseAndExecution();

//         def user1 = new Student(USER_1_NAME, USER_1_USERNAME, USER_1_EMAIL, false, AuthUser.Type.EXTERNAL)
//         userRepository.save(user1)

//         def user2 = new Student(USER_2_NAME, USER_2_USERNAME, USER_2_EMAIL, false, AuthUser.Type.EXTERNAL)
//         userRepository.save(user2)

//         user1.addCourse(courseExecution1)
//         user2.addCourse(courseExecution3)

//         def question1 = new Question()
//         def question2 = new Question()
//         def question3 = new Question()
//         def question4 = new Question()

//         def quiz = new Quiz()
//         quiz.setKey(1)
//         quizQuestion1 = new QuizQuestion(quiz, question1, 0)
//         quizQuestion2 = new QuizQuestion(quiz, question2, 1)
//         quizQuestion3 = new QuizQuestion(quiz, question3, 2)
//         quizQuestion4 = new QuizQuestion(quiz, question4, 3)



//     }

//     def "get number of total students per execution"() {
        
//         when: ""
//         def year1 = courseExecution1.getStudentStats().getNumStudents()
//         def year2 = courseExecution2.getStudentStats().getNumStudents()
//         def year3 = courseExecution3.getStudentStats().getNumStudents()

//         then: ""
//         year1 == 1
//         year2 == 0
//         year3 == 1
        
//     }

//     def "get number of students who answered correctly more than 75% of the questions"() {

//         given:

//         def quizAnswer1 = new QuizAnswer(user1, quiz)
//         def questionAnswer1 = new QuestionAnswer(quizAnswer1, quizQuestionOne, 10, 0)
        
//         def quizAnswer2 = new QuizAnswer(user2, quiz)
//         def questionAnswer2 = new QuestionAnswer(quizAnswer2, quizQuestionOne, 10, 0)
        
//         Option optionCorrect = new Option()
//         optionCorrect.setContent("Option Content")
//         optionCorrect.setCorrect(true)
//         optionCorrect.setSequence(0)

//         Option optionFalse = new Option()
//         optionFalse.setContent("Option Content")
//         optionFalse.setCorrect(true)
//         optionFalse.setSequence(0)

//         questionAnswer1.setAnswerDetails(new MultipleChoiceAnswer(questionAnswer1, optionCorrect))
//         questionAnswer2.setAnswerDetails(new MultipleChoiceAnswer(questionAnswer2, optionFalse))
        
//         when: ""
        
//         def year1 = courseExecution1.getStudentStats().getNumMore75CorrectQuestions()

//         then: ""
//         dashboardDto.getId() == getDashboardDto.getId()
//     }

//     def "cannot get a dashboard for a user that does not belong to the course execution"() {
//         given: "another course execution"
//         createExternalCourseAndExecution()

//         when: "get a dashboard"
//         teacherDashboardService.getTeacherDashboard(externalCourseExecution.getId(), authUserDto.getId())

//         then: "exception is thrown"
//         def exception = thrown(TutorException)
//         exception.getErrorMessage() == ErrorMessage.TEACHER_NO_COURSE_EXECUTION
//     }

//     @Unroll
//     def "cannot get a dashboard with invalid courseExecutionId=#courseExecutionId"() {
//         when:
//         teacherDashboardService.getTeacherDashboard(courseExecutionId, authUserDto.getId())

//         then: "an exception is thrown"
//         def exception = thrown(TutorException)
//         exception.getErrorMessage() == ErrorMessage.COURSE_EXECUTION_NOT_FOUND

//         where:
//         courseExecutionId << [0, 100]
//     }

//     @Unroll
//     def "cannot get a dashboard with invalid teacherId=#teacherId"() {
//         when:
//         teacherDashboardService.getTeacherDashboard(courseExecutionDto.getCourseExecutionId(), teacherId)

//         then: "an exception is thrown"
//         def exception = thrown(TutorException)
//         exception.getErrorMessage() == ErrorMessage.USER_NOT_FOUND

//         where:
//         teacherId << [0, 100]
//     }

//     @TestConfiguration
//     static class LocalBeanConfiguration extends BeanConfiguration {}
// }
