![logo](https://cloud.githubusercontent.com/assets/17325506/25775119/47ef2c8a-32a6-11e7-9ee6-a970a4db40d3.png)

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
         If you put :P at the end of the escape string, it will replace with "parameter" String


Usage
-----
Cu.A.Doc can be used with in different ways.

1. Define Annotation
   Add CuADoc.jar file into your project.
   
2. Define CuADoc annotations for Cucumber-Java Step Definitions.

   ```
    @CuADoc(purpose = "This step does something",params = {"definition for param1","definition for param2"},escapes = {"^(?i)","(.*):P"," ","$"})
    ```
    ```
    @Given("^(?i)Do something with this step definition param1 \"(.*)\" param2 \"(.*)\" $")
    ```
    ```
    public void doSomething(String parameter1, String parameter2) {
      //Do something
    }
   ```

3. Generate Report
   There are two ways for generating document (html, json).
   
   First add CuADoc.jar file into your project.
   
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
   
Output
------
<html><head></head><body><table><tr><td><h2>Cu.A.Doc - Cucumber Step Definitions Documentation</h2></td></tr><tr><td><table><tr><td><ul><li><span><b>Package:Steps</b></span><br/><br/><ul><li><span><b>Command:</b></span>Do something with this step definition param1 &quot;Parameter&quot; param2 &quot;Parameter&quot;<br/><span><b>Purpose:</b></span>This step does something<br/><b>Parameter:</b>definition for param1<br/><b>Parameter:</b>definition for param2<br/></li><br/></ul></li></ul></td></tr></table></td></tr></table></body></html>


Limitations
-----------
Cu.A.Doc is only works for @Given and @And Cucumber annotations, others will be implemented in time.
   
   
   
   
