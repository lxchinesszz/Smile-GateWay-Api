package iflyer.gateway.util;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.json.JSONObject;
import org.springframework.util.StringUtils;

public class JsonUtils {
    protected final static Log logger = LogFactory.getLog(JsonUtils.class);

    public static boolean isBadJson(String json) {
        return !isGoodJson(json);
    }

    public static boolean isGoodJson(Object json) {
        try {
            JSONObject jsonObject = (JSONObject) json;
            return true;
        } catch (Exception e) {
            logger.error("bad json: " + json);
            return false;
        }
    }
}