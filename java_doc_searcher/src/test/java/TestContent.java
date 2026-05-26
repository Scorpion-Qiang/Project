import search.Parser;

import java.io.File;

/**
 * Created with IntelliJ IDEA.
 * Description:
 * User: 强仔
 * Date: 2022-07-11
 * Time: 18:29
 */
public class TestContent {
    public static void main(String[] args) {
        File file = new File("C:\\Users\\86188\\Desktop\\docs\\api\\java\\util\\ArrayList.html");

        Parser parser = new Parser();
        String content = parser.parseContent(file);
        String content2 = parser.parseContentByRegex(file);
        System.out.println(content);
        System.out.println(content2);
    }
}
