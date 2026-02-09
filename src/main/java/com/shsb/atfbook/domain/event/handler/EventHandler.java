package com.shsb.atfbook.domain.event.handler;

import com.shsb.atfbook.utils.event.Event;
import com.shsb.atfbook.utils.event.payload.EventPayload;

public interface EventHandler<T extends EventPayload> {
    void handle(Event<T> event);
    boolean supports(Event<T> event);
}
