package ServletAPI;

import Operation.UserOp.User;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

/**
 * Created with IntelliJ IDEA.
 * Description:
 * User: 强仔
 * Date: 2022-09-20
 * Time: 20:48
 */
// 登录监测功能, 监测是否登录过
public class Util {

    private static final String sendRedictPath = "http://127.0.0.1:8080/OnlineJudge/login.html";

    public static User isLogin(HttpServletRequest req){
        HttpSession session = req.getSession(false);
        if(session == null){
            return null;
        }

        User user = (User) session.getAttribute("user");
        return user;
    }


    public static void sendRedirect(HttpServletRequest req, HttpServletResponse resp){
        // 2. 判断是不是 ajax 请求
        // 如果 req 中的请求头中有 X-Requested-With: XMLHttpRequest; 那么这个请求就是 ajax 请求
        String xml = req.getHeader("X-Requested-With");
        if(xml != null && xml.equals("XMLHttpRequest")){
            // 3. 告诉 ajax 我是重定向
            resp.setHeader("REDIRECT", "true");
            // 4. 告诉 ajax 我重定向的 URL
            resp.setHeader("SENDPATH", sendRedictPath);
            // 显示自定义键值对
            resp.setHeader("Access-Control-Expose-Headers", "REDIRECT, SENDPATH");
        } else {
            try {
                resp.sendRedirect("login.html");
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

}
