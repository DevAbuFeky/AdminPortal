package com.adminportal.repository;

import com.adminportal.domain.Book;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.CrudRepository;

public interface BookRepository extends JpaRepository<Book, Integer> {

}
