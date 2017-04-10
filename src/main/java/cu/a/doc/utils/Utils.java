package cu.a.doc.utils;

import com.google.gson.JsonObject;

/**
 * Created by TCAYBEKEN on 10.4.2017.
 */
public class Utils {

    public static String getParamsAsJson(String... params)
    {
        JsonObject paramsObject = new JsonObject();
        Integer count = 1;
        for(String parameter : params)
        {
            paramsObject.addProperty("parameter" + count,parameter);
            count++;
        }
        return paramsObject.toString();
    }

}
