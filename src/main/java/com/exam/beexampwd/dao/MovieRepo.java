package com.exam.beexampwd.dao;

import org.springframework.data.jpa.repository.JpaRepository;

import com.exam.beexampwd.entity.Movie;

public interface MovieRepo extends JpaRepository<Movie, Integer>{

}
