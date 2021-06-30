package com.adminportal.controller;

import com.adminportal.domain.Book;
import com.adminportal.domain.Category;
import com.adminportal.repository.CategoryRepository;
import com.adminportal.service.BookService;
import com.adminportal.utility.FileUploadUtil;
import io.netty.util.internal.StringUtil;
import javassist.NotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpServletRequest;
import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Controller
@RequestMapping("/product")
public class BookController {

    @Autowired
    private BookService bookService;

    @Autowired
    private CategoryRepository categoryRepository;

    @RequestMapping(value = "/addProduct", method = RequestMethod.GET)
    public String addProduct(Model model){
        List<Category> listCategories = categoryRepository.findAll();
        Book book = new Book();
        model.addAttribute("book", book);
        model.addAttribute("listCategories", listCategories);

        return "addProduct";
    }

    @RequestMapping(value = "/addProduct", method = RequestMethod.POST)
    public String addProductPost(@ModelAttribute(name = "book") Book book,
                                 RedirectAttributes redirectAttributes, @RequestParam("bookImage") MultipartFile multipartFile) throws IOException {

        if (!multipartFile.isEmpty()){
            String fileName = StringUtils.cleanPath(Objects.requireNonNull(multipartFile.getOriginalFilename()));
            book.setLogo(fileName);
            Book savedBook = bookService.save(book);

            String uploadDir = "image/" + savedBook.getId();
            FileUploadUtil.cleanDir(uploadDir);
            FileUploadUtil.saveFile(uploadDir, fileName, multipartFile);
        } else {
         if (book.getLogo().isEmpty()){
             book.setLogo(null);
         }
         bookService.save(book);
        }

        redirectAttributes.addFlashAttribute("message", "The Product has been saved successfully.");

//        System.out.println(fileName.);
//        FileUploadUtil.saveFile(uploadDir, fileName, multipartFile);

//        String bookImage = book.getImage();

//        try{
//            byte[] bytes = bookImage.getBytes();
//            //create path
//            String name = book.getId() + ".png";
//            Path path = Paths.get("./src/main/resources/static/image/book/").toAbsolutePath().normalize();
//            Files.createDirectories(path);
//            BufferedOutputStream stream = new BufferedOutputStream(new FileOutputStream(path.toString() + name));
//            stream.write(bytes);
//            stream.close();
//        }catch (Exception e){
//            e.printStackTrace();
//        }

        return "redirect:productsList";
    }

    @RequestMapping("/productDetails")
    public String productDetails(@RequestParam("id") Long id, Model model) throws NotFoundException {


        Optional<Book> book = bookService.findOne(id);
        if(book.isPresent()) {
            System.out.println(book.get().getId());
            model.addAttribute("book", book.get());

            return "productDetails";
        } else {
            throw new NotFoundException("book not found");
        }
//        model.addAttribute("book", book.get());
//
//        return "productDetails";
    }

    @RequestMapping("/updateProduct")
    public String updateProduct(Model model,@RequestParam("id") Long id) throws NotFoundException {

        Optional<Book> book = bookService.findOne(id);
        if(book.isPresent()) {
            List<Category> listCategories = categoryRepository.findAll();
            model.addAttribute("listCategories", listCategories);
            System.out.println(book.get().getId());
            model.addAttribute("book", book.get());
            return "updateProduct";
        } else {
            throw new NotFoundException("Product not found");
        }
    }

    @RequestMapping(value = "/updateProduct", method = RequestMethod.POST)
    public String updateProductPost(@ModelAttribute("book") Book book, RedirectAttributes redirectAttributes, @RequestParam("bookImage") MultipartFile multipartFile) throws IOException{


        bookService.save(book);

        if (!multipartFile.isEmpty()){
            String fileName = StringUtils.cleanPath(Objects.requireNonNull(multipartFile.getOriginalFilename()));
            book.setLogo(fileName);
            book.setLogo(fileName);
            Book savedBook = bookService.save(book);

            String uploadDir = "image/" + savedBook.getId();
            FileUploadUtil.cleanDir(uploadDir);
            FileUploadUtil.saveFile(uploadDir, fileName, multipartFile);
        } else {
            if (book.getLogo().isEmpty()){
                book.setLogo(null);
            }
            bookService.save(book);
        }
        redirectAttributes.addFlashAttribute("message", "The Product has been updated successfully.");

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
