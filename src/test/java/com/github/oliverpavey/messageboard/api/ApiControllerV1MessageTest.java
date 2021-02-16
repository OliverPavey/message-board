package com.github.oliverpavey.messageboard.api;

import com.github.oliverpavey.messageboard.TestUtilityFunctions;
import com.github.oliverpavey.messageboard.dao.Message;
import com.github.oliverpavey.messageboard.dto.MessageDto;
import com.github.oliverpavey.messageboard.repo.BoardRepo;
import com.github.oliverpavey.messageboard.repo.MessageRepo;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static com.github.oliverpavey.messageboard.TestUtilityFunctions.firstElem;
import static com.github.oliverpavey.messageboard.TestUtilityFunctions.lastElem;
import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class ApiControllerV1MessageTest {

    private final String API = "/api/messageboard/v1";

    @Autowired
    MockMvc mockMvc;

    @Autowired
    BoardRepo boardRepo;

    @Autowired
    MessageRepo messageRepo;

    @Autowired
    TestUtilityFunctions utils;

    @BeforeEach
    void beforeEach() {
        utils.resetSeedData();
    }

    @Test
    void getMessages() throws Exception {

        final Long boardId = firstElem(boardRepo.findByName("Items for sale")).getId();

        mockMvc.perform(get(API + "/board/" + boardId + "/messages"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(3));
    }

    @Test
    void getMessage() throws Exception {

        final Long boardId = firstElem(boardRepo.findByName("Items for sale")).getId();
        final Long messageId = firstElem(messageRepo.findByMessage("Stroller Pram.")).getId();

        mockMvc.perform(get(API + "/board/" + boardId + "/message/" + messageId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(messageId))
                .andExpect(jsonPath("$.message").value("Stroller Pram."));
    }

    @Test
    void postMessage() throws Exception {

        final Long boardId = firstElem(boardRepo.findByName("Items for sale")).getId();

        MessageDto messageDto = new MessageDto();
        messageDto.setBoardId(boardId);
        messageDto.setMessage("Exercise Bike.");

        mockMvc.perform(post(API + "/message")
                .contentType(MediaType.APPLICATION_JSON)
                .content(utils.json(messageDto)))
                .andExpect(status().is(HttpStatus.CREATED.value()));

        final Message message = lastElem(messageRepo.findByBoard_IdOrderByCreateDate(boardId));

        assertEquals("Exercise Bike.", message.getMessage(), "Message Text");
    }

    @Test
    void putMessage() throws Exception {

        final Message original = firstElem(messageRepo.findByMessage("Bread Maker."));
        final Long originalId = original.getId();
        final Long originalBoardId = original.getBoard().getId();

        MessageDto messageDto = new MessageDto();
        messageDto.setId(originalId);
        messageDto.setBoardId(originalBoardId);
        messageDto.setMessage("Home Baking Kit.");

        mockMvc.perform(put(API + "/message")
                .contentType(MediaType.APPLICATION_JSON)
                .content(utils.json(messageDto)))
                .andExpect(status().is(HttpStatus.CREATED.value()));

        final Message message = messageRepo.findById(originalId).orElseThrow();

        assertAll(
                () -> assertEquals(originalId, message.getId()),
                () -> assertEquals(originalBoardId, message.getBoard().getId()),
                () -> assertEquals("Home Baking Kit.", message.getMessage(), "Message Text"));
    }

    @Test
    void deleteMessage() throws Exception {

        final Message original = firstElem(messageRepo.findByMessage("Stroller Pram."));
        final Long messageId = original.getId();
        final Long boardId = original.getBoard().getId();

        mockMvc.perform(delete(API + "/board/" + boardId + "/message/" + messageId))
                .andExpect(status().is(HttpStatus.ACCEPTED.value()));

        assertTrue(messageRepo.findByBoard_IdOrderByCreateDate(boardId).stream()
                .noneMatch(message -> "Stroller Pram.".equals(message.getMessage())),
                "Message no longer in repo.");
    }
}