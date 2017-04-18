package cu.a.doc.utils;

import com.google.gson.JsonObject;

/**
 * Created by Aytunc Beken on 10.4.2017.
 * This class contains helper method which are used widely
 */
public class Utils {

    /**
     * Convert Cu.A.Doc param annotation to Json Object
     * @param params params information which gathered from Cu.A.Doc annotation
     * @return generated Json Object as String
     */
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
