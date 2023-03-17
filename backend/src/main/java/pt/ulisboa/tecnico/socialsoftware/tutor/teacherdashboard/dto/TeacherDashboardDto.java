package pt.ulisboa.tecnico.socialsoftware.tutor.teacherdashboard.dto;

import java.util.ArrayList;
import java.util.List;

import pt.ulisboa.tecnico.socialsoftware.tutor.execution.domain.CourseExecution;
import pt.ulisboa.tecnico.socialsoftware.tutor.question.domain.Course;
import pt.ulisboa.tecnico.socialsoftware.tutor.teacherdashboard.domain.QuizStats;
import pt.ulisboa.tecnico.socialsoftware.tutor.teacherdashboard.domain.TeacherDashboard;

import java.util.ArrayList;

 

public class TeacherDashboardDto {
    private Integer id;
    private Integer numberOfStudents;
    private ArrayList<QuizStatsDto> quizStatsDto;

    private ArrayList<Integer> numMore75CorrectQuestions;
    private ArrayList<Integer> numAtLeast3Quizes;
    private ArrayList<Integer> numStudents;


    public TeacherDashboardDto() {
    }

    public TeacherDashboardDto(TeacherDashboard teacherDashboard) {
        this.id = teacherDashboard.getId();
        // For the number of students, we consider only active students
        this.numberOfStudents = teacherDashboard.getCourseExecution().getNumberOfActiveStudents();
  
        this.numStudents = new ArrayList<>();
        this.numMore75CorrectQuestions = new ArrayList<>();
        this.numAtLeast3Quizes = new ArrayList<>();

        teacherDashboard.getStudentStats().forEach(studentStat -> {

            this.numAtLeast3Quizes.add(studentStat.getNumAtLeast3Quizzes());
            this.numMore75CorrectQuestions.add(studentStat.getNumMore75CorrectQuestions());
            this.numStudents.add(studentStat.getNumStudents());

        });

        this.quizStatsDto = new ArrayList<>();
        createQuizStatsDtos(teacherDashboard);
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getNumberOfStudents() {
        return numberOfStudents;
    }

    public void setNumberOfStudents(Integer numberOfStudents) {
        this.numberOfStudents = numberOfStudents;
    }

    public ArrayList<Integer> getNumStudents(){
        return numStudents;
    }

    public ArrayList<Integer> getnumMore75CorrectQuestions(){
        return numMore75CorrectQuestions;
    }

    public ArrayList<Integer> getnumAtLeast3Quizes(){
        return numAtLeast3Quizes;
    }

    public ArrayList<QuizStatsDto> getQuizStatsDto() {
        return quizStatsDto;
    }

    private void createQuizStatsDtos(TeacherDashboard teacherDashboard){
        for(QuizStats qz : teacherDashboard.getQuizStats()){
            this.quizStatsDto.add(new QuizStatsDto(qz));
        }
    }

    @Override
    public String toString() {
        return "TeacherDashboardDto{" +
                "id=" + id +
                ", numberOfStudents=" + numberOfStudents +
                ", numStudents=" + numStudents +
                ", numMore75CorrectQuestions=" + numMore75CorrectQuestions +
                ", numAtLeast3Quizes=" + numAtLeast3Quizes +
                '}';
    }
}
