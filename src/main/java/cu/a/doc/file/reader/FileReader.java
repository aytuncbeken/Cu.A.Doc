package cu.a.doc.file.reader;

import org.apache.log4j.Logger;

import java.io.*;
import java.util.ArrayList;

/**
 * Created by TCAYBEKEN on 9.12.2016.
 */
public class FileReader {

    private static Logger logger = Logger.getLogger(FileReader.class);


    public ArrayList<String> getFileContents(String fileNameWithPath)
    {
        FileInputStream fstream = null;
        BufferedReader br = null;
        String strLine = null;
        ArrayList<String> fileContentsArray = new ArrayList<String>();
        try
        {
            fstream = new FileInputStream(fileNameWithPath);
            br = new BufferedReader(new InputStreamReader(fstream));
            while ((strLine = br.readLine()) != null)   {
                fileContentsArray.add(strLine);
            }
            br.close();
            return fileContentsArray;
        }
        catch (Exception e)
        {
            logger.error(e);
            return null;
        }
        finally
        {
            try
            {
                if( br != null)
                    br.close();
            } catch (IOException e) {
                logger.error(e);
            }
        }
    }
}


