package ServletAPI.SettleReviewAPI;

import ServletAPI.Util;
import Operation.SettleOp.Settle;
import Operation.SettleOp.SettleDao;

import MyException.SettleNotFiledException;
import MyException.ReviewNotFiledException;
import Operation.SettleReviewOp.SettleReview;
import Operation.SettleReviewOp.SettleReviewDao;
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
 * Date: 2022-09-25
 * Time: 14:42
 */
/*
* 查看所有的一级或二级评论
* 方法: GET
* URL: /settlereview?settleId= ? & underId = ?
* 返回响应:
*
* */
@WebServlet("/settlereview")
public class SelectSettleReviewServlet extends HttpServlet {
    private ObjectMapper mapper = new ObjectMapper();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        SettleReviewDao settleReviewDao = new SettleReviewDao();
        SettleDao settleDao = new SettleDao();

        // 监测登录
        User user = Util.isLogin(req);
        if(user == null){
            Util.sendRedirect(req, resp);
            return;
        }

        UserDao userDao = new UserDao();

        List<SettleReview> list = new ArrayList<>();
        List<SelectSettleReviewResp> res = new ArrayList<>();
        // 1. 获取参数
        String param1 = req.getParameter("settleId");
        String param2 = req.getParameter("underId");

        // 2. 获取二级评论
        try {
            // 校验参数 underId
            int underId = 0;
            if(param2 != null && !param2.trim().equals("")){
                underId = Integer.parseInt(param2);
                SettleReview settleReview = settleReviewDao.selectOneSettleReview(underId);
                if(settleReview == null){
                    throw new ReviewNotFiledException();
                }
            }

            // 校验参数 settleId
            int settleId = Integer.parseInt(param1);
            Settle settle = settleDao.selectOneSettle(settleId);
            if(settle == null){
                throw new SettleNotFiledException();
            }

            list = settleReviewDao.selectSettleReview(settleId, underId);

            for (SettleReview review : list) {
                SelectSettleReviewResp selectSettleReviewResp = new SelectSettleReviewResp();
                selectSettleReviewResp.setId(review.getId());
                selectSettleReviewResp.setUsername(review.getUsername());
                selectSettleReviewResp.setContent(review.getContent());
                selectSettleReviewResp.setTime(review.getTime());


                int sendTo = review.getSendTo();
                // sendTo == 0, 一级评论不需要添加 接收的对象名称
                // sendTo != 0, 说明这是二级评论，需要添加 接收的对象名称
                if(sendTo != 0) {
                    SettleReview settleReview = settleReviewDao.selectOneSettleReview(sendTo);
                    selectSettleReviewResp.setSendTo(settleReview.getUsername());
                }

                User u = userDao.selectOne(review.getUsername());
                selectSettleReviewResp.setUserphoto(u.getPhoto());

                res.add(selectSettleReviewResp);
            }

        } catch (NumberFormatException e){
            resp.getWriter().write("输入格式有误!!!");
            return;
        } catch (SettleNotFiledException e) {
            resp.getWriter().write("该题目不存在!!!");
            return;
        } catch (ReviewNotFiledException e) {
            resp.getWriter().write("该评论不存在!!!");
            return;
        }

        // 3. 返回响应
        resp.setContentType("application/json;charset=utf8");
        String body = mapper.writeValueAsString(res);
        resp.getWriter().write(body);
    }
}
