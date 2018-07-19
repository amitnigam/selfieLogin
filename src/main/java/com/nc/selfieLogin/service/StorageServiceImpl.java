package com.nc.selfieLogin.service;

import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.entity.InputStreamEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.nc.selfie.security.SelfieUserDetailServiceImpl;

@Service
public class StorageServiceImpl implements StorageService {

	private Logger log = LogManager.getLogger(getClass()) ;
	
	private final static String rootPath = System.getProperty("catalina.home");
	private final static String imagePath = "/webapps/lg/" ;
	private final static String imageWebPath = "/lg/" ;
	private static final String faceListURI = "https://eastus.api.cognitive.microsoft.com/face/v1.0/"
			+ "facelists/facerecognitionlist/persistedFaces" ;
	@Value("${subscriptionKey}")
	private String subscriptionKey;
	
	@Override
	public String store(MultipartFile file, String fileFolder) {
		
		   try {
			   
                byte[] bytes = file.getBytes();
                log.info("Storing file at root path: " + rootPath );
                Path path = Paths.get(rootPath + imagePath + file.getOriginalFilename());
                log.info("Storing file at path: " + path );
                Files.write(path, bytes);

                

            } catch (IOException e) {
                e.printStackTrace();
            }

        String uploadedFileName = imageWebPath + file.getOriginalFilename();
        if (StringUtils.isEmpty(uploadedFileName)) {
            return " Error: Please select a file to upload" ;
        } else {
            return uploadedFileName;
        }
		

		
	}


	@Override
	public String storeCmsImage(MultipartFile file, String fileField) {
		if (file.isEmpty()) {
        	log.error("File is empty for " + fileField);
             return "error:file is empty" ;
        }
        	return store(file, "images/") ;
      
	}


	@Override
	public String storeVisitorImage(MultipartFile file, String fileField) {
		if (file.isEmpty()) {
        	log.error("File is empty for " + fileField);
             return "error:file is empty" ;
        }
        	return store(file, "img/") ;
	}


	@Override
	public String storeVisitorInFaceList(MultipartFile selfie) {
		HttpClient httpclient = new DefaultHttpClient();
		String faceId = null;
				
		 try {
			URIBuilder builder = new URIBuilder(faceListURI);
			 URI uri = builder.build();
	          HttpPost request = new HttpPost(uri);
	              request.setHeader("Ocp-Apim-Subscription-Key", subscriptionKey);
	              request.setHeader("Content-Type", "application/octet-stream");
	          InputStreamEntity reqEntity = new InputStreamEntity(selfie.getInputStream());
	            request.setEntity(reqEntity);
	            
	           	 HttpResponse response = httpclient.execute(request);
	            HttpEntity entity = response.getEntity();
	            if (entity != null)
	            {
	                // Format and display the JSON response.
	                String jsonString = EntityUtils.toString(entity).trim();
	                log.info("REST Response: " +jsonString);
	                ObjectMapper objectMapper = new ObjectMapper();
	                JsonNode rootNode = objectMapper.readTree(jsonString);
	                JsonNode idNode = rootNode.path("persistedFaceId") ;
	              
	                 faceId = idNode.asText() ;
	                log.info("Extracted face Id " + faceId) ;
	                return faceId ;
	            }  
	            
	          
	            
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		
		}
		return null;
	}

}
