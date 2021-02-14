package me.sombrero.demorestapi.accounts;


import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;

import java.io.IOException;

/**
 * Account 객체를 JSON으로 변환할 때 id만 JSON 내용에 들어가도록 커스텀 시리얼라이저를 만들었다.
 * Serialization(객체 -> JSON으로 변환)
 */
public class AccountSerializer extends JsonSerializer<Account> {

    @Override
    public void serialize(Account account, JsonGenerator jsonGenerator, SerializerProvider serializerProvider) throws IOException {
        jsonGenerator.writeStartObject();
        jsonGenerator.writeNumberField("id", account.getId());
        jsonGenerator.writeEndObject();
    }

}
