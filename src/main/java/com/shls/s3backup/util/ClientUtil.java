package com.shls.s3backup.util;

import com.amazonaws.ClientConfiguration;
import com.amazonaws.Protocol;
import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.client.builder.AwsClientBuilder;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;

/**
 * @author ：Killer
 * @date ：Created in 20-6-29 下午3:44
 * @description：${description}
 * @modified By：
 * @version: version
 */
public class ClientUtil {

    public static AmazonS3 getS3Client(String accessKey,String secretKey,String endpoint) {
        ClientConfiguration configuration = new ClientConfiguration();
        configuration.setProtocol(Protocol.HTTP);
        configuration.setMaxConnections(400);

        AWSCredentials srcAwsCredentials = new BasicAWSCredentials(accessKey, secretKey);
        return AmazonS3ClientBuilder
                .standard()
                .withEndpointConfiguration(new AwsClientBuilder.EndpointConfiguration(endpoint, "default"))
                .withCredentials(new AWSStaticCredentialsProvider(srcAwsCredentials))
                .withClientConfiguration(configuration)
                .withPathStyleAccessEnabled(true).disableChunkedEncoding().build();
    }
}
