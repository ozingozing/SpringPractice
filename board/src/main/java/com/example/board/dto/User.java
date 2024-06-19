package com.example.board.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@NoArgsConstructor // 기본생성자가 자동으로 만들어짐
public class User {
    private String userId;
    private String email;
    private String password;
    private String regdate;
}
// user_id	int	            NO	    PRI		            auto_increment
// email	varchar(255)	NO			
// name	    varchar(50)	    NO			
// password	varchar(500)	NO			
// regdate	timestamp	    YES		CURRENT_TIMESTAMP	DEFAULT_GENERATED