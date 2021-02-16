package com.github.oliverpavey.messageboard.repo;

import com.github.oliverpavey.messageboard.dao.Board;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface BoardRepo extends CrudRepository<Board, Long> {

    List<Board> findByName(String name);
}
