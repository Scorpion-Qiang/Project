package ServletAPI;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

/**
 * Created with IntelliJ IDEA.
 * Description:
 * User: 强仔
 * Date: 2022-10-23
 * Time: 11:41
 */
/*
* 注销操作
*
* */
@WebServlet("/loginout")
public class LoginOutServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        // 登录状态，输出 session 中的user
        HttpSession session = req.getSession(false);
        if(session != null ) {
            session.removeAttribute("user");
        }
        Util.sendRedirect(req, resp);

    }
}
