package ServletAPI.RecordAPI;

import MyException.ProblemNotFiledException;
import ServletAPI.Util;
import Operation.UserOp.User;
import Operation.UserOp.UserDao;
import com.fasterxml.jackson.databind.ObjectMapper;
import Operation.ProblemOp.Problem;
import Operation.ProblemOp.ProblemDao;
import Operation.RecordOp.Record;
import Operation.RecordOp.RecordDao;

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
 * Date: 2022-09-24
 * Time: 14:19
 */
/*
* 某个用户，查询该用户在某个题上的所有提交记录
* 方法: GET
* URL: /record?problemId=..
* */

@WebServlet("/problemrecord")
public class SelectRecordServlet extends HttpServlet {
    private ObjectMapper mapper = new ObjectMapper();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        RecordDao recordDao = new RecordDao();
        ProblemDao problemDao = new ProblemDao();

        // 监测登录
        User user = Util.isLogin(req);
        if(user == null){
            Util.sendRedirect(req, resp);
            return;
        }

        UserDao userDao = new UserDao();

        // 1. 获取参数
        String param = req.getParameter("problemId");
        List<Record> list = new ArrayList<>();
        try {
            // 校验参数合法性
            int problemId = Integer.parseInt(param);
            // 判断是否有此题目
            Problem problem = problemDao.selectOne(problemId);
            if (problem == null) {
                throw new ProblemNotFiledException();
            }

            list = recordDao.selectOnePro(problemId, user.getId());
        } catch (NumberFormatException e) {
            resp.getWriter().write("输入格式错误!!!");
            return;
        } catch (ProblemNotFiledException e) {
            resp.getWriter().write("该题目不存在!!!");
            return;
        }


        // 4. 返回响应
        resp.setContentType("application/json;charset=utf8");
        String res = mapper.writeValueAsString(list);
        resp.getWriter().write(res);

    }
}
