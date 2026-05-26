package Operation.ProblemReviewOp;

import java.sql.Timestamp;

/**
 * Created with IntelliJ IDEA.
 * Description:
 * User: 强仔
 * Date: 2022-09-18
 * Time: 18:48
 */
public class ProblemReview {
    private int id;
    private String username;
    private String content;

    private Timestamp time;
    private int problemId;
    private int underId;
    private int sendTo;


    public int getSendTo() {
        return sendTo;
    }

    public void setSendTo(int sendTo) {
        this.sendTo = sendTo;
    }

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

    @Override
    public String toString() {
        return "ProblemReview{" +
                "id=" + id +
                ", username='" + username + '\'' +
                ", content='" + content + '\'' +
                ", time=" + time +
                ", problemId=" + problemId +
                ", underId=" + underId +
                '}';
    }
}
