package ServletAPI.UserAPI;

/**
 * Created with IntelliJ IDEA.
 * Description:
 * User: 强仔
 * Date: 2022-09-30
 * Time: 22:05
 */
public class UserDetailResp {
    private String username;
    private String photo;
    private String introduce;
    private int submit;
    private int passproblemcount;

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPhoto() {
        return photo;
    }

    public void setPhoto(String photo) {
        this.photo = photo;
    }

    public String getIntroduce() {
        return introduce;
    }

    public void setIntroduce(String introduce) {
        this.introduce = introduce;
    }

    public int getSubmit() {
        return submit;
    }

    public void setSubmit(int submit) {
        this.submit = submit;
    }

    public int getPassproblemcount() {
        return passproblemcount;
    }

    public void setPassproblemcount(int passproblemcount) {
        this.passproblemcount = passproblemcount;
    }
}
