package com.shls.s3backup.config;

import com.amazonaws.ClientConfiguration;
import com.amazonaws.Protocol;
import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.client.builder.AwsClientBuilder;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author ：Killer
 * @date ：Created in 20-5-7 上午11:16
 * @description：${description}
 * @modified By：
 * @version: version
 */
@Configuration
public class S3ClientConfig {

    @Value("${src.access.id}")
    private String SRC_AWS_ACCESS_KEY;

    @Value("${src.secret.key}")
    private String SRC_AWS_SECRET_KEY;

    @Value("${src.endpoint}")
    private String SRC_ENDPOINT;

    @Value("${des.access.id}")
    private String DES_AWS_ACCESS_KEY;

    @Value("${des.secret.key}")
    private String DES_AWS_SECRET_KEY;

    @Value("${des.endpoint}")
    private String DES_ENDPOINT;

    @Bean(name = "srcAmazonS3")
    public AmazonS3 srcAmazonS3() {
        ClientConfiguration configuration = new ClientConfiguration();
        configuration.setProtocol(Protocol.HTTP);
        configuration.setMaxConnections(400);

        AWSCredentials srcAwsCredentials = new BasicAWSCredentials(SRC_AWS_ACCESS_KEY, SRC_AWS_SECRET_KEY);
        return AmazonS3ClientBuilder
                .standard()
                .withEndpointConfiguration(new AwsClientBuilder.EndpointConfiguration(SRC_ENDPOINT, "default"))
                .withCredentials(new AWSStaticCredentialsProvider(srcAwsCredentials))
                .withClientConfiguration(configuration)
                .withPathStyleAccessEnabled(true).disableChunkedEncoding().build();
    }

    @Bean(name = "desAmazonS3")
    public AmazonS3 desAmazonS3() {

        ClientConfiguration configuration = new ClientConfiguration();
        configuration.setProtocol(Protocol.HTTP);
        configuration.setMaxConnections(400);
        AWSCredentials desAwsCredentials = new BasicAWSCredentials(DES_AWS_ACCESS_KEY, DES_AWS_SECRET_KEY);
        return AmazonS3ClientBuilder
                .standard()
                .withEndpointConfiguration(new AwsClientBuilder.EndpointConfiguration(DES_ENDPOINT, "default"))
                .withCredentials(new AWSStaticCredentialsProvider(desAwsCredentials))
                .withClientConfiguration(configuration)
                .withPathStyleAccessEnabled(true).disableChunkedEncoding().build();


    }
}
