package MySQL;

/**
 * Created with IntelliJ IDEA.
 * Description:
 * User: 强仔
 * Date: 2022-03-20
 * Time: 16:35
 */
// 球队表的 每一条数据 就是一个对象
public class Team {
    private int id;
    private String teamName;
    private String password;
    private String photoPath;
    private String introduce;
    private int playerCount;

    public int getPlayerCount() {
        return playerCount;
    }

    public void setPlayerCount(int playerCount) {
        this.playerCount = playerCount;
    }

    public String getIntroduce() {
        return introduce;
    }

    public void setIntroduce(String introduce) {
        this.introduce = introduce;
    }

    public String getPhotoPath() {
        return photoPath;
    }

    public void setPhotoPath(String photoPath) {
        this.photoPath = photoPath;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTeamName() {
        return teamName;
    }

    public void setTeamName(String teamName) {
        this.teamName = teamName;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }


    // 如果不重写toString(),会打印 类型@hashcode

    @Override
    public String toString() {
        return "Team{" +
                "id=" + id +
                ", teamName='" + teamName + '\'' +
                ", password='" + password + '\'' +
                ", photoPath='" + photoPath + '\'' +
                ", introduce='" + introduce + '\'' +
                ", playerCount=" + playerCount +
                '}';
    }
}


