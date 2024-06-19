package com.example.board.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.board.dao.BoardDao;
import com.example.board.dto.Board;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor 
public class BoardService {
    private final BoardDao boardDao;

    @Transactional
    public void addBoard(int userId, String title, String content) {
        boardDao.addBoard(userId, title, content);
    }

    @Transactional(readOnly = true) /* select만 */
    public int getTotalCount() {
        return boardDao.getTotalCount();
    }

    @Transactional(readOnly = true) /* select만 */
    public List<Board> getBoards(int page) {
        return boardDao.getBoards(page);
    }

    @Transactional
    public Board getBoard(int boardId) {
        /*TODO : id에 해당하는 게시물을 읽어온다.
         * TODO : id에 해당하는 게시물의 조회수도 1 증가한다.*/
        Board board = boardDao.getBoard(boardId);
        boardDao.updateViewCnt(boardId);
        return board;
    }
    
}
