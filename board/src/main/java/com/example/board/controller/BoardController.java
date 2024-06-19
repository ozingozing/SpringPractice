package com.example.board.controller;

import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.example.board.dto.Board;
import com.example.board.dto.LoginInfo;
import com.example.board.service.BoardService;

import ch.qos.logback.core.model.Model;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;



/* HTTP요청을 받아서 응답을 하는 컴포넌트
 * 스프링 부트가 자동으로 Bean으로 생성해줌
 */
@Controller
@RequiredArgsConstructor
public class BoardController {
    private final BoardService boardService;
    /* 게시물 목록을 보여준다.
     * 컨트롤러의 메소드가 리턴하는 문자열은
     * 템플릿 이름임
     * http://localhost:8080/ 
     * ---> "list"라는 이름의 템플릿을 사용(forward)하여 화면에 출력
     * list를 리턴한다는 것은
     * classpath:/templates/list.html
     */
    @GetMapping("/")
                      //HttpSession, Model은 Spring이 자동으로 넣어줌 아니 스프링이 뭐 다하는데 난 뭐함?;;;
    public String list(
        @RequestParam(name = "page", defaultValue = "1") int page,
        HttpSession session, 
        org.springframework.ui.Model model) {
        LoginInfo loginInfo = (LoginInfo)session.getAttribute("loginInfo");
        model.addAttribute("loginInfo", loginInfo);
        
        /* page가 1, 2, 3, 4, ... */
        int totalCount = boardService.getTotalCount();//12
        List<Board> list = boardService.getBoards(page);
        int pageCount = totalCount / 10;
        if(totalCount > 0){//나머지가 있으면 페이지 추가
            pageCount++;
        }
        int curretPage = page;
        // System.out.println("totalCount : " + totalCount);
        // for(Board board : list){
        //     System.out.print(board);
        // }
        model.addAttribute("list", list);
        model.addAttribute("pageCount", pageCount);
        model.addAttribute("curretPage", curretPage);
        return "list";
    }
    
    /* board?id=3 */
    /* board?id=2 */
    /* board?id=1 */
    @GetMapping("/board")
    public String board(
        @RequestParam("boardId") int boardId,
        org.springframework.ui.Model model,
        HttpSession session) {
        System.out.println("id : " + boardId);

        try{
            LoginInfo loginInfo = (LoginInfo)session.getAttribute("loginInfo");
            model.addAttribute("loginInfo", loginInfo);
        }catch(Exception ex){
            return "redirect:/loginform?error=true";
        }
        
        Board board = boardService.getBoard(boardId);
        model.addAttribute("board", board);
        return "board";
    }
    
    @GetMapping("/writeForm")
    public String writeForm(HttpSession session, 
    org.springframework.ui.Model model) {
        /* 로그인한 사용자면 글을 쓸 수 있게
         * 로그인을 안했으면 리스트보기로 자동 이동
         * TODO : 세션에서 로그인한 정보를 읽어 들인다.
         */
        LoginInfo loginInfo = (LoginInfo)session.getAttribute("loginInfo");
        if(loginInfo == null){//세션에 로그인 정보가 없으면 /loginInfo로 페이지 이동
            return "redirect:/loginform";
        }

        model.addAttribute("loginInfo", loginInfo);
        return "writeForm";
    }
    
    @PostMapping("/write")
    public String write(
        @RequestParam("title") String title,
        @RequestParam("content") String content,
        HttpSession session
    ) {
        LoginInfo loginInfo = (LoginInfo)session.getAttribute("loginInfo");
        if(loginInfo == null){//세션에 로그인 정보가 없으면 /loginInfo로 페이지 이동
            return "redirect:/loginform";
        }

        /* TODO: 로그인한 사용자만 글을 써야한다.
         * 세션에서 로그인한 정보를 읽어들인다. 
         * 로그인을 하지 않았다면 리스트보기로 자동 이동
         */
        System.out.println("title : " + title);
        System.out.println("content : " + content);
        /* 로그인한 회원 정보 + 제목, 내용을 저장한다. */
        
        boardService.addBoard(loginInfo.getUserId(), title, content);
        
        return "redirect:/";//리스트 보기로 리다이렉트 한다.
    }
    
}
