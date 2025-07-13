package co.com.bancolombia.events.handlers;

import org.reactivecommons.api.domain.DomainEvent;
import org.reactivecommons.async.impl.config.annotations.EnableEventListeners;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import reactor.core.publisher.Mono;

@EnableEventListeners
public class EventsHandler {
    private static final Logger log = LoggerFactory.getLogger(EventsHandler.class);

    public EventsHandler(/*SampleUseCase sampleUseCase*/) {
        //this.sampleUseCase = sampleUseCase;
    }

    public Mono<Void> createBoxListenEvent(DomainEvent<Object> event) {
        log.info("Event received: {} -> {}", event.getName(), event.getData());
        return Mono.empty();
    }

    public Mono<Void> openedBoxListenEvent(DomainEvent<Object> event) {
        log.info("Event received: {} -> {}", event.getName(), event.getData());
        return Mono.empty();
    }

    public Mono<Void> closedBoxListenEvent(DomainEvent<Object> event) {
        log.info("Event received: {} -> {}", event.getName(), event.getData());
        return Mono.empty();
    }
}
