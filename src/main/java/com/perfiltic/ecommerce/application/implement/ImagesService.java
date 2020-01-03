package com.perfiltic.ecommerce.application.implement;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.UUID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.perfiltic.ecommerce.application.services.images.DeleteImageService;
import com.perfiltic.ecommerce.application.services.images.GetImageService;
import com.perfiltic.ecommerce.application.services.images.SaveImageService;

@Service
public class ImagesService implements GetImageService, SaveImageService, DeleteImageService {

	private static final Logger log = LoggerFactory.getLogger(ImagesService.class);
	private static final String PATH_FILES = "pictures";
	
	@Override
	public Resource getImage(String nameImage) {
		Resource resource = null;
		Path path = getPath(nameImage);
		
		try {
			resource = new UrlResource(path.toUri());
		
			if(!resource.exists() || !resource.isReadable()) {
				log.error("The image {} can't be uploaded.", nameImage);			
				path = Paths.get("src/main/resources/static/images").resolve("no-picture.png").toAbsolutePath();
				resource = new UrlResource(path.toUri());
			}
		} catch (MalformedURLException exception) {
			log.error(exception.getMessage().concat(": ").concat(exception.getCause().toString()));
		}
			
		return resource;
	}
	
	@Override
	public String saveImage(MultipartFile image) throws IOException {
		String nameImage = UUID.randomUUID().toString() +"_"+ image.getOriginalFilename().replace(" ", "");		
		Files.copy(image.getInputStream(), getPath(nameImage));
		
		return nameImage;
	}

	@Override
	public void deleteImage(String nameImage) throws IOException {
		if(nameImage != null && !nameImage.isEmpty()) {
			Path pathImage = getPath(nameImage);
			File image = pathImage.toFile();
			
			if(image.exists() && image.canRead())
				Files.delete(pathImage);
		}
	}

	private Path getPath(String nameImage) {
		return Paths.get(PATH_FILES).resolve(nameImage).toAbsolutePath();
	}
}
