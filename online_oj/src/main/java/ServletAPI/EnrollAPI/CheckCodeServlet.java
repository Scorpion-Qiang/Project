package ServletAPI.EnrollAPI;
import cn.dsna.util.images.ValidateCode;
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
 * Date: 2022-09-26
 * Time: 21:36
 */
/*
* 获取验证码
*
*
* */
@WebServlet("/checkcode")
public class CheckCodeServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        // 声明存储验证码的宽度、高度、验证码字母个数、验证码的干扰线条数
        int width  = 120;
        int height = 40;
        int count = 4;
        int lineCount = 20;
        // 创建验证码对象
        ValidateCode validateCode = new ValidateCode(width, height, count, lineCount);
        // 获取验证码
        String code = validateCode.getCode();
        // 将验证码保存到 session 中
        HttpSession session = req.getSession(true);
        session.setAttribute("checkcode", code);

        validateCode.write(resp.getOutputStream());
    }
}
