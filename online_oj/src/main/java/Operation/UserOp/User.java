package Operation.UserOp;

import java.util.Objects;

/**
 * Created with IntelliJ IDEA.
 * Description:
 * User: 强仔
 * Date: 2022-09-15
 * Time: 20:35
 */
public class User {
    private int id;
    private String username;
    private String password;

    private String photo;
    private String introduce;
    private int submitcount;
    private int passcount;
    private int passproblemcount;
    private int status;

    public int getPassproblemcount() {
        return passproblemcount;
    }

    public void setPassproblemcount(int passproblemcount) {
        this.passproblemcount = passproblemcount;
    }

    public int getPasscount() {
        return passcount;
    }

    public void setPasscount(int passcount) {
        this.passcount = passcount;
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

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
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

    public int getSubmitcount() {
        return submitcount;
    }

    public void setSubmitcount(int submitcount) {
        this.submitcount = submitcount;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", username='" + username + '\'' +
                ", password='" + password + '\'' +
                ", photo='" + photo + '\'' +
                ", introduce='" + introduce + '\'' +
                ", submitcount=" + submitcount +
                ", status=" + status +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        User user = (User) o;
        return id == user.id &&
                submitcount == user.submitcount &&
                passcount == user.passcount &&
                passproblemcount == user.passproblemcount &&
                status == user.status &&
                Objects.equals(username, user.username) &&
                Objects.equals(password, user.password) &&
                Objects.equals(photo, user.photo) &&
                Objects.equals(introduce, user.introduce);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, username, password, photo, introduce, submitcount, passcount, passproblemcount, status);
    }
}
