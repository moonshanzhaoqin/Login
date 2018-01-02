package com.yuyutechnology.exchange.util;

import java.io.InputStream;
import javax.annotation.PostConstruct;

import org.apache.commons.lang.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Component;
import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import com.amazonaws.services.s3.model.CannedAccessControlList;
import com.amazonaws.services.s3.model.ObjectMetadata;
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
	public void init() {
		awsSAccessKeyId = ResourceUtils.getBundleValue4String("aws.saccesskey.id", "AKIAIUWEBIICN6VCXRCQ");
		awsSecretKey = ResourceUtils.getBundleValue4String("aws.secretkey", "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");
		s3region = ResourceUtils.getBundleValue4String("s3.region", "ap-southeast-1");
		s3bucketName = ResourceUtils.getBundleValue4String("s3.bucketname", "test-goldpay");

		BasicAWSCredentials awsCreds = new BasicAWSCredentials(awsSAccessKeyId, awsSecretKey);
		s3Client = AmazonS3ClientBuilder.standard().withRegion(Regions.fromName(s3region))
				.withCredentials(new AWSStaticCredentialsProvider(awsCreds)).build();
	}

	public static String uploadFile(String keyName, InputStream input, long contentLength, String contentType) {
		String imgUrl = null;
		logger.info("Uploading a new object to S3 from a file");
		// s3Client.putObject(
		// new PutObjectRequest(s3bucketName, keyName,
		// file).withCannedAcl(CannedAccessControlList.PublicRead));
		ObjectMetadata metadata = new ObjectMetadata();
		metadata.setContentLength(contentLength);
		metadata.setContentType(contentType);
		s3Client.putObject(new PutObjectRequest(s3bucketName, keyName, input, metadata)
				.withCannedAcl(CannedAccessControlList.PublicRead));
		imgUrl = "https://s3-[s3region].amazonaws.com/[s3bucketName]/[keyName]".replace("[s3bucketName]", s3bucketName)
				.replace("[s3region]", s3region).replace("[keyName]", keyName);
		logger.info("Upload successfully,the image url : {}", imgUrl);
		return imgUrl;
	}

	public static String getImgUrl(String keyName) {
		if (StringUtils.isBlank(keyName)) {
			return "";
		}
		String imgUrl = "https://s3-[s3region].amazonaws.com/[s3bucketName]/[keyName]"
				.replace("[s3bucketName]", s3bucketName).replace("[s3region]", s3region).replace("[keyName]", keyName);
		return imgUrl;
	}
}
