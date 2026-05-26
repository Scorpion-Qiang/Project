package ServletAPI.ProblemReviewAPI;

import Operation.ProblemOp.Problem;
import Operation.ProblemOp.ProblemDao;
import Operation.ProblemReviewOp.ProblemReviewDao;
import MyException.ProblemNotFiledException;
import ServletAPI.Util;
import Operation.UserOp.User;
import Operation.UserOp.UserDao;
import com.fasterxml.jackson.databind.ObjectMapper;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Created with IntelliJ IDEA.
 * Description:
 * User: 强仔
 * Date: 2022-10-11
 * Time: 18:17
 */
/*
* 获取某个题目的评论总数
* url: "./problemreviewcount?problemId=.."
* 响应: SelectProblemReviewCountResp
*
* */
@WebServlet("/problemreviewcount")
public class SelectProblemReviewCountServlet extends HttpServlet {
    private ObjectMapper mapper = new ObjectMapper();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.setContentType("application/json;charset=utf8");
        ProblemReviewDao problemReviewDao = new ProblemReviewDao();
        ProblemDao problemDao = new ProblemDao();

        // 监测登录
        User user = Util.isLogin(req);
        if(user == null){
            Util.sendRedirect(req, resp);
            return;
        }

        UserDao userDao = new UserDao();

        SelectProblemReviewCountResp selectProblemReviewCountResp = new SelectProblemReviewCountResp();

        // 1. 获取参数
        String param = req.getParameter("problemId");

        try {
            // 校验参数 problemId
            int problemId = Integer.parseInt(param);
            Problem problem = problemDao.selectOne(problemId);
            if(problem == null){
                throw new ProblemNotFiledException();
            }

            int count = problemReviewDao.selectProblemReviewCount(problemId);

            selectProblemReviewCountResp.setCount(count);
        } catch (NumberFormatException e){
            selectProblemReviewCountResp.setError(1);
            selectProblemReviewCountResp.setReason("输入格式有误!!!");
        } catch (ProblemNotFiledException e) {
            selectProblemReviewCountResp.setError(1);
            selectProblemReviewCountResp.setReason("此题目不存在!!!");
        }

        // 3. 返回响应
        String body = mapper.writeValueAsString(selectProblemReviewCountResp);
        resp.getWriter().write(body);
    }
}
