package com.adminportal.service;

import com.adminportal.domain.Book;

import java.util.List;
import java.util.Optional;

public interface BookService {
    Book save(Book book);

    List<Book> findAll();

    Optional<Book> findOne(Long id);

    void removeOne(Long id);
}
