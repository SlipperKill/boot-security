package com.boot.security.server.utils;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import org.springframework.util.StringUtils;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentLinkedQueue;

/**
 * JSON工具类，以来jackson尽量前置检查参数，做到快速返回
 */
public final class JsonUtil {

    private JsonUtil(){

    }

    public static ConcurrentLinkedQueue<ObjectMapper> mapperQueue = new ConcurrentLinkedQueue<ObjectMapper>();

    private static ObjectMapper getObjectMapper() {
        ObjectMapper mapper = mapperQueue.poll();
        if (null == mapper) {
            mapper = new ObjectMapper();
            mapper.configure(JsonParser.Feature.ALLOW_UNQUOTED_FIELD_NAMES, true);
            mapper.configure(JsonParser.Feature.ALLOW_SINGLE_QUOTES, true);
            /** 不输出空属性Include.NON_NULL */
            mapper.setSerializationInclusion(JsonInclude.Include.ALWAYS);
            mapper.configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false);
            mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        }
        return mapper;
    }

    private static void returnObjectMapper(ObjectMapper mapper) {
        if (null != mapper) {
            mapperQueue.offer(mapper);
        }
    }

    /**bean转map,参数为空则返回空map*/
    @SuppressWarnings("unchecked")
    public static Map<String, Object> bean2Map(Object obj) {
        if (null == obj)
            return new HashMap<String, Object>();
        return convertValue(obj, Map.class);
    }

    /**可能会返回空*/
    public static <K, V> Map<K,V> bean2Map(Object value, Class<K> keyClazz, Class<V> valueClazz) {
        if (null == value) return null;
        ObjectMapper mapper = getObjectMapper();
        JavaType javaType = mapper.getTypeFactory().constructParametricType(HashMap.class,keyClazz, valueClazz);
        try {
            if (value instanceof String) {
                if (!((String) value).startsWith("{")) return null;
                return mapper.readValue((String) value, javaType);
            }
            return  mapper.convertValue(value, javaType);
        }catch (IOException e) {
            throw new IllegalArgumentException(e);
        } finally {
            returnObjectMapper(mapper);
        }
    }

    private static <T> T convertValue(Object value, Class<T> clazz) throws IllegalArgumentException {
        if (null == value) return null;
        ObjectMapper mapper = getObjectMapper();
        try {
            if (value instanceof String) {
                if (!((String) value).startsWith("{")) return null;
                value = mapper.readTree((String) value);
            }
            return mapper.convertValue(value, clazz);
        } catch (IOException e) {
            throw new IllegalArgumentException(e);
        }finally {
            returnObjectMapper(mapper);
        }
    }

    /** 对象转JSON串,参数为空则返回null*/
    public static String bean2Json(Object bean) {
        if (null == bean)
            return null;
        ObjectMapper mapper = getObjectMapper();
        try {
            return mapper.writeValueAsString(bean);
        } catch (Exception e) {
            throw new IllegalArgumentException(e);
        } finally {
            returnObjectMapper(mapper);
        }
    }

    /** JSON串转对象,参数为空则返回null*/
    public static <T> T json2Bean(String content, Class<T> type) {
        if (StringUtils.isEmpty(content))
            return null;
        ObjectMapper mapper = getObjectMapper();
        try {
            return mapper.readValue(content, type);
        } catch (Exception e) {
            throw new IllegalArgumentException(e);
        } finally {
            returnObjectMapper(mapper);
        }
    }

    /** byte[]转对象,参数为空则返回null */
    public static <T> T json2Bean(byte[] content, Class<T> type) {
        if (null == content || content.length == 0)
            return null;
        ObjectMapper mapper = getObjectMapper();
        try {
            return mapper.readValue(content, type);
        } catch (Exception e) {
            throw new IllegalArgumentException(e);
        } finally {
            returnObjectMapper(mapper);
        }
    }

    /** JSON串转对象集合 ,参数为空则返回null*/
    public static <T> List<T> json2List(String content, Class<T> elementType) {
        if (StringUtils.isEmpty(content))
            return null;
        ObjectMapper mapper = getObjectMapper();
        try {
            return mapper.readValue(content, mapper.getTypeFactory().constructCollectionType(List.class, elementType));
        } catch (Exception e) {
            throw new IllegalArgumentException(e);
        } finally {
            returnObjectMapper(mapper);
        }
    }

    public static <T> Set<T> json2Set(String content, Class<T> elementType) {
        if (StringUtils.isEmpty(content))
            return null;
        ObjectMapper mapper = getObjectMapper();
        try {
            return mapper.readValue(content, mapper.getTypeFactory().constructCollectionType(Set.class, elementType));
        } catch (Exception e) {
            throw new IllegalArgumentException(e);
        } finally {
            returnObjectMapper(mapper);
        }
    }

    /** JSON串转对象数组 ,参数为空则返回null*/
    public static <T> T[] json2Array(String content, Class<T> elementType) {
        if (StringUtils.isEmpty(content))
            return null;
        ObjectMapper mapper = getObjectMapper();
        try {
            return mapper.readValue(content, mapper.getTypeFactory().constructArrayType(elementType));
        } catch (Exception e) {
            throw new IllegalArgumentException(e);
        } finally {
            returnObjectMapper(mapper);
        }
    }

    /** JSON串转泛型 ,参数为空则返回null*/
    public static <T> T readCollectionType(String content, TypeReference<T> typeRef) {
        if (StringUtils.isEmpty(content))
            return null;
        ObjectMapper mapper = getObjectMapper();
        try {
            return mapper.readValue(content, typeRef);
        } catch (Exception e) {
            throw new IllegalArgumentException(e);
        } finally {
            returnObjectMapper(mapper);
        }
    }

    /** 对象转byte[],参数为空则返回空字节数组*/
    public static byte[] bean2Bytes(Object bean) {
        if (null == bean)
            return new byte[0];
        ObjectMapper mapper = getObjectMapper();
        try {
            return mapper.writeValueAsBytes(bean);
        } catch (Exception e) {
            throw new IllegalArgumentException(e);
        } finally {
            returnObjectMapper(mapper);
        }
    }

}
