package pt.ulisboa.tecnico.socialsoftware.tutor.questionsubmission.webservice

import groovyx.net.http.RESTClient
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.web.server.LocalServerPort
import pt.ulisboa.tecnico.socialsoftware.tutor.SpockTest
import pt.ulisboa.tecnico.socialsoftware.tutor.course.domain.Course
import pt.ulisboa.tecnico.socialsoftware.tutor.course.domain.CourseExecution
import pt.ulisboa.tecnico.socialsoftware.tutor.question.domain.Question
import pt.ulisboa.tecnico.socialsoftware.tutor.question.dto.OptionDto
import pt.ulisboa.tecnico.socialsoftware.tutor.question.dto.QuestionDto
import pt.ulisboa.tecnico.socialsoftware.tutor.questionsubmission.dto.QuestionSubmissionDto
import pt.ulisboa.tecnico.socialsoftware.tutor.user.User

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class GetStudentQuestionSubmissionsWebServiceIT extends SpockTest {
    @LocalServerPort
    private int port

    def course
    def courseExecution
    def student
    def question
    def response

    def setup() {
        restClient = new RESTClient("http://localhost:" + port)

        course = new Course(COURSE_1_NAME, Course.Type.EXTERNAL)
        courseRepository.save(course)
        courseExecution = new CourseExecution(course, COURSE_1_ACRONYM, COURSE_1_ACADEMIC_TERM, Course.Type.EXTERNAL)
        courseExecutionRepository.save(courseExecution)

        student = new User(USER_1_NAME, USER_1_EMAIL, USER_1_EMAIL, User.Role.STUDENT, true, false)
        student.setPassword(passwordEncoder.encode(USER_1_PASSWORD))
        student.addCourse(courseExecution)
        courseExecution.addUser(student)
        userRepository.save(student)

        def questionDto = new QuestionDto()
        questionDto.setTitle(QUESTION_1_TITLE)
        questionDto.setContent(QUESTION_1_CONTENT)
        questionDto.setStatus(Question.Status.SUBMITTED.name())
        def optionDto = new OptionDto()
        optionDto.setContent(OPTION_1_CONTENT)
        optionDto.setCorrect(true)
        def options = new ArrayList<OptionDto>()
        options.add(optionDto)
        questionDto.setOptions(options)

        def questionSubmissionDto = new QuestionSubmissionDto()
        questionSubmissionDto.setCourseExecutionId(courseExecution.getId())
        questionSubmissionDto.setSubmitterId(student.getId())
        questionSubmissionDto.setQuestion(questionDto)

        questionSubmissionService.createQuestionSubmission(questionSubmissionDto)
        question = questionRepository.findAll().get(0)

        createdUserLogin(USER_1_EMAIL, USER_1_PASSWORD)
    }

    def "get student's question submission"() {
        when:
        response = restClient.get(
                path: '/submissions/'+courseExecution.getId()+'/student',
                requestContentType: 'application/json'
        )

        then: "check the response status"
        response != null
        response.status == 200
        and: "if it responds with the correct question submissions"
        def submissions = response.data
        submissions.get(0).id != null
        submissions.get(0).question.id == question.getId()
        submissions.get(0).submitterId == student.getId()
        submissions.get(0).courseExecutionId == courseExecution.getId()
    }

    def cleanup() {
        persistentCourseCleanup()

        userRepository.deleteById(student.getId())
        courseExecutionRepository.deleteById(courseExecution.getId())

        courseRepository.deleteById(course.getId())
    }
}




