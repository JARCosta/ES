package pt.ulisboa.tecnico.socialsoftware.tutor.question.service

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest
import org.springframework.boot.test.context.TestConfiguration
import org.springframework.context.annotation.Bean
import pt.ulisboa.tecnico.socialsoftware.tutor.question.dto.ImageDto
import pt.ulisboa.tecnico.socialsoftware.tutor.question.dto.OptionDto
import pt.ulisboa.tecnico.socialsoftware.tutor.question.dto.QuestionDto
import pt.ulisboa.tecnico.socialsoftware.tutor.question.repository.QuestionRepository
import spock.lang.Specification

@DataJpaTest
class CreateQuestionServiceSpockTest extends Specification {
    public static final String QUESTION_TITLE = 'question title'
    public static final String QUESTION_CONTENT = 'question content'
    public static final String OPTION_CONTENT = "optionId content"
    public static final String URL = 'URL'

    @Autowired
    QuestionService questionService

    @Autowired
    QuestionRepository questionRepository

    def "create a question with no image and one option"() {
        given: "a questionDto"
        def question = new QuestionDto()
        question.setTitle(QUESTION_TITLE)
        question.setContent(QUESTION_CONTENT)
        question.setActive(true)
        and: 'a optionId'
        def option = new OptionDto()
        option.setContent(OPTION_CONTENT)
        option.setCorrect(true)
        def options = new ArrayList<>()
        options.add(option)
        question.setOptions(options)

        when:
        questionService.createQuestion(question)

        then: "the correct question is inside the repository"
        questionRepository.count() == 1L
        def result = questionRepository.findAll().get(0)
        result.getId() != null
        result.getNumber() == 1
        result.getActive()
        result.getTitle() == QUESTION_TITLE
        result.getContent() == QUESTION_CONTENT
        result.getImage() == null
        result.getOptions().size() == 1
        def resOption = result.getOptions().get(0)
        resOption.getContent() == OPTION_CONTENT
        resOption.getCorrect()

    }

    def "create a question with image and two options"() {
        given: "a questionDto"
        def question = new QuestionDto()
        question.setTitle(QUESTION_TITLE)
        question.setContent(QUESTION_CONTENT)
        question.setActive(true)

        and: 'an image'
        def image = new ImageDto()
        image.setUrl(URL)
        image.setWidth(20)
        question.setImage(image)
        and: 'two options'
        def option = new OptionDto()
        option.setContent(OPTION_CONTENT)
        option.setCorrect(true)
        def options = new ArrayList<>()
        options.add(option)
        option = new OptionDto()
        option.setContent(OPTION_CONTENT)
        option.setCorrect(false)
        options.add(option)
        question.setOptions(options)

        when:
        questionService.createQuestion(question)

        then: "the correct question is inside the repository"
        questionRepository.count() == 1L
        def result = questionRepository.findAll().get(0)
        result.getId() != null
        result.getNumber() == 1
        result.getActive()
        result.getTitle() == QUESTION_TITLE
        result.getContent() == QUESTION_CONTENT
        result.getImage().getId() != null
        result.getImage().getUrl() == URL
        result.getImage().getWidth() == 20
        result.getOptions().size() == 2
    }

    def "create two questions"() {
        given: "a questionDto"
        def question = new QuestionDto()
        question.setTitle(QUESTION_TITLE)
        question.setContent(QUESTION_CONTENT)
        question.setActive(true)
        and: 'a optionId'
        def option = new OptionDto()
        option.setContent(OPTION_CONTENT)
        option.setCorrect(true)
        def options = new ArrayList<>()
        options.add(option)
        question.setOptions(options)

        when: 'are created two questions'
        questionService.createQuestion(question)
        questionService.createQuestion(question)

        then: "the two questions are created with the correct numbers"
        questionRepository.count() == 2L
        def resultOne = questionRepository.findAll().get(0)
        def resultTwo = questionRepository.findAll().get(1)
        resultOne.getNumber() + resultTwo.getNumber() == 3
    }



    @TestConfiguration
    static class QuestionServiceImplTestContextConfiguration {

        @Bean
        QuestionService questionService() {
            return new QuestionService()
        }
    }

}