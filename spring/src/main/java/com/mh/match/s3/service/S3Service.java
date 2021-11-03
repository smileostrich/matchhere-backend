package com.mh.match.s3.service;


import com.amazonaws.AmazonServiceException;
import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import com.amazonaws.services.s3.internal.Mimetypes;
import com.amazonaws.services.s3.model.*;
import com.mh.match.s3.entity.DBFile;
import com.mh.match.s3.repository.DBFileRepository;
import com.mh.match.s3.exception.FileStorageException;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.PostConstruct;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@RequiredArgsConstructor
@Service
public class S3Service {
    @Value("${cloud.aws.credentials.accessKey}")
    private String accessKey;

    @Value("${cloud.aws.credentials.secretKey}")
    private String secretKey;

    @Value("${cloud.aws.s3.bucket}")
    private String bucket;

    @Value("${cloud.aws.region.static}")
    private String region;

    private AmazonS3 s3Client;
    private final DBFileRepository dbFileRepository;

    @PostConstruct
    public void setS3Client(){
        AWSCredentials credentials = new BasicAWSCredentials(this.accessKey, this.secretKey);

        s3Client = AmazonS3ClientBuilder.standard()
                .withCredentials(new AWSStaticCredentialsProvider(credentials))
                .withRegion(this.region)
                .build();
    }

    public void deleteS3File(String path) {
        s3Client.deleteObject(new DeleteObjectRequest(bucket, path));
    }

    /**
     * S3 파일 삭제
     * @param url : 경로
     * @param dir : 버킷 내 폴더
     */
//    public void deleteS3(String url, String dir){
//        if(url == null) return;
//        String[] tmp = url.split("/");
//        s3Client.deleteObject(this.bucket, dir + "/" + tmp[tmp.length - 1]);
//    }

    /**
     * S3 파일 다중 삭제
     * @param urls
     * @param dir
     */
//    public void deleteS3(List<String> urls, String dir) {
//        if(urls == null) return;
//
//        DeleteObjectsRequest deleteObjectsRequest = new DeleteObjectsRequest(this.bucket);
//        List<DeleteObjectsRequest.KeyVersion> keyList = new ArrayList<>();
//        for(String url : urls) {
//            String[] tmp = url.split("/");
//            keyList.add(new DeleteObjectsRequest.KeyVersion(dir + "/" + tmp[tmp.length - 1]));
//        }
//        deleteObjectsRequest.setKeys(keyList);
//        s3Client.deleteObjects(deleteObjectsRequest);
//    }

    /**
     * S3 파일 업로드
     * @param file : 파일
     * @param dir : 버킷 내 업로드 폴더
     * @return : file uri
     * @throws IOException : AWS IO
     */
    @Transactional
    public DBFile uploadFile(MultipartFile file, String dir) {
        try {
            String originalFilename = file.getOriginalFilename();
            if(originalFilename.contains("..")) {
                throw new FileStorageException("Sorry! Filename contains invalid path sequence " + originalFilename);
            }
            String fileName = UUID.randomUUID() + originalFilename;
            ObjectMetadata objectMetadata = new ObjectMetadata();
            objectMetadata.setContentType(Mimetypes.getInstance().getMimetype(fileName));
            objectMetadata.setContentLength(file.getSize());

            s3Client.putObject(new PutObjectRequest(bucket, dir + fileName, file.getInputStream(), objectMetadata)
                    .withCannedAcl(CannedAccessControlList.PublicRead));
            String uri = s3Client.getUrl(bucket, dir+fileName).toString();
            return updateDb(fileName, originalFilename, file.getContentType(), uri);
        } catch (IOException ioe) {
            throw new IllegalStateException("IOException: " + ioe.getMessage());
        } catch (AmazonServiceException serviceException) {
            throw new IllegalStateException("AmazonServiceException: " + serviceException.getMessage());
        }
    }

    @Transactional
    public DBFile updateDb(String id, String fileName, String contentType, String downloadUri) {
        DBFile dbFile = DBFile.builder()
                .id(id)
                .file_name(fileName)
                .file_type(contentType)
                .download_uri(downloadUri)
                .build();
        return dbFileRepository.save(dbFile);
    }
}
