package cu.a.report.file.finder;

import org.apache.log4j.Logger;

import java.io.File;
import java.util.ArrayList;

/**
 * Created by TCAYBEKEN on 9.12.2016.
 */
public class JavaFileFinder {

    private static Logger logger = Logger.getLogger(JavaFileFinder.class);
    private ArrayList<File> fileList = new ArrayList<File>();

    public void walk( String path )
    {
        try {
            File root = new File(path);
            File[] list = root.listFiles();
            if (list == null) return;
            for (File f : list) {
                if (f.isDirectory()) {
                    this.walk(f.getAbsolutePath());
                } else {
                    fileList.add(f.getAbsoluteFile());
                }
            }
        }
        catch (Exception ex)
        {
            logger.error(ex);
        }
    }
}
