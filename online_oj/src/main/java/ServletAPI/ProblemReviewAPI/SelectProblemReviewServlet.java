package ServletAPI.ProblemReviewAPI;

import Operation.ProblemReviewOp.ProblemReview;
import Operation.ProblemReviewOp.ProblemReviewDao;
import MyException.ProblemNotFiledException;
import MyException.ReviewNotFiledException;
import ServletAPI.Util;
import Operation.UserOp.User;
import Operation.UserOp.UserDao;
import com.fasterxml.jackson.databind.ObjectMapper;
import Operation.ProblemOp.Problem;
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
 * Date: 2022-09-25
 * Time: 14:42
 */
/*
* 查看所有的二级评论
* 方法: GET
* URL: /problemreview?problemId= ? & underId = ?
*       如果参数 underId != 0, 说明要查二级评论
        如果参数 underId == null || underId == "", 说明要查一级评论
* 返回响应:
*
* */
@WebServlet("/problemreview")
public class SelectProblemReviewServlet extends HttpServlet {
    private ObjectMapper mapper = new ObjectMapper();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        ProblemReviewDao problemReviewDao = new ProblemReviewDao();
        ProblemDao problemDao = new ProblemDao();

        // 监测登录
        User user = Util.isLogin(req);
        if(user == null){
            Util.sendRedirect(req, resp);
            return;
        }

        UserDao userDao = new UserDao();

        List<ProblemReview> list = new ArrayList<>();
        List<SelectProblemReviewResp> res = new ArrayList<>();
        // 1. 获取参数
        String param1 = req.getParameter("problemId");
        String param2 = req.getParameter("underId");

        // 2. 获取二级评论
        try {
            // 校验参数 underId, 如果参数 underId != 0, 说明要查二级评论
            //                 如果参数 underId == null || underId == "", 说明要查一级评论
            int underId = 0;
            if(param2 != null && !param2.trim().equals("")){
                underId = Integer.parseInt(param2);
                ProblemReview problemReview = problemReviewDao.selectOneProblemReview(underId);
                if(problemReview == null){
                    throw new ReviewNotFiledException();
                }
            }

            // 校验参数 problemId，看看题目是不是存在
            int problemId = Integer.parseInt(param1);
            Problem problem = problemDao.selectOne(problemId);
            if(problem == null){
                throw new ProblemNotFiledException();
            }

            list = problemReviewDao.selectProblemReview(problemId, underId);

            for (ProblemReview review : list) {
                SelectProblemReviewResp selectProblemReviewResp = new SelectProblemReviewResp();
                selectProblemReviewResp.setId(review.getId());
                selectProblemReviewResp.setUsername(review.getUsername());
                selectProblemReviewResp.setContent(review.getContent());
                selectProblemReviewResp.setTime(review.getTime());


                // 如果 sendTo == 0，说明这是一级评论，不需要添加 接收的对象名称
                //     sendTo != 0，说明是二级评论，添加接受的对象名称
                int sendTo = review.getSendTo();
                if(sendTo != 0) {
                    ProblemReview problemReview = problemReviewDao.selectOneProblemReview(sendTo);
                    selectProblemReviewResp.setSendTo(problemReview.getUsername());
                }

                User u = userDao.selectOne(review.getUsername());
                selectProblemReviewResp.setUserphoto(u.getPhoto());

                res.add(selectProblemReviewResp);
            }

        } catch (NumberFormatException e){
            resp.getWriter().write("输入格式有误!!!");
            return;
        } catch (ProblemNotFiledException e) {
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
