package com.example.demo.base.s3.config;


import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.client.builder.AwsClientBuilder;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import com.example.demo.base.s3.properties.AmazonS3Properties;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@RequiredArgsConstructor
public class AmazonS3Config {

    private final AmazonS3Properties amazonS3Properties;

    @Bean
    public AmazonS3 amazonS3() {

        AmazonS3 s3 = AmazonS3ClientBuilder.standard()
                .withEndpointConfiguration(new AwsClientBuilder.EndpointConfiguration(
                        amazonS3Properties.getEndPoint(), amazonS3Properties.getRegionName()))
                .withCredentials(new AWSStaticCredentialsProvider(new BasicAWSCredentials(
                        amazonS3Properties.getAccessKey(), amazonS3Properties.getSecretKey())))
                .build();

        return s3;
    }
}


