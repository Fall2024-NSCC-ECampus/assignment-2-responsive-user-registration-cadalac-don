package com.example.springbootmvc.controller;

import jakarta.servlet.http.HttpSession;
import org.springframework.ui.Model;
import com.example.springbootmvc.model.User;
import com.example.springbootmvc.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Controller
public class CreateUserController {
    private static final String EMAIL_REGEX = "^[A-Za-z0-9+_.-]+@[^.]+(.[A-Za-z0-9-]+)+$";
    private static final Pattern EMAIL_PATTERN = Pattern.compile(EMAIL_REGEX);
    private static final String PASSWORD_REGEX = "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%^&+=])(?=\\S+$).{8,}$";
    private static final Pattern PASSWORD_PATTERN = Pattern.compile(PASSWORD_REGEX);

    @Autowired
    UserRepository userRepository;

    @GetMapping("/mainpage")
        public String mainpage(){

        return "mainpage";
    }

    @GetMapping("/login")
    public String LogginOn(){
        return "login";
    }

    @PostMapping("/login")
    public String login(@RequestParam("username") String username, @RequestParam("password") String password, HttpSession session, Model model){
        if(session.getAttribute("user") != null){
            model.addAttribute("error","You are already signed in!");
            return "signupfailure";
        }

        User user = userRepository.findByUsername(username);
        if(user != null && password.equals(user.getPassword())){
            session.setAttribute("user", user);
            return "redirect:/loginComplete";
        }else{
            model.addAttribute("error", "Invalid username or password");
            return "signupfailure";
        }
    }

    @GetMapping("/success")
    public String logoutNow(){
        return "successful";
    }

    @GetMapping("/logout")
    public String loginComplete(HttpSession session, Model model) {
        if (session == null || session.getAttribute("user") == null){
            model.addAttribute("error", "You are not signed in");
            return "signupfailure";
        }
        session.invalidate();
        return "successful";
    }


    @GetMapping("/loginComplete")
    public String signinSuccessPage(){return "loginComplete";}

    @PostMapping("/user")
    public String createUser(@RequestParam("name") String name, @RequestParam("username") String username, @RequestParam("email") String email, @RequestParam("password") String password, Model model){

        if(userRepository.existsByUsername(username)||userRepository.existsByEmail(email)) {
            model.addAttribute("error", "The Account already exists!");
            return "signupfailure";
        }

        Matcher emailMatcher = EMAIL_PATTERN.matcher(email);
        if(!emailMatcher.matches()){
            model.addAttribute("error","Not a valid email format!");
            return "signupfailure";
        }

        Matcher passwordMatcher = PASSWORD_PATTERN.matcher(password);
        if(!passwordMatcher.matches()){
            model.addAttribute("error", "A password must be at least 8 characters long, have an upper and lower case letter, number and special character");
            return "signupfailure";
        }


        User user = new User(name, username, email, password);
        userRepository.save(user);
        //User newUser = userRepository.save(new User(user.getName(), user.getUsername(), user.getEmail(), user.getPassword()));
        return "viewdata";
    }

    @GetMapping("/getUser")
    public String viewData(Model model){
        model.addAttribute("users", userRepository.findAll());
        //
        return "viewdata";

    }
}