package ServletAPI.SettleAPI;

import Operation.ProblemOp.Problem;
import MyException.ProblemNotFiledException;
import ServletAPI.Util;
import Operation.SettleOp.Settle;
import Operation.SettleOp.SettleDao;
import Operation.UserOp.User;
import Operation.UserOp.UserDao;
import com.fasterxml.jackson.databind.ObjectMapper;
import Operation.ProblemOp.ProblemDao;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * Description:
 * User: 强仔
 * Date: 2022-10-03
 * Time: 21:11
 */
/*
* 查看某个题目全部的题解
* url: /problemsettle?problemId=..
* 方法: get
* 返回响应: 如果 problemId格式不对，或者没有这个题目，响应头中添加键值对
*         否则，返回 Settle
*
* */
@WebServlet("/problemsettle")
public class SelectProblemSettleServlet extends HttpServlet {
    private ObjectMapper mapper = new ObjectMapper();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        ProblemDao problemDao = new ProblemDao();
        SettleDao settleDao = new SettleDao();

        // 监测登录
        User user = Util.isLogin(req);
        if(user == null){
            Util.sendRedirect(req, resp);
            return;
        }

        UserDao userDao = new UserDao();

        List<Settle> list = new ArrayList<>();
        List<SelectProblemSettleResp> res = new ArrayList<>();
        // 1. 获取参数
        String param = req.getParameter("problemId");

        // 2. 校验参数
        try {
            int problemId = Integer.parseInt(param);

            Problem problem = problemDao.selectOne(problemId);
            if(problem == null){
                throw new ProblemNotFiledException();
            }

            list = settleDao.selectProblemSettle(problemId);

            for (Settle settle : list) {
                SelectProblemSettleResp selectProblemSettleResp = new SelectProblemSettleResp();
                selectProblemSettleResp.setId(settle.getId());
                selectProblemSettleResp.setUsername(settle.getUsername());

                User u = userDao.selectOne(settle.getUsername());
                selectProblemSettleResp.setUserphoto(u.getPhoto());

                selectProblemSettleResp.setTitle(settle.getTitle());

                String content = settle.getContent();
                content = content.length() < 100 ? content + "..." : content.substring(0, 100) + "...";
                selectProblemSettleResp.setContent(content);

                selectProblemSettleResp.setTime(settle.getTime());

                res.add(selectProblemSettleResp);
            }
        } catch (NumberFormatException e){
            resp.getWriter().write("输入格式错误!!!");
            return;
        } catch (ProblemNotFiledException e) {
            resp.getWriter().write("此题目不存在!!!");
            return;
        }

        resp.setContentType("application/json;charset=utf8");
        String body = mapper.writeValueAsString(res);
        resp.getWriter().write(body);
    }
}
