package cu.a.doc.loader;

import cu.a.doc.data.DocData;
import cu.a.doc.utils.Utils;
import org.apache.log4j.Logger;
import java.io.File;
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
    private boolean loadFromJar = true;
    private DocData docData = new DocData();

    public JarLoader(String jarFilePath, String packageName, String htmlFilePath) {
        logger.info("Constructor With Params:" + Utils.getParamsAsJson(jarFilePath,packageName,htmlFilePath));
        this.jarFilePath = jarFilePath;
        this.packageName = packageName;
        this.htmlFilePath = htmlFilePath;
        this.loadClassesAndParse(this.getClassNamesFromJarFile());
        if( htmlFilePath != null)
        {
            this.exportHtmlToFile();
        }
    }

    public JarLoader( String packageName, String htmlFilePath) {
        logger.info("Constructor With Params:" + Utils.getParamsAsJson(packageName,htmlFilePath));
        this.packageName = packageName;
        this.htmlFilePath = htmlFilePath;
        this.loadFromJar = false;
        this.loadClassesAndParse(this.getClassNamesFromInternalPackage());
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
            logger.info("Export Html To File With FileName:" + this.htmlFilePath);
            PrintWriter writer = new PrintWriter(this.htmlFilePath, "UTF-8");
            writer.print(this.docData.generateHtml());
            writer.close();
            logger.info("Export Html To File Finished");
        } catch (Exception e) {
            logger.error(e.getLocalizedMessage(),e);
        }
    }

    private ArrayList<String> getClassNamesFromJarFile()
    {
        logger.info("Get Class Names From Jar File");
        ArrayList<String> classNames = new ArrayList<>();
        try {
            logger.info("Jar File To Process:" + this.jarFilePath);
            JarFile jarFile = new JarFile(this.jarFilePath);
            Enumeration<JarEntry> jarEntryEnumeration = jarFile.entries();
            while (jarEntryEnumeration.hasMoreElements()) {
                JarEntry je = jarEntryEnumeration.nextElement();
                String className = (je.getName() != null ? (je.getName().replaceAll("/",".")):(null));
                logger.info("ClassName Found In Jar:" + className);
                if(className != null && className.indexOf(".class") != -1 && className.indexOf(this.packageName) != -1)
                {
                    className = className.substring(0, className.indexOf(".class"));
                    classNames.add(className);
                    logger.info("Class:" + className + " added to list");
                }
            }
        }
        catch (Exception e)
        {
            logger.error(e.toString(),e);
            return null;
        }
        finally {
            return classNames;
        }
    }

    public ArrayList<String> getClassNamesFromInternalPackage()
    {
        logger.info("Get Class Names From Internal Package");
        ArrayList<String> classNames = new ArrayList<>();
        try
        {
            logger.info("Package To Process:" + this.packageName);
            ArrayList<String> resourceDirs = new ArrayList<>();
            String packageNameWithSlash =  this.packageName.replace(".","/");
            ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
            Enumeration resources = classLoader.getResources(packageNameWithSlash);
            while(resources.hasMoreElements())
            {
                URL url = (URL) resources.nextElement();
                resourceDirs.add(url.getFile());
                logger.info("Package Directory:" + url.getFile() + " added to list");
            }

            for(String resourceDir: resourceDirs)
            {
                logger.info("Processing Package Dir:" + resourceDir);
                File[] packageFiles = new File(resourceDir).listFiles();
                for(File classFile : packageFiles)
                {
                    logger.info("Processing Class File:" + classFile);
                    String classFileName = classFile.getName();
                    if( classFileName != null && classFileName.contains(".class") )
                    {
                        classFileName = classFileName.substring(0,classFileName.length()-6);
                        String classNameWithPackage = this.packageName + "." +  classFileName;
                        classNames.add(classNameWithPackage);
                        logger.info("ClassFile:" + classFileName + " added to list");
                    }
                }
            }

        }
        catch (Exception ex)
        {
            logger.error(ex.toString(),ex);
        }
        finally {
            return classNames;
        }
    }

    private void loadClassesAndParse(ArrayList<String> classNames)
    {
        try
        {
            logger.info("Load Classes From Given List");
            URLClassLoader urlClassLoader = null;
            Class stepClass = null;
            if( this.loadFromJar) {
                logger.info("Loading Classes from jar file");
                urlClassLoader = URLClassLoader.newInstance(new URL[]{new URL("jar:file:" + this.jarFilePath + "!/")});
            }

            for(String className : classNames)
            {
                logger.info("Loading Class:" + className);
                if( this.loadFromJar)
                    stepClass = urlClassLoader.loadClass(className);
                else
                    stepClass = Class.forName(className);
                logger.info("Class Loaded:" + className);
                String stepClassName = stepClass.getSimpleName();
                if( stepClassName == null) {
                    logger.error("Step Class Name is Null - Continue");
                    continue;
                }
                logger.info("Loading Class Methods");
                Method[] stepClassMethods = stepClass.getMethods();
                for(Method stepClassMethod : stepClassMethods)
                {
                    logger.info("Processing Class Method:" + stepClassMethod.getName());
                    Annotation cuADocAnn = stepClassMethod.getAnnotation(cu.a.doc.annonation.CuADoc.class);
                    if( cuADocAnn != null)
                    {
                        logger.info("Found Cu.A.Doc Annotation");
                        String purpose = cuADocAnn.annotationType().getMethod("purpose").invoke(cuADocAnn).toString();
                        Object params = cuADocAnn.annotationType().getMethod("params").invoke(cuADocAnn);
                        Object escapes = cuADocAnn.annotationType().getMethod("escapes").invoke(cuADocAnn);
                        logger.info("Purpose:" + Utils.getParamsAsJson(purpose));
                        logger.info("Params:" + Utils.getParamsAsJson((String[]) params));
                        logger.info("Escapes:" + Utils.getParamsAsJson((String[]) escapes));
                        Annotation cucumberAnn = stepClassMethod.getAnnotation(cucumber.api.java.en.And.class);
                        if( cucumberAnn == null)
                            cucumberAnn = stepClassMethod.getAnnotation(cucumber.api.java.en.Given.class);
                        if( cucumberAnn != null)
                        {
                            logger.info("Found Cucumber Annotation");
                            String cucumberDefinition = cucumberAnn.annotationType().getMethod("value").invoke(cucumberAnn).toString();
                            docData.addClassToData(stepClassName,cucumberDefinition,purpose,params, escapes);
                            logger.info("Class Data added to DocData list");
                        }
                    }
                }
            }
        } catch (Exception e) {
            logger.error(e.toString(),e);
        }
    }



}
