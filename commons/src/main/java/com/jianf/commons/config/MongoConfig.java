package com.jianf.commons.config;

import com.mongodb.MongoClient;
import com.mongodb.MongoClientOptions;
import com.mongodb.MongoCredential;
import com.mongodb.ServerAddress;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.mongodb.MongoDbFactory;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.SimpleMongoDbFactory;
import org.springframework.data.mongodb.core.convert.DbRefResolver;
import org.springframework.data.mongodb.core.convert.DefaultDbRefResolver;
import org.springframework.data.mongodb.core.convert.DefaultMongoTypeMapper;
import org.springframework.data.mongodb.core.convert.MappingMongoConverter;
import org.springframework.data.mongodb.core.mapping.MongoMappingContext;
import org.springframework.data.mongodb.gridfs.GridFsTemplate;

import java.util.ArrayList;
import java.util.List;

/**
 * @author: fengjian
 * @ClassName: MongoConfig
 * @Description: mongi配置
 * @date 2016年8月5日 上午1:18:17
 */
@Configuration
public class MongoConfig {
    @Value("${mongo.replicaSet.host}")
    private String host;

    @Value("${mongo.replicaSet.port}")
    private String port;

    @Value("${mongo.replicaSet.name}")
    private String replicaSetName;

    @Value("${mongo.replicaSet.datebase}")
    private String dateBase;

    @Value("${mongo.replicaSet.username}")
    private String username;

    @Value("${mongo.replicaSet.password}")
    private String passWord;

    @Bean
    public MongoDbFactory mongoDbFactory() {

        List<ServerAddress> addresses = new ArrayList<>();
        String[] hosts = host.split(",");
        String[] ports = port.split(",");
        for (int i = 0; i < hosts.length; i++) {
            ServerAddress address = new ServerAddress(hosts[i], Integer.valueOf(ports[i]));
            addresses.add(address);
        }
        MongoClientOptions clientOptions = new MongoClientOptions.Builder().requiredReplicaSetName(replicaSetName)
                .build();

        MongoCredential mongoCredential = MongoCredential.createScramSha1Credential(username, dateBase,
                passWord.toCharArray());
        List<MongoCredential> credentialsList = new ArrayList<>();
        credentialsList.add(mongoCredential);

        MongoDbFactory mongoDbFactory = new SimpleMongoDbFactory(
                new MongoClient(addresses, credentialsList, clientOptions), dateBase);
        return mongoDbFactory;
    }

    /**
     * @param @return
     * @param @throws
     *            exception 设定文件
     * @return MongoTemplate 返回类型
     * @throws @auth:fengjian
     * @Title: mongoTemplate
     * @Description: 初始化mongoTemplate
     */

    @Bean(name = "mongoTemplate")
    public MongoTemplate mongoTemplate() {
        MongoDbFactory mongoDbFactory = mongoDbFactory();
        DbRefResolver dbRefResolver = new DefaultDbRefResolver(mongoDbFactory);
        MappingMongoConverter converter = new MappingMongoConverter(dbRefResolver, new MongoMappingContext());
        converter.setTypeMapper(new DefaultMongoTypeMapper(null));
        return new MongoTemplate(mongoDbFactory, converter);
    }

     @Bean
     public GridFsTemplate gridFsTemplate() {
         MongoDbFactory mongoDbFactory = mongoDbFactory();
         DbRefResolver dbRefResolver = new DefaultDbRefResolver(mongoDbFactory);
         return new GridFsTemplate(mongoDbFactory, new MappingMongoConverter(dbRefResolver, new MongoMappingContext()));
     }

}