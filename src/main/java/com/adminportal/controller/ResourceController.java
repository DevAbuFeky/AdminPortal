package com.adminportal.controller;

import com.adminportal.repository.BookRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;

@RestController
public class ResourceController {
    @Autowired
    private BookRepository bookRepository;

    @RequestMapping(value = "/product/removeList", method = RequestMethod.POST)
    public String removeList(@RequestBody ArrayList<String> bookIdList, Model model){
        for (String id : bookIdList){
            String bookId = id.substring(8);
//            bookRepository.deleteById(bookId);
        }

        return "delete success";
    }
}
