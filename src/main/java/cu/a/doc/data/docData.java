package cu.a.doc.data;


import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import cu.a.doc.utils.Utils;
import org.apache.log4j.Logger;
import org.rendersnake.HtmlCanvas;

import java.util.Map;
import java.util.Set;

import static org.rendersnake.HtmlAttributesFactory.http_equiv;


/**
 * Created by Aytunc BEKEN on 13.3.2017.
 * This class stores json data which contains Cucumber definitions and Cu.A.Doc annotations and generates html report for given path
 */
public class DocData {

    private static Logger logger = Logger.getLogger(DocData.class);
    private String classDataGherkin = "gherkinValue";
    private String classDataCuADocPurpose = "purpose";
    private String classDataCuADocParam = "param";
    private JsonObject cuAdocData = new JsonObject();


    /**
     * Add necessary information which gathered from annotations to json data
     * @param className className which contains annotations
     * @param gherkinValue gherkin patternt which gathered from gherkin annotations
     * @param purpose purpose information which gathered from Cu.A.Doc annotation
     * @param params params information which gathered from Cu.A.Doc annotation
     * @param escapes escapes information which gathered from Cu.A.Doc annotation
     */
    public void addClassToData(String className, String gherkinValue, String purpose, Object params, Object escapes)
    {
        logger.debug("Add Class To DocData with params:" + Utils.getParamsAsJson(className, gherkinValue, purpose, Utils.getParamsAsJson((String[]) params), Utils.getParamsAsJson((String[]) escapes)));
        JsonArray classDatas = new JsonArray();
        if( cuAdocData.get(className) == null )
        {
            logger.debug("Add New Json Property");
            cuAdocData.add(className, classDatas);
        }
        else
        {
            logger.debug("Update existing Json Property");
            classDatas = cuAdocData.get(className).getAsJsonArray();
        }
        JsonObject classData = new JsonObject();
        classData.addProperty(classDataGherkin, this.formatGherkinValue(gherkinValue, escapes));
        classData.addProperty(classDataCuADocPurpose, purpose);
        classData.add(classDataCuADocParam, convertParamsToJsonObject(params));
        classDatas.add(classData);
        logger.debug("ClassDataJson Object added to DocData list:" + classData.toString());
    }


    /**
     * Convert Cu.A.Doc parameter list to json array for storing in json object
     * @param params params information which gathered from Cu.A.Doc annotation
     * @return Json Array which contains parameters given by Cu.A.Doc annotation
     */
    private JsonArray convertParamsToJsonObject(Object params)
    {
        logger.debug("Convert Params To Json Object:" + Utils.getParamsAsJson((String[]) params));
        JsonArray paramsJson = new JsonArray();
        if( params == null)
            return paramsJson;
        for(String param : (String[])params)
        {
            logger.debug("Adding Param To paramJsonArray:" + param);
            paramsJson.add(param);
        }
        logger.debug("Convert Params Finished");
        return paramsJson;
    }

    /**
     * Get Cu.A.Doc data as Json String
     * @return String contains Json Data
     */
    public String getDataAsJsonString()
    {
        return cuAdocData.toString();
    }

    /**
     * Generate Cu.A.Doc Html file
     * @return String which contains html
     */
    public String generateHtml()
    {
        logger.info("Generating Html Report");
        try {
            HtmlCanvas html = new HtmlCanvas();
            html = html.html();
            html = html.head().title().content("Cu.A.Doc - Cucumber Documentation").meta(http_equiv("Content-Type").content("text/html; charset=utf-8"))._head();
            html = html.body();
            html = html.table().tr().td().h2().content("Cu.A.Doc - Cucumber Step Definitions Documentation")._td()._tr();
            html = html.tr().td();
            html = html.table().tr().td().ul();

            Set<Map.Entry<String, JsonElement>> entries = cuAdocData.entrySet();
            for (Map.Entry<String, JsonElement> entry: entries)
            {
                String className = entry.getKey();
                JsonArray classDatas = cuAdocData.get(className).getAsJsonArray();
                html = html.li().span().b().content("Package:" + className)._span().br().br();
                html = html.ul();
                for(JsonElement tempData: classDatas)
                {
                    JsonObject classData = (JsonObject) tempData;
                    String gherkinValue = classData.get(classDataGherkin).getAsString().trim();
                    String purpose = classData.get(classDataCuADocPurpose).getAsString();
                    html = html.li().span().b().content("Command:")._span().write(gherkinValue).br().span().b().content("Purpose:")._span().write(purpose).br();
                    for(JsonElement paramJson : classData.get(classDataCuADocParam).getAsJsonArray())
                    {
                        String param = paramJson.getAsString();
                        if( param != null && param.equals("") == false)
                        html = html.b().content("Parameter:").write(param).br();
                    }
                    html = html._li().br();
                }
                html = html._ul()._li();
            }
            html = html._ul()._td()._tr()._table()._td()._tr()._table()._body()._html();
            logger.info("Html Report Generated");
            return html.toHtml();
        }
        catch (Exception e)
        {
            logger.error(e.toString(),e);
            return null;
        }
    }

    /**
     * Replace given Escape values with space
     * @param gherkinValue gherkin patternt which gathered from gherkin annotations
     * @param _escapes escapes information which gathered from Cu.A.Doc annotation
     * @return Replaced gherkin String
     */
    private String formatGherkinValue(String gherkinValue, Object _escapes)
    {
        logger.debug("Format Gherkin Value with params:" + Utils.getParamsAsJson(gherkinValue,Utils.getParamsAsJson((String[]) _escapes)));
        String[] escapes = (String[]) _escapes;
        gherkinValue = gherkinValue.replace("\\\"","\"");
        for(String escapeValue : escapes)
        {
            if( escapeValue.equals(""))
                continue;
            if( escapeValue.indexOf(":P") != -1)
            {
                escapeValue = escapeValue.split(":")[0];
                gherkinValue = gherkinValue.replace(escapeValue,"Parameter");
            }
            else
            {
                gherkinValue = gherkinValue.replace(escapeValue," ");
            }
        }
        return gherkinValue;
    }

}
