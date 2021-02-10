package me.sombrero.demorestapi.events;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class EventTest {

    @Test
    public void builder() {
        Event event = Event.builder()
                .name("Spring REST API")
                .description("REST API development with Spring")
                .build();
        assertThat(event).isNotNull();
    }

    @Test
    public void javaBean() {
        // Given
        String name = "Event";
        String description = "Spring";

        // When
        Event event = new Event();
        event.setName(name);
        event.setDescription(description);

        // Then
        assertThat(event.getName()).isEqualTo(name);
        assertThat(event.getDescription()).isEqualTo(description);
    }

    @Test
    public void testFree() {
        // Given (이런 상태에서..)
        Event event = Event.builder()
                .basePrice(0)
                .maxPrice(0)
                .build();

        // When (이런 일이 벌어지면..)
        event.update();

        // Then (이렇게 된다.)
        assertThat(event.isFree()).isTrue();


        // Given (이런 상태에서..)
        event = Event.builder()
                .basePrice(100)
                .maxPrice(0)
                .build();

        // When (이런 일이 벌어지면..)
        event.update();

        // Then (이렇게 된다.)
        assertThat(event.isFree()).isFalse();


        // Given (이런 상태에서..)
        event = Event.builder()
                .basePrice(0)
                .maxPrice(100)
                .build();

        // When (이런 일이 벌어지면..)
        event.update();

        // Then (이렇게 된다.)
        assertThat(event.isFree()).isFalse();
    }

    @Test
    public void testOffline() {
        // Given (이런 상태에서..)
        Event event = Event.builder()
                .location("스타텁 팩토리")
                .build();

        // When (이런 일이 벌어지면..)
        event.update();

        // Then (이렇게 된다.)
        assertThat(event.isOffline()).isTrue();


        // Given (이런 상태에서..)
        event = Event.builder()
                .build();

        // When (이런 일이 벌어지면..)
        event.update();

        // Then (이렇게 된다.)
        assertThat(event.isOffline()).isFalse();
    }

}