package ServletAPI.RecordAPI;

import Operation.ProblemOp.Problem;
import Operation.ProblemOp.ProblemDao;
import Operation.RecordOp.Record;
import Operation.RecordOp.RecordDao;
import MyException.UserNotFiledException;
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
import java.util.ArrayList;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * Description:
 * User: 强仔
 * Date: 2022-10-14
 * Time: 20:51
 */

/*
* 查询某个用户通过的所有的提交记录
* url: "/useronerecord?username=..."
* method: get
* 响应: selectUserOneRecordResp
*
* */
@WebServlet("/useronerecord")
public class SelectUserOneRecordServlet extends HttpServlet {
    private ObjectMapper mapper = new ObjectMapper();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        RecordDao recordDao = new RecordDao();

        ProblemDao problemDao = new ProblemDao();

        // 监测登录
        User user = Util.isLogin(req);
        if(user == null){
            resp.sendRedirect("login.html");
            return;
        }

        UserDao userDao = new UserDao();

        // 返回的响应
        List<Record> list = new ArrayList<>();
        List<SelectUserOneRecordResp> res = new ArrayList<>();

        // 1. 获取参数
        String param = req.getParameter("username");
        try {
            User u = null;
            if(param == null || param.equals("")){
                u = user;
            } else {
                u = userDao.selectOne(param);
            }
            // 2. 校验参数
            // 校验 recordId 对应的记录是否存在
            if (u == null) {
                throw new UserNotFiledException();
            }

            list = recordDao.selectAllPassByUser(u.getId());

            for (Record record : list) {
                int problemId = record.getProblemId();
                Problem problem = problemDao.selectOne(problemId);

                SelectUserOneRecordResp selectUserOneRecordResp = new SelectUserOneRecordResp();
                selectUserOneRecordResp.setId(record.getId());
                selectUserOneRecordResp.setProblemId(problemId);
                selectUserOneRecordResp.setProblemTitle(problem.getTitle());
                selectUserOneRecordResp.setTime(record.getTime());

                res.add(selectUserOneRecordResp);
            }
        } catch (UserNotFiledException e) {
            resp.getWriter().write("该用户不存在!!!");
            return;
        }

        // 3. 返回响应
        resp.setContentType("application/json;charset=utf8");
        String body = mapper.writeValueAsString(res);
        resp.getWriter().write(body);
    }
}

