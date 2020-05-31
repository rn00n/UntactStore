package com.untacstore.modules.waiting.event;

import com.untacstore.modules.waiting.Waiting;
import lombok.Getter;
import org.springframework.context.ApplicationEvent;

@Getter
public class NewWaitingEvent {

    private Waiting waiting;

    public NewWaitingEvent(Waiting waiting) {
        this.waiting = waiting;
    }
}
