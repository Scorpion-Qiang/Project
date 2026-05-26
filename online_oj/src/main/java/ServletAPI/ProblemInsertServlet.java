package ServletAPI;

import Operation.ProblemOp.Problem;
import Operation.ProblemOp.ProblemDao;
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
 * Date: 2022-10-25
 * Time: 15:18
 */
@WebServlet("/insertproblem")
public class ProblemInsertServlet extends HttpServlet {
    private ObjectMapper mapper = new ObjectMapper();
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        Problem p = mapper.readValue(req.getInputStream(), Problem.class);
        ProblemDao problemDao = new ProblemDao();
        problemDao.insert(p);

        resp.getWriter().write("添加成功");
    }
}
