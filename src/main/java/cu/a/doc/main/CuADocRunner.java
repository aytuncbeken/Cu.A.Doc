package cu.a.doc.main;

import cu.a.doc.loader.JarLoader;
import org.apache.commons.cli.*;
import org.apache.log4j.Logger;

/**
 * Created by Aytunc BEKEN on 22.3.2017.
 */
public class CuADocRunner {

    private static Logger logger = Logger.getLogger(CuADocRunner.class);
    private JarLoader jarLoader = null;
    private static String jarFilePathKey = "jarFilePath";
    private static String stepPackageNameKey = "stepsPackageName";
    private static String htmlFilePathKey = "htmlFilePath";

    public CuADocRunner(String jarFilePath, String stepPackageName, String htmlFilePath) {
        this.jarLoader = new JarLoader(jarFilePath,stepPackageName,htmlFilePath);
    }

    public CuADocRunner(String stepPackageName, String htmlFilePath) {
        this.jarLoader = new JarLoader(stepPackageName,htmlFilePath);
    }

    public String getDocDataAsJson()
    {
        return this.jarLoader.getDocData().getDataAsJsonString();
    }

    public String getDocDataAsHtml()
    {
        return this.jarLoader.getDocData().generateHtml();
    }

    public static void main(String[] args)  {
        checkAndRun(args);
    }

    private static void printUsage(Options options)
    {
        HelpFormatter helpFormatter = new HelpFormatter();
        helpFormatter.printHelp("Main",options);
    }

    private static void checkAndRun(String[] args)
    {
        Options cliOptions = new Options();
        CommandLineParser commandLineParser = new BasicParser();
        CommandLine commandLine = null;
        try {
            cliOptions.addOption(jarFilePathKey, true, "Jar File Full Path");
            cliOptions.addOption(stepPackageNameKey, true, "Package Name of Step Definitions");
            cliOptions.addOption(htmlFilePathKey, true, "Html File Full Path For Storing Doc");
            commandLine = commandLineParser.parse(cliOptions, args);
            if( commandLine.hasOption(jarFilePathKey) && commandLine.hasOption(stepPackageNameKey) && commandLine.hasOption(htmlFilePathKey))
            {
                new CuADocRunner(commandLine.getOptionValue(jarFilePathKey),commandLine.getOptionValue(stepPackageNameKey),commandLine.getOptionValue(htmlFilePathKey));
            }
            else if( commandLine.hasOption(jarFilePathKey) == false && commandLine.hasOption(stepPackageNameKey) && commandLine.hasOption(htmlFilePathKey))
            {
                new CuADocRunner(commandLine.getOptionValue(stepPackageNameKey),commandLine.getOptionValue(htmlFilePathKey));
            }
            else
            {
                printUsage(cliOptions);
            }
        }
        catch (Exception ex)
        {
            printUsage(cliOptions);
            logger.error(ex.getLocalizedMessage(),ex);
        }
    }

}
