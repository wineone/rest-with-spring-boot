package br.com.wineone.services;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import br.com.wineone.config.FileStorageConfig;
import br.com.wineone.exceptions.FileStorageException;
import br.com.wineone.exceptions.MyFileNotFoundException;

@Service
public class FileStorageService {

	
	private final Path fileStorageLocation;
	
	@Autowired
	public FileStorageService(FileStorageConfig fileStorageConfig) {
		Path path = Paths.get(fileStorageConfig.getUploadDir()).toAbsolutePath().normalize();
		
		this.fileStorageLocation = path;
		
		try {
			Files.createDirectory(this.fileStorageLocation);
		
		}catch(Exception e) {
		}
	}
	
	
	public String storeFile(MultipartFile file) {
		String fileName = file.getOriginalFilename();
		try {
			Path targetLocation = this.fileStorageLocation.resolve(fileName);
			Files.copy(file.getInputStream(), targetLocation, StandardCopyOption.REPLACE_EXISTING);
			return fileName;
		}catch (Exception e) {
			throw new FileStorageException("Could not store the file try again",e);
		}
	}
	
	
	public Resource loadFileAsResource(String filename) {
		try {
			Path filePath = this.fileStorageLocation.resolve(filename);
			Resource resource = new UrlResource(filePath.toUri());
			if (resource.exists()) return resource;
			else throw new MyFileNotFoundException("File not found");
		}catch (Exception e) {
			throw new MyFileNotFoundException("File not found",e);
		}
	}
	
}



















