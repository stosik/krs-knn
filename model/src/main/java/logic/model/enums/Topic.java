package logic.model.enums;

import lombok.Getter;
import lombok.ToString;

@Getter
@ToString
public enum Topic
{
    EARN("earn"),
    ACQ("acq"),
    CRUDE("crude"),
    TRADE("trade"),
    MONEY_FX("money-fx"),
    INTEREST("interest");
    
    private final String topic;
    
    Topic(String topic) {
        this.topic = topic;
    }
}
