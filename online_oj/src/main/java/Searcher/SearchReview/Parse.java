package Searcher.SearchReview;

import Operation.ReviewOp.Review;
import Operation.ReviewOp.ReviewDao;

import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * Description:
 * User: 强仔
 * Date: 2022-10-20
 * Time: 20:34
 */
// TODO 构造倒排索引
public class Parse {
    private Index index = new Index();
    public void run(){
        // 1. 查询所有的评论
        ReviewDao reviewDao = new ReviewDao();
        List<Review> list = reviewDao.selectAllReview(0);

        // 2. 将每个评论都添加到索引中
        for (Review review : list) {
            index.insertForwardIndex(review);
        }
    }
}
