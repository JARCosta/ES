package pt.ulisboa.tecnico.socialsoftware.tutor.teacherdashboard.domain;

import pt.ulisboa.tecnico.socialsoftware.tutor.answer.domain.QuizAnswer;
import pt.ulisboa.tecnico.socialsoftware.tutor.execution.domain.CourseExecution;
import pt.ulisboa.tecnico.socialsoftware.tutor.impexp.domain.DomainEntity;
import pt.ulisboa.tecnico.socialsoftware.tutor.impexp.domain.Visitor;
import pt.ulisboa.tecnico.socialsoftware.tutor.user.domain.Student;
import pt.ulisboa.tecnico.socialsoftware.tutor.exceptions.TutorException;

import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;
import javax.persistence.*;

import static pt.ulisboa.tecnico.socialsoftware.tutor.exceptions.ErrorMessage.*;


@Entity
public class QuizStats implements DomainEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    
    private int numberOfQuizzes;
    private int numberOfUniqueQuizzesSolved;
    private float averageQuizzesSolved;

    @OneToOne
    private CourseExecution courseExecution;

    @ManyToOne
    private TeacherDashboard teacherDashboard;
    
    public QuizStats(){

    }

    public QuizStats(CourseExecution courseExecution) {
        if (!(courseExecution instanceof CourseExecution)){
            throw new TutorException(NOT_AN_INSTANCE_OF_COURSE_EXECUTION);
        }

        this.courseExecution = courseExecution;
        this.setNumberOfQuizzes();
        this.setNumberOfUniqueQuizzesSolved();
        this.setAverageQuizzesSolved();
    }

    public void update() {
        //get number of quizzes from course execution
        this.setNumberOfQuizzes();
        //get number of unique quizzes solved from course execution
        this.setNumberOfUniqueQuizzesSolved();
        //get average number of unique quizzes solved by student
        this.setAverageQuizzesSolved();
    }

    public void setNumberOfQuizzes() {
        this.numberOfQuizzes = this.courseExecution.getNumberOfQuizzes();

    }

    public void setNumberOfUniqueQuizzesSolved() {
        Set<QuizAnswer> quizAnswers = new HashSet<QuizAnswer>();
        for (Student student : this.courseExecution.getStudents()) {
            quizAnswers.addAll(student.getQuizAnswers());
        }
        this.numberOfUniqueQuizzesSolved = quizAnswers.stream()
            .map((quizAnswer) -> quizAnswer.getQuiz().getId())
            .distinct()
            .collect(Collectors.toList())
            .size();
    }

    public void setAverageQuizzesSolved() {
        int sum = 0;
        for (Student student : this.courseExecution.getStudents()) {
            sum += student.getQuizAnswers().stream()
                .map((quizAnswer) -> quizAnswer.getQuiz().getId())
                .distinct()
                .collect(Collectors.toList())
                .size();
        }
        if (this.courseExecution.getStudents().size() == 0) {
            this.averageQuizzesSolved = 0;
        }
        else {
            this.averageQuizzesSolved = sum / this.courseExecution.getStudents().size();
        }
    }

    public Integer getId() {
        return id;
    }

    public CourseExecution getCourseExecution(){
        return courseExecution;
    }

    public int getNumberOfQuizzes() {
        return numberOfQuizzes;
    }

    public int getNumberOfUniqueQuizzesSolved() {
        return numberOfUniqueQuizzesSolved;
    }

    public float getAverageQuizzesSolved() {
        return averageQuizzesSolved;
    }

    @Override
    public void accept(Visitor visitor) {
        // Only used for XML generation
    }

    @Override
    public String toString() {
        return "QuizStats{" +
                "id=" + id +
                ", numberOfQuizzes=" + numberOfQuizzes +
                ", numberOfUniqueQuizzesSolved=" + numberOfUniqueQuizzesSolved +
                ", averageQuizzesSolved=" + averageQuizzesSolved +
                ", courseExecution=" + courseExecution +
                '}';
    }


}