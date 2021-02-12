package me.sombrero.demorestapi.common;

import com.fasterxml.jackson.databind.ObjectMapper;
import me.sombrero.demorestapi.events.EventRepository;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

/**
 * 테스트 코드를 작성할 때 테스트를 실행하려면
 * 일일이 Mocking해줘야 할 부분들이 많기 때문에 @SpringBootTest로 해주는 것이 편리하다.
 */
@RunWith(SpringRunner.class)
// @WebMvcTest // 슬라이스 테스트. 웹용 빈들만 등록해줌. 때문에 Repository 빈은 자동으로 등록해주지 않는다.
@SpringBootTest // 실제 Repository에 저장하는 것까지 포함해서 테스트하게 해준다.
@AutoConfigureMockMvc // MVC용 Mock DispatcherServlet을 만들어준다.
@ActiveProfiles("test")
@Ignore // 현재 이 BaseControllerTest는 테스트코드를 가지지 않기 때문에 테스트코드를 실행하지 않도록 @Ignore를 붙여준다.
public class BaseControllerTest {

    @Autowired
    protected MockMvc mockMvc;

    @Autowired
    protected ObjectMapper objectMapper;

    @Autowired
    protected ModelMapper modelMapper;

}
