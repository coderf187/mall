package com.jianf.commons.config;

import com.alibaba.druid.spring.boot.autoconfigure.DruidDataSourceBuilder;
import com.github.pagehelper.PageHelper;
import org.apache.ibatis.plugin.Interceptor;
import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.core.io.support.ResourcePatternResolver;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import javax.sql.DataSource;
import java.util.Properties;

/**
 * @author: fengjian
 * @ClassName: MyBatisConfig
 * @Description: mybatis config
 * @date 2016年8月9日 上午12:49:34
 */
@Configuration
@EnableTransactionManagement
public class MyBatisConfig {
    private static final Logger log = LoggerFactory.getLogger(MyBatisConfig.class);

    private String typeAliasesPackage = "com.jianf.commons.entity";
    private String mapperLocations = "classpath*:mapper/*.xml";

    @Bean
    public PlatformTransactionManager annotationDrivenTransactionManager() {
        return new DataSourceTransactionManager(dataSource());
    }

    @ConfigurationProperties(prefix = "spring.datasource")
    @Bean(destroyMethod = "close", initMethod = "init")
    public DataSource dataSource() {
        return DruidDataSourceBuilder.create().build();
    }

    /**
     * @throws Exception
     * @auth:fengjian @Title: sqlSessionFactory @Description:
     *                获取sqlSessionFactory @param @return 设定文件 @return
     *                SqlSessionFactoryBean 返回类型 @throws
     */
    @Bean(name = "sqlSessionFactory")
    public SqlSessionFactory sqlSessionFactory() {
        SqlSessionFactoryBean bean = new SqlSessionFactoryBean();
        bean.setDataSource(dataSource());
        bean.setTypeAliasesPackage(typeAliasesPackage);
        // 分页插件
        PageHelper pageHelper = new PageHelper();
        Properties properties = new Properties();
        properties.setProperty("reasonable", "true");
        properties.setProperty("supportMethodsArguments", "true");
        properties.setProperty("returnPageInfo", "check");
        properties.setProperty("params", "count=countSql");
        pageHelper.setProperties(properties);
        // 添加插件
        bean.setPlugins(new Interceptor[] { pageHelper });

        //添加XML目录
        ResourcePatternResolver resolver = new PathMatchingResourcePatternResolver();
        try {
            bean.setMapperLocations(resolver.getResources(mapperLocations));
            return bean.getObject();
        } catch (Exception e) {
            log.error("初始化mybatis异常,异常原因{}", e);
            throw new RuntimeException(e);
        }
    }
}
