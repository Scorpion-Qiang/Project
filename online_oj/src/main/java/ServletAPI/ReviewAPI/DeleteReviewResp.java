package ServletAPI.ReviewAPI;

/**
 * Created with IntelliJ IDEA.
 * Description:
 * User: 强仔
 * Date: 2022-09-25
 * Time: 17:29
 */
public class DeleteReviewResp {
    private int error;   // 0--删除成功   1--失败
    private String reason;

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
