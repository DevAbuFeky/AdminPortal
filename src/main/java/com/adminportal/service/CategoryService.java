package com.adminportal.service;

import com.adminportal.domain.Category;

import java.util.List;

public interface CategoryService {


    void removeOne(Integer id);

    List<Category> findAll();
}
