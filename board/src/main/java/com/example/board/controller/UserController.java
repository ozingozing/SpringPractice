package com.example.board.controller;

import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.example.board.dto.LoginInfo;
import com.example.board.dto.User;
import com.example.board.service.UserService;

import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;



@Controller
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;

    /* httpL//localhost:8080/userRegForm
     * classpath:/templates/userRegForm.html
     */
    @GetMapping("/userRegForm")
    public String userRegForm() {
        return "userRegForm";
    }
    
    @PostMapping("/userReg")
    public String userReg(
        @RequestParam("name") String name,
        @RequestParam("email") String email,
        @RequestParam("password") String password
    ) {
        System.out.println("name : " + name);
        System.out.println("email : " + email);
        System.out.println("password : " + password);
        
        userService.addUser(name, email, password);
        
        /* 브라우저가 자동으로 httpL//localhost:8080/welcome으로 이동시켜줌 */
        return "redirect:/welcome";
    }

    @GetMapping("/welcome")
    public String welcome() {
        return "welcome";
    }
    
    @GetMapping("/loginform")
    public String loginform() {
        return "loginform";
    }
    
    @PostMapping("/login")
    public String login(
        @RequestParam("email") String email,
        @RequestParam("password") String password,
        HttpSession httpSession //spring이 자동으로 session을 처리하는 HttpSession객체를 넣어줌   
    ) {
        /* email에 해당하는 회원 정보를 읽어온 후
         * 아이디 암호가 맞다면 세션에 회원정보를 저장
         */
        System.out.println("email : " + email);
        System.out.println("password : " + password);
        
        try{
            User user = userService.getUser(email);
            if(user.getPassword().equals((password))) {
                System.out.println("암호가 같슴다\n");
                LoginInfo loginInfo = new LoginInfo(user.getUserId(), user.getEmail(), user.getName());
                httpSession.setAttribute("loginInfo", loginInfo); // 첫번째 파라미터가 key, 두번째 파라미터가 값
                System.out.println("세션에 로그인 정보가 저장됨!\n");
            }else{
                throw new RuntimeException("암호가 같지않음\n");
            }
        }catch(Exception ex){
            return "redirect:/loginform?error=true";
        }
        
        return "redirect:/";
    }

    @GetMapping("/logout")
    public String logout(HttpSession session) {
        /* TODO : 세션에서 회원정보 삭제 */
        session.removeAttribute("loginInfo");
        return "redirect:/";
    }
    
}
