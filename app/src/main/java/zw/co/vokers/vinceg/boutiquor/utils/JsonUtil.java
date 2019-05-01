package zw.co.vokers.vinceg.boutiquor.utils;

import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.Map;

/**
 * Created by Vince G on 13/3/2019
 */

public class JsonUtil {
    private static final ObjectMapper mapper = new ObjectMapper();

    public static <T> T fromJson(byte[] value, Class<T> clazz) throws Exception {
        return mapper.readValue(value, clazz);
    }

    public static <T> T fromJson(String value, Class<T> clazz) throws Exception {
        return mapper.readValue(value, clazz);
    }

    public static String asJson(Object value) throws Exception {
        return mapper.writeValueAsString(value);
    }

    public static <T> T convert(Map<String, Object> value, Class<T> clazz) throws Exception {
        return mapper.convertValue(value, clazz);
    }
}
