package pt.ulisboa.tecnico.socialsoftware.tutor.teacherdashboard.service

import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest
import org.springframework.boot.test.context.TestConfiguration
import pt.ulisboa.tecnico.socialsoftware.tutor.BeanConfiguration
import pt.ulisboa.tecnico.socialsoftware.tutor.SpockTest
import pt.ulisboa.tecnico.socialsoftware.tutor.teachersdashboard.domain.QuizStats
import pt.ulisboa.tecnico.socialsoftware.tutor.teachersdashboard.domain.TeacherDashboard

@DataJpaTest
class CreateQuizStatsTest extends SpockTest {

    def teacher
    def teacherdashboard

    def setup() {
        createExternalCourseAndExecution()

        teacher = new Teacher(USER_1_NAME, false)
        userRepository.save(teacher)

        teacherdashboard = new TeacherDashboard(externalCourseExecution, teacher)
    }
    
    def "create quiz stats for a course execution"(){
        then:
        teacherdashboard.getNumberOfQuizzes() == 0;
        teacherdashboard.getNumberOfUniqueQuizzesSolved() == 0;
        teacherdashboard.getAverageQuizzesSolved() == 0;
    }
}
