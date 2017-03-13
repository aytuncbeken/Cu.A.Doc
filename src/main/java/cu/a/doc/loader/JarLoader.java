package cu.a.doc.loader;

import org.apache.log4j.Logger;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

/**
 * Created by TCAYBEKEN on 10.3.2017.
 */
public class JarLoader {

    private static Logger logger = Logger.getLogger(JarLoader.class);
    private String jarFilePath = null;
    private String packageName = null;

    public JarLoader(String jarFilePath, String packageName) {
        this.jarFilePath = jarFilePath;
        this.packageName = packageName;
        this.loadClasses(this.getClassNames());
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
            logger.error(e);
            return null;
        }
    }

    private void loadClasses(ArrayList<String> classNames)
    {
        try
        {
            URLClassLoader urlClassLoader = URLClassLoader.newInstance(new URL[] {new URL("jar:file:"+this.jarFilePath + "!/")});
            for(String className : classNames)
            {
                Class stepClass = urlClassLoader.loadClass(className);
                Method[] stepClassMethods = stepClass.getMethods();
                for(Method stepClassMethod : stepClassMethods)
                {
                    Annotation cucumberAnd = stepClassMethod.getAnnotation(cucumber.api.java.en.And.class);
                    Annotation cucumberGiven = stepClassMethod.getAnnotation(cucumber.api.java.en.Given.class);
                    if( cucumberAnd != null || cucumberGiven != null) {
                        System.out.println(stepClassMethod.toString());
                    }
                }

            }
        } catch (Exception e) {
            logger.error(e);
        }
    }



}
