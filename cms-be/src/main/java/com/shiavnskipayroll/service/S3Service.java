package com.shiavnskipayroll.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import software.amazon.awssdk.auth.credentials.AwsBasicCredentials;
import software.amazon.awssdk.auth.credentials.StaticCredentialsProvider;
import software.amazon.awssdk.core.exception.SdkException;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.DeleteObjectRequest;
import software.amazon.awssdk.services.s3.model.GetObjectRequest;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
@Service
public class S3Service {  // Rename S4Service to S3Service

    private final S3Client s3Client;
    private final String bucketName;

    public S3Service(@Value("${aws.s3.bucket}") String bucketName,
                     @Value("${aws.access.key}") String accessKey,
                     @Value("${aws.secret.key}") String secretKey,
                     @Value("${aws.s3.region}") String region) {
        this.bucketName = bucketName;
        AwsBasicCredentials awsCredentials = AwsBasicCredentials.create(accessKey, secretKey);
        this.s3Client = S3Client.builder()
                .region(Region.of(region))
                .credentialsProvider(StaticCredentialsProvider.create(awsCredentials))
                .build();
    }
    //-----------------------------------------------------------------------------------------
    public String uploadFiles(MultipartFile file, String path) throws IOException {
       

        PutObjectRequest putObjectRequest = PutObjectRequest.builder()
                .bucket(bucketName)
                .key(path)
                .build();

        s3Client.putObject(putObjectRequest, RequestBody.fromBytes(file.getBytes()));
        return "https://" + bucketName + ".s3." + "  "+ ".amazonaws.com/" + path;
    }
    //--------------------------------------------------------------------------
    public InputStream displayS3FileContent(String s3FilePath) {
    	
    	String key = extractKeyFromPath(s3FilePath, bucketName); 
    	GetObjectRequest getObjectRequest = GetObjectRequest.builder()
        .bucket(bucketName)
        .key(key)
        .build();
    	
    	return s3Client.getObject(getObjectRequest);
    }
    //-------------------------download FIle from s3---------------------------------------------------
    public byte[] downloadFile( String filePath) throws IOException {
        
            // Fetch the InputStream from S3
            InputStream inputStream = displayS3FileContent(filePath);
            byte[] fileBytes = inputStreamToByteArray(inputStream);
            // Extract the file name for the response
            String fileName = filePath.substring(filePath.lastIndexOf("/") + 1);
            return fileBytes;
        
    }
    //-------------------------------------------------------------------------------
    public MultipartFile getFileFromS3(String filePath) throws IOException {
    	 String key = extractKeyFromPath(filePath, bucketName); // Call the previously defined method
            InputStream inputStream = s3Client.getObject(GetObjectRequest.builder()
    	            .bucket(bucketName)
    	            .key(key)
    	            .build());
    	    byte[] content = inputStreamToByteArray(inputStream);
    	    String fileName = key.substring(key.lastIndexOf("/") + 1); // Extract file name from key
    	    return new MockMultipartFile(fileName, fileName, "application/octet-stream", content); 
    }
    //--------------------------------------------------------------------------------------
    // Method to delete an old file from S3
    public void deleteOldFileFromS3(String oldFileNamePath) {
        try {
        	 String key = extractKeyFromPath(oldFileNamePath, bucketName);
        	

        	 
            // Create a DeleteObjectRequest using the builder
            DeleteObjectRequest deleteRequest = DeleteObjectRequest.builder()
                    .bucket(bucketName)
                    .key(key)
                    .build();
                    
            // Perform the delete operation
            s3Client.deleteObject(deleteRequest);
        } catch (SdkException e) {
            // Log the exception or handle it as necessary
            throw new RuntimeException("Error while deleting file from S3: " + e.getMessage(), e);
        } catch (Exception e) {
            // Catch any other exceptions and throw a runtime exception
            throw new RuntimeException("An unexpected error occurred while deleting file from S3", e);
        }
    }
    
    //---------------------------------HELPER-----------------------------------------------------
    private String extractKeyFromPath(String filePath, String bucketName) {
        filePath = filePath.trim();        
        int index =filePath.indexOf(".com/");
        return filePath.substring(index+5);
    }

    // Helper method to convert InputStream to byte[]
    private byte[] inputStreamToByteArray(InputStream inputStream) throws IOException {
      ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        byte[] buffer = new byte[50000];
       
        int length;
        while ((length = inputStream.read(buffer)) != -1) {
            byteArrayOutputStream.write(buffer, 0, length);
        }
        return byteArrayOutputStream.toByteArray();
    }
    
   
    
}
