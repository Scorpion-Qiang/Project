package ServletAPI.SettleAPI;

import Operation.ProblemOp.Problem;
import Operation.ProblemOp.ProblemDao;
import MyException.UserNotFiledException;
import ServletAPI.Util;
import Operation.SettleOp.Settle;
import Operation.SettleOp.SettleDao;
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
 * Date: 2022-10-03
 * Time: 21:28
 */
/*
 * 查看某个用户全部的题解
 * url: /usersettle?username=..
 * 方法: get
 * 返回响应: 如果 username = null 或为空，查询当前的用户题解
 *         否则，返回 Settle
 *
 * */
@WebServlet("/usersettle")
public class SelectUserSettleServlet extends HttpServlet {
    private ObjectMapper mapper = new ObjectMapper();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {


        UserDao userDao = new UserDao();
        SettleDao settleDao = new SettleDao();

        // 监测登录
        User user = Util.isLogin(req);
        if(user == null){
            Util.sendRedirect(req, resp);
            return;
        }

        List<Settle> list = new ArrayList<>();
        List<SelectUserSettleResp> res = new ArrayList<>();
        // 1. 获取参数
        String param = req.getParameter("username");

        // 2. 校验参数
        try {
            String username = null;
            if(param == null || param.trim().equals("")){
                username = user.getUsername();
            } else {
                User u = userDao.selectOne(param);
                if (u == null) {
                    throw new UserNotFiledException();
                }
                username = u.getUsername();
            }

            list = settleDao.selectUserSettle(username);
            res = change(list);
        }  catch (UserNotFiledException e) {
            resp.getWriter().write("该用户不存在!!!");
            return;
        }

        resp.setContentType("application/json;charset=utf8");
        String body = mapper.writeValueAsString(res);
        resp.getWriter().write(body);
    }

    private List<SelectUserSettleResp> change(List<Settle> list) {
        ProblemDao problemDao = new ProblemDao();
        List<SelectUserSettleResp> res = new ArrayList<>();
        for (Settle settle : list) {
            SelectUserSettleResp userSettleResp = new SelectUserSettleResp();
            Problem p = problemDao.selectOne(settle.getProblemId());
            userSettleResp.setId(settle.getId());
            userSettleResp.setProblemId(p.getId());
            userSettleResp.setProblemName(p.getTitle());
            userSettleResp.setTime(settle.getTime());
            userSettleResp.setTitle(settle.getTitle());
            res.add(userSettleResp);
        }
        return res;
    }
}

