package com.exam.beexampwd.dao;

import org.springframework.data.jpa.repository.JpaRepository;

import com.exam.beexampwd.entity.Category;

public interface CategoryRepo extends JpaRepository<Category, Integer>{

}
