package ServletAPI.ReviewAPI;

import Operation.ReviewOp.Review;
import Operation.ReviewOp.ReviewDao;
import MyException.ReviewNotFiledException;
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
 * Date: 2022-10-06
 * Time: 21:47
 */
/*
* 查找某指向某评论的所有的评论
* url: "/appointreview?sendto=.."
* method: get
* 请求：
* 响应：SelectAppointReviewResp
*
* */
@WebServlet("/appointreview")
public class SelectAppointReviewServlet extends HttpServlet {
    private ObjectMapper mapper = new ObjectMapper();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        // 监测登录
        User user = Util.isLogin(req);
        if(user == null){
            Util.sendRedirect(req, resp);
            return;
        }

        UserDao userDao = new UserDao();

        // 1. 获取参数
        String param = req.getParameter("sendto");

        List<SelectAppointReviewResp> res = new ArrayList<>();
        // 2. 校验参数
        try {
            int sendTo = Integer.parseInt(param);

            ReviewDao reviewDao = new ReviewDao();
            Review review = reviewDao.selectReviewOne(sendTo);
            if(review == null){
                throw new ReviewNotFiledException();
            }

            List<Review> list = reviewDao.selectAppointReview(sendTo);
            for (Review r : list) {
                SelectAppointReviewResp selectAppointReviewResp = new SelectAppointReviewResp();
                selectAppointReviewResp.setId(r.getId());
                selectAppointReviewResp.setUsername(r.getUsername());
                selectAppointReviewResp.setTime(r.getTime());
                selectAppointReviewResp.setContent(r.getContent());

                User u = userDao.selectOne(r.getUsername());
                selectAppointReviewResp.setUserphoto(u.getPhoto());

                res.add(selectAppointReviewResp);
            }

        } catch (NumberFormatException e){
            resp.getWriter().write("输入参数格式有误!!");
            return;
        } catch (ReviewNotFiledException e){
            resp.getWriter().write("该评论不存在!!");
            return;
        }

        // 3. 返回响应
        resp.setContentType("application/json;charset=utf8");
        String body = mapper.writeValueAsString(res);
        resp.getWriter().write(body);
    }
}
