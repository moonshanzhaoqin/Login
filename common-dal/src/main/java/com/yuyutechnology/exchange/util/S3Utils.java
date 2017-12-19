package com.yuyutechnology.exchange.util;

import java.io.File;

import javax.annotation.PostConstruct;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Component;

import com.amazonaws.AmazonClientException;
import com.amazonaws.AmazonServiceException;
import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import com.amazonaws.services.s3.model.CannedAccessControlList;
import com.amazonaws.services.s3.model.PutObjectRequest;

@Component
public class S3Utils {
	
	private String awsSAccessKeyId;
	private String awsSecretKey;
	private static String s3region;
	private static String s3bucketName;
	
	private static AmazonS3 s3Client;
	
	public static Logger logger = LogManager.getLogger(S3Utils.class);
	
	@PostConstruct
	public void init(){
		awsSAccessKeyId = ResourceUtils.getBundleValue4String("aws.saccesskey.id");
		awsSecretKey = ResourceUtils.getBundleValue4String("aws.secretkey");
		s3region = ResourceUtils.getBundleValue4String("s3.region");
		s3bucketName = ResourceUtils.getBundleValue4String("s3.bucketname");
		
		 BasicAWSCredentials awsCreds = new BasicAWSCredentials(awsSAccessKeyId, awsSecretKey);
		 s3Client = AmazonS3ClientBuilder.standard()
	                .withRegion(Regions.fromName(s3region))
	                .withCredentials(new AWSStaticCredentialsProvider(awsCreds))
	                .build();
	}
	
	 public static String uploadFile(String keyName,String uploadFileName){
		 
		 	String imgUrl = null;
		 
	        try {
	        	logger.info("Uploading a new object to S3 from a file");
	            File file = new File(uploadFileName);
	            s3Client.putObject(new PutObjectRequest(
	            		s3bucketName, keyName, file).withCannedAcl(CannedAccessControlList.PublicRead));        
//	            https://s3-ap-southeast-1.amazonaws.com/bucket4gp/xpf5.jpg
	            imgUrl = "https://s3-[s3region].amazonaws.com/[s3bucketName]/[keyName]"
	            		.replace("[s3bucketName]", s3bucketName)
	            		.replace("[s3region]", s3region)
	            		.replace("[keyName]", keyName);
	            
	            logger.info("Upload successfully,the image url : {}",imgUrl);
	            
	            
	        } catch (AmazonServiceException ase) {
	        	
	        	logger.info("Caught an AmazonServiceException, which means your "
	        			+ "request made it to Amazon S3, but was rejected "
	        			+ "with an error response for some reason.");

	        	logger.info("Error Message:		{}",ase.getMessage());
	        	logger.info("HTTP Status Code:	{}",ase.getStatusCode());
	        	logger.info("AWS Error Code:	{}",ase.getErrorCode());
	        	logger.info("Error Type:		{}",ase.getErrorType());
	        	logger.info("Request ID:		{}",ase.getRequestId());
	        	
	        } catch (AmazonClientException ace) {
	        	
	        	logger.info("Caught an AmazonClientException, which means "
	        			+ "the client encountered an internal error while "
	        			+ "trying to communicate with S3,such as "
	        			+ "not being able to access the network.");
	        	
	        	logger.info("Error Message:		{}",ace.getMessage());
	        }
	        
	        return imgUrl;
	    }
	

}
