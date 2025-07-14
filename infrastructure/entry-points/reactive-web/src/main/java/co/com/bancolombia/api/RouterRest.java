package co.com.bancolombia.api;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.ServerResponse;

import static org.springframework.web.reactive.function.server.RequestPredicates.*;
import static org.springframework.web.reactive.function.server.RouterFunctions.route;

@Configuration
public class RouterRest {
    @Bean
    public RouterFunction<ServerResponse> routerFunction(Handler handler) {
        return route(PUT("/api/close"), handler::close)
                .andRoute(PUT("/api/open"), handler::open)
                .and(route(POST("/api/create"), handler::createBox)
                .andRoute(POST("/api/boxes/{boxId}/movements"), handler::uploadMovements));
    }
}
