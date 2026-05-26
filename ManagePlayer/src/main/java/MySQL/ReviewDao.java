package MySQL;

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
 * Date: 2022-03-24
 * Time: 18:39
 */
public class ReviewDao {

    // 1.显示这个球员的所有评论  最新的评论放到最上面
    public static List<Review> selectAll(int playerId){
        Connection connection = null;
        PreparedStatement statement = null;
        ResultSet resultSet = null;
        List<Review> reviews = new ArrayList<Review>();
        try {
            // 1） 与数据库建立连接
            connection = DBUtil.getConnection();
            // 2） 构建sql语句   查到的临时表的数据按 时间降序排序
            String sql = "select * from reviews where playerId = ? order by postTime desc ";
            statement = connection.prepareStatement(sql);
            statement.setInt(1, playerId);
            // 3） 执行sql语句
            resultSet = statement.executeQuery();
            while (resultSet.next()){
                Review review = new Review();
                review.setId(resultSet.getInt("id"));
                review.setPostTime(resultSet.getTimestamp("postTime"));
                review.setContent(resultSet.getString("content"));
                review.setPostAuthor(resultSet.getString("postAuthor"));
                review.setPlayerId(resultSet.getInt("playerId"));
                reviews.add(review);
            }
            return reviews;
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            DBUtil.close(connection, statement, resultSet);
        }
        return null;
    }

    // 2.新增评论
    public static boolean insert(Review review){
        Connection connection = null;
        PreparedStatement statement = null;

        try {
            // 1）与数据库建立连接
            connection = DBUtil.getConnection();
            // 2）构建sql语句
            String sql = "insert into reviews value(null,?,?,?,?)";
            statement = connection.prepareStatement(sql);
            statement.setTimestamp(1, review.getPostTime());
            statement.setString(2, review.getContent());
            statement.setString(3, review.getPostAuthor());
            statement.setInt(4, review.getPlayerId());
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

    // 3. 删除评论
    public static boolean delete(int id){
        Connection connection = null;
        PreparedStatement statement = null;

        try {
            // 1） 与数据库建立连接
            connection = DBUtil.getConnection();
            // 2） 构建sql语句
            String sql = "delete from reviews where id = ?";
            statement = connection.prepareStatement(sql);
            statement.setInt(1, id);
            // 3） 执行sql语句
            int ret = statement.executeUpdate();
            return ret == 1;
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            DBUtil.close(connection, statement, null);
        }
        return false;
    }

    // 4.根据reviewId 找到评论
    public static Review selectById(int id){
        Connection connection = null;
        PreparedStatement statement = null;
        ResultSet resultSet = null;

        try {
            // 1）与数据库建立连接
            connection = DBUtil.getConnection();
            // 2）构建sql语句
            String sql = "select * from reviews where id = ?";
            statement = connection.prepareStatement(sql);
            statement.setInt(1, id);
            // 3）执行sql语句
            resultSet = statement.executeQuery();
            if(resultSet.next()){
                Review review = new Review();
                review.setId(resultSet.getInt("id"));
                review.setPostAuthor(resultSet.getString("postAuthor"));
                review.setPostTime(resultSet.getTimestamp("postTime"));
                review.setContent(resultSet.getString("content"));
                review.setPlayerId(resultSet.getInt("playerId"));
                return review;
            }
            return null;
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            DBUtil.close(connection, statement, resultSet);
        }
        return null;
    }
}
