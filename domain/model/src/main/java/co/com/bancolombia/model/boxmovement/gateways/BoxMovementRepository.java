package co.com.bancolombia.model.boxmovement.gateways;

import co.com.bancolombia.model.boxmovement.BoxMovement;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface BoxMovementRepository {
    Mono<BoxMovement> save(BoxMovement movement);
    Flux<BoxMovement> findByBoxId(String boxId);
}
