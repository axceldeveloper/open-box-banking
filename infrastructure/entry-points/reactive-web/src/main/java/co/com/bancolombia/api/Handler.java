package co.com.bancolombia.api;

import co.com.bancolombia.model.box.Box;
import co.com.bancolombia.usecase.closebox.CloseBoxUseCase;
import co.com.bancolombia.usecase.createbox.CreateBoxUseCase;
import co.com.bancolombia.usecase.openbox.OpenBoxUseCase;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

import java.math.BigDecimal;
import java.net.URI;

@Component
public class Handler {

    private final CreateBoxUseCase createBoxUseCase;
    private final OpenBoxUseCase openBoxUseCase;
    private final CloseBoxUseCase closeBoxUseCase;


    public Handler(CreateBoxUseCase createBoxUseCase, OpenBoxUseCase openBoxUseCase, CloseBoxUseCase closeBoxUseCase) {
        this.createBoxUseCase = createBoxUseCase;
        this.openBoxUseCase = openBoxUseCase;
        this.closeBoxUseCase = closeBoxUseCase;
    }

    public Mono<ServerResponse> createBox(ServerRequest serverRequest) {
        return serverRequest.bodyToMono(Box.class).flatMap(box -> {
            return createBoxUseCase.createBox(box.getName());
        }).flatMap(currentBox -> ServerResponse.ok().body(BodyInserters.fromValue(currentBox)));
    }

    public Mono<ServerResponse> open(ServerRequest request){
        String id = request.pathVariable("id");
        return openBoxUseCase.openBox(id, BigDecimal.ZERO).flatMap(currentPet -> ServerResponse.ok()
                        .body(BodyInserters.fromValue(currentPet)))
                .switchIfEmpty(ServerResponse.notFound().build());
    }

    public Mono<ServerResponse> close(ServerRequest request){
        String id = request.pathVariable("id");
        return closeBoxUseCase.closeBox(id, BigDecimal.ZERO).flatMap(currentPet -> ServerResponse.ok()
                        .body(BodyInserters.fromValue(currentPet)))
                .switchIfEmpty(ServerResponse.notFound().build());
    }


}
