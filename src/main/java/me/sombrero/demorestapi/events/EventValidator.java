package me.sombrero.demorestapi.events;

import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;

import java.time.LocalDateTime;

@Component
public class EventValidator {

    public void validate(EventDto eventDto, Errors errors) {
        if(eventDto.getBasePrice() > eventDto.getMaxPrice() && eventDto.getMaxPrice() > 0) {
            // 필드 에러 rejectValue()
            // errors.rejectValue("basePrice", "wrongValue", "basePrice is wrong."); // basePrice 값이 이상하다는 에러코드와 메세지.
            // errors.rejectValue("maxPrice", "wrongValue", "maxPrice is wrong."); // maxPrice 값이 이상하다는 에러코드와 메세지.

            // 글로벌 에러 reject()
            errors.reject("wrongPrices", "values for prices are wrong.");
        }
        LocalDateTime endEventDateTime = eventDto.getEndEventDateTime();
        if(endEventDateTime.isBefore(eventDto.getBeginEventDateTime()) ||
        endEventDateTime.isBefore(eventDto.getCloseEnrollmentDateTime()) ||
        endEventDateTime.isBefore(eventDto.getBeginEnrollmentDateTime())) {
            errors.rejectValue("endEventDateTime", "wrongValue", "endEventDateTime is wrong."); // endEventDateTime 값이 이상하다는 에러코드와 메세지.
        }

        // TODO beginEventDateTime
        // TODO closeEnrollmentDateTime
    }

}
