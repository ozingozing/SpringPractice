package com.example.board.dao;

import java.time.LocalDateTime;

import javax.sql.DataSource;
import javax.swing.tree.RowMapper;

import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.namedparam.BeanPropertySqlParameterSource;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.jdbc.core.simple.SimpleJdbcInsertOperations;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.example.board.dto.User;

//spring이 관리하는 Bean
@Repository
public class UserDao {
    private final NamedParameterJdbcTemplate jdbcTemplate;
    private SimpleJdbcInsertOperations insertUser;
    public UserDao(DataSource dataSource) {
        jdbcTemplate = new NamedParameterJdbcTemplate(dataSource);
        insertUser = new SimpleJdbcInsert(dataSource)
                    .withTableName("user")
                    .usingGeneratedKeyColumns("user_id");
    }

    @Transactional
    public User addUser(String email, String name, String password) {
        //이걸 호출하는 함수 호출자에서 트랜잭션이 시작됐으면
        //여기에 붙어있는 트랜잭션은 그 트랜잭션에 포함됨
        // insert into user(email, name, password, regdate)values(?, ?, ?, now);
        // select last_insert_id();
        User user = new User();
        user.setName(name);
        user.setEmail(email);
        user.setPassword(password);
        user.setRegdate(LocalDateTime.now());
        SqlParameterSource params = new BeanPropertySqlParameterSource(user);
        Number number = insertUser.executeAndReturnKey(params);
        int userId = number.intValue();
        user.setUserId(userId);
        return user;
    }

    @Transactional
    public void mappingUserRole(int userId) {
        //이걸 호출하는 함수 호출자에서 트랜잭션이 시작됐으면
        //여기에 붙어있는 트랜잭션은 그 트랜잭션에 포함됨
        // insert into user_role(user_id, role_id)values(?, 1);
        String sql = "insert into user_role(user_id, role_id)values(:userId, 1)";
        SqlParameterSource params = new MapSqlParameterSource("userId", userId);
        jdbcTemplate.update(sql, params);
    }

    @Transactional
    public User getUser(String email) {
        // user_id => setUserId, email => setEmail ...
        String sql = "select user_id, email, name, password, regdate from user where email = :email";
        SqlParameterSource params = new MapSqlParameterSource("email", email);
        org.springframework.jdbc.core.RowMapper<User> rowMapper = BeanPropertyRowMapper.newInstance(User.class);
        User user = jdbcTemplate.queryForObject(sql, params, rowMapper);
        return user;
    }
}
/*
 * 이 파일에서 mysql에서 값을 가져올거임
 * 어케 가져올까 넘 궁금하당
 * insert into user(email, name, password, regdate)values(?, ?, ?, now);
 * select last_insert_id();
 * insert into user_role(user_id, role_id)values(?, 1);
 */