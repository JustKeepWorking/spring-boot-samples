package com.github.nduyhai.stompwebsocket;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.socket.WebSocketHandler;
import org.springframework.web.reactive.socket.WebSocketMessage;
import org.springframework.web.reactive.socket.WebSocketSession;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Component
public class ReactiveWebSocketHandler implements WebSocketHandler {
    @Override
    public Mono<Void> handle(WebSocketSession session) {


        return session.send(eventFlux.map(session::textMessage)).and(session.receive()
                .map(WebSocketMessage::getPayloadAsText)
                .log());

//        return session.send(Mono.justOrEmpty(Optional.empty()))
//                .and(session.receive().map(WebSocketMessage::getPayloadAsText).log());
    }

    private static final ObjectMapper json = new ObjectMapper();

    private Flux<String> eventFlux = Flux.generate(sink -> {
        final Greeting greeting = new Greeting() {{
            setContent("Flux socket !!!");
        }};
        try {
            sink.next(json.writeValueAsString(greeting));
        } catch (JsonProcessingException e) {
            sink.error(e);
        }
    });

}
