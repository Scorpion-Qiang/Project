package ServletAPI.SettleReviewAPI;

import java.sql.Timestamp;

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
public class SubmitSettleReviewReq {
    private String content;
    private int settleId;
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


    public int getSettleId() {
        return settleId;
    }

    public void setSettleId(int settleId) {
        this.settleId = settleId;
    }

    public int getUnderId() {
        return underId;
    }

    public void setUnderId(int underId) {
        this.underId = underId;
    }
}
