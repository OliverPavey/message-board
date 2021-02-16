package com.github.oliverpavey.messageboard.repo;

import com.github.oliverpavey.messageboard.dao.Message;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface MessageRepo extends CrudRepository<Message, Long> {

    List<Message> findByMessage(String message);

    List<Message> findByBoard_IdOrderByCreateDate(Long boardId);
}
