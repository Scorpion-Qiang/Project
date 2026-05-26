package ServletAPI.SettleReviewAPI;

/**
 * Created with IntelliJ IDEA.
 * Description:
 * User: 强仔
 * Date: 2022-09-25
 * Time: 16:21
 */
public class SubmitSettleReviewResp {
    private int error;    // 0--新增成功   1--失败
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
