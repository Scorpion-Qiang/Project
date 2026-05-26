package Searcher.SearchProblem;

import Operation.ProblemOp.Problem;
import Operation.ProblemOp.ProblemDao;
import Searcher.SearcherReq;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.ansj.domain.Term;
import org.ansj.splitWord.analysis.ToAnalysis;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.*;

// TODO 此处的搜索用的是模糊匹配
/*
* 搜索题目(只根据题目搜)
* url: 'searcherproblem'
* 方法: 'post'
* 请求: SearcherProblemReq
* 响应: Problem
*
* */
@WebServlet("/searcherproblem")
public class SearcherProblemServlet extends HttpServlet {
    private ObjectMapper mapper = new ObjectMapper();

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.setCharacterEncoding("utf8");


        // 监测登录
//        User user = Util.isLogin(req);
//        if(user == null){
//            Util.sendRedirect(req, resp);
//            return;
//        }

        SearcherReq searcherProblemReq = new SearcherReq();
        searcherProblemReq = mapper.readValue(req.getInputStream(), SearcherReq.class);
        // 1. 获取参数
        String param = searcherProblemReq.getQuery();

        // 校验参数
        if(param == null || param.trim().equals("")){
            resp.setContentType("text/html;charset=utf8");
            resp.getWriter().write("<h3>输入参数不规范!!!</h3>");
            return;
        }

        ProblemDao problemDao = new ProblemDao();

        // 2. 将输入的查询信息进行分词
        List<Term> list = ToAnalysis.parse(param).getTerms();
        HashMap<Problem, Integer> map = new HashMap<>();

        // 3. 针对每一个分词，进行查询，找到匹配的 Problem，使用 HashMap，统计 Problem(key) 和 Integer(value，这个Problem出现的次数，即权重)
        for (Term term : list) {
            String word = term.getName();
            // TODO 模糊匹配
            List<Problem> l = problemDao.selectAllLink("%" + word + "%");
            for (Problem problem : l) {
                map.put(problem, map.getOrDefault(problem, 0) + 1);
            }
        }

        // 4. 将 HashMap 整理成 List<Weight>
        List<Weight> res = new ArrayList<>();
        for (Map.Entry<Problem, Integer> entry : map.entrySet()) {
            Weight weight = new Weight();
            weight.setCount(entry.getValue());
            weight.setProblem(entry.getKey());
            res.add(weight);
        }

        // 5. 将 List<Weight> 按照权重(weight) 排序（权重相同按 id 排序）
        res.sort(new Comparator<Weight>() {
            @Override
            public int compare(Weight o1, Weight o2) {
                if(o1.getCount() == o2.getCount()) {
                    return o1.getProblem().getId() - o2.getProblem().getId();
                }
                return o2.getCount() - o1.getCount();
            }
        });

        List<Problem> result = new ArrayList<>();
        for (Weight weight : res) {
            result.add(weight.getProblem());
        }

        // 4. 返回响应
        resp.setContentType("application/json;charset=utf8");
        String body = mapper.writeValueAsString(result);
        resp.getWriter().write(body);

    }
}
