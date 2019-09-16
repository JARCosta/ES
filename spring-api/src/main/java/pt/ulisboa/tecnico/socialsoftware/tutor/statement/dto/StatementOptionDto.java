package pt.ulisboa.tecnico.socialsoftware.tutor.statement.dto;

import pt.ulisboa.tecnico.socialsoftware.tutor.question.domain.Option;
import java.io.Serializable;

public class StatementOptionDto implements Serializable {
    private Integer optionId;
    private String content;

    public StatementOptionDto(Option option) {
        this.optionId = option.getId();
        this.content = option.getContent();
    }

    public Integer getOptionId() {
        return optionId;
    }

    public void setOptionId(Integer optionId) {
        this.optionId = optionId;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

}