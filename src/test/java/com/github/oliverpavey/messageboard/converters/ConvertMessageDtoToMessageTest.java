package com.github.oliverpavey.messageboard.converters;

import com.github.oliverpavey.messageboard.TestUtilityFunctions;
import com.github.oliverpavey.messageboard.dao.Message;
import com.github.oliverpavey.messageboard.dto.MessageDto;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.convert.ConversionService;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class ConvertMessageDtoToMessageTest {

    @Autowired
    ConversionService conversionService;

    @Autowired
    TestUtilityFunctions utils;

    @BeforeEach
    void beforeEach() {
        utils.resetSeedData();
    }

    @Test
    void convert() {

        MessageDto messageDto = new MessageDto();
        messageDto.setId(72);
        messageDto.setBoardId(2);
        messageDto.setMessage("The leaves are turning brown.");

        final Message message = conversionService.convert(messageDto, Message.class);

        assertNotNull(message, "Null message");
        assertAll(
                () -> assertEquals(72, message.getId(), "Message ID"),
                () -> assertNotNull(message.getBoard(), "Board null"),
                () -> assertEquals(2, message.getBoard().getId(), "Board ID"),
                () -> assertEquals("The leaves are turning brown.", message.getMessage(), "Message"));
    }
}