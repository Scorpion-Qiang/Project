package MySQL;

import java.sql.Timestamp;

/**
 * Created with IntelliJ IDEA.
 * Description:
 * User: 强仔
 * Date: 2022-03-24
 * Time: 18:36
 */
public class Review {
    private int id;
    private Timestamp postTime;
    private String content;
    private String postAuthor;
    private int playerId;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Timestamp getPostTime() {
        return postTime;
    }

    public void setPostTime(Timestamp postTime) {
        this.postTime = postTime;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getPostAuthor() {
        return postAuthor;
    }

    public void setPostAuthor(String postAuthor) {
        this.postAuthor = postAuthor;
    }

    public int getPlayerId() {
        return playerId;
    }

    public void setPlayerId(int playerId) {
        this.playerId = playerId;
    }

    @Override
    public String toString() {
        return "Review{" +
                "id=" + id +
                ", postTime=" + postTime +
                ", content='" + content + '\'' +
                ", postAuthor='" + postAuthor + '\'' +
                ", playerId=" + playerId +
                '}';
    }

}
