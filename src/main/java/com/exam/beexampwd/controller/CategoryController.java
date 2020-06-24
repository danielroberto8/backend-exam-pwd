package com.exam.beexampwd.controller;

import java.util.Optional;

import java.util.List;

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

@RestController
@RequestMapping("/category")
@CrossOrigin(origins ="http://localhost:3000")
public class CategoryController {

	@Autowired
	private CategoryRepo categoryRepo;
	
	@Autowired
	private MovieRepo movieRepo;
	
	@GetMapping
	public Iterable<Category> getAllCategory(){
		return categoryRepo.findAll();
	}
	
	//ADD CATEGORY
	@PostMapping
	public Category addCategory(@RequestBody Category category){
		category.setId(0);
		return categoryRepo.save(category);
	}
	
	//Edit Category
	@PutMapping
	public Category updateCategory(@RequestBody Category category) {
		Optional<Category> findCategory = categoryRepo.findById(category.getId());
		if(findCategory.toString() == "Optional.empty") {
			throw new RuntimeException("movie with id "+category.getId()+" doesnt exist.");
		}
		return categoryRepo.save(category);
	}

	//Delete Category Berdasarkan Movie Id
	@GetMapping("/{cat_id}/movies/{movie_id}")
	public void deleteCategoryByMovieId(@PathVariable int cat_id, @PathVariable int movie_id) {
		Category findCat = categoryRepo.findById(cat_id).get();
		if(findCat==null) {
			throw new RuntimeException("Category tidak ditemukan.");
		}
		
		findCat.getMovies().forEach(movie->{
			if(movie.getId()==movie_id) {
				List<Category> movieCategory = movie.getCategories();
				movieCategory.remove(findCat);
				movieRepo.save(movie);
			}
		});
	}
	
	//Delete Category Berdasarkan Category Id
	@DeleteMapping("/{id}")
	public void deleteCategory(@PathVariable int id) {
		Category findCat = categoryRepo.findById(id).get();
		if(findCat==null) {
			throw new RuntimeException("Datanya ga ada mang.");
		}
		
		findCat.getMovies().forEach(movie ->{
			List<Category> movieCategories = movie.getCategories();
			movieCategories.remove(findCat);
			movieRepo.save(movie);
		});
		
		findCat.setMovies(null);
		categoryRepo.deleteById(id);
	}
}
