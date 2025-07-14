package co.com.bancolombia.usecase.uploadboxmovements;

import co.com.bancolombia.model.box.gateways.BoxRepository;
import co.com.bancolombia.model.boxmovement.BoxMovement;
import co.com.bancolombia.model.boxmovement.gateways.BoxMovementRepository;
import co.com.bancolombia.model.events.gateways.EventsGateway;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.math.BigDecimal;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class UploadBoxMovementsUseCase {
    private final BoxRepository boxRepository;
    private final BoxMovementRepository movementRepository;
    private final EventsGateway eventsGateway;
    private final long maxFileSizeBytes = 5 * 1024 * 1024; // 5MB

    public UploadBoxMovementsUseCase(BoxRepository boxRepository, BoxMovementRepository movementRepository, EventsGateway eventsGateway) {
        this.boxRepository = boxRepository;
        this.movementRepository = movementRepository;
        this.eventsGateway = eventsGateway;
    }

    public Mono<BoxMovementsUploadedEvent> uploadMovements(String boxId, InputStream csvStream, long fileSize, String contentType) {
        if (!"text/csv".equals(contentType)) {
            return Mono.error(new UnsupportedOperationException("Tipo de contenido no soportado"));
        }
        if (fileSize > maxFileSizeBytes) {
            return Mono.error(new IllegalArgumentException("Archivo excede el tamaño permitido"));
        }
        return boxRepository.findById(boxId)
                .switchIfEmpty(Mono.error(new IllegalArgumentException("Caja no encontrada")))
                .flatMap(box -> parseCsv(csvStream, boxId)
                        .collectList()
                        .flatMap(movements -> {
                            int total = movements.size();
                            List<BoxMovement> valid = new ArrayList<>();
                            int failed = 0;
                            for (BoxMovement m : movements) {
                                if (m.getAmount().compareTo(BigDecimal.ZERO) < 0 ||
                                        (!"INCOME".equals(m.getType()) && !"EXPENSE".equals(m.getType()))) {
                                    failed++;
                                } else {
                                    valid.add(m);
                                }
                            }
                            int finalFailed = failed;
                            return Flux.fromIterable(valid)
                                    .flatMap(movementRepository::save)
                                    .count()
                                    .flatMap(success -> {
                                        BoxMovementsUploadedEvent event = new BoxMovementsUploadedEvent(boxId, total, success.intValue(), finalFailed);
                                        return eventsGateway.emit("box.movements.uploaded", event)
                                                .thenReturn(event);
                                    });
                        })
                );
    }

    private Flux<BoxMovement> parseCsv(InputStream csvStream, String boxId) {
        return Flux.defer(() -> {
            try (BufferedReader reader = new BufferedReader(new InputStreamReader(csvStream, StandardCharsets.UTF_8));
                 CSVParser parser = new CSVParser(reader, CSVFormat.DEFAULT.withFirstRecordAsHeader())) {

                List<BoxMovement> movements = new ArrayList<>();
                for (CSVRecord record : parser) {
                    try {
                        BoxMovement movement = BoxMovement.builder()
                                .movementId(record.get("movementId"))
                                .boxId(boxId)
                                .date(LocalDateTime.parse(record.get("date")))
                                .type(record.get("type"))
                                .amount(new BigDecimal(record.get("amount")))
                                .currency(record.get("currency"))
                                .description(record.get("description"))
                                .build();
                        movements.add(movement);
                    } catch (Exception e) {
                        // Puedes loggear el error o manejarlo según tu lógica
                    }
                }
                return Flux.fromIterable(movements);
            } catch (Exception e) {
                return Flux.error(e);
            }
        });
    }
}
