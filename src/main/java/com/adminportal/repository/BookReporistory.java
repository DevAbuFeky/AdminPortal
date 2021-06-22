package com.adminportal.repository;

import com.adminportal.domain.Book;
import org.springframework.data.repository.CrudRepository;

public interface BookReporistory extends CrudRepository<Book, Long> {

}
