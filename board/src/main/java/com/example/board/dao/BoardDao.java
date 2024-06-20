package com.example.board.dao;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

import javax.sql.DataSource;
import javax.swing.border.Border;
import javax.swing.tree.RowMapper;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.namedparam.BeanPropertySqlParameterSource;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.jdbc.core.simple.SimpleJdbcInsertOperations;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.example.board.dto.Board;
import com.example.board.dto.User;

@Repository
public class BoardDao {
    // @Autowired
    /* 각종 sql를 실행하기위한  NamedParameterJdbcTemplate*/
    private final  NamedParameterJdbcTemplate jdbcTemplate;
    /* insert를 위한 인터페이스 */
    private final SimpleJdbcInsertOperations insertBoard;

    public BoardDao(DataSource dataSource){
        jdbcTemplate = new NamedParameterJdbcTemplate(dataSource);
        insertBoard = new SimpleJdbcInsert(dataSource)
                    .withTableName("board")
                    .usingGeneratedKeyColumns("board_id");
    }

    @Transactional
    public void addBoard(int userId, String title, String content) {
        Board board = new Board();
        board.setUserId(userId);
        board.setTitle(title);
        board.setContent(content);
        board.setRegdate(LocalDateTime.now());
        SqlParameterSource params = new BeanPropertySqlParameterSource(board);
        insertBoard.execute(params);
    }

    @Transactional(readOnly = true) /* select만 */
    public int getTotalCount() {
        //여기선 1건만 실행
        String sql = "select count(*) as total_count from board";
        Integer totalCount = jdbcTemplate.queryForObject(sql, Map.of(), Integer.class);
        return totalCount.intValue();
    }

    @Transactional(readOnly = true) /* select만 */
    public List<Board> getBoards(int page) {
        //start는 0, 10, 20, 30... 1page, 2page, 3page...이런 식으로 진행
        int start = (page - 1) * 10;
        String sql = "select b.user_id, b.board_id, b.title, b.regdate, b.view_cnt, u.name from board b, user u where b.user_id = u.user_id order by board_id desc limit :start, 10";
        // RowMapper는 데이터베이스에서 가져온 결과를
        // 자바 객체로 매핑하는 규칙을 정의합니다. 
        // 여기서는 Board 클래스를 사용하여 매핑 규칙을 정의합니다. 
        // BeanPropertyRowMapper.newInstance(Board.class)는 
        // 데이터베이스의 열 이름과 
        // Board 클래스의 필드 이름을 자동으로 매핑합니다.
        org.springframework.jdbc.core.RowMapper<Board> rowMapper 
            = BeanPropertyRowMapper.newInstance(Board.class); 
        /*
         * 1.은 위에 정의한 query문을 넣고
         * 2.은 쿼리문에 있는 ":start"의 값을 "start"변수에 할당
         * 3.은 위에서 각열마다 class타입에 맞게 세팅된 Map으로서
         * query문 반환값을 여기에 저장(매핑)해서
         * 자바객체에 넘김
        */
        List<Board> list = jdbcTemplate.query(sql, Map.of("start", start), rowMapper);
        return list;
    }

    public void updateViewCnt(int boardId) {
        // SQL 쿼리를 정의합니다. 이 쿼리는 board 테이블에서 특정 board_id를 가진 행의 view_cnt를 1 증가시킵니다.
        String sql = "UPDATE board SET view_cnt = view_cnt + 1 WHERE board_id = :boardId";
       // MapSqlParameterSource 객체를 생성하여 매개변수 boardId의 값을 설정합니다.
        MapSqlParameterSource params = new MapSqlParameterSource();
        params.addValue("boardId", boardId);
        // 정의한 SQL 쿼리와 매개변수를 사용하여 데이터베이스에 업데이트 작업을 수행합니다.
        jdbcTemplate.update(sql, params);
    }

    @Transactional(readOnly = true)
    public Board getBoard(int boardId) {
        // SQL 쿼리를 정의합니다. 이 쿼리는 board와 user 테이블을 조인하여 특정 board_id를 가진 게시물의 세부 정보를 가져옵니다.
        String sql = "SELECT b.user_id, b.board_id, b.title, b.regdate, b.view_cnt, u.name, b.content " +
                     "FROM board b, user u " +
                     "WHERE b.user_id = u.user_id AND b.board_id = :boardId";
        // MapSqlParameterSource 객체를 생성하여 매개변수 boardId의 값을 설정합니다.
        MapSqlParameterSource params = new MapSqlParameterSource();
        params.addValue("boardId", boardId);
        // RowMapper 객체를 생성하여 SQL 쿼리 결과를 Board 객체에 매핑할 수 있도록 합니다.
        org.springframework.jdbc.core.RowMapper<Board> rowMapper = BeanPropertyRowMapper.newInstance(Board.class);
        // 정의한 SQL 쿼리와 매개변수를 사용하여 단일 결과 객체(Board)를 쿼리하고 반환합니다.
        return jdbcTemplate.queryForObject(sql, params, rowMapper);
    }

    @Transactional
    public void deleteBoard(int boardId) {
        String sql = "delete from board where board_id = :boardId";
        MapSqlParameterSource params = new MapSqlParameterSource();
        params.addValue("boardId", boardId);
        // 정의한 SQL 쿼리와 매개변수를 사용하여 데이터베이스에 업데이트 작업을 수행합니다.
        jdbcTemplate.update(sql, params);
    }

    @Transactional
    public void updateBoard(int boardId, String title, String content) {
        String sql = "update board\n" +
                     "set title = :title, content = :content\n" +
                     "Where board_id = :boardId";
        Board board = new Board();
        board.setBoardId(boardId);
        board.setTitle(title);
        board.setContent(content);
        SqlParameterSource parmas = new BeanPropertySqlParameterSource(board);
        jdbcTemplate.update(sql, parmas);
    }
}
