package com.example.board.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.board.dao.UserDao;
import com.example.board.dto.User;

import lombok.RequiredArgsConstructor;

//트랜젝션 단위로 실행될 메소드를 선언하고 있는 클래스
//스프링이 관리하는 Bean
@Service
// 이것만 넣어도 lombok이 final 필드를
// 초기화하는 생성자를 자동으로 생성해줌(편하다!)
@RequiredArgsConstructor 
public class UserService {
    /* final구문이 들어가면 무조건 생성자에서 초기화
     * 드가자~*/
    private final UserDao userDao;
    
    // Spring이 UserService를 Bean으로 생성할 때 생성자를 이요해 생성하는데,
    // 이떄 SuerDao Bean이 있는지 보고
    // 그 빈을 주입해줌
    // "생성자 주입."
    // public UserService(UserDao userDao) {
    //     this.userDao = userDao;
    // }

    /* 보통 서비에서는 @Transactional을
    붙여서 하나의 트랜잭션으로 처리하게 함
    Spring Boot는 트랜잭션을 처리해주는
    트랜잭션 관리자를 가지고 있음 */
    @Transactional
    public User addUser(String name, String email, String password) {
        //트랜잭션 시작
        if(userDao.getUser(email) != null) {
            throw new RuntimeException("이미 가입된 메일입니다.\n");
        }
        User user = userDao.addUser(email, name, password);
        userDao.mappingUserRole(user.getUserId());
        return user;
        //트랜잭션 끝
    }

    @Transactional
    public User getUser(String email) {
        
        return userDao.getUser(email);
    }
}
