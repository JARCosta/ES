package pt.ulisboa.tecnico.socialsoftware.tutor.teacherdashboard.domain;

import pt.ulisboa.tecnico.socialsoftware.tutor.answer.domain.QuizAnswer;
import pt.ulisboa.tecnico.socialsoftware.tutor.execution.domain.CourseExecution;
import pt.ulisboa.tecnico.socialsoftware.tutor.impexp.domain.DomainEntity;
import pt.ulisboa.tecnico.socialsoftware.tutor.impexp.domain.Visitor;
import pt.ulisboa.tecnico.socialsoftware.tutor.user.domain.Student;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;
import javax.persistence.*;


@Entity
public class QuizStats implements DomainEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    
    private int numberOfQuizzes = 0;
    private int numberOfUniqueQuizzesSolved = 0;
    private int averageQuizzesSolved = 0;

    @OneToOne
    private CourseExecution courseExecution;

    QuizStats(CourseExecution courseExecution) {
        this.courseExecution = courseExecution;
    }

    public void update() {
        //get number of quizzes from course execution
        this.numberOfQuizzes = updateNumberOfQuizzes();
        //get number of unique quizzes solved from course execution
        this.numberOfUniqueQuizzesSolved = updateNumberOfUniqueQuizzesSolved();


    }

    public int updateNumberOfQuizzes() {
        return courseExecution.getNumberOfQuizzes();

    }

    public int updateNumberOfUniqueQuizzesSolved() {
        Set<QuizAnswer> quizAnswers = new HashSet<QuizAnswer>();
        for (Student student : courseExecution.getStudents()) {
            quizAnswers.addAll(student.getQuizAnswers());
        }
        return quizAnswers.stream()
            .map((quizAnswer) -> quizAnswer.getQuiz().getId())
            .distinct()
            .collect(Collectors.toList())
            .size();
    }





    public int getNumberOfQuizzes() {
        return numberOfQuizzes;
    }

    public int getNumberOfUniqueQuizzesSolved() {
        return numberOfUniqueQuizzesSolved;
    }



    @Override
    public void accept(Visitor visitor) {
        // Only used for XML generation
    }
    
}
