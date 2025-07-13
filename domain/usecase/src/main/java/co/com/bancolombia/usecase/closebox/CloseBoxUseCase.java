package co.com.bancolombia.usecase.closebox;

import co.com.bancolombia.model.box.Box;
import co.com.bancolombia.model.box.gateways.BoxRepository;
import co.com.bancolombia.model.events.gateways.EventsGateway;
import reactor.core.publisher.Mono;

import java.math.BigDecimal;

public class CloseBoxUseCase {
    public static final String BOX_CLOSED = "box.event.closed";
    private final BoxRepository boxRepository;
    private final EventsGateway eventsGateway;

    public CloseBoxUseCase(BoxRepository boxRepository, EventsGateway eventsGateway){
        this.boxRepository = boxRepository;
        this.eventsGateway = eventsGateway;
    }

    public Mono<Box> closeBox(String boxId, BigDecimal closingAmount) {
        return boxRepository.findById(boxId)
                .flatMap(box -> {
                    box.close(closingAmount);
                    return boxRepository.save(box).
                            flatMap(savedBox -> eventsGateway.emit(BOX_CLOSED, savedBox)
                                .thenReturn(savedBox));
                });
    }
}
