package co.com.bancolombia.api;

import co.com.bancolombia.model.box.Box;
import co.com.bancolombia.usecase.closebox.CloseBoxUseCase;
import co.com.bancolombia.usecase.createbox.CreateBoxUseCase;
import co.com.bancolombia.usecase.openbox.OpenBoxUseCase;
import co.com.bancolombia.usecase.uploadboxmovements.UploadBoxMovementsUseCase;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

import java.io.InputStream;
import java.math.BigDecimal;
import java.net.URI;

@Component
public class Handler {

    private final CreateBoxUseCase createBoxUseCase;
    private final OpenBoxUseCase openBoxUseCase;
    private final CloseBoxUseCase closeBoxUseCase;
    private final UploadBoxMovementsUseCase uploadBoxMovementsUseCase;


    public Handler(CreateBoxUseCase createBoxUseCase,
                   OpenBoxUseCase openBoxUseCase,
                   CloseBoxUseCase closeBoxUseCase,
                   UploadBoxMovementsUseCase uploadBoxMovementsUseCase) {
        this.createBoxUseCase = createBoxUseCase;
        this.openBoxUseCase = openBoxUseCase;
        this.closeBoxUseCase = closeBoxUseCase;
        this.uploadBoxMovementsUseCase = uploadBoxMovementsUseCase;

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

    public Mono<ServerResponse> uploadMovements(ServerRequest request) {
        String boxId = request.pathVariable("boxId");
        return request.bodyToMono(InputStream.class)
                .flatMap(inputStream -> {
                    long fileSize = request.headers().firstHeader("Content-Length") != null ?
                            Long.parseLong(request.headers().firstHeader("Content-Length")) : 0;
                    String contentType = request.headers().firstHeader("Content-Type");
                    return uploadBoxMovementsUseCase.uploadMovements(boxId, inputStream, fileSize, contentType);
                })
                .flatMap(event -> ServerResponse.created(URI.create("/api/boxes/" + boxId + "/movements"))
                        .body(BodyInserters.fromValue(event)))
                .onErrorResume(e -> ServerResponse.badRequest().body(BodyInserters.fromValue(e.getMessage())));
    }


}
