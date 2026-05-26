package ServletAPI.UserAPI;

/**
 * Created with IntelliJ IDEA.
 * Description:
 * User: 强仔
 * Date: 2022-10-23
 * Time: 10:36
 */
public class UpdateUserPasswordReq {
    private String priorPassword;
    private String newPassword;
    private String againPassword;

    public String getPriorPassword() {
        return priorPassword;
    }

    public void setPriorPassword(String priorPassword) {
        this.priorPassword = priorPassword;
    }

    public String getNewPassword() {
        return newPassword;
    }

    public void setNewPassword(String newPassword) {
        this.newPassword = newPassword;
    }

    public String getAgainPassword() {
        return againPassword;
    }

    public void setAgainPassword(String againPassword) {
        this.againPassword = againPassword;
    }
}
