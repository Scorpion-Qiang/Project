package ServletAPI.ReviewAPI;

import Operation.ReviewOp.Review;
import Operation.ReviewOp.ReviewDao;
import MyException.PermissionNotException;
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

/**
 * Created with IntelliJ IDEA.
 * Description:
 * User: 强仔
 * Date: 2022-09-25
 * Time: 16:47
 */
/*
* 删除论坛评论
* 方法: GET
* URL: /deletereview?id=
* 返回响应: DeleteReviewResp
*
*
* */
@WebServlet("/deletereview")
public class DeleteReviewServlet extends HttpServlet {
    private ObjectMapper mapper = new ObjectMapper();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.setContentType("application/json;charset=utf8");
        ReviewDao reviewDao = new ReviewDao();

        // 监测登录
        User user = Util.isLogin(req);
        if(user == null){
            Util.sendRedirect(req, resp);
            return;
        }

        UserDao userDao = new UserDao();


        // 1. 获取参数
        String param = req.getParameter("id");

        // 2. 判断参数合法性
        DeleteReviewResp deleteReviewResp = new DeleteReviewResp();
        try {
            int id = Integer.parseInt(param);
            Review review = reviewDao.selectReviewOne(id);
            if(review == null){
                throw new ReviewNotFiledException();
            }

            // 没有修改权限
            if(!review.getUsername().equals(user.getUsername())){
                throw new PermissionNotException();
            }

            // 3. 删除评论
            reviewDao.delete(id);

        } catch (NumberFormatException e){
            deleteReviewResp.setError(1);
            deleteReviewResp.setReason("输入不正确");
        } catch (ReviewNotFiledException e){
            deleteReviewResp.setReason("不存在该评论");
        } catch (PermissionNotException e){
            deleteReviewResp.setError(1);
            deleteReviewResp.setReason("没有删除权限");
        }

        // 4. 返回响应
        String body = mapper.writeValueAsString(deleteReviewResp);
        resp.getWriter().write(body);
    }
}
