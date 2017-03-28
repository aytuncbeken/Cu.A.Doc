package cu.a.doc.main;


import cu.a.doc.loader.JarLoader;

/**
 * Created by Aytunc BEKEN on 9.12.2016.
 * Main class of Cu.A.Doc project
 */
public class Main {


    public static void main(String[] args)  {

        CuADocRunner cuADocRunner = new CuADocRunner("D:\\IdeaProjects\\Otomat\\Cucumber\\target\\Cucumber-1.0-SNAPSHOT.jar", "steps","C:\\Users\\TCAYBEKEN\\Desktop\\a.html");
        String html = cuADocRunner.getDocDataAsHtml();
        System.out.println(html);
    }

}
