import java.io.File;

/**
 * Created with IntelliJ IDEA.
 * Description:
 * User: 强仔
 * Date: 2022-07-11
 * Time: 16:32
 */
public class TestGetName {
    public static void main(String[] args) {
        File file = new File("C:\\Users\\86188\\Desktop\\docs\\api\\java\\util\\ArrayList.html");
        System.out.println(file.getAbsolutePath());
        System.out.println(file.getName());
    }
}
