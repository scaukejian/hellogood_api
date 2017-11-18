package com.hellogood.http.controller;

import com.google.gson.Gson;
import com.hellogood.utils.WeatherUtil;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by KJ on 2017/11/18.
 */
@RequestMapping("weather")
@Controller
public class WeatherController extends BaseController{

    @ResponseBody
    @RequestMapping("/getWeather/{location}.do")
    public Map<String, Object> getNoteList(@PathVariable String location) {
        logger.info("请求天气预报location:"+location);
        Map<String, Object> map = new HashMap<>();
        WeatherUtil weatherUtil = new WeatherUtil();
        try {
            String weatherUrl = weatherUtil.generateGetDiaryWeatherURL(
                    location,
                    "zh-Hans", //中文简体
                    "c", //摄氏温度
                    "1",
                    "1"
            ); //location可以是城市拼音，也可以是经纬度坐标，比如location= "39.93:116.40"（格式是 纬度:经度，英文冒号分隔）,详情看 https://www.seniverse.com/doc#language
            logger.info("weatherUrl:" + weatherUrl);
            StringBuffer weatherResult = new StringBuffer();
            try{
                URL url = new URL(weatherUrl);
                URLConnection conn = url.openConnection();
                BufferedReader reader = new BufferedReader(new InputStreamReader(conn.getInputStream(),"utf-8"));//转码。
                String line = null;
                while ((line = reader.readLine()) != null)
                    weatherResult.append(line + " ");
                reader.close();
            }catch(MalformedURLException e) {
                e.printStackTrace();
            }catch(IOException e){
                e.printStackTrace();
            }
            Gson gson = new Gson();
            Map<String,Object> resultMap = gson.fromJson(weatherResult.toString(), Map.class);
            logger.info(resultMap.toString());
            map.put("weatherResult", resultMap);
        } catch (Exception e) {
            e.printStackTrace();
        }
        map.put(STATUS, STATUS_SUCCESS);
        return map;
    }

}
