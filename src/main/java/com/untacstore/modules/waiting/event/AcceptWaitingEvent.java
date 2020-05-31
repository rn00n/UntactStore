package com.untacstore.modules.waiting.event;

import com.untacstore.modules.waiting.Waiting;
import lombok.Getter;

@Getter
public class AcceptWaitingEvent {

    private Waiting waiting;

    public AcceptWaitingEvent(Waiting waiting) {
        this.waiting = waiting;
    }
}
