package Searcher.SearchSettle;
import Operation.ProblemOp.Problem;
import Operation.ProblemOp.ProblemDao;
import ServletAPI.SettleAPI.SelectProblemSettleResp;
import Operation.SettleOp.Settle;
import Operation.UserOp.User;
import Operation.UserOp.UserDao;
import com.fasterxml.jackson.databind.ObjectMapper;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.*;

/**
 * Created with IntelliJ IDEA.
 * Description:
 * User: 强仔
 * Date: 2022-10-21
 * Time: 18:39
 */
/*
* 搜索题解（根据题目）
* url: "searchersettle"
* 方法: post
* 请求: SearchSettleReq
* 响应: SelectProblemSettleResp
*
* */


@WebServlet("/searchersettle")
public class SearchSettleServlet extends HttpServlet {
    private ObjectMapper mapper = new ObjectMapper();

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.setCharacterEncoding("utf8");

        SearchSettleReq searchSettleReq = mapper.readValue(req.getInputStream(), SearchSettleReq.class);
        // 1. 获取参数
        int problemId = searchSettleReq.getProblemId();
        String query = searchSettleReq.getQuery();
        // 2. 验证参数
        if (query == null || query.trim().equals("")) {
            resp.setContentType("text/html;charset=utf8");
            resp.getWriter().write("<h3>输入参数不合适</h3>");
            return;
        }
        ProblemDao problemDao = new ProblemDao();
        Problem p = problemDao.selectOne(problemId);
        if (p == null) {
            resp.setContentType("text/html;charset=utf8");
            resp.getWriter().write("<h3>该题目不存在</h3>");
            return;
        }

        // 3. 查
        SearchSettle searchSettle = new SearchSettle();
        List<SettleWeight> list = searchSettle.search(problemId, query);

        // 4. 转化为响应需要的列表
        List<SelectProblemSettleResp> res = new ArrayList<>();
        for (SettleWeight settleWeight : list) {
            Settle settle = settleWeight.getSettle();
            SelectProblemSettleResp selectProblemSettleResp = new SelectProblemSettleResp();
            selectProblemSettleResp.setId(settle.getId());
            selectProblemSettleResp.setUsername(settle.getUsername());
            selectProblemSettleResp.setTitle(settle.getTitle());

            String content = settle.getContent();
            content = content.length() < 100 ? content + "..." : content.substring(0, 100) + "...";
            selectProblemSettleResp.setContent(content);

            selectProblemSettleResp.setTime(settle.getTime());

            UserDao userDao = new UserDao();
            User user = userDao.selectOne(settle.getUsername());
            selectProblemSettleResp.setUserphoto(user.getPhoto());

            res.add(selectProblemSettleResp);
        }

        // 5. 返回响应
        String body = mapper.writeValueAsString(res);
        resp.setContentType("application/json;charset=utf8");
        resp.getWriter().write(body);

    }
}
