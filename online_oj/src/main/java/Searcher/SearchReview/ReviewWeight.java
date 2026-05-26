package Searcher.SearchReview;

import Operation.ReviewOp.Review;

/**
 * Created with IntelliJ IDEA.
 * Description:
 * User: 强仔
 * Date: 2022-10-20
 * Time: 20:41
 */
// TODO 某关键词在某评论的权重（反应关键词在此评论中的出现频率）
public class ReviewWeight {
    private Review review;
    private int weight;

    public Review getReview() {
        return review;
    }

    public void setReview(Review review) {
        this.review = review;
    }

    public int getWeight() {
        return weight;
    }

    public void setWeight(int weight) {
        this.weight = weight;
    }
}
