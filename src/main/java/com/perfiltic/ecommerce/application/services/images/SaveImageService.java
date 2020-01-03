package com.perfiltic.ecommerce.application.services.images;

import java.io.IOException;

import org.springframework.web.multipart.MultipartFile;

public interface SaveImageService {

	public String saveImage(MultipartFile image) throws IOException;
}
