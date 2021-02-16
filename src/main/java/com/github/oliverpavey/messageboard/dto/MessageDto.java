package com.github.oliverpavey.messageboard.dto;

import lombok.Data;

@Data
public class MessageDto {
    private long id;
    private long boardId;
    private String timestamp;
    private String message;
}
