package ServletAPI.LoginAPI;

/**
 * Created with IntelliJ IDEA.
 * Description:
 * User: 强仔
 * Date: 2022-09-20
 * Time: 21:25
 */
public class LoginResq {
    private int error;      // 0--成功 1--失败
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
