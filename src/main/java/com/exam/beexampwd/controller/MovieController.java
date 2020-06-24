package com.exam.beexampwd.controller;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.exam.beexampwd.dao.CategoryRepo;
import com.exam.beexampwd.dao.MovieRepo;
import com.exam.beexampwd.entity.Category;
import com.exam.beexampwd.entity.Movie;

@RestController
@RequestMapping("/movies")
@CrossOrigin(origins ="http://localhost:3000")
public class MovieController {

	@Autowired
	private MovieRepo movieRepo;
	
	@Autowired
	private CategoryRepo categoryRepo;
	
	@GetMapping
	public Iterable<Movie> getAllMovie(){
		return movieRepo.findAll();
	}
	
	//ADD MOVIES
	@PostMapping
	public Movie addMovie(@RequestBody Movie movie) {
		movie.setId(0);
		return movieRepo.save(movie);
	}
	
	//ADD CATEGORIES TO MOVIE
	@PostMapping("/{movie_id}/category/{cat_id}")
	public Movie addCategoryToMovie(@PathVariable int movie_id, @PathVariable int cat_id) {
		Movie findMovie = movieRepo.findById(movie_id).get();
		if(findMovie==null) {
			throw new RuntimeException("Filmnya tydac ada");
		}
		Category findCat = categoryRepo.findById(cat_id).get();
		if(findCat==null) {
			throw new RuntimeException("Kategorinya tydac ada");
		}
		findMovie.getCategories().forEach(category -> {
			if(category.getId()==cat_id){
				throw new RuntimeException("Category sudah ditambahkan");
			}
		});
		
		findMovie.getCategories().add(findCat);
		return movieRepo.save(findMovie);
	}
	
	//EDIT MOVIES TITLE AND YEAR
	@PutMapping()
	public Movie updateMovie(@RequestBody Movie movie) {
		Optional<Movie> findMovie = movieRepo.findById(movie.getId());
		if(findMovie.toString() == "Optional.empty") {
			throw new RuntimeException("movie with id "+movie.getId()+" doesnt exist.");
		}
		return movieRepo.save(movie);
	}
	
	//DELETE MOVIES
	@DeleteMapping("/{id}")
	public void deleteMovie(@PathVariable int id) {
		Movie findMovie = movieRepo.findById(id).get();
		if(findMovie==null) {
			throw new RuntimeException("Movie tidak ditemukan.");
		}
		
		findMovie.getCategories().forEach(category->{
			List<Movie> categoryMovies = category.getMovies();
			categoryMovies.remove(findMovie);
			categoryRepo.save(category);
		});
		
		findMovie.setCategories(null);
		movieRepo.deleteById(id);
	}
}
