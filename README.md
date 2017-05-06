Cu.A.Doc
========

About
-----
Cu.A.Doc is annotation based Cucumber-Java step definition document generator.


Problem
-------
When javadoc is used for documenting Cucumber Step definitions, generated document consist lots of technical definition and also complicated regex expressions used for gherkin definitions.
Those kind of document is not readable for non-technical users whom only want to learn about Cucumner-Java step definitions.

Solution
--------
Cu.A.Doc is provide one annotation which must be defined plus  to gherkin definitions @Given, @And.
Within more attributes in Cu.A.Doc annotation, you can create more human-readable document(html, json) for Cucumber definitions.

purpose: Explain the purpose of the Cucumber-Java Step definition
params: Explain the parameters of the Cucumber-Java Step definition
escapes: Escape special characters with space in Regex Expression

Usage
-----
Cu.A.Doc can be used with in different ways.

1. Define Annotation
   Add CuADoc.jar file into your project. After that you can define @CuADoc Annotation in your Cucumber-Java Step definitions

2. Generate Report
   There are two ways for generating document (html, json).
   
   First add CuaDoc.jar file into your project.
   
   **Generate document with in your runtime**, which need two parameters. First parameter is package name (Ex: com.your.code.steps) which has Java files for Cucumber-Java step definitions.
   Second is the full file path for generated html file.
   
   ```
   CuADocRunner cuADocRunnber = new CuADocRunner("com.you.code.steps","./document.html")
   ```
   
   Or you can generate document from external jar file which contains your step definitions. Additional to above parameters, third parameter is the external jar file path.
   
   ```
   CuADocRunner cuADocRunnber = new CuADocRunner("./externalJarFile.jar","com.you.code.steps","./document.html")
   ```
   
   You can look into JavaDoc for further usage.
   
   
Limitations
-----------
Cu.A.Doc is only works for @Given and @And Cucumber annotations, others will be implemented in time.
   
   
   
   
