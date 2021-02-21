package com.github.oliverpavey.messageboard;

import com.github.oliverpavey.messageboard.dao.Board;
import com.github.oliverpavey.messageboard.dao.Message;
import com.github.oliverpavey.messageboard.repo.BoardRepo;
import com.github.oliverpavey.messageboard.repo.MessageRepo;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;

@Component
public class SeedData {

    @Value("${messageboard.seeddata:false}")
    boolean useSeedData;

    final BoardRepo boardRepo;
    final MessageRepo messageRepo;

    public SeedData(BoardRepo boardRepo, MessageRepo messageRepo) {
        this.boardRepo = boardRepo;
        this.messageRepo = messageRepo;
    }

    @PostConstruct
    void postConstruct() {
        if (useSeedData) {
            writeSeedData();
        }
    }

    public void clearData() {
        messageRepo.deleteAll();
        boardRepo.deleteAll();
    }

    public void writeSeedData() {
        final Board systemMessages = boardRepo.save(newBoard("System messages"));
        messageRepo.save(newMessage(systemMessages, "System up."));

        final Board forSale = boardRepo.save(newBoard("Items for sale"));
        messageRepo.save(newMessage(forSale, "Ford Granada."));
        messageRepo.save(newMessage(forSale, "Stroller Pram."));
        messageRepo.save(newMessage(forSale, "Bread Maker."));
    }

    Board newBoard(String name) {
        Board board = new Board();
        board.setName(name);
        return board;
    }

    Message newMessage(Board board, String messageText) {
        final Message message = new Message();
        message.setBoard(board);
        message.setMessage(messageText);
        return message;
    }

}
