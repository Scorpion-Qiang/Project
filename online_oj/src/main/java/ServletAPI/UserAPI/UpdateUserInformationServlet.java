package ServletAPI.UserAPI;


import Operation.ReviewOp.ReviewDao;
import Operation.ProblemReviewOp.ProblemReviewDao;
import MyException.*;
import ServletAPI.Util;
import Operation.SettleOp.SettleDao;
import Operation.SettleReviewOp.SettleReviewDao;
import Operation.UserOp.User;
import Operation.UserOp.UserDao;
import com.fasterxml.jackson.databind.ObjectMapper;

import javax.servlet.ServletException;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.Part;
import java.io.File;
import java.io.IOException;
import java.util.UUID;

/**
 * Created with IntelliJ IDEA.
 * Description:
 * User: 强仔
 * Date: 2022-10-22
 * Time: 20:46
 */
/*
* 修改个人信息，包括修改用户名，密码，个人介绍，头像
* url: 'updateuser'
*
*
* */
@MultipartConfig
@WebServlet("/updateuserinformation")
public class UpdateUserInformationServlet extends HttpServlet {
    private ObjectMapper mapper = new ObjectMapper();
    private static final String USER_PHOTO_PATH = "/root/apache-tomcat-8.5.76/webapps/OnlineJudge/image/";

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.setCharacterEncoding("utf8");
        resp.setContentType("application/json;charset=utf8");

        // 监测登录
        User user = Util.isLogin(req);
        if(user == null){
            Util.sendRedirect(req, resp);
            return;
        }

        UserDao userDao = new UserDao();
        UpdateUserInformationResp updateResp = new UpdateUserInformationResp();

        // 1. 获取参数
        String priorName = user.getUsername();
        String newName = req.getParameter("username");
        String introduce = req.getParameter("introduce");

        // 2. 校验参数
        try {
            // 2-1. 新的用户名合法性
            if(newName == null || newName.trim().equals("")){
                throw new UsernameOrPasswordNotException();
            }
            if(!newName.equals(user.getUsername())) {
                User uu = userDao.selectOne(newName);
                if (uu != null) {
                    throw new UserExistException();
                }
            }
            // 2-2. 个人介绍
            if(introduce != null && introduce.length() > 50){
                throw new IntroduceOutException();
            }

            // 2-3. 图片处理
            String newPhoto = user.getPhoto();
            // 不传图片，part != null, 是 org.apache.catalina.core.ApplicationPart@62a62fa4
            Part part = req.getPart("myfile");
            // 2-3-1. 获取part的绝对路径
            String prior = part.getSubmittedFileName();
            if(prior != null){
                // 2-3-2. 获取图片名的后缀 .jpg .png .jiff
                int index = prior.lastIndexOf('.');
                String type = prior.substring(index);
                // 2-3-3. 给新的照片命名(唯一)
                String path = UUID.randomUUID().toString() + type;
                // 2-3-4. 设置其绝对路径
                String location = USER_PHOTO_PATH + path;
                // 2-3-5. 将文件写入
                part.write(location);

                newPhoto = path;

                // 2-3-6. 删除旧的头像
                File file = new File(USER_PHOTO_PATH + "/" + user.getPhoto());
                file.delete();
            }

            // 6. 修改用户信息
            // TODO 同时修改了 session 中的 user
            user.setUsername(newName);
            user.setIntroduce(introduce);
            user.setPhoto(newPhoto);
            userDao.update(user);

            // 7. 我背锅，建表时的字段就应该选 userId wc,
            ReviewDao reviewDao = new ReviewDao();
            reviewDao.updateUserName(newName, priorName);

            ProblemReviewDao problemReviewDao = new ProblemReviewDao();
            problemReviewDao.updateUserName(newName, priorName);

            SettleReviewDao settleReviewDao = new SettleReviewDao();
            settleReviewDao.updateUserName(newName, priorName);

            SettleDao settleDao = new SettleDao();
            settleDao.updateUserName(newName, priorName);

        } catch (UsernameOrPasswordNotException e) {
            updateResp.setError(1);
            updateResp.setReason("用户名不合法!!!");
        } catch (UserExistException e) {
            updateResp.setError(1);
            updateResp.setReason("该用户名已存在!!!");
        } catch (IntroduceOutException e) {
            updateResp.setError(1);
            updateResp.setReason("个人介绍超出限制!!!");
        }


        // 3. 返回响应
        String body = mapper.writeValueAsString(updateResp);
        resp.getWriter().write(body);
    }
}
