package com.example.board.dto;

import java.time.LocalDateTime;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Setter
@Getter
@NoArgsConstructor // 기본생성자가 자동으로 만들어짐
@ToString          // Object toString()를 자동으로 만들어줌
public class User {
    private int userId;
    private String name;
    private String email;
    private String password;
    private LocalDateTime regdate;
}
// user_id	int	            NO	    PRI		            auto_increment
// email	varchar(255)	NO			
// name	    varchar(50)	    NO			
// password	varchar(500)	NO			
// regdate	timestamp	    YES		CURRENT_TIMESTAMP	DEFAULT_GENERATED