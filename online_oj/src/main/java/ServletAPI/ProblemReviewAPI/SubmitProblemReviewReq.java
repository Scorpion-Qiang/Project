package ServletAPI.ProblemReviewAPI;

/**
 * Created with IntelliJ IDEA.
 * Description:
 * User: 强仔
 * Date: 2022-09-25
 * Time: 18:03
 */

// 提交一级评论
//    underId == 0 sendTo == 0
// 提交二级评论
//    underId != 0 sendTo != 0
public class SubmitProblemReviewReq {
    private String content;
    private int problemId;
    private int underId;
    private int sendTo;

    public int getSendTo() {
        return sendTo;
    }

    public void setSendTo(int sendTo) {
        this.sendTo = sendTo;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }


    public int getProblemId() {
        return problemId;
    }

    public void setProblemId(int problemId) {
        this.problemId = problemId;
    }

    public int getUnderId() {
        return underId;
    }

    public void setUnderId(int underId) {
        this.underId = underId;
    }
}
