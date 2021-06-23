package com.adminportal.controller;

import com.adminportal.domain.Book;
import com.adminportal.service.BookService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping("/product")
public class BookController {

    @Autowired
    private BookService bookService;

    @RequestMapping(value = "/addProduct", method = RequestMethod.GET)
    public String addProduct(Model model){
        Book book = new Book();
        model.addAttribute("book", book);
        return "addProduct";
    }

    @RequestMapping(value = "/addProduct", method = RequestMethod.POST)
    public String addProductPost(@ModelAttribute("book") Book book,
                              HttpServletRequest request){
        bookService.save(book);
        MultipartFile bookImage = book.getBookImage();

        try{
            byte[] bytes = bookImage.getBytes();
            String name = book.getId() + ".png";
            BufferedOutputStream stream = new BufferedOutputStream(new FileOutputStream(new File("src/main/resource/static/image/book" + name)));
            stream.write(bytes);
            stream.close();
        }catch (Exception e){
            e.printStackTrace();
        }
        return "redirect:productsList";
    }

    @RequestMapping("/productDetails")
    public String productDetails(@RequestParam("id") Long id, Model model){
        Optional<Book> book = bookService.findOne(id);
        model.addAttribute("book", book);

        return "productDetails";
    }

    @RequestMapping("/updateProduct")
    public String updateProduct(Model model,@RequestParam("id") Long id){
        Optional<Book> book = bookService.findOne(id);
        model.addAttribute("book", book);
        return "updateProducts";
    }

    @RequestMapping(value = "/updateProduct", method = RequestMethod.POST)
    public String updateProductPost(@ModelAttribute("book") Book book, HttpServletRequest request){
        bookService.save(book);

        MultipartFile bookImage = book.getBookImage();

        if (!bookImage.isEmpty()){
            try{
                byte[] bytes = bookImage.getBytes();
                String name = book.getId() + ".png";

                Files.delete(Paths.get("src/main/resource/static/image/book" + name));

                BufferedOutputStream stream = new BufferedOutputStream(new FileOutputStream(new File("src/main/resource/static/image/book" + name)));
                stream.write(bytes);
                stream.close();
            }catch (Exception e) {
                e.printStackTrace();
            }
        }
        return "redirect:/product/productDetails?id=" + book.getId();
    }

    @RequestMapping("/productsList")
    public String products(Model model){
        List<Book> bookList = bookService.findAll();
        model.addAttribute("bookList", bookList);
        return "productsList";
    }

    @RequestMapping(value = "/remove", method = RequestMethod.POST)
    public String remove(@ModelAttribute("id") String id, Model model){
        bookService.removeOne(Long.parseLong(id.substring(8)));
        List<Book> bookList = bookService.findAll();
        model.addAttribute("bookList", bookList);

        return "redirect:/product/productsList";
    }
}
