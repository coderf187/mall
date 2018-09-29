package com.jianf.commons.utils;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.Arrays;
import java.util.List;

/**
 * 反射工具类
 * 
 * @author mshi
 */
public class RefectUtil {// NOSONAR

    private static Log logger = LogFactory.getLog(RefectUtil.class);

    /** An empty class array */
    @SuppressWarnings("rawtypes")
    private static final Class[] EMPTY_CLASS_PARAMS = new Class[0];

    /** An empty object array */
    private static final Object[] EMPTY_OBJECTS = new Object[0];

    /** The start of an index. */
    private static final String INDEX_START = "[";

    /** The end of an index. */
    private static final String INDEX_END = "]";

    /** Delimitter for property. */
    private static final String PROP_DELIM = "\\.";

    private RefectUtil() {
    }

    /**
     * returns the value of the property in the specified bean
     * 
     * @param bean
     *            is the execution object, support the type of bean: Object
     *            Array & List
     * @param property
     *            is the property in this bean, for Example: sample.item
     *            sample.item.itemName samples[i].items
     *            samples[i].items[j].itemName samples[i].items[j]
     * @return the value of the property in the specified bean
     * @throws Exception
     */
    @SuppressWarnings("rawtypes")
    public static Object findValue(Object bean, String property) throws Exception {// NOSONAR
        if (bean == null || StringUtils.isEmpty(property))
            return null;
        String property0 = property.split(PROP_DELIM)[0];
        int index = -1;
        int start = property0.indexOf(INDEX_START);
        if (start != -1) {
            int end = property0.indexOf(INDEX_END);
            index = Integer.parseInt(property0.substring(start + 1, end));
        }
        Object nextVal = invokeGetter(bean, (start == -1) ? property0 : property0.substring(0, start));
        if (index != -1) {
            Object values = nextVal;
            if (values instanceof Object[]) {
                nextVal = ((Object[]) values)[index];
            } else if (values instanceof List) {
                nextVal = ((List) values).get(index);
            }
        }
        if (property.equals(property0))
            return nextVal;
        return findValue(nextVal, property.substring((property0 + PROP_DELIM).length()));
    }

    /**
     * change the value of the property in the specified bean
     * 
     * @param bean
     *            is the execution object, support the type of bean: Object
     *            Array & List
     * @param property
     *            is the property in this bean, for Example: sample.item
     *            sample.item.itemName samples[i].items
     *            samples[i].items[j].itemName samples[i].items[j]
     * @param value
     * @throws Exception
     */
    @SuppressWarnings("rawtypes")
    public static void putValue(Object bean, String property, Object value) throws Exception {// NOSONAR
        if (null == bean || StringUtils.isEmpty(property))
            return;
        String fieldName = property.split(PROP_DELIM)[0];
        int start = fieldName.indexOf(INDEX_START);
        int index = -1;
        String prefix = fieldName;
        if (start != -1) {
            index = Integer.parseInt(fieldName.substring(start + 1, fieldName.indexOf(INDEX_END)));
            prefix = fieldName.substring(0, start);
        }
        if (property.equals(fieldName)) {
            if (bean instanceof Object[]) {
                Object[] beans = (Object[]) bean;
                bean = Arrays.asList(beans);
            }
            if (bean instanceof List) {
                List beanList = (List) bean;
                if (index >= 0) {
                    invokeSetter(beanList.get(index), fieldName, value);
                } else {
                    for (int i = 0; i < beanList.size(); i++) {// NOSONAR
                        invokeSetter(beanList.get(i), fieldName, value);
                    }
                }
            } else {
                invokeSetter(bean, fieldName, value);
            }
        } else {
            if (bean instanceof Object[]) {
                Object[] beans = (Object[]) bean;
                bean = Arrays.asList(beans);
            }
            if (bean instanceof List) {
                List beansList = (List) bean;
                property = property.substring((fieldName + PROP_DELIM).length());
                if (index >= 0) {
                    for (int i = 0; i < beansList.size(); i++) {// NOSONAR
                        Object obj = invokeGetter(beansList.get(i), prefix);
                        obj = getObject(index, obj);
                        putValue(obj, property, value);
                    }
                } else {
                    for (int i = 0; i < beansList.size(); i++) {// NOSONAR
                        Object obj = invokeGetter(beansList.get(i), prefix);
                        putValue(obj, property, value);
                    }
                }
            } else {
                property = property.substring((fieldName + PROP_DELIM).length());
                Object obj = invokeGetter(bean, prefix);
                if (index >= 0) {
                    obj = getObject(index, obj);
                }
                putValue(obj, property, value);
            }
        }
    }

    private static Object getObject(int index, Object obj) {
        if (obj instanceof Object[]) {
            Object[] beans = (Object[]) obj;
            obj = beans[index];
        } else if (obj instanceof List) {
            List beanList = (List) obj;
            obj = beanList.get(index);
        }
        return obj;
    }

    /**
     * overload method putValue
     * 
     * @param bean
     *            is the execution object
     * @param propName
     *            is the property
     * @param value
     *            int to Integer
     * @throws Exception
     */
    public static void putValue(Object bean, String propName, int value) throws Exception {// NOSONAR
        putValue(bean, propName, new Integer(value));
    }

    /**
     * overload method putValue
     * 
     * @param bean
     *            is the execution object
     * @param propName
     *            is the property
     * @param value
     *            long to Long
     * @throws Exception
     */
    public static void putValue(Object bean, String propName, long value) throws Exception {// NOSONAR
        putValue(bean, propName, new Long(value));
    }

    /**
     * invokes the getter method of the property specified
     * 
     * @param obj
     *            is the execution object
     * @param property
     *            is the property in this object
     * @return the value returned by the invoked getter
     * @throws Exception
     */
    private static Object invokeGetter(Object obj, String property) throws Exception {// NOSONAR
        try {
            String methodName = "get" + StringUtils.capitalize(property);
            Method getter = obj.getClass().getMethod(methodName, EMPTY_CLASS_PARAMS);
            return getter.invoke(obj, EMPTY_OBJECTS);
        } catch (Exception e) {
            logger.error("error to invoke getter method of '" + property + "'", e);
            throw e;
        }
    }

    /**
     * invokes the setter method of the property specified to set value
     * 
     * @param obj
     *            is the execution object
     * @param property
     *            is the property in this object
     * @param value
     *            is the property value to set
     * @throws Exception
     */
    @SuppressWarnings("rawtypes")
    private static void invokeSetter(Object obj, String property, Object value) throws Exception {// NOSONAR
        try {
            String methodName = "set" + StringUtils.capitalize(property);
            Field field = obj.getClass().getDeclaredField(property);
            Class[] parameterTypes = new Class[1];
            parameterTypes[0] = field.getType();
            Method setter = obj.getClass().getMethod(methodName, parameterTypes);
            setter.invoke(obj, new Object[] { value });
        } catch (Exception e) {
            logger.error("error to invoke setter method of '" + property + "'", e);
            throw e;
        }
    }

    /**
     * 获取复杂对象属性对应的值
     * 
     * @author wangcj
     * @param bean
     * @param property
     * @return
     * @throws Exception
     */
    public static Object findMultiValue(Object bean, String property) throws Exception {// NOSONAR
        String[] propertys = property.split(PROP_DELIM);
        Object obj = bean;
        for (String pro : propertys) {
            obj = findValue(obj, pro);
        }
        return obj;
    }

    /**
     * @author mshi
     * @param clazz
     * @param classname
     * @return
     */
    @SuppressWarnings("rawtypes")
    public static boolean hasGenricType(final Class clazz, String classname) {
        return getClassGenricType(clazz, 0).toString().indexOf(classname) > -1;
    }

    /**
     * 根据索引获得超类的参数类型
     * 
     * @author mshi
     * @param clazz
     * @param index
     * @return
     */
    @SuppressWarnings("rawtypes")
    public static Class getClassGenricType(final Class clazz, final int index) {
        Type genType = clazz.getGenericSuperclass();
        if (!(genType instanceof ParameterizedType)) {
            return Object.class;
        }
        Type[] params = ((ParameterizedType) genType).getActualTypeArguments();
        if (index >= params.length || index < 0) {
            return Object.class;
        }
        if (!(params[index] instanceof Class)) {
            return Object.class;
        }
        return (Class) params[index];
    }

    /**
     * 判断数据类型是否为基本类型 包括string fengjian
     * 
     * @param clz
     * @return
     */
    public static boolean isWrapClass(Class clz) {
        try {
            return ((Class) clz.getField("TYPE").get(null)).isPrimitive() || clz.newInstance() instanceof String;
        } catch (Exception e) {
            return false;
        }
    }



}
