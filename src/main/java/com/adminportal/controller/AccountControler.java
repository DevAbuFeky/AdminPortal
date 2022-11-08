package com.adminportal.controller;

import com.adminportal.domain.User;
import com.adminportal.service.UserService;
import com.adminportal.service.impl.UserSecurityService;
import com.adminportal.utility.SecurityUtility;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;


import java.io.IOException;
import java.security.Principal;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

@Controller
public class AccountControler {

    @Autowired
    private UserService userService;

    @Autowired
    private UserSecurityService userSecurityService;

    @RequestMapping("/account")
    public String myProfile(Model model, Principal principal){
        User user = userService.findByUsername(principal.getName());
        model.addAttribute("user", user);

        model.addAttribute("classActiveEdit", true);

        return "account";

    }

    @RequestMapping(value = "/updateUserInfo", method = RequestMethod.POST)
    public String updateUserInfo (@ModelAttribute("user") User user, @ModelAttribute("newPassword") String newPassword, Model model) throws Exception{

        Optional<User> currentUser = userService.findById(user.getId());

        if (currentUser == null){
            throw new Exception("User not found");
        }

        if(currentUser.isPresent()){

//        check email already exists
            if (userService.findByEmail(user.getEmail()) != null){
                if (userService.findByEmail(user.getEmail()).getId() != currentUser.get().getId()){
                    model.addAttribute("emailExists", true);
                    return "account";
                }
            }

            //        check username already exists
            if (userService.findByUsername(user.getUsername()) != null){
                if (userService.findByUsername(user.getUsername()).getId() != currentUser.get().getId()){
                    model.addAttribute("usernameExists", true);
                    return "account";
                }
            }

            // update password
            if (newPassword != null && !newPassword.isEmpty() && !newPassword.equals("")){
                BCryptPasswordEncoder passwordEncoder = SecurityUtility.passwordEncoder();
                String dbPassword = currentUser.get().getPassword();
                if (passwordEncoder.matches(user.getPassword(), dbPassword)){
                    currentUser.get().setPassword(passwordEncoder.encode(newPassword));
                }else {
                    model.addAttribute("incorrectPassword", true);

                    return "account";
                }
            }
        }

        currentUser.get().setFirstName(user.getFirstName());
        currentUser.get().setLastName(user.getLastName());
        currentUser.get().setUsername(user.getUsername());
        currentUser.get().setEmail(user.getEmail());

        userService.save(currentUser.get());

        model.addAttribute("updateSuccess", true);
        model.addAttribute("user", currentUser.get());
        model.addAttribute("classActiveEdit",true);


        UserDetails userDetails = userSecurityService.loadUserByUsername(currentUser.get().getUsername());

        Authentication authentication = new UsernamePasswordAuthenticationToken(userDetails, userDetails.getPassword(), userDetails.getAuthorities());

        SecurityContextHolder.getContext().setAuthentication(authentication);

        return "account";
    }
}
