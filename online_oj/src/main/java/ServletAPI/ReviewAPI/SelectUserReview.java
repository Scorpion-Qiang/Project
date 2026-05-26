package ServletAPI.ReviewAPI;

import Operation.ReviewOp.Review;
import Operation.ReviewOp.ReviewDao;
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
 * Date: 2022-10-03
 * Time: 13:57
 */

/*
* 获取某个用户所有评论
* url: /userreview?username=
* 返回响应： username = null,返回当前用户所有的评论
*          username不存在，resp响应头添加 ERROR 和 ERRORREASON
*
* */

@WebServlet("/userreview")
public class SelectUserReview extends HttpServlet {
    private ObjectMapper mapper = new ObjectMapper();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        ReviewDao reviewDao = new ReviewDao();
        UserDao userDao = new UserDao();

        // 判断是否登录过
        User user = Util.isLogin(req);
        if(user == null){
            Util.sendRedirect(req, resp);
            return;
        }

        // 1. 获取参数
        List<Review> list = new ArrayList<>();
        String param = req.getParameter("username");

        try {
            if (param == null) {
                list = reviewDao.selectUserAllReview(user.getUsername());
            } else {
                User u = userDao.selectOne(param);
                if (u == null) {
                    throw new UserNotFiledException();
                }
                list = reviewDao.selectUserAllReview(param);
            }
        } catch (UserNotFiledException e){
            resp.getWriter().write("该用户不存在!!!");
            return;
        }

        // 返回响应
        resp.setContentType("application/json;charset=utf8");
        String body = mapper.writeValueAsString(list);
        resp.getWriter().write(body);

    }
}
