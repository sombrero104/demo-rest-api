package me.sombrero.demorestapi.events;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.MediaTypes;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.Errors;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.net.URI;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

/**
 * 이 안에 있는 모든 핸들러들은 'HAL_JSON_VALUE' 미디어타입으로 응답을 보낸다.
 */
@RestController
@RequestMapping(value = "/api/events", produces = MediaTypes.HAL_JSON_VALUE)
public class EventController {

    private final EventRepository eventRepository;

    private final ModelMapper modelMapper;

    public EventController(EventRepository eventRepository, ModelMapper modelMapper) {
        this.eventRepository = eventRepository;
        this.modelMapper = modelMapper;
    }

    /*@PostMapping
    public ResponseEntity createEvent(@RequestBody Event event) {
        // 메서드에 '/api/events' 경로를 줄 경우..
        // URI createUri = linkTo(methodOn(EventController.class).createEvent(null)).slash("{id}").toUri();
        // 컨트롤러에 '/api/events' 경로를 줄 경우..
        // URI createUri = linkTo(EventController.class).slash("{id}").toUri();
        // return ResponseEntity.created(createUri).build();
        // event.setId(10);

        Event newEvent = this.eventRepository.save(event);
        URI createUri = linkTo(EventController.class).slash(newEvent.getId()).toUri();
        return ResponseEntity.created(createUri).body(event);
    }*/

    /**
     * [validator]
     * EventDto 파라미터 값을 검증하기 위해
     * EventDto에 @NotEmpty, @NotNull, @Min(0) 등을 설정해 주고,
     * 해당 파라미터를 받을 때 앞에 @Valid를 붙여준다.
     * 이때 검증 에러를 받고 싶은 경우,
     * @Valid 애노테이션 사용한 파라미터 오른쪽에 Errors를 주면 Validation 관련 에러를 받을 수 있다.
     */
    @PostMapping
    public ResponseEntity createEvent(@RequestBody @Valid EventDto eventDto, Errors errors) {
        if(errors.hasErrors()) { // 파라미터 검증 에러가 있으면 BadReqeust 에러를 리턴한다.
            return ResponseEntity.badRequest().build();
        }

        /**
         * ModelMapper를 사용해서
         * EventDto로 받은 값을 Event 객체에 매핑하여 담는다.
         */
        Event event = modelMapper.map(eventDto, Event.class);

        Event newEvent = this.eventRepository.save(event);
        URI createUri = linkTo(EventController.class).slash(newEvent.getId()).toUri();
        return ResponseEntity.created(createUri).body(event);
    }

}
