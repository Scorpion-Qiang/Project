package ServletAPI;

import Searcher.SearchReview.Parse;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;

/**
 * Created with IntelliJ IDEA.
 * Description:
 * User: 强仔
 * Date: 2022-10-20
 * Time: 21:39
 */
@WebServlet(value={"/HelloServlet"}, loadOnStartup=1)
public class HelloServlet extends HttpServlet {
    @Override
    public void init(){
        System.out.println("重写 init() 方法, 制作索引");
        Parse parse = new Parse();
        parse.run();
    }
}
