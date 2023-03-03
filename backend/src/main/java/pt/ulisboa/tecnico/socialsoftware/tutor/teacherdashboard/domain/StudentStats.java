package pt.ulisboa.tecnico.socialsoftware.tutor.teacherdashboard.domain;

import pt.ulisboa.tecnico.socialsoftware.tutor.answer.domain.QuizAnswer;
import pt.ulisboa.tecnico.socialsoftware.tutor.execution.domain.CourseExecution;
import pt.ulisboa.tecnico.socialsoftware.tutor.impexp.domain.DomainEntity;
import pt.ulisboa.tecnico.socialsoftware.tutor.impexp.domain.Visitor;
import pt.ulisboa.tecnico.socialsoftware.tutor.user.domain.Student;

import javax.persistence.*;

@Entity
public class StudentStats implements DomainEntity {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private Integer numStudents;

    private Integer numMore75CorrectQuestions;

    private Integer numAtLeast3Quizes;

    @ManyToOne
    private TeacherDashboard teacherDashboard;

    @OneToOne
    private CourseExecution courseExecution;

    public StudentStats() {
    }

    public StudentStats(TeacherDashboard teacherDashboard) {
        // if(0 students over this and the last 2 years?)
        //     CANNOT_CREATE_STUDENT_STATS
        setTeacherDashboard(teacherDashboard);
        teacherDashboard.addStudentStats(this);
        setCourseExecution(teacherDashboard.getCourseExecution());
        courseExecution.setStudentStats(this);
        update();
    }

    public void remove() {
        courseExecution.setStudentStats(null);
        teacherDashboard.getStudentStats().remove(this);
        courseExecution = null;
        teacherDashboard = null;
    }

    public CourseExecution getCourseExecution(){
        return courseExecution;
    }
    
    public void setCourseExecution(CourseExecution courseExecution) {
        this.courseExecution = courseExecution;
        // this.courseExecution.addStudentStats(this);
    }
    
    public Integer getId() {
        return id;
    }

    public Integer getNumStudents(){
        return numStudents;
    }

    public void setNumStudents(Integer numStudents){
        this.numStudents = numStudents;
    }
    
    public Integer getNumMore75CorrectQuestions(){
        return numMore75CorrectQuestions;
    }

    public void setNumMore75CorrectQuestions(Integer numMore75CorrectQuestions){
        this.numMore75CorrectQuestions = numMore75CorrectQuestions;
    }
    
    public Integer getNumAtLeast3Quizes(){
        return numAtLeast3Quizes;
    }

    public void setNumAtLeast3Quizes(Integer numAtLeast3Quizes){
        this.numAtLeast3Quizes = numAtLeast3Quizes;
    }

    public TeacherDashboard getTeacherDashboard(){
        return teacherDashboard;
    }

    public void setTeacherDashboard(TeacherDashboard teacherDashboard){
        this.teacherDashboard = teacherDashboard;
    }

    public void update(){
        this.setNumStudents(this.getCourseExecution().getNumberOfActiveStudents());
        
        this.setNumMore75CorrectQuestions(0);
        this.setNumAtLeast3Quizes(0);

        for (Student student : this.getCourseExecution().getStudents()) {
            for(QuizAnswer quizAnswer : student.getQuizAnswers()){
                if(quizAnswer.getQuiz().getCourseExecution().getId() == this.getCourseExecution().getId())
                    if(quizAnswer.getNumberOfCorrectAnswers() / quizAnswer.getQuiz().getQuizQuestionsNumber() >= 0.75)
                        this.setNumMore75CorrectQuestions(this.getNumMore75CorrectQuestions() + 1);
            } 
            
            if (student.getQuizAnswers().size() >= 3) {
                this.setNumAtLeast3Quizes(this.getNumAtLeast3Quizes() + 1);
            }
        }
    }

    @Override
    public void accept(Visitor visitor) {
        // Only used for XML generation
    }

    @Override
    public String toString() {
        return "StudentStats{" +
                "id=" + id +
                ", numStudents=" + numStudents +
                ", numMore75CorrectQuestions=" + numMore75CorrectQuestions +
                ", numAtLeast3Quizes=" + numAtLeast3Quizes +
                "}";
    }

}