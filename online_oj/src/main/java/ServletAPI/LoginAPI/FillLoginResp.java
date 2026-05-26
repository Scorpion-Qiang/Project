package ServletAPI.LoginAPI;

/**
 * Created with IntelliJ IDEA.
 * Description:
 * User: 强仔
 * Date: 2022-09-20
 * Time: 21:44
 */
// 填充用户登录页("/FillLogin") 返回的响应
public class FillLoginResp {
    private int error;         // 0, 表示用户没有登录过  1 表示用户登录过
    private String username;   // error为 1, username 表示登录过的用户的 用户名
    private String password;   // error为 1, password 表示登录过的用户的 密码

    public int getError() {
        return error;
    }

    public void setError(int error) {
        this.error = error;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
