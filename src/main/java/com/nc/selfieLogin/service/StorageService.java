package com.nc.selfieLogin.service;

import org.springframework.web.multipart.MultipartFile;

public interface StorageService {
	
 String store(MultipartFile file, String fileFolder) ;	
 String storeCmsImage (MultipartFile file, String fileField) ;
 String storeVisitorImage(MultipartFile file, String fileField) ;
 String storeVisitorInFaceList(MultipartFile image) ;
}
