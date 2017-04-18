package cu.a.doc.main;

import cu.a.doc.loader.JarLoader;
import cu.a.doc.utils.Utils;
import org.apache.commons.cli.*;
import org.apache.log4j.Logger;

/**
 * Created by Aytunc BEKEN on 22.3.2017.
 * This class runs Cu.A.Doc as jar file which can run separate runnable or internal library on runtime
 */
public class CuADocRunner {

    private static Logger logger = Logger.getLogger(CuADocRunner.class);
    private JarLoader jarLoader = null;
    private static String jarFilePathKey = "jarFilePath";
    private static String stepPackageNameKey = "stepsPackageName";
    private static String htmlFilePathKey = "htmlFilePath";

    /**
     * Used when running Cu.A.Doc separate runnable jar file which loads and parses jar file which contains Cucumber Step Definitions
     * @param jarFilePath Jar file name with full path to load and parse
     * @param stepPackageName Package name which have Cucumber Steps definition class files
     * @param htmlFilePath Html file name with full path. If given null, no html file is generated
     */
    public CuADocRunner(String jarFilePath, String stepPackageName, String htmlFilePath) {
        logger.debug("Constructor With Params:" + Utils.getParamsAsJson(jarFilePath,stepPackageName,htmlFilePath));
        this.jarLoader = new JarLoader(jarFilePath,stepPackageName,htmlFilePath);
    }

    /**
     * Used when running Cu.A.Doc as internal library which loads and parses classes in runtime
     * @param stepPackageName Package name which have Cucumber Steps definition class files
     * @param htmlFilePath Html file name with full path. If given null, no html file is generated
     */
    public CuADocRunner(String stepPackageName, String htmlFilePath) {
        logger.debug("Constructor With Params:" + Utils.getParamsAsJson(stepPackageName,htmlFilePath));
        this.jarLoader = new JarLoader(stepPackageName,htmlFilePath);
    }

    /**
     * Get Cu.A.Doc data as json
     * @return String contains Cu.A.Doc Json Data as String
     */
    public String getDocDataAsJson()
    {
        return this.jarLoader.getDocData().getDataAsJsonString();
    }

    /**
     * Get Cu.A.Doc data as html
     * @return String contains Cu.A.Doc Htmla as String
     */
    public String getDocDataAsHtml()
    {
        return this.jarLoader.getDocData().generateHtml();
    }

    /**
     * Main method
     * @param args Arguments from command line
     */
    public static void main(String[] args)  {
        checkAndRun(args);
    }

    /**
     * Print Cu.A.Doc Runnable jar file usage
     * @param options CliOpitons
     */
    private static void printUsage(Options options)
    {
        HelpFormatter helpFormatter = new HelpFormatter();
        helpFormatter.printHelp("Main",options);
    }

    /**
     * Check arguments and run Cu.A.Doc runnable jar file
     * @param args Arguments from command line
     */
    private static void checkAndRun(String[] args)
    {
        logger.debug("CheckAndRun With Params:" + Utils.getParamsAsJson(args));
        logger.info("Running Cu.A.Doc");
        Options cliOptions = new Options();
        CommandLineParser commandLineParser = new BasicParser();
        CommandLine commandLine = null;
        try {
            cliOptions.addOption(jarFilePathKey, true, "Jar File Full Path");
            cliOptions.addOption(stepPackageNameKey, true, "Package Name of Step Definitions");
            cliOptions.addOption(htmlFilePathKey, true, "Html File Full Path For Storing Doc");
            commandLine = commandLineParser.parse(cliOptions, args);
            logger.debug("Options:" + Utils.getParamsAsJson(commandLine.getOptionValue(jarFilePathKey),commandLine.getOptionValue(stepPackageNameKey),commandLine.getOptionValue(htmlFilePathKey)));
            if( commandLine.hasOption(jarFilePathKey) && commandLine.hasOption(stepPackageNameKey) && commandLine.hasOption(htmlFilePathKey))
            {
                new CuADocRunner(commandLine.getOptionValue(jarFilePathKey),commandLine.getOptionValue(stepPackageNameKey),commandLine.getOptionValue(htmlFilePathKey));
            }
            else
            {
                printUsage(cliOptions);
            }
            logger.info("Cu.A.Doc Finished");
        }
        catch (Exception ex)
        {
            printUsage(cliOptions);
            logger.error(ex.toString(),ex);
        }
    }

}
