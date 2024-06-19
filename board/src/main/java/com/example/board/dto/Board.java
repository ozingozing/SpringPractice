package com.example.board.dto;

import java.time.LocalDateTime;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class Board {
    private int boardId;
    private String title;
    private String content;
    private String name;
    private int userId;
    private LocalDateTime regdate;
    private int viewCnt;
}
/*
+----------+--------------+------+-----+-------------------+-------------------+
| Field    | Type         | Null | Key | Default           | Extra             |
+----------+--------------+------+-----+-------------------+-------------------+
| board_id | int          | NO   | PRI | NULL              | auto_increment    |
| title    | varchar(100) | NO   |     | NULL              |                   |
| cont     | text         | YES  |     | NULL              |                   |
| user_id  | int          | NO   | MUL | NULL              |                   |
| regate   | timestamp    | YES  |     | CURRENT_TIMESTAMP | DEFAULT_GENERATED |
| view_cnt | int          | YES  |     | 0                 |                   |
+----------+--------------+------+-----+-------------------+-------------------+
 */