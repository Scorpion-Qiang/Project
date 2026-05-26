package Searcher.SearchReview;

import Operation.ReviewOp.Review;
import Searcher.SearcherReq;

import ServletAPI.ReviewAPI.SelectReviewResp;
import Operation.UserOp.User;
import Operation.UserOp.UserDao;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.ansj.domain.Term;
import org.ansj.splitWord.analysis.ToAnalysis;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created with IntelliJ IDEA.
 * Description:
 * User: 强仔
 * Date: 2022-10-20
 * Time: 21:59
 */

/*
* 搜索 Review（题目 + 内容）
* url: "/searchreview?query=...."
* 返回响应: SelectReviewResp
*
* */
@WebServlet("/searcherreview")
public class SearcherReviewServlet extends HttpServlet {
    private ObjectMapper mapper = new ObjectMapper();

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.setCharacterEncoding("utf8");

        SearcherReq searcherReviewReq = mapper.readValue(req.getInputStream(), SearcherReq.class);
        // 1. 获取参数
        String query = searcherReviewReq.getQuery();
        // 2. 校验参数
        if(query == null || query.trim().equals("")){
            resp.setContentType("text/html;charset=utf8");
            resp.getWriter().write("<h3>输入参数不规范!!!</h3>");
            return;
        }
        // 3. 开始查询
        Search search = new Search();
        List<Review> list = search.search(query);
        // 4. 处理返回结果
        List<SelectReviewResp> res = new ArrayList<>();
        for (Review review : list) {
            SelectReviewResp selectReviewResp = new SelectReviewResp();
            selectReviewResp.setId(review.getId());
            selectReviewResp.setUsername(review.getUsername());
            selectReviewResp.setTitle(review.getTitle());
            selectReviewResp.setTime(review.getTime());

            String content = review.getContent();
            content = disposeContent(content, query);
            selectReviewResp.setContent(content);

            UserDao userDao = new UserDao();
            User u = userDao.selectOne(review.getUsername());
            selectReviewResp.setUserphoto(u.getPhoto());

            res.add(selectReviewResp);
        }

        // 5. 返回响应
        String body = mapper.writeValueAsString(res);
        resp.setContentType("application/json;charset=utf8");
        resp.getWriter().write(body);
    }


    // 处理显示的内容
    private String disposeContent(String content, String query) {
        // 正文中第一个分词出现的位置
        int firstPos = -1;
        content = content.toLowerCase();
        List<Term> queryTerms = ToAnalysis.parse(query).getTerms();

        for (Term term : queryTerms) {
            String word = term.getName();
            // 判断分词中是否有汉字
            Pattern pattern = Pattern.compile("[\\u4e00-\\u9fa5]");
            Matcher matcher = pattern.matcher(word);
            if(matcher.find()){
                // 有汉字, 一个分词中有汉字, 它和其他的词是紧靠，它始终是独立成词的
            } else {
                // 如果都是字母的话，那么这个分词就是一个单词喽，要注意让其独立成词
                // 如分词是 list, 正文中出现 arraylist, 它们是不可以匹配的
                // 只有 与分词相同，且前后均有空格或标点符号的一部分，才是和分词匹配的词
                content = content.replaceAll("\\b" + word + "\\b", " " + word + " ");
                word = " " + word + " ";
            }

            firstPos =  content.indexOf(word);
            if(firstPos != -1){
                break;
            }
        }

        // 如果 firstPos = -1 的话，说明内容中并没有出现分词，有分词出现在标题中

        int start = Math.max(firstPos - 70, 0);
        int end = Math.min(content.length(), start + 140);

        return content.substring(start, end);
    }

}
