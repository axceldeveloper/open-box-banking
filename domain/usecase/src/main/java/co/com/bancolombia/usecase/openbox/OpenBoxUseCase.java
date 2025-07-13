package co.com.bancolombia.usecase.openbox;

import co.com.bancolombia.model.box.Box;
import co.com.bancolombia.model.box.gateways.BoxRepository;
import co.com.bancolombia.model.events.gateways.EventsGateway;
import reactor.core.publisher.Mono;

import java.math.BigDecimal;

public class OpenBoxUseCase {
    public static final String BOX_OPENED = "box.event.opened";
    private final BoxRepository boxRepository;
    private final EventsGateway eventsGateway;

    public OpenBoxUseCase(BoxRepository boxRepository, EventsGateway eventsGateway){
        this.boxRepository = boxRepository;
        this.eventsGateway = eventsGateway;
    }


    public Mono<Box> openBox(String boxId, BigDecimal openingAmount) {
        return boxRepository.findById(boxId)
                .flatMap(box -> {
                    box.open(openingAmount);
                    return boxRepository.save(box)
                            .flatMap(savedBox -> eventsGateway.emit(BOX_OPENED, savedBox)
                                    .thenReturn(savedBox));
                });
    }
}
