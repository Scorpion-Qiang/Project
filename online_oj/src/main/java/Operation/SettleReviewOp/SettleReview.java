package Operation.SettleReviewOp;

import java.sql.Timestamp;

/**
 * Created with IntelliJ IDEA.
 * Description:
 * User: 强仔
 * Date: 2022-10-18
 * Time: 20:42
 */
public class SettleReview {
    private int id;
    private String username;
    private String content;

    private Timestamp time;
    private int settleId;
    private int underId;
    private int sendTo;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public Timestamp getTime() {
        return time;
    }

    public void setTime(Timestamp time) {
        this.time = time;
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

    public int getSendTo() {
        return sendTo;
    }

    public void setSendTo(int sendTo) {
        this.sendTo = sendTo;
    }
}
