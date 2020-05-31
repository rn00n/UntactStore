package com.untacstore.modules.table.event;

import com.untacstore.modules.table.Event;
import com.untacstore.modules.table.Tables;
import lombok.Getter;

@Getter
public class TableRequestEvent {

    private Event event;

    public TableRequestEvent(Event event) {
        this.event = event;
    }
}
