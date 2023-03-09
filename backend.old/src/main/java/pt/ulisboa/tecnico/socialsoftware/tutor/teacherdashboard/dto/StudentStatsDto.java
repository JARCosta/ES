package pt.ulisboa.tecnico.socialsoftware.tutor.teacherdashboard.dto;

import pt.ulisboa.tecnico.socialsoftware.tutor.teacherdashboard.domain.StudentStats;

public class StudentStatsDto {
    Integer id;
    Integer numStudents;
    Integer numMore75CorrectQuestions;
    Integer numAtLeast3Quizes;

    public StudentStatsDto() {
    }

    public StudentStatsDto(StudentStats studentStats) {
        this.id = studentStats.getId();
        this.numStudents = studentStats.getNumStudents();
        this.numMore75CorrectQuestions = studentStats.getNumMore75CorrectQuestions();
        this.numAtLeast3Quizes = studentStats.getNumAtLeast3Quizes();
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getNumStudents() {
        return numStudents;
    }

    public void setNumStudents(Integer numStudents) {
        this.numStudents = numStudents;
    }

    public Integer getNumMore75CorrectQuestions() {
        return numMore75CorrectQuestions;
    }

    public void setNumMore75CorrectQuestions(Integer numMore75CorrectQuestions) {
        this.numMore75CorrectQuestions = numMore75CorrectQuestions;
    }

    public Integer getNumAtLeast3Quizes() {
        return numAtLeast3Quizes;
    }

    public void setNumAtLeast3Quizes(Integer numAtLeast3Quizes) {
        this.numAtLeast3Quizes = numAtLeast3Quizes;
    }

    @Override
    public String toString() {
        return "StudentStatsDto{" +
                "id=" + id +
                ", numStudents=" + numStudents +
                ", numMore75CorrectQuestions=" + numMore75CorrectQuestions +
                ", numAtLeast3Quizes=" + numAtLeast3Quizes +
                '}';
    }
}
