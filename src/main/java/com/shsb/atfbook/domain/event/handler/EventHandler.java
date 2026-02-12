package com.shsb.atfbook.domain.event.handler;

import com.shsb.atfbook.domain.shared.event.Event;
import com.shsb.atfbook.domain.shared.event.payload.EventPayload;

public interface EventHandler<T extends EventPayload> {
    void handle(Event<T> event);
    boolean supports(Event<T> event);
}
