package me.sombrero.demorestapi.events;

import junitparams.JUnitParamsRunner;
import junitparams.Parameters;
import org.junit.Test;
import org.junit.runner.RunWith;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(JUnitParamsRunner.class)
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

    /**
     * 'parametersFor'라는 prefix가 컨벤션.
     * 이 prefix가 붙어있는 메소드를 찾아서 자동으로 파라미터로 사용하게 해준다.
     */
    private Object[] parametersForTestFree() {
        return new Object[] {
            new Object[] {0, 0, true},
            new Object[] {100, 0, false},
            new Object[] {0, 100, false},
            new Object[] {100, 200, false}
        };
    }

    @Test
    /*@Parameters({
            "0, 0, true",
            "100, 0, false",
            "0, 100, false"
    })*/
    // @Parameters(method = "parametersForTestFree")
    @Parameters // 'parametersFor'라는 prefix가 컨벤션할 경우.
    public void testFree(int basePrice, int maxPrice, boolean isFree) {
        /*// Given (이런 상태에서..)
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
        assertThat(event.isFree()).isFalse();*/

        /**
         * 위의 코드를 JUnitParams를 사용하여 (의존성 추가해줘야함.)
         * 아래처럼 중복코드를 제거할 수 있다.
         */
        Event event = Event.builder()
                .basePrice(basePrice)
                .maxPrice(maxPrice)
                .build();

        // When (이런 일이 벌어지면..)
        event.update();

        // Then (이렇게 된다.)
        assertThat(event.isFree()).isEqualTo(isFree);
    }

    private Object[] parametersForTestOffline() {
        return new Object[] {
            new Object[] {"강남", true},
            new Object[] {null, false},
            new Object[] {null, false}
        };
    }

    @Test
    @Parameters
    public void testOffline(String location, boolean isOffline) {
        /*// Given (이런 상태에서..)
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
        assertThat(event.isOffline()).isFalse();*/

        // Given (이런 상태에서..)
        Event event = Event.builder()
                .location(location)
                .build();

        // When (이런 일이 벌어지면..)
        event.update();

        // Then (이렇게 된다.)
        assertThat(event.isOffline()).isEqualTo(isOffline);
    }

}