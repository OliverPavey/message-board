package com.github.oliverpavey.messageboard.api;

import com.github.oliverpavey.messageboard.TestUtilityFunctions;
import com.github.oliverpavey.messageboard.dao.Board;
import com.github.oliverpavey.messageboard.dto.BoardDto;
import com.github.oliverpavey.messageboard.repo.BoardRepo;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static com.github.oliverpavey.messageboard.TestUtilityFunctions.firstElem;
import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class ApiControllerV1BoardTest {

    private final String API = "/api/messageboard/v1";

    @Autowired
    MockMvc mockMvc;

    @Autowired
    BoardRepo boardRepo;

    @Autowired
    TestUtilityFunctions utils;

    @BeforeEach
    void beforeEach() {
        utils.resetSeedData();
    }

    @Test
    void getBoards() throws Exception {

        mockMvc.perform(get(API + "/boards"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(2));
    }

    @Test
    void getBoard() throws Exception {

        final Long boardId = firstElem(boardRepo.findByName("Items for sale")).getId();

        mockMvc.perform(get(API + "/board/" + boardId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(boardId))
                .andExpect(jsonPath("$.name").value("Items for sale"));
    }

    @Test
    void postBoard() throws Exception {

        BoardDto boardDto = new BoardDto();
        boardDto.setName("To do list");

        mockMvc.perform(post(API + "/board")
                .contentType(MediaType.APPLICATION_JSON)
                .content(utils.json(boardDto)))
                .andExpect(status().is(HttpStatus.CREATED.value()));

        final Board newItem = firstElem(boardRepo.findByName("To do list"));

        assertAll(
                () -> assertEquals("To do list", newItem.getName(), "Name"),
                () -> assertNotNull(newItem.getId(), "ID null"),
                () -> assertNotEquals(0L, newItem.getId(), "ID zero"));
    }

    @Test
    void putBoard() throws Exception {

        final Long boardId = firstElem(boardRepo.findByName("Items for sale")).getId();

        BoardDto boardDto = new BoardDto();
        boardDto.setId(boardId);
        boardDto.setName("Online Bargains");

        mockMvc.perform(put(API + "/board")
                .contentType(MediaType.APPLICATION_JSON)
                .content(utils.json(boardDto)))
                .andExpect(status().is(HttpStatus.CREATED.value()));

        final Board board = boardRepo.findById(boardId).orElseThrow();

        assertAll(
                () -> assertEquals(boardId, board.getId(), "Board ID"),
                () -> assertEquals("Online Bargains", board.getName(), "Board name"));
    }
}