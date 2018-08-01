package com.github.configUtil;

import org.apache.commons.configuration.ConfigurationException;
import org.apache.commons.configuration.PropertiesConfiguration;

/**
 * 配置文件读取工具类助手
 * Created by yuliang on 2015/8/3.
 */
public class ConfigHelper {

    private PropertiesConfiguration propertiesConfiguration = null;

    public ConfigHelper(String propertyPath) {
        try {
            propertiesConfiguration = new PropertiesConfiguration(propertyPath);
        } catch (Exception e) {
            throw new RuntimeException(
                    "Please configure check  file: " + propertyPath);
        }
    }


    public void setProperty(String key, Object val) {
        propertiesConfiguration.setProperty(key, val);
        try {
            propertiesConfiguration.save();
        } catch (ConfigurationException e) {
            e.printStackTrace();
        }
    }

    public String getStringValue(String key) {
        return propertiesConfiguration.getString(key);
    }

    public String[] getStringArrayValue(String key) {
        return propertiesConfiguration.getStringArray(key);
    }



    public int getIntValue(String key) {
        return propertiesConfiguration.getInt(key);
    }

    public float getFloatValue(String key) {
        return propertiesConfiguration.getFloat(key);
    }



}
