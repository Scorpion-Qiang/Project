package MySQL;

import javax.xml.transform.Templates;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * Description:
 * User: 强仔
 * Date: 2022-03-20
 * Time: 16:36
 */
// 针对球队表的操作
public class TeamDao {


    // 1.根据id查找球队信息
    public static Team selectById(int id){
        Connection connection = null;
        PreparedStatement statement = null;
        ResultSet resultSet = null;

        try {
            // 1) 与数据库建立连接
            connection = DBUtil.getConnection();
            // 2) 构建sql语句
            String sql = "select * from team where id = ?";
            statement = connection.prepareStatement(sql);
            statement.setInt(1, id);
            // 3) 执行sql语句
            resultSet = statement.executeQuery();

            //  处理结果集
            if(resultSet.next()){
                Team team = new Team();
                team.setId(resultSet.getInt("id"));
                team.setTeamName(resultSet.getString("teamName"));
                team.setPassword(resultSet.getString("password"));
                team.setPhotoPath(resultSet.getString("photoPath"));
                team.setIntroduce(resultSet.getString("introduce"));
                team.setPlayerCount(resultSet.getInt("playerCount"));
                return team;
            }
            return null;
        } catch (SQLException e){
            e.printStackTrace();
        }finally {
            DBUtil.close(connection, statement, resultSet);
        }
        return null;
    }

    // 2. 根据球队名查找 球队信息
    public static Team selectByName(String name){
        Connection connection = null;
        PreparedStatement statement = null;
        ResultSet resultSet = null;

        try {
            // 1) 与数据库建立连接
            connection = DBUtil.getConnection();
            // 2) 构建sql语句
            String sql = "select * from team where teamName = ?";
            statement = connection.prepareStatement(sql);
            statement.setString(1, name);
            // 3) 执行sql语句
            resultSet = statement.executeQuery();

            //  处理结果集
            if(resultSet.next()){
                Team team = new Team();
                team.setId(resultSet.getInt("id"));
                team.setTeamName(resultSet.getString("teamName"));
                team.setPassword(resultSet.getString("password"));
                team.setPhotoPath(resultSet.getString("photoPath"));
                team.setIntroduce(resultSet.getString("introduce"));
                team.setPlayerCount(resultSet.getInt("playerCount"));
                return team;
            }
            return null;
        } catch (SQLException e){
            e.printStackTrace();
        }finally {
            DBUtil.close(connection, statement, resultSet);
        }
        return null;
    }

    // 3.新增球队
    public static boolean insert(Team team){
        Connection connection = null;
        PreparedStatement statement = null;
        try {
            // 1) 与数据库建立连接
            connection = DBUtil.getConnection();
            // 2） 构造sql语句
            String sql = "insert into team value(null,?,?,?,?,0)";
            statement = connection.prepareStatement(sql);
            statement.setString(1, team.getTeamName());
            statement.setString(2, team.getPassword());
            statement.setString(3, team.getPhotoPath());
            statement.setString(4, team.getIntroduce());
            // 3) 执行sql语句
            int ret = statement.executeUpdate();
            return ret == 1;
        } catch (SQLException e) {
            e.printStackTrace();
        }finally {
            DBUtil.close(connection, statement, null);
        }
        return false;
    }

    // 4.获取所有的球队的数目
    public static int teamCount(){
        Connection connection = null;
        PreparedStatement statement = null;
        ResultSet resultSet = null;
        try {
            // 1）与数据库建立连接
            connection = DBUtil.getConnection();
            // 2）构造sql语句
            String sql = "select count(*) from team";
            statement = connection.prepareStatement(sql);
            // 3）执行sql语句
            resultSet = statement.executeQuery();
            resultSet.next();
            return  resultSet.getInt("count(*)");
        } catch (SQLException e) {
            e.printStackTrace();
        }finally {
            DBUtil.close(connection, statement, resultSet);
        }
        return 0;
    }

    // 5.查看所有的球队
    public static List<Team> selectAll(){
        Connection connection = null;
        PreparedStatement statement = null;
        ResultSet resultSet = null;
        List<Team> teams = new ArrayList<Team>();
        try {
            // 1）与数据库建立连接
            connection = DBUtil.getConnection();
            // 2）构建sql语句
            String sql = "select * from team";
            statement = connection.prepareStatement(sql);
            // 3）执行sql语句
            resultSet = statement.executeQuery();
            while(resultSet.next()){
                Team team = new Team();
                // password 不能给,直接为空
                team.setId(resultSet.getInt("id"));
                team.setTeamName(resultSet.getString("teamName"));
                team.setIntroduce(resultSet.getString("introduce"));
                team.setPhotoPath(resultSet.getString("photoPath"));
                team.setPlayerCount(resultSet.getInt("playerCount"));
                teams.add(team);
            }
            return teams;
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            DBUtil.close(connection, statement, resultSet);
        }
        return null;
    }

    // 6.新增球员时，所拥有的球员数 +1
    public static boolean addPlayerCount(Team team){
        Connection connection = null;
        PreparedStatement statement = null;

        try {
            // 1） 与数据库建立连接
            connection = DBUtil.getConnection();
            // 2）构建sql语句
            String sql = "update team set playerCount = ? where id = ?";
            statement = connection.prepareStatement(sql);
            statement.setInt(1, team.getPlayerCount()+1);
            statement.setInt(2, team.getId());
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
