package co.com.bancolombia.events;
import co.com.bancolombia.events.handlers.EventsHandler;
import org.reactivecommons.async.api.HandlerRegistry;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class HandlerRegistryConfiguration {
    public static final String BOX_CREATED = "box.event.created";
    public static final String BOX_OPENED = "box.event.opened";
    public static final String BOX_CLOSED = "box.event.closed";
    @Bean
    public HandlerRegistry handlerRegistry(EventsHandler events) {
        return HandlerRegistry.register()
                .listenEvent(BOX_CREATED, events::createBoxListenEvent, Object.class)
                .listenEvent(BOX_OPENED, events::openedBoxListenEvent, Object.class)
                .listenEvent(BOX_CLOSED, events::closedBoxListenEvent, Object.class);
    }


}
