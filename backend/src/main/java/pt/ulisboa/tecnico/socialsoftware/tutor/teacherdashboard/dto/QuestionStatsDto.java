package pt.ulisboa.tecnico.socialsoftware.tutor.teacherdashboard.dto;

import java.io.Serializable;
import java.util.ArrayList;

import pt.ulisboa.tecnico.socialsoftware.tutor.teacherdashboard.domain.QuestionStats;

public class QuestionStatsDto implements Serializable{
    private Integer id;

    private Integer numAvailable;
    private Integer answeredQuestionUnique;
    private Float averageQuestionAnswered;

    public QuestionStatsDto() {
    }

    public QuestionStatsDto(QuestionStats questionStats) {
        setId(questionStats.getId());
        setnumAvailable(questionStats.getnumQuestAvail());
        setanswerQuestionsUnique(questionStats.getansQuestUniq());
        setaverageQuestionAnswered(questionStats.getavgQuestAns());
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getnumAvailablevail() {
        return numAvailable;
    }

    public void setnumAvailable(Integer numAvailable) {
        this.numAvailable = numAvailable;
    }

    public Integer getAnsweredQuestionUnique() {
        return answeredQuestionUnique;
    }

    public void setanswerQuestionsUnique(Integer answeredQuestionUnique) {
        this.answeredQuestionUnique = answeredQuestionUnique;
    }

    public Float getAverageQuestionAnswered() {
        return averageQuestionAnswered;
    }

    public void setaverageQuestionAnswered(Float averageQuestionAnswered) {
        this.averageQuestionAnswered = averageQuestionAnswered;
    }

    @Override
    public String toString() {
        return "QuestionStatsDto{" +
                "id=" + id +
                ", numAvailable=" + numAvailable +
                ", answeredQuestionUnique=" + answeredQuestionUnique +
                ", averageQuestionAnswered=" + averageQuestionAnswered +
                '}';
    }
}
