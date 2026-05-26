import java.util.regex.Pattern;

/**
 * Created with IntelliJ IDEA.
 * Description:
 * User: 强仔
 * Date: 2024-05-20
 * Time: 18:36
 */
public class TestQust {
    public static void main(String[] args) {
        Child child = new Child();
        System.out.println(Child.val);
    }
}

class Par {
    static int val = 5;
}

class Child extends Par{

}
