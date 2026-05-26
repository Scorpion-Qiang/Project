package MySQL;


import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * Description:
 * User: 强仔
 * Date: 2022-03-20
 * Time: 16:39
 */
// 针对 球员表的 操作
public class PlayerDao {
    // 1.新增球员
    public static boolean insert(Player player){
        Connection connection = null;
        PreparedStatement statement = null;
        try {
            // 1）数据库连接
            connection = DBUtil.getConnection();
            // 2) 构造sql语句
            String sql = "insert into players value(null,?,?,?,?)";
            statement = connection.prepareStatement(sql);
            statement.setString(1, player.getPlayerName());
            statement.setTimestamp(2, player.getPostTime());
            statement.setString(3, player.getContent());
            statement.setInt(4, player.getUserId());
            // 3) 执行sql语句
            int ret = statement.executeUpdate();

            return ret == 1;
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            // 4) 回收资源
            DBUtil.close(connection, statement, null);
        }
        return false;
    }

    // 2. 查看所有的球员  注意：新增的球员放到最上面
    //  把数据库中所有的数据都查询出来，数据库中的数据量过大，并不科学；
    //  通常使用分页查询
    public static List<Player> selectAll(){
        Connection connection = null;
        PreparedStatement statement = null;
        ResultSet resultSet = null;
        List<Player> list = new ArrayList<Player>();

        try {
            // 1) 与数据建立连接
            connection = DBUtil.getConnection();
            // 2) 构造sql语句  根据发布时间降序排序，将新增的球员放到上面
            String sql = "select * from players order by postTime desc";
            statement = connection.prepareStatement(sql);
            // 3) 执行sql语句
            resultSet = statement.executeQuery();
            // 遍历结果集
            while(resultSet.next()){
                Player player = new Player();
                player.setId(resultSet.getInt("id"));
                player.setPlayerName(resultSet.getString("playerName"));
                player.setPostTime(resultSet.getTimestamp("postTime"));

                // 球员content只显示一部分
                String content = resultSet.getString("content");
                if(content.length() > 100){
                    content = content.substring(0,100);
                }
                player.setContent(content);

                player.setUserId(resultSet.getInt("userId"));
                list.add(player);
            }
            return list;
        } catch (SQLException e) {
            e.printStackTrace();
        }finally {
            DBUtil.close(connection, statement, resultSet);
        }
        return list;
    }

    // 3.查询指定的player
    public static Player selectById(int id){
        Connection connection = null;
        PreparedStatement statement = null;
        ResultSet resultSet = null;

        try {
            // 1) 与数据库建立连接
            connection = DBUtil.getConnection();
            // 2) 构造sql语句
            String sql = "select * from players where id = ?";
            statement = connection.prepareStatement(sql);
            statement.setInt(1, id);
            // 3) 执行sql语句
            resultSet = statement.executeQuery();
            // 处理结果集
            // id不为空且唯一，所以resultSet要么是空；要么是一条数据
            if(!resultSet.next()){
                return null;
            }
            Player player = new Player();
            player.setId(resultSet.getInt("id"));
            player.setPlayerName(resultSet.getString("playerName"));
            player.setPostTime(resultSet.getTimestamp("postTime"));
            player.setContent(resultSet.getString("content"));
            player.setUserId(resultSet.getInt("userId"));
            return player;
        } catch (SQLException e) {
            e.printStackTrace();
        }finally {
            DBUtil.close(connection, statement, resultSet);
        }
        return null;
    }

    // 4.删除球员
    public static boolean delete(int id){
        Connection connection = null;
        PreparedStatement statement = null;
        ResultSet resultSet = null;
        try {
            // 1) 与数据库建立连接
            connection = DBUtil.getConnection();
            // 2）构建sql语句
            String sql = "delete from players where id = ?";
            statement = connection.prepareStatement(sql);
            statement.setInt(1, id);
            // 3) 执行sql语句
            int ret = statement.executeUpdate();
            if(ret == 1){
                return true;
            }
            System.out.println("没有这个球员");
            return false;

        } catch(SQLException e){
            e.printStackTrace();
        } finally {
            DBUtil.close(connection, statement, resultSet);
        }
        return false;
    }


    // 6.查看 用户所拥有的 球员
    public static List<Player> selectForTeam(int id){
        Connection connection = null;
        PreparedStatement statement = null;
        ResultSet resultSet = null;
        List<Player> list = new ArrayList<Player>();
        try {
            // 1） 与数据库建立连接
            connection = DBUtil.getConnection();
            // 2） 构建sql语句
            String sql = "select * from players where userId = ?";
            statement = connection.prepareStatement(sql);
            statement.setInt(1, id);
            // 3） 执行sql语句
            resultSet = statement.executeQuery();
            // 4） 处理结果集
            while (resultSet.next()){
                Player player = new Player();
                player.setId(resultSet.getInt("id"));
                player.setPlayerName(resultSet.getString("playerName"));
                player.setPostTime(resultSet.getTimestamp("postTime"));
                // 让内容 只显示一部分
                String content = resultSet.getString("content");
                if(content.length() > 100){
                    content = content.substring(0,100);
                }
                player.setContent(content);

                player.setUserId(resultSet.getInt("userId"));
                list.add(player);
            }
            return list;
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            DBUtil.close(connection, statement, resultSet);
        }
        return null;
    }

    // 7.修改球员
    public static boolean Update(int id, String playerName, String content){
        Connection connection = null;
        PreparedStatement statement = null;
        try {
            // 1）与数据建立连接
            connection = DBUtil.getConnection();
            // 2）构建sql语句
            String sql = "update players set playerName = ?, content = ?, postTime = ? where id = ?";
            statement = connection.prepareStatement(sql);
            statement.setString(1, playerName);
            statement.setString(2, content);
            statement.setTimestamp(3, new Timestamp(System.currentTimeMillis()));
            statement.setInt(4, id);
            // 3）执行sql语句
            int ret = statement.executeUpdate();
            return ret == 1;
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            DBUtil.close(connection, statement, null);
        }
        return false;
    }
}
