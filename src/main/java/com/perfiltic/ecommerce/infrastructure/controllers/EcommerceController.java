package com.perfiltic.ecommerce.infrastructure.controllers;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.dao.DataAccessException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpHeaders;
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
import com.perfiltic.ecommerce.application.services.images.GetImageService;
import com.perfiltic.ecommerce.application.services.images.SaveImageService;
import com.perfiltic.ecommerce.application.services.products.DeleteProductService;
import com.perfiltic.ecommerce.application.services.products.GetProductService;
import com.perfiltic.ecommerce.application.services.products.GetProductsByCategoryService;
import com.perfiltic.ecommerce.application.services.products.SaveProductService;
import com.perfiltic.ecommerce.domain.exceptions.CategoryIsNotLastLevelException;
import com.perfiltic.ecommerce.domain.exceptions.CategoryNotRemovableException;
import com.perfiltic.ecommerce.domain.model.Category;
import com.perfiltic.ecommerce.domain.model.Product;

@RestController
@RequestMapping("/api")
@CrossOrigin(origins = { "http://localhost:4200" })
public class EcommerceController {

	public static final String URL_GET_CATEGORY = "/category/{idCategory}";
	public static final String URL_SUPER_CATEGORIES = "/categories/{page}";
	public static final String URL_SUBCATEGORIES = "/subcategories/{idCategory}/{page}";
	public static final String URL_SAVE_CATEGORY = "/categories/save";
	public static final String URL_DELETE_CATEGORY = "/category/delete/{idCategory}";
	
	public static final String URL_GET_PRODUCT = "/product/{idProduct}";
	public static final String URL_PRODUCTS = "/products/{idCategory}/{page}";
	public static final String URL_DELETE_PRODUCT = "/product/delete/{idProduct}";
	public static final String URL_SAVE_PRODUCT = "/products/save";

	public static final String URL_GET_IMAGE = "/image/{nameImage:.+}";

	public static final String MESSAGE = "message";
	private static final String ERROR = "error";
	private static final int PAGE_SIZE = 2;
	private static final String NOT_REGISTERED = "%s with ID %d is not registered in the database.";	

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
	private GetProductService getProductService;
	@Autowired
	private GetProductsByCategoryService getProductsByCategoryService;
	@Autowired
	private DeleteProductService deleteProductService;
	@Autowired
	private SaveProductService saveProductService;

	@Autowired
	private SaveImageService saveImageService;
	@Autowired
	private DeleteImageService deleteImageService;
	@Autowired
	private GetImageService getImageService;
	
	private ObjectMapper objectMapper = new ObjectMapper();

	@GetMapping(URL_GET_CATEGORY)
	public ResponseEntity<Object> getCategory(@PathVariable Long idCategory) {
		Category category = getCategoryService.getCategory(idCategory);

		if (category == null) {
			Map<String, Object> response = new HashMap<>();
			response.put(MESSAGE, String.format(NOT_REGISTERED, "Category", idCategory));
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
		Category category = getCategoryService.getCategory(idCategory);
		
		try {
			deleteCategoryService.deleteCategory(idCategory);
			deleteImageService.deleteImage(category.getPicture());
			response.put(MESSAGE, "The category has been removed successfully");
			return ResponseEntity.ok(response);
			
		} catch (CategoryNotRemovableException exception) {
			response.put(MESSAGE, "The category is not removable");
			response.put(ERROR, exception.getMessage());
			return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
			
		} catch (DataAccessException exception) {
			response.put(MESSAGE, "There was an error querying the database");
			response.put(ERROR, exception.getMessage() + ": " + exception.getMostSpecificCause().getMessage());
			
		} catch (IOException exception) {
			response.put(MESSAGE, "There was an error deleting the image");
			response.put(ERROR, exception.getMessage() + ": " + exception.getCause().getMessage());
		} 
		
		return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
	}
	
	@GetMapping(URL_GET_PRODUCT)
	public ResponseEntity<Object> getProduct(@PathVariable Long idProduct) {
		Product product = getProductService.getProductById(idProduct);

		if (product == null) {
			Map<String, Object> response = new HashMap<>();
			response.put(MESSAGE, String.format(NOT_REGISTERED, "Product", idProduct));
			return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
		}

		return ResponseEntity.ok( product );
	}
	

	@GetMapping(URL_PRODUCTS)
	public Page<Product> getProductsByCategory(@PathVariable Long idCategory, @PathVariable Integer page) {
		return getProductsByCategoryService.getProductsByCategory(idCategory, PageRequest.of(page, PAGE_SIZE));
	}
	
	@DeleteMapping(URL_DELETE_PRODUCT)
	public ResponseEntity<Object> deleteProduct(@PathVariable Long idProduct) {
		Map<String, Object> response = new HashMap<>();
		Product product = getProductService.getProductById(idProduct);

		try {
			deleteProductService.deleteProduct(idProduct);
			deleteImageService.deleteImage(product.getPicture1());
			deleteImageService.deleteImage(product.getPicture2());
			deleteImageService.deleteImage(product.getPicture3());
			
			response.put(MESSAGE, "The product has been removed successfully");
			return ResponseEntity.ok(response);
			
		} catch (DataAccessException exception) {
			response.put(MESSAGE, "There was an error querying the database");
			response.put(ERROR, exception.getMessage() + ": " + exception.getMostSpecificCause().getMessage());
			
		} catch (IOException exception) {
			response.put(MESSAGE, "There was an error deleting the images");
			response.put(ERROR, exception.getMessage() + ": " + exception.getCause().getMessage());
		}
		
		return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
	}
	
	@PostMapping(URL_SAVE_PRODUCT)
	public ResponseEntity<Object> saveProduct(@RequestParam("product") String productJson,
											  @RequestParam(name = "images", required = false) List<MultipartFile> images) {
		Map<String, Object> response = new HashMap<>();
		
		try {
			Product product = objectMapper.readValue(productJson, Product.class);
			
			if(images != null && !images.isEmpty()) {
				List<String> pictures = saveProductPictures(images);
				
				switch(pictures.size()) {
					case 3:
						product.setPicture1(pictures.get(0));
						product.setPicture2(pictures.get(1));
						product.setPicture3(pictures.get(2));
						break;
					case 2:
						product.setPicture1(pictures.get(0));
						product.setPicture2(pictures.get(1));
						break;
					default:
						product.setPicture1(pictures.get(0));
						break;
				}
			}

			return ResponseEntity.ok(saveProductService.saveProduct(product));
			
		} catch (CategoryIsNotLastLevelException exception) {
			response.put(MESSAGE, exception.getMessage());
			return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
			
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
	
	public List<String> saveProductPictures(List<MultipartFile> images) throws IOException {			
		List<String> namesImages = new ArrayList<>();
		
		for(MultipartFile image: images)
			namesImages.add( saveImageService.saveImage(image) );
		
		return namesImages;
	}

	@GetMapping(URL_GET_IMAGE)
	public ResponseEntity<Resource> getImage(@PathVariable String nameImage) {
		Resource image = getImageService.getImage(nameImage);

		HttpHeaders headers = new HttpHeaders();
		headers.add(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + nameImage + "\"");

		return new ResponseEntity<>(image, headers, HttpStatus.OK);
	}
}
