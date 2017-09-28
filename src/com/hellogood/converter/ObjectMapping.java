package com.hellogood.converter;


import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;

public class ObjectMapping extends ObjectMapper
{  
  
    public ObjectMapping()
    {  
        super();  
        // 允许单引号  
        this.configure(JsonParser.Feature.ALLOW_SINGLE_QUOTES, true);
        // 字段和值都加引号  
        this.configure(JsonParser.Feature.ALLOW_UNQUOTED_FIELD_NAMES, true); 
        this.configure(JsonParser.Feature.ALLOW_UNQUOTED_CONTROL_CHARS, true);
        //忽略未知的属性
        this.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        //null值不显示
        this.setSerializationInclusion(Include.NON_DEFAULT);
        //排除null list/arrays
        this.configure(SerializationFeature.WRITE_NULL_MAP_VALUES, false);


        // 数字也加引号  
        //this.configure(JsonParser.Feature.ALLOW_BACKSLASH_ESCAPING_ANY_CHARACTER, true);  
//        this.configure(JsonGenerator.Feature.QUOTE_NON_NUMERIC_NUMBERS, true);  
       
        // 空值处理为空串  
//        this.getSerializerProvider().setNullValueSerializer(new JsonSerializer<Object>()  
//        {  
//  
//            @Override  
//            public void serialize(  
//                    Object value,  
//                    JsonGenerator jg,  
//                    SerializerProvider sp) throws IOException, JsonProcessingException  
//            {  
//                jg.writeString("");  
//            }  
//        });  
    }  
}  