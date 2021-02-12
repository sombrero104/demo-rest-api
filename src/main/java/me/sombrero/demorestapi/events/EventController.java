package me.sombrero.demorestapi.events;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PagedResourcesAssembler;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.MediaTypes;
import org.springframework.hateoas.PagedModel;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.Errors;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.net.URI;
import java.time.LocalDateTime;
import java.util.Optional;

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

    private final EventValidator eventValidator;

    public EventController(EventRepository eventRepository, ModelMapper modelMapper, EventValidator eventValidator) {
        this.eventRepository = eventRepository;
        this.modelMapper = modelMapper;
        this.eventValidator = eventValidator;
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
        /**
         * BeanSerializer: 자바 빈 스펙 규칙을 준수하는 객체를 Serialization(Object -> JSON 으로 변환)
         * ObjectMapper에 여러가지 종류의 Serializer가 등록되어 있다.
         * 현재 우리는 실제로 ObjectMapper 사용하고 있다.
         * 컨트롤러에서 body로 반환하는 event를 json으로 변환할 때 ObjectMapper를 써서 변환을 한다.
         * ObjectMapper는 BeanSerializer를 사용해서 (자바 빈 스펙을 준수하는 객체이기 때문에) event 객체를 json으로 변환할 수 있었던 것.
         * 커스텀한 Serializer를 등록하지 않아도 기본적으로 등록되어 있는 BeanSerializer를 사용해서 json으로 변환한 것이다.
         * Errors는 자바 빈 스펙을 준수하는 객체가 아니기 때문에 BeanSerializer를 사용해서 json으로 변환할 수 없다.
         * 위에서 @RequestMapping(value = "/api/events", produces = MediaTypes.HAL_JSON_VALUE)으로
         * 응답을 HAL_JSON 형태로 주기로 명시했기 때문에 응답을 반환할 때 자동적으로 json으로 변환을 시도한다.
         *
         * 그래서 JsonSerializer를 상속받는 ErrorsSerializer를 만든다. (ErrorsSerializer 자바 파일 참조.)
         * 그리고 결과를 확인하면 @Valid 에러가 발생했을 때 아래처럼 Errors가 JSON으로 변환되어 body로 응답해주는 것을 확인할 수 있다.
         */

        /**
         * {
         *     "field": "endEventDateTime",
         *     "objectName": "eventDto",
         *     "code": "wrongValue",
         *     "defaultMessage": "endEventDateTime is wrong.",
         *     "rejectedValue": "2018-11-23T14:21"
         * },
         * {
         *     "objectName": "eventDto",
         *     "code": "wrongPrices",
         *     "defaultMessage": "values for prices are wrong."
         * }
         */

        if(errors.hasErrors()) { // 파라미터 검증 에러가 있으면 BadReqeust 에러를 리턴한다.
            return ResponseEntity.badRequest().body(errors);
        }

        eventValidator.validate(eventDto, errors);
        if(errors.hasErrors()) { // 파라미터 검증 에러가 있으면 BadReqeust 에러를 리턴한다.
            // 위에서 디버그 잡으면 errors 안에 에러 객체들이 있는 것을 확인할 수 있다.
            return ResponseEntity.badRequest().body(errors);
        }

        /**
         * ModelMapper를 사용해서
         * EventDto로 받은 값을 Event 객체에 매핑하여 담는다.
         */
        Event event = modelMapper.map(eventDto, Event.class);
        event.update(); // 유료/무료 변겅.
        Event newEvent = this.eventRepository.save(event);
        URI createUri = linkTo(EventController.class).slash(newEvent.getId()).toUri();
        return ResponseEntity.created(createUri).body(event);
    }

    @GetMapping
    public ResponseEntity queryEvents(Pageable pageable, PagedResourcesAssembler<Event> assembler) {
        Page<Event> page = this.eventRepository.findAll(pageable);
        PagedModel<EntityModel<Event>> pagedModel = assembler.toModel(page);
        return ResponseEntity.ok(pagedModel);
    }

    @GetMapping("/{id}")
    public ResponseEntity getEvent(@PathVariable Integer id) {
        Optional<Event> optionalEvent = this.eventRepository.findById(id);
        if(optionalEvent.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        Event event = optionalEvent.get();
        return ResponseEntity.ok(event);
    }

}
