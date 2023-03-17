package pt.ulisboa.tecnico.socialsoftware.tutor.teacherdashboard.dto;

import java.io.Serializable;
import java.util.ArrayList;

import pt.ulisboa.tecnico.socialsoftware.tutor.teacherdashboard.domain.QuizStats;

public class QuizStatsDto implements Serializable{
    private Integer id;

    private Integer numQuizzes;
    private Integer numUniqueAnsweredQuizzes;
    private Float averageQuizzesSolved;

    public QuizStatsDto() {
    }

    public QuizStatsDto(QuizStats quizStats) {
        setId(quizStats.getId());
        setNumQuizzes(quizStats.getNumQuizzes());
        setNumUniqueAnsweredQuizzes(quizStats.getNumUniqueAnsweredQuizzes());
        setAverageQuizzesSolved(quizStats.getAverageQuizzesSolved());
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getNumQuizzes() {
        return numQuizzes;
    }

    public void setNumQuizzes(Integer numQuizzes) {
        this.numQuizzes = numQuizzes;
    }

    public Integer getNumUniqueAnsweredQuizzes() {
        return numUniqueAnsweredQuizzes;
    }

    public void setNumUniqueAnsweredQuizzes(Integer numUniqueAnsweredQuizzes) {
        this.numUniqueAnsweredQuizzes = numUniqueAnsweredQuizzes;
    }

    public Float getAverageQuizzesSolved() {
        return averageQuizzesSolved;
    }

    public void setAverageQuizzesSolved(Float averageQuizzesSolved) {
        this.averageQuizzesSolved = averageQuizzesSolved;
    }

    @Override
    public String toString() {
        return "QuizStatsDto{" +
                "id=" + id +
                ", numQuizzes=" + numQuizzes +
                ", numUniqueAnsweredQuizzes=" + numUniqueAnsweredQuizzes +
                ", averageQuizzesSolved=" + averageQuizzesSolved +
                '}';
    }
}
