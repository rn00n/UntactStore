package com.untacstore.modules.store.event;

import com.untacstore.modules.store.Store;
import lombok.Getter;

@Getter
public class StoreCreateEvent {
    private Store store;

    public StoreCreateEvent(Store store) {
        this.store = store;
    }

}
