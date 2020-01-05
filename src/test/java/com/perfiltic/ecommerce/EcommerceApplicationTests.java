package com.perfiltic.ecommerce;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.io.UnsupportedEncodingException;

import org.json.JSONArray;
import org.json.JSONObject;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.TestInstance.Lifecycle;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.perfiltic.ecommerce.application.services.implementations.ImagesService;
import com.perfiltic.ecommerce.domain.model.Category;
import com.perfiltic.ecommerce.infrastructure.controllers.EcommerceController;
import com.perfiltic.ecommerce.infrastructure.daos.CategoryRepositoryDao;
import com.perfiltic.ecommerce.infrastructure.daos.ProductRepositoryDao;

@SpringBootTest
@TestInstance(Lifecycle.PER_CLASS)
@TestPropertySource(locations = "classpath:application-test.properties")
class EcommerceApplicationTests {

	private static final String LOCALHOST_API = "http://localhost:8080/api";

	@Autowired
	private EcommerceController ecommerceController;
	@Autowired
	private CategoryRepositoryDao categoryRepositoryDao;
	@Autowired
	private ProductRepositoryDao productRepositoryDato;
	@Autowired
	private ImagesService imagesService;

	private MockMvc mockMvc;
	private ObjectMapper objectMapper;

	@BeforeAll
	public void setUp() {
		mockMvc = MockMvcBuilders.standaloneSetup(ecommerceController).build();
		objectMapper = new ObjectMapper();
	}

	@AfterAll
	public void tearDown() {
		productRepositoryDato.deleteAll();
		categoryRepositoryDao.deleteAll();
	}

	@Test
	public void getCategoryTest() throws Exception {
		// Arrange
		int idCategory = 1;
		String expectedName = "Computers";

		// Act
		Category category = objectMapper.readValue( mockMvc.perform( get(LOCALHOST_API + EcommerceController.URL_GET_CATEGORY, idCategory) )
								.andExpect(status().isOk()).andReturn().getResponse().getContentAsString(), Category.class);
		// Assert
		assertEquals(expectedName, category.getName());
	}
	
	@Test
	public void getSuperCategoriesTest() throws Exception {
		// Arrange
		String expectedName = "Computers";
		
		// Act
		JSONObject jsonObject = new JSONObject(mockMvc.perform( get(LOCALHOST_API + EcommerceController.URL_SUPER_CATEGORIES, 0) )
				.andExpect(status().isOk()).andReturn().getResponse().getContentAsString());

		JSONArray jsonArray = jsonObject.getJSONArray("content");
		Category category =  objectMapper.readValue(jsonArray.get(0).toString(), Category.class);
		
		// Assert
		assertEquals(expectedName, category.getName());
	}
	
	@Test
	public void getSubCategoriesTest() throws Exception {
		// Arrange
		String expectedName = "Furnitures";
		
		// Act
		JSONObject jsonObject = new JSONObject(mockMvc.perform( get(LOCALHOST_API + EcommerceController.URL_SUBCATEGORIES, 2, 0) )
				.andExpect(status().isOk()).andReturn().getResponse().getContentAsString());

		JSONArray jsonArray = jsonObject.getJSONArray("content");
		Category category =  objectMapper.readValue(jsonArray.get(0).toString(), Category.class);
		
		// Assert
		assertEquals(expectedName, category.getName());
	}
	
	@Test
	public void createCategoryTest() throws Exception {
		// Arrange
		String fileName = "imageName.jpg";
		MockMultipartFile image = new MockMultipartFile("image", fileName, "image/jpg", fileName.getBytes());
		String category = "{\"name\": \"Staff\", \"superCategory\": 1}";
		
		// Act
		long amountBefore = categoryRepositoryDao.count();
		
		String response = mockMvc.perform(MockMvcRequestBuilders.multipart(LOCALHOST_API + EcommerceController.URL_SAVE_CATEGORY)
				.file(image).param("category", category)).andExpect(status().isOk())
				.andReturn().getResponse().getContentAsString();
		
		Category savedCategory = objectMapper.readValue(response, Category.class);
		imagesService.deleteImage(savedCategory.getPicture());

		long amountAfter = categoryRepositoryDao.count();
		
		// Assert
		assertTrue(savedCategory.getPicture().contains(fileName) && amountAfter == amountBefore+1);
	}
	
	@Test
	public void updateCategoryTest() throws Exception {
		// Arrange
		String fileName = "imageName.jpg";
		MockMultipartFile image = new MockMultipartFile("image", fileName, "image/jpg", fileName.getBytes());
		String category = "{\"idCategory\": 4, \"name\": \"Staff\", \"superCategory\": 1,"
				+ "\"picture\": \"pc-tel-1.jpg\"}";
		
		// Act
		String response = mockMvc.perform(MockMvcRequestBuilders.multipart(LOCALHOST_API + EcommerceController.URL_SAVE_CATEGORY)
				.file(image).param("category", category)).andExpect(status().isOk())
				.andReturn().getResponse().getContentAsString();
		
		Category savedCategory = objectMapper.readValue(response, Category.class);
		imagesService.deleteImage(savedCategory.getPicture());
		
		// Assert
		assertTrue(savedCategory.getPicture().contains(fileName) && savedCategory.getName().equals("Staff"));
	}
	
	@Test
	public void deleteCategoryTest() throws UnsupportedEncodingException, Exception {
		// Arrange
		long amountBefore;
		long amountAfter;
		
		// Act
		amountBefore = categoryRepositoryDao.count();		
		mockMvc.perform( delete(LOCALHOST_API + EcommerceController.URL_DELETE_CATEGORY, 5) ).andExpect(status().isOk());
		amountAfter = categoryRepositoryDao.count();
		
		// Assert
		assertTrue(amountAfter == amountBefore - 1);
	}

}
