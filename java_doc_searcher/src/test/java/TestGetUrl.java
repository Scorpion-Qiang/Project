import java.io.File;

/**
 * Created with IntelliJ IDEA.
 * Description:
 * User: 强仔
 * Date: 2022-07-11
 * Time: 17:10
 */
public class TestGetUrl {
    private static final String INPUT_PATH = "C:/Users/86188/Desktop/docs/api/";

    public static void main(String[] args) {

        File file = new File("C:\\Users\\86188\\Desktop\\docs\\api\\java\\util\\ArrayList.html");
        String part1 = "https://docs.oracle.com/javase/8/docs/api/";
        String part2 = file.getAbsolutePath().substring(INPUT_PATH.length());
        String path = part1 + part2;
        System.out.println(path);
    }
}
