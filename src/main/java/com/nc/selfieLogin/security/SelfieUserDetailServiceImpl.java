package com.nc.selfieLogin.security;


import java.io.InputStream;
import java.net.URI;
import java.util.Map;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;

import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.InputStreamEntity;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.json.BasicJsonParser;
import org.springframework.boot.json.JsonParser;
import org.springframework.boot.json.JsonParserFactory;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.nc.selfieLogin.dao.repository.UserRepository;




@Service("selfieUserDetailService")
public class SelfieUserDetailServiceImpl implements SelfieUserDetailService {
	
	@Autowired
	UserRepository userRepo ;

	//private static final String subscriptionKey = "a8a0fbd2096a428d8836b40e0386e924";
	private static final String uriBase = "https://eastus.api.cognitive.microsoft.com/face/v1.0/detect";
	private static final String findMatchUri = "https://eastus.api.cognitive.microsoft.com/face/v1.0/findsimilars" ;
	private static final String faceListId = "facerecognitionlist" ;
	private static final Logger log = LogManager.getLogger() ;
	
	@Value("${subscriptionKey}")
	private String subscriptionKey;
	
	@Override
	public UserDetails loadUserBySelfie(String username, Object selfie) throws UsernameNotFoundException {
		HttpClient httpclient = new DefaultHttpClient();
		String faceId = null;
				
		 try {
			URIBuilder builder = new URIBuilder(uriBase);
			 builder.setParameter("returnFaceId", "true");
			 URI uri = builder.build();
	          HttpPost request = new HttpPost(uri);
	              request.setHeader("Ocp-Apim-Subscription-Key", subscriptionKey);
	              request.setHeader("Content-Type", "application/octet-stream");
	          InputStreamEntity reqEntity = new InputStreamEntity((InputStream) selfie);
	            request.setEntity(reqEntity);
	            
	           	            
	          HttpResponse response = httpclient.execute(request);
	            HttpEntity entity = response.getEntity();
	            if (entity != null)
	            {
	                // Format and display the JSON response.
	                String jsonString = EntityUtils.toString(entity).trim();
	                log.info("REST Response: " +jsonString);
	                jsonString = jsonString.substring(1) ;
	            	jsonString = jsonString.substring(0, jsonString.length() -1 ) ;
	                ObjectMapper objectMapper = new ObjectMapper();
	                JsonNode rootNode = objectMapper.readTree(jsonString);
	                JsonNode idNode = rootNode.path("faceId") ;
	              
	                 faceId = idNode.asText() ;
	                log.info("Extracted face Id " + faceId) ;
	                
	            }
	            	            
	            
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		
		}
		 String matchedFace = findFaceMatch(faceId) ;
		 if(matchedFace!=null){
		 com.nc.visitorManagement.dao.entity.User user = userRepo.findByHeadShot(matchedFace) ;
		 if (user!=null) {
		 return convertUser(user);
		 }
		 return null ;
		 }
		 return null ;
		
	}
	@Override
	public String findFaceMatch(String detectedFaceId) {
		
		HttpClient httpclient = new DefaultHttpClient();
		
		
		 try {
			URIBuilder builder = new URIBuilder(findMatchUri);
			
			 URI uri = builder.build();
	          HttpPost request = new HttpPost(uri);
	              request.setHeader("Ocp-Apim-Subscription-Key", subscriptionKey);
	              request.setHeader("Content-Type", "application/json");
	              String json = "{\"faceId\": \""+ detectedFaceId + "\", \"faceListId\": \""+faceListId+"\" , "
	              		+ "\"maxNumOfCandidatesReturned\":1,\"mode\": \"matchPerson\"}";
	              StringEntity reqEntity = new StringEntity(json, ContentType.APPLICATION_JSON);
	           	   request.setEntity(reqEntity);         
	            HttpResponse response = httpclient.execute(request);
	            HttpEntity entity = response.getEntity();
	            if (entity != null)
	            {
	                // Format and display the JSON response.
	                String jsonString = EntityUtils.toString(entity).trim();
	                log.info("REST Response: " +jsonString);
	                jsonString = jsonString.substring(1) ;
	            	jsonString = jsonString.substring(0, jsonString.length() -1 ) ;
	                ObjectMapper objectMapper = new ObjectMapper();
	                JsonNode rootNode = objectMapper.readTree(jsonString);
	                JsonNode idNode = rootNode.path("persistedFaceId") ;
	                JsonNode confidenceNode = rootNode.path("confidence") ;
	                String persistedFaceId = idNode.asText() ;
	                Double confidence = confidenceNode.asDouble() ;
	                log.info("Extracted face Id " + persistedFaceId + "with confidence of "+ confidence) ;
	                if(confidence > 0.6) {
	                	return persistedFaceId ;
	                }
	                
	                log.error("Face match entity in the response was empty");
	            }
	            
	            
		} catch (Exception e) {
			// TODO Auto-generated catch block
			log.error("Face match entity in the response was empty");
			e.printStackTrace();
			return null ;
		
		}
		
		return null;
	}

	private User convertUser(com.nc.visitorManagement.dao.entity.User user){
		
		//Returning a fake user as of now.
	User userToReturn = new User(user.getName(), "test", true, true, true, true, AuthorityUtils.NO_AUTHORITIES) ; 
	return userToReturn ;
		
	}
}
