package com.perfiltic.ecommerce.infrastructure.controllers;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.perfiltic.ecommerce.application.services.categories.DeleteCategoryService;
import com.perfiltic.ecommerce.application.services.categories.GetCategoryService;
import com.perfiltic.ecommerce.application.services.categories.GetSubCategoriesService;
import com.perfiltic.ecommerce.application.services.categories.GetSuperCategoriesService;
import com.perfiltic.ecommerce.application.services.categories.SaveCategoryService;
import com.perfiltic.ecommerce.application.services.images.DeleteImageService;
import com.perfiltic.ecommerce.application.services.images.SaveImageService;
import com.perfiltic.ecommerce.domain.model.Category;

@RestController
@RequestMapping("/api")
@CrossOrigin(origins = { "http://localhost:4200" })
public class EcommerceController {

	public static final String URL_GET_CATEGORY = "/category/{idCategory}";
	public static final String URL_SUPER_CATEGORIES = "/categories/{page}";
	public static final String URL_SUBCATEGORIES = "/subcategories/{idCategory}/{page}";
	public static final String URL_SAVE_CATEGORY = "/categories/save";
	public static final String URL_DELETE_CATEGORY = "/categories/delete/{idCategory}";

	private static final int PAGE_SIZE = 2;
	private static final String MESSAGE = "message";
	private static final String ERROR = "error";

	@Autowired
	private GetCategoryService getCategoryService;
	@Autowired
	private GetSuperCategoriesService getSuperCategoriesService;
	@Autowired
	private GetSubCategoriesService getSubCategoriesService;
	@Autowired
	private SaveCategoryService saveCategoryService;
	@Autowired 
	private DeleteCategoryService deleteCategoryService;

	@Autowired
	private SaveImageService saveImageService;
	@Autowired
	private DeleteImageService deleteImageService;
	
	private ObjectMapper objectMapper = new ObjectMapper();

	@GetMapping(URL_GET_CATEGORY)
	public ResponseEntity<Object> getCategory(@PathVariable Long idCategory) {
		Category category = getCategoryService.getCategory(idCategory);

		if (category == null) {
			Map<String, Object> response = new HashMap<>();
			response.put(MESSAGE, "Category with ID " + idCategory + " is not registered in the database.");
			return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
		}

		return ResponseEntity.ok(category);
	}

	@GetMapping(URL_SUPER_CATEGORIES)
	public Page<Category> getSuperCategories(@PathVariable Integer page) {
		return getSuperCategoriesService.getSuperCategories(PageRequest.of(page, PAGE_SIZE));
	}

	@GetMapping(URL_SUBCATEGORIES)
	public Page<Category> getSubCategories(@PathVariable Long idCategory, @PathVariable Integer page) {
		return getSubCategoriesService.getSubCategories(idCategory, PageRequest.of(page, PAGE_SIZE));
	}

	@PostMapping(URL_SAVE_CATEGORY)
	public ResponseEntity<Object> saveCategory(@RequestParam("category") String categoryJson,
											@RequestParam(name = "image", required = false) MultipartFile image) {		
		Map<String, Object> response = new HashMap<>();
		
		try {
			Category category = objectMapper.readValue(categoryJson, Category.class);

			if(image != null) {
				String nameNewImage = saveImageService.saveImage(image);
				
				String nameLastImage = category.getPicture();
				if(nameLastImage != null)
					deleteImageService.deleteImage(nameLastImage);
				
				category.setPicture(nameNewImage);
			}

			return ResponseEntity.ok(saveCategoryService.saveCategory(category));

		} catch (JsonProcessingException exception) {
			response.put(MESSAGE, "The category has not been sent correctly");
			response.put(ERROR, exception.getMessage() + ": " + exception.getCause().getMessage());
			
		} catch (DataAccessException exception) {
			response.put(MESSAGE, "There was an error saving the category");
			response.put(ERROR, exception.getMessage() + ": " + exception.getMostSpecificCause().getMessage());

		} catch (IOException exception) {
			response.put(MESSAGE, "There was an error saving the image");
			response.put(ERROR, exception.getMessage() + ": " + exception.getCause().getMessage());
		} 

		return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
	}
	
	@DeleteMapping(URL_DELETE_CATEGORY)
	public ResponseEntity<Object> deleteCategory(@PathVariable Long idCategory) {
		Map<String, Object> response = new HashMap<>();

		try {
			deleteCategoryService.deleteCategory(idCategory);
			response.put(MESSAGE, "The category has been removed successfully");
			return ResponseEntity.ok(response);
			
		} catch (DataAccessException exception) {
			response.put(MESSAGE, "There was an error querying the database");
			response.put(ERROR, exception.getMessage() + ": " + exception.getMostSpecificCause().getMessage());
			return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
		} 
	}
}
