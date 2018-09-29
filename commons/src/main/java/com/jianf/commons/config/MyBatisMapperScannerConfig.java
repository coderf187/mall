package com.jianf.commons.config;

import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import tk.mybatis.mapper.common.Mapper;
import tk.mybatis.spring.mapper.MapperScannerConfigurer;

import java.util.Properties;

/**
 * MyBatis 版本过低，采用@MapperScan的方式，可能会导致SqlSessionFactoryBean初始化失败，需要强制sudo运行,
 * 改用此方式可指定setSqlSessionFactoryBeanName
 * 
 * @author: fengjian
 * @ClassName: MyBatisMapperScannerConfig
 * @Description: mybatis 包扫描配置
 * @date 2016年5月7日 下午10:01:26
 */
@Configuration
@AutoConfigureAfter(MyBatisConfig.class)
public class MyBatisMapperScannerConfig {

    /**
     * @auth:fengjian @Title: mapperScannerConfigurer @Description:
     * mybatis配置 @param @return 设定文件 @return MapperScannerConfigurer
     * 返回类型 @throws
     */
    @Bean
    public MapperScannerConfigurer mapperScannerConfigurer() {
        MapperScannerConfigurer mapperScannerConfigurer = new MapperScannerConfigurer();
        mapperScannerConfigurer.setSqlSessionFactoryBeanName("sqlSessionFactory");
        mapperScannerConfigurer.setBasePackage("com.jianf.commons.mapper");
        Properties properties = new Properties();
        // 这里要特别注意，不要把MyMapper放到 basePackage 中，也就是不能同其他Mapper一样被扫描到。
        properties.setProperty("mappers", Mapper.class.getName());
        properties.setProperty("notEmpty", "false");
        properties.setProperty("IDENTITY", "MYSQL");
        mapperScannerConfigurer.setProperties(properties);
        return mapperScannerConfigurer;
    }
}
