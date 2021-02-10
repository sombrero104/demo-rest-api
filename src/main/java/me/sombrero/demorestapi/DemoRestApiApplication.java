package me.sombrero.demorestapi;

import org.modelmapper.ModelMapper;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class DemoRestApiApplication {

    public static void main(String[] args) {
        SpringApplication.run(DemoRestApiApplication.class, args);
    }

    /**
     * ModelMapper를 공용으로 사용하므로
     * 이곳에서 빈으로 등록.
     */
    @Bean
    public ModelMapper modelMapper() {
        return new ModelMapper();
    }

}
