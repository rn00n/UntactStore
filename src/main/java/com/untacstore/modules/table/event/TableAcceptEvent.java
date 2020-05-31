package com.untacstore.modules.table.event;

import com.untacstore.modules.table.Event;
import com.untacstore.modules.table.Tables;
import com.untacstore.modules.waiting.Waiting;
import lombok.Getter;

@Getter
public class TableAcceptEvent {

    private Event event;

    public TableAcceptEvent(Event event) {
        this.event = event;
    }
}
