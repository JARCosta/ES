package pt.ulisboa.tecnico.socialsoftware.tutor.answer.dto;

import pt.ulisboa.tecnico.socialsoftware.tutor.answer.domain.QuizAnswer;

import java.time.LocalDateTime;

public class QuizAnswerDto {
    private Integer id;
    private Boolean scramble;
    private LocalDateTime availableDate;
    private LocalDateTime answerDate;
    private Boolean completed;


    public QuizAnswerDto(QuizAnswer quizAnswer) {
        this.id = quizAnswer.getId();
        this.scramble = quizAnswer.getQuiz().getScramble();
        this.availableDate = quizAnswer.getQuiz().getAvailableDate();
        this.completed = quizAnswer.getCompleted();
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Boolean getScramble() {
        return scramble;
    }

    public void setScramble(Boolean scramble) {
        this.scramble = scramble;
    }

    public LocalDateTime getAvailableDate() {
        return availableDate;
    }

    public void setAvailableDate(LocalDateTime availableDate) {
        this.availableDate = availableDate;
    }

    public LocalDateTime getAnswerDate() {
        return answerDate;
    }

    public void setAnswerDate(LocalDateTime answerDate) {
        this.answerDate = answerDate;
    }

    public Boolean getCompleted() {
        return completed;
    }

    public void setCompleted(Boolean completed) {
        this.completed = completed;
    }
}