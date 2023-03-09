package pt.ulisboa.tecnico.socialsoftware.tutor.teacherdashboard.domain;

import pt.ulisboa.tecnico.socialsoftware.tutor.answer.domain.QuizAnswer;
import pt.ulisboa.tecnico.socialsoftware.tutor.execution.domain.CourseExecution;
import pt.ulisboa.tecnico.socialsoftware.tutor.impexp.domain.DomainEntity;
import pt.ulisboa.tecnico.socialsoftware.tutor.impexp.domain.Visitor;
import pt.ulisboa.tecnico.socialsoftware.tutor.user.domain.Student;
import pt.ulisboa.tecnico.socialsoftware.tutor.question.domain.Question;
import pt.ulisboa.tecnico.socialsoftware.tutor.questionsubmission.domain.QuestionSubmission;

import java.util.Arrays;

import javax.persistence.*;

@Entity
public class QuestionStats implements DomainEntity {
    
    @Id
    @GeneratedValue
    private Integer id;

    private Integer numQuestAvail;

    private Integer ansQuestUniq;

    private Float avgQuestAns;

    @ManyToOne
    private TeacherDashboard teacherDashboard;

    @OneToOne
    private CourseExecution courseExecution;

    public void QuestionStats() {
    }

    public void QuestionStats(TeacherDashboard teacherDashboard, CourseExecution courseExecution) {

        setTeacherDashboard(teacherDashboard);
        setCourseExecution(courseExecution);
        update();
    }

    public void remove() {
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

    public void setId(Integer id){
        this.id = id;
    }

    public Integer getnumQuestAvail(){
        return numQuestAvail;
    }

    public void setnumQuestAvail(Integer numQuestAvail){
        this.numQuestAvail = numQuestAvail;
    }
    
    public Integer getansQuestUniq(){
        return ansQuestUniq;
    }

    public void setansQuestUniq(Integer ansQuestUniq){
        this.ansQuestUniq = ansQuestUniq;
    }
    
    public float getavgQuestAns(){
        return avgQuestAns;
    }

    public void setavgQuestAns(Float avgQuestAns){
        this.avgQuestAns = avgQuestAns;
    }

    public TeacherDashboard getTeacherDashboard(){
        return teacherDashboard;
    }

    public void setTeacherDashboard(TeacherDashboard teacherDashboard){
        this.teacherDashboard = teacherDashboard;
    }

    public void update(){

        /*int totalquest = 0;
        for (Question question : this.getCourseExecution().getQuestions()) {
            if (question.getStatus() == Question.Stats.AVAILABLE) {
                totalquest ++;
            }
        }*/
        this.setnumQuestAvail(this.courseExecution.getNumberOfQuestions());

        this.setansQuestUniq((int)courseExecution.getQuestionSubmissions().stream()
            .map(sub -> Arrays.asList(sub.getSubmitter(), sub.getQuestion()))
            .distinct()
            .count()
        );
        //make setansQuestUni

            
        this.setavgQuestAns((float)this.getansQuestUniq()/this.courseExecution.getStudents().size());
    }

    @Override
    public void accept(Visitor visitor) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'accept'");
    }
}