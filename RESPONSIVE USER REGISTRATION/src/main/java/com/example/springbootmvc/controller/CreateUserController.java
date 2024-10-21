package com.example.springbootmvc.controller;

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

@Controller
public class CreateUserController {

    @Autowired
    UserRepository userRepository;

    @PostMapping("/user")
    public String createUser(@RequestParam("name") String name, @RequestParam("username") String username, @RequestParam("email") String email, @RequestParam("password") String password){
        User user = new User(name, username, email, password);
        userRepository.save(user);
        //User newUser = userRepository.save(new User(user.getName(), user.getUsername(), user.getEmail(), user.getPassword()));

        return "viewdata";
    }

    @GetMapping("/getUser")
    public String viewData(Model model){
        model.addAttribute("users", userRepository.findAll());
        return "viewdata";
    }
}