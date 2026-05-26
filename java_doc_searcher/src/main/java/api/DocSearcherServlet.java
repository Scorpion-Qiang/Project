package api;

import com.fasterxml.jackson.databind.ObjectMapper;
import search.DocSearcher;
import search.Result;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * Description:
 * User: 强仔
 * Date: 2022-07-15
 * Time: 17:07
 */

@WebServlet("/search")
public class DocSearcherServlet extends HttpServlet {

    // 引入搜索对象，全局唯一
    private static DocSearcher searcher = new DocSearcher();

    // Jackson 第三方库的类
    private ObjectMapper mapper = new ObjectMapper();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        // 1. 解析用户的参数
        String query = req.getParameter("query");
        if(query == null || query.equals("")){
            String msg = "您输入的信息有误";
            System.out.println(msg);
            resp.sendError(404, msg);
            return;
        }

        // 2. 打印 query 的值
        System.out.println("query" + query);

        // 3. 调用搜索模块
        List<Result> results = searcher.search(query);

        // 4. 将搜索结果打包
        resp.setContentType("application/json;charset=utf8");
        String JSON = mapper.writeValueAsString(results);
        resp.getWriter().write(JSON);
    }
}
