package ServletAPI.ProblemAPI;

/**
 * Created with IntelliJ IDEA.
 * Description:
 * User: 强仔
 * Date: 2022-10-25
 * Time: 18:54
 */
public class ProblemListResp {
    private int id;
    private String title;
    private String level;
    private int status;   // 0---表示通过此题  1--表示做过了，但没通过  2--表示没做过

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getLevel() {
        return level;
    }

    public void setLevel(String level) {
        this.level = level;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }
}
