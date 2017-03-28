package cu.a.doc.loader;

import cu.a.doc.data.DocData;
import org.apache.log4j.Logger;
import java.io.PrintWriter;
import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

/**
 * Created by Aytunc BEKEN on 10.3.2017.
 */
public class JarLoader {

    private static Logger logger = Logger.getLogger(JarLoader.class);
    private String jarFilePath = null;
    private String packageName = null;
    private String htmlFilePath = null;
    private DocData docData = new DocData();

    public JarLoader(String jarFilePath, String packageName, String htmlFilePath) {
        this.jarFilePath = jarFilePath;
        this.packageName = packageName;
        this.htmlFilePath = htmlFilePath;
        this.loadClassesAndParse(this.getClassNames());
        if( htmlFilePath != null)
        {
            this.exportHtmlToFile();
        }
    }


    public DocData getDocData() {
        return docData;
    }

    private void exportHtmlToFile()
    {
        try{
            PrintWriter writer = new PrintWriter(this.htmlFilePath, "UTF-8");
            writer.print(this.docData.generateHtml());
            writer.close();
        } catch (Exception e) {
            logger.error(e);
        }
    }

    private ArrayList<String> getClassNames()
    {
        try {
            ArrayList<String> classNames = new ArrayList<String>();
            JarFile jarFile = new JarFile(this.jarFilePath);
            Enumeration<JarEntry> jarEntryEnumeration = jarFile.entries();
            while (jarEntryEnumeration.hasMoreElements()) {
                JarEntry je = jarEntryEnumeration.nextElement();
                String className = (je.getName() != null ? (je.getName().replaceAll("/",".")):(null));
                if(className != null && className.indexOf(".class") != -1 && className.indexOf(this.packageName) != -1)
                {
                    className = className.substring(0, className.indexOf(".class"));
                    classNames.add(className);
                }
            }
            return classNames;
        }
        catch (Exception e)
        {
            logger.error(e.toString(),e);
            return null;
        }
    }

    private void loadClassesAndParse(ArrayList<String> classNames)
    {
        try
        {
            URLClassLoader urlClassLoader = URLClassLoader.newInstance(new URL[] {new URL("jar:file:"+this.jarFilePath + "!/")});
            for(String className : classNames)
            {
                Class stepClass = urlClassLoader.loadClass(className);
                String stepClassName = stepClass.getSimpleName();
                if( stepClassName == null) {
                    logger.error("Step Class Name is Null - Continue");
                    continue;
                }
                Method[] stepClassMethods = stepClass.getMethods();
                for(Method stepClassMethod : stepClassMethods)
                {
                    Annotation cuADocAnn = stepClassMethod.getAnnotation(cu.a.doc.annonation.CuADoc.class);
                    if( cuADocAnn != null) {
                        String purpose = cuADocAnn.annotationType().getMethod("purpose").invoke(cuADocAnn).toString();
                        Object params = cuADocAnn.annotationType().getMethod("params").invoke(cuADocAnn);
                        Object escapes = cuADocAnn.annotationType().getMethod("escapes").invoke(cuADocAnn);
                        Annotation cucumberAnn = stepClassMethod.getAnnotation(cucumber.api.java.en.And.class);
                        if( cucumberAnn == null)
                            cucumberAnn = stepClassMethod.getAnnotation(cucumber.api.java.en.Given.class);
                        if( cucumberAnn != null)
                        {
                            String cucumberDefinition = cucumberAnn.annotationType().getMethod("value").invoke(cucumberAnn).toString();
                            docData.addClassToData(stepClassName,cucumberDefinition,purpose,params, escapes);
                        }
                    }
                }
            }
        } catch (Exception e) {
            logger.error(e);
        }
    }



}
