package com.example.board.dao;

import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.example.board.dto.User;

//spring이 관리하는 Bean
@Repository
public class UserDao {
    @Transactional
    public User addUser(String email, String name, String password) {
        //이걸 호출하는 함수 호출자에서 트랜잭션이 시작됐으면
        //여기에 붙어있는 트랜잭션은 그 트랜잭션에 포함됨
        // insert into user(email, name, password, regdate)values(?, ?, ?, now);
        // select last_insert_id();
        return null;
    }

    @Transactional
    public void mappingUserRole(String userId) {
        //이걸 호출하는 함수 호출자에서 트랜잭션이 시작됐으면
        //여기에 붙어있는 트랜잭션은 그 트랜잭션에 포함됨
        // insert into user_role(user_id, role_id)values(?, 1);
    }
}
/*
 * 이 파일에서 mysql에서 값을 가져올거임
 * 어케 가져올까 넘 궁금하당
 * insert into user(email, name, password, regdate)values(?, ?, ?, now);
 * select last_insert_id();
 * insert into user_role(user_id, role_id)values(?, 1);
 */