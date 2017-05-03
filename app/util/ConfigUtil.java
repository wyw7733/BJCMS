package util;

import com.typesafe.config.ConfigFactory;

/**
 * Created by lilonghui on 2016/7/7.
 */
public class ConfigUtil {
    public static String getApplication(String key) {
        return ConfigFactory.load().getString(key);
    }
}
