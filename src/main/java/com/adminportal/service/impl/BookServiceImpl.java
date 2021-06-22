package com.adminportal.service.impl;

import com.adminportal.domain.Book;
import com.adminportal.repository.BookReporistory;
import com.adminportal.service.BookService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class BookServiceImpl implements BookService {
    @Autowired
    private BookReporistory bookRepository;

    public Book save(Book book){
        return bookRepository.save(book);
    }

    public List<Book> findAll(){
        return (List<Book>) bookRepository.findAll();
    }

    public Optional<Book> findOne(Long id){
        return bookRepository.findById(id);
    }

    @Override
    public void removeOne(Long id) {
        bookRepository.deleteById(id);
    }
}
