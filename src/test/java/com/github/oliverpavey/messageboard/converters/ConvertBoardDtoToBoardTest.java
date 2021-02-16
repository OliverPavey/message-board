package com.github.oliverpavey.messageboard.converters;

import com.github.oliverpavey.messageboard.TestUtilityFunctions;
import com.github.oliverpavey.messageboard.dao.Board;
import com.github.oliverpavey.messageboard.dto.BoardDto;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.convert.ConversionService;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class ConvertBoardDtoToBoardTest {

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

        BoardDto boardDto = new BoardDto();
        boardDto.setId(12);
        boardDto.setName("Test board.");

        final Board board = conversionService.convert(boardDto, Board.class);

        assertNotNull(board, "Null board");
        assertAll(
                () -> assertEquals(12, board.getId()),
                () -> assertEquals("Test board.", board.getName()));
    }
}