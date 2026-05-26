package Operation.SettleOp;

import java.sql.Timestamp;
import java.util.Objects;

/**
 * Created with IntelliJ IDEA.
 * Description:
 * User: 强仔
 * Date: 2022-09-18
 * Time: 20:29
 */
public class Settle {
    private int id;
    private String username;
    private String title;
    private String content;


    private Timestamp time;
    private int problemId;

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

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }


    @Override
    public String toString() {
        return "Settle{" +
                "id=" + id +
                ", username='" + username + '\'' +
                ", title='" + title + '\'' +
                ", content='" + content + '\'' +
                ", time=" + time +
                ", problemId=" + problemId +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Settle settle = (Settle) o;
        return id == settle.id &&
                problemId == settle.problemId &&
                Objects.equals(username, settle.username) &&
                Objects.equals(title, settle.title) &&
                Objects.equals(content, settle.content) &&
                Objects.equals(time, settle.time);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, username, title, content, time, problemId);
    }
}
