package cu.a.doc.main;

import cu.a.doc.loader.JarLoader;
import org.apache.log4j.Logger;

/**
 * Created by Aytunc BEKEN on 22.3.2017.
 */
public class CuADocRunner {

    private static Logger logger = Logger.getLogger(CuADocRunner.class);
    private String jarFilePath = null;
    private String htmlFilePath = null;
    private JarLoader jarLoader = null;
    private String stepPackageName = null;

    public CuADocRunner(String jarFilePath, String stepPackageName, String htmlFilePath) {
        this.jarFilePath = jarFilePath;
        this.htmlFilePath = htmlFilePath;
        this.stepPackageName = stepPackageName;
        this.jarLoader = new JarLoader(this.jarFilePath,this.stepPackageName,this.htmlFilePath);
    }

    public String getDocDataAsJson()
    {
        return this.jarLoader.getDocData().getDataAsJsonString();
    }

    public String getDocDataAsHtml()
    {
        return this.jarLoader.getDocData().generateHtml();
    }

}
