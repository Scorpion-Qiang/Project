import MySQL.*;

import java.awt.*;
import java.sql.Timestamp;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * Description:
 * User: 强仔
 * Date: 2022-03-20
 * Time: 16:52
 */
// 单元测试 针对一个类或一个方法进行的测试

public class TestDemo {

    // 测试插入
    public static void main1(String[] args) {
        PlayerDao playerDao = new PlayerDao();

        Player player = new Player();
        player.setPlayerName("拉塞尔·维斯布鲁克");
        player.setPostTime(new Timestamp(System.currentTimeMillis()));
        player.setContent("神龟三双王");
        player.setUserId(1);

        System.out.println(playerDao.insert(player));
    }

    // 测试查询
    public static void main2(String[] args) {
        List<Player> list = PlayerDao.selectAll();
        System.out.println(list);
    }

    // 测试查询
    public static void main3(String[] args) {
        Player player = PlayerDao.selectById(2);
        System.out.println(player);
    }

    //测试删除
    public static void main4(String[] args) {
        PlayerDao.delete(8);
    }

    //测试根据id查看球队信息
    public static void main5(String[] args) {
        Team team = TeamDao.selectByName("小牛");
        System.out.println(team);
    }

    // 测试新增评论
    public static void main6(String[] args) {
        Review review = new Review();
        review.setPostTime(new Timestamp(System.currentTimeMillis()));
        review.setContent("牛逼");
        review.setPlayerId(2);
        review.setPostAuthor("雷霆老板");
        ReviewDao.insert(review);
    }
}
