package me.sombrero.demorestapi.events;

import org.springframework.hateoas.MediaTypes;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.net.URI;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

/**
 * 이 안에 있는 모든 핸들러들은 'HAL_JSON_VALUE' 미디어타입으로 응답을 보낸다.
 */
@RestController
@RequestMapping(value = "/api/events", produces = MediaTypes.HAL_JSON_VALUE)
public class EventController {

    @PostMapping()
    public ResponseEntity createEvent(@RequestBody Event event) {
        // 메서드에 '/api/events' 경로를 줄 경우..
        // URI createUri = linkTo(methodOn(EventController.class).createEvent(null)).slash("{id}").toUri();
        // 컨트롤러에 '/api/events' 경로를 줄 경우..
        URI createUri = linkTo(EventController.class).slash("{id}").toUri();
        // return ResponseEntity.created(createUri).build();
        event.setId(10);
        return ResponseEntity.created(createUri).body(event);
    }

}
