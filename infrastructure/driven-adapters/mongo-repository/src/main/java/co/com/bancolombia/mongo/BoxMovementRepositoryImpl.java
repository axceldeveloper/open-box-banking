package co.com.bancolombia.mongo;

import co.com.bancolombia.model.box.Box;
import co.com.bancolombia.model.box.gateways.BoxRepository;
import co.com.bancolombia.model.boxmovement.BoxMovement;
import co.com.bancolombia.model.boxmovement.gateways.BoxMovementRepository;
import co.com.bancolombia.mongo.helper.AdapterOperations;
import org.reactivecommons.utils.ObjectMapper;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Repository
public class BoxMovementRepositoryImpl extends AdapterOperations<BoxMovement, BoxData, String, MongoDBRepository>
        implements BoxMovementRepository {

    public BoxMovementRepositoryImpl(MongoDBRepository repository, ObjectMapper mapper) {
        super(repository, mapper, d -> mapper.map(d, BoxMovement.class));
    }


    @Override
    public Mono<BoxMovement> save(BoxMovement movement) {

        BoxData boxData = new BoxData();

        return repository.save(boxData).map(this::toEntity);
    }

    @Override
    public Flux<BoxMovement> findByBoxId(String boxId) {
        return Flux.just(BoxMovement.builder().boxId(boxId).build());
    }
}
