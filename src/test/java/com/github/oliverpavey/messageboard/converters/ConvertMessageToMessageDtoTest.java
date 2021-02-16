package com.github.oliverpavey.messageboard.converters;

import com.github.oliverpavey.messageboard.TestUtilityFunctions;
import com.github.oliverpavey.messageboard.dao.Board;
import com.github.oliverpavey.messageboard.dao.Message;
import com.github.oliverpavey.messageboard.dto.MessageDto;
import com.github.oliverpavey.messageboard.repo.BoardRepo;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.convert.ConversionService;

import static com.github.oliverpavey.messageboard.TestUtilityFunctions.firstElem;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class ConvertMessageToMessageDtoTest {

    @Autowired
    ConversionService conversionService;

    @Autowired
    BoardRepo boardRepo;

    @Autowired
    TestUtilityFunctions utils;

    @BeforeEach
    void beforeEach() {
        utils.resetSeedData();
    }

    @Test
    void convert() {

        final Board board = firstElem(boardRepo.findByName("Items for sale"));
        final Long boardId = board.getId();

        Message message = new Message();
        message.setId(84L);
        message.setBoard(board);
        message.setMessage("Spring has sprung.");

        final MessageDto messageDto = conversionService.convert(message, MessageDto.class);

        assertNotNull(messageDto, "Null messageDto");
        assertAll(
                () -> assertEquals(84, messageDto.getId(), "Message ID"),
                () -> assertEquals(boardId, messageDto.getBoardId(), "Board ID"),
                () -> assertEquals("Spring has sprung.", messageDto.getMessage(), "Message"));
    }
}