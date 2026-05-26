package ServletAPI.SettleAPI;

/**
 * Created with IntelliJ IDEA.
 * Description:
 * User: 强仔
 * Date: 2022-10-18
 * Time: 16:41
 */
public class SubmitSettleResp {
    private int error;    // 0--提交成功  1--提交失败
    private String reason;

    private int id;      // 新发表的这个评论的id

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getError() {
        return error;
    }

    public void setError(int error) {
        this.error = error;
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }
}
