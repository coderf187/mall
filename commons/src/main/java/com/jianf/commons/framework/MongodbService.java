package com.jianf.commons.framework;

import com.jianf.commons.utils.DateUtils;
import com.jianf.commons.utils.RefectUtil;
import com.mongodb.DBObject;
import com.mongodb.util.JSON;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Component;

import java.io.Serializable;
import java.util.List;

/**
 * mongodb通用dao,如有特殊需求需要继承mongodbDao私人订制
 * mongodb 没有事务,不要存储一些比较重要的数据，也不要在mongodb上去开启事务
 */
@Component
public class MongodbService<T, PK extends Serializable> {
    protected Class<T> entityClass;
    @Autowired
    @Qualifier("mongoTemplate")
    private MongoTemplate mongoTemplate;

    public MongodbService() {
        super();
        this.entityClass = RefectUtil.getClassGenricType(getClass(), 0);
    }

    public MongodbService(MongoTemplate mongoTemplate) {
        super();
        this.mongoTemplate = mongoTemplate;
    }

    public MongoTemplate getMongodbFile() {
        return this.mongoTemplate;
    }

    @Autowired
    public void setMongoTemplate(MongoTemplate mongoTemplate) {
        this.mongoTemplate = mongoTemplate;
    }

    /**
     * 查询
     */
    public List<T> find(Query query) {
        return this.mongoTemplate.find(query, this.entityClass);
    }

    /**
     * 查询一个(最好通过主键去查询)
     */
    public T findOne(Query query) {
        return this.mongoTemplate.findOne(query, this.entityClass);
    }

    /**
     * 查询出所有
     */
    public List<T> findAll() {
        return this.mongoTemplate.findAll(this.entityClass);
    }

    /**
     * 根据条件查询出来后再去修改
     */
    public T findAndModify(Query query, Update update) {
        return this.mongoTemplate.findAndModify(query, update, this.entityClass);
    }

    /**
     * 查询后删除
     */
    public T findAndRemove(Query query) {
        return this.mongoTemplate.findAndRemove(query, this.entityClass);
    }

    /**
     * 更新操作 单条更新,基本上通过id去查询再更新
     * 例子：mongoTemplate.updateMulti(new Query(new Criteria("id").in(id)),
     * new Update().set("name", "fengjian"), Person.class)。
     */
    public void updateFirst(Query query, Update update) {
        this.mongoTemplate.updateFirst(query, update, this.entityClass);
    }

    /**
     * 更新操作 更新所有匹配的(慎用更新操作) 根据条件先查询再更新，更新多条
     * 例子：mongoTemplate.updateMulti(new Query(new Criteria("name").in(name)),
     * new Update().set("name", "fengjian"), Person.class) 先通过name字段查询出结果再把结果的name字段更新。
     */
    public void updateMulti(Query query, Update update) {
        this.mongoTemplate.updateMulti(query, update, this.entityClass);
    }

    /**
     * 保存
     */
    public T save(T bean) {
        this.mongoTemplate.save(bean);
        return bean;
    }

    /**
     * 通过id查询
     */
    public T findById(String id) {
        return this.mongoTemplate.findById(id, this.entityClass);
    }

    /**
     * 通过id查询
     */
    public T findById(String id, String collectionName) {
        return this.mongoTemplate.findById(id, this.entityClass, collectionName);
    }

    /**
     * 通过回调直接调用mongo
     */
    public String saveJson(String collectionName, final String json) {
        return this.mongoTemplate.execute(collectionName, collection -> {
            DBObject dbObject = (DBObject) JSON.parse(json);
            dbObject.put("create_time", DateUtils.getDate());
            collection.save(dbObject);
            return String.valueOf(dbObject.get("_id"));
        });
    }


}