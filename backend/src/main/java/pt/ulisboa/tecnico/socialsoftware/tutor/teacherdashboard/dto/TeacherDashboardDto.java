package pt.ulisboa.tecnico.socialsoftware.tutor.teacherdashboard.dto;

import pt.ulisboa.tecnico.socialsoftware.tutor.teacherdashboard.domain.TeacherDashboard;

import java.util.ArrayList;

public class TeacherDashboardDto {
    private Integer id;
    private Integer numberOfStudents;

    private ArrayList<Integer> numAvailable;
    private ArrayList<Integer> answeredQuestionUnique;
    private ArrayList<Float> averageQuestionAnswered;

    public TeacherDashboardDto() {
    }

    public TeacherDashboardDto(TeacherDashboard teacherDashboard) {
        this.id = teacherDashboard.getId();
        // For the number of students, we consider only active students
        this.numberOfStudents = teacherDashboard.getCourseExecution().getNumberOfActiveStudents();

        this.numAvailable = new ArrayList<>();
        this.answeredQuestionUnique = new ArrayList<>();
        this.averageQuestionAnswered = new ArrayList<>();

        teacherDashboard.getQuestionStats().forEach(questionStat -> {

            this.numAvailable.add(questionStat.getNumAvailable());
            this.answeredQuestionUnique.add(questionStat.getAnsweredQuestionsUnique());
            this.averageQuestionAnswered.add(questionStat.getAverageQuestionsAnswered());
        });
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

    public ArrayList<Integer> getnumAvailable() {
        return numAvailable;
    }

    public void setNumAvailable(ArrayList<Integer> numAvailable) {
        this.numAvailable = numAvailable;
    }

    public ArrayList<Integer> getAnsweredQuestionUnique() {
        return answeredQuestionUnique;
    }

    public void setAnsweredQuestionsUnique(ArrayList<Integer> answeredQuestionUnique) {
        this.answeredQuestionUnique = answeredQuestionUnique;
    }

    public ArrayList<Float> getAverageQuestionAnswered() {
        return averageQuestionAnswered;
    }

    public void setAverageQuestionsAnswered(ArrayList<Float> averageQuestionAnswered) {
        this.averageQuestionAnswered = averageQuestionAnswered;
    }


    @Override
    public String toString() {
        return "TeacherDashboardDto{" +
                "id=" + id +
                ", numberOfStudents=" + this.getNumberOfStudents() +
                ", numAvailable=" + this.getnumAvailable() +
                ", answeredQuestionUnique=" + this.getAnsweredQuestionUnique() +
                ", averageQuestionAnswered=" + this.getAverageQuestionAnswered() +
                "}";
    }
}
