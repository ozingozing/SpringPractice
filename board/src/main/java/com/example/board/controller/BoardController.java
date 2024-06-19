package com.example.board.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.example.board.dto.LoginInfo;

import ch.qos.logback.core.model.Model;
import jakarta.servlet.http.HttpSession;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;



/* HTTP요청을 받아서 응답을 하는 컴포넌트
 * 스프링 부트가 자동으로 Bean으로 생성해줌
 */
@Controller
public class BoardController {
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
    public String list(HttpSession session, org.springframework.ui.Model model) {
        LoginInfo loginInfo = (LoginInfo)session.getAttribute("loginInfo");
        model.addAttribute("loginInfo", loginInfo);
        return "list";
    }
    
    /* board?id=3 */
    /* board?id=2 */
    /* board?id=1 */
    @GetMapping("/board")
    public String board(@RequestParam("id") int id) {
        System.out.println("id : " + id);
        /*TODO : id에 해당하는 게시물을 읽어온다.
         * TODO : id에 해당하는 게시물의 조회수도 1 증가한다.
         */
        return "board";
    }
    
    @GetMapping("/writeForm")
    public String writeForm() {
        /* 로그인한 사용자면 글을 쓸 수 있게
         * 로그인을 안했으면 리스트보기로 자동 이동
         * TODO : 세션에서 로그인한 정보를 읽어 들인다.
         */
        return "writeForm";
    }
    
    @PostMapping("/write")
    public String write(
        @RequestParam("title") String title,
        @RequestParam("content") String content
    ) {
        /* TODO: 로그인한 사용자만 글을 써야한다.
         * 세션에서 로그인한 정보를 읽어들인다. 
         * 로그인을 하지 않았다면 리스트보기로 자동 이동
         */
        System.out.println("title : " + title);
        System.out.println("content : " + content);
        /* 로그인한 회원 정보 + 제목, 내용을 저장한다. */
        return "redirect:/";//리스트 보기로 리다이렉트 한다.
    }
    
}
