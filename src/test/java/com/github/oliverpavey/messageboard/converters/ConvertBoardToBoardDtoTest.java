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
class ConvertBoardToBoardDtoTest {

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

        Board board = new Board();
        board.setId(15L);
        board.setName("Samples board.");

        final BoardDto boardDto = conversionService.convert(board, BoardDto.class);

        assertNotNull(boardDto, "Null boardDto");
        assertAll(
                () -> assertEquals(15L, boardDto.getId()),
                () -> assertEquals("Samples board.", boardDto.getName()));
    }
}