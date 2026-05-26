package Operation.ReviewOp;

import Util.DBUtil;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * Description:
 * User: 强仔
 * Date: 2022-09-18
 * Time: 17:04
 */
public class ReviewDao {

    // 1. 查询所有的评论（underId = 0，表示一级评论，underId != 0，表示这个一级评论下面的二级评论）
    public List<Review> selectAllReview(int underId){
        Connection connection = null;
        PreparedStatement statement = null;
        ResultSet resultSet = null;

        List<Review> res = new ArrayList<>();

        try {
            connection = DBUtil.getConnection();

            String sql = "select id, username, title, content, time, sendTo from review where underId = ? order by time desc";
            statement = connection.prepareStatement(sql);
            statement.setInt(1, underId);

            resultSet = statement.executeQuery();
            while (resultSet.next()){
                Review review = new Review();
                review.setId(resultSet.getInt("id"));
                review.setUsername(resultSet.getString("username"));
                review.setTitle(resultSet.getString("title"));
                review.setContent(resultSet.getString("content"));
                review.setTime(resultSet.getTimestamp("time"));
                review.setSendTo(resultSet.getInt("sendTo"));
                res.add(review);
            }
            return res;
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            DBUtil.close(connection, statement, resultSet);
        }
        return null;
    }

    // 2. 查询某个用户所有的评论（userId = 0）
    public List<Review> selectUserAllReview(String username){
        Connection connection = null;
        PreparedStatement statement = null;
        ResultSet resultSet = null;

        List<Review> res = new ArrayList<>();

        try {
            connection = DBUtil.getConnection();

            String sql = "select id, title, time from review where underId = 0 and username = ? order by time desc";
            statement = connection.prepareStatement(sql);
            statement.setString(1, username);

            resultSet = statement.executeQuery();
            while (resultSet.next()){
                Review review = new Review();
                review.setId(resultSet.getInt("id"));
                review.setTitle(resultSet.getString("title"));
                review.setTime(resultSet.getTimestamp("time"));
                res.add(review);
            }
            return res;
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            DBUtil.close(connection, statement, resultSet);
        }
        return null;
    }

    // 3. 查看评论详情(一级评论)
    public Review selectReviewOne(int id){
        Connection connection = null;
        PreparedStatement statement = null;
        ResultSet resultSet = null;

        try {
            connection = DBUtil.getConnection();

            String sql = "select id, username, title, content, time from review where id = ?";
            statement = connection.prepareStatement(sql);
            statement.setInt(1, id);

            resultSet = statement.executeQuery();
            if (resultSet.next()){
                Review review = new Review();
                review.setId(resultSet.getInt("id"));
                review.setUsername(resultSet.getString("username"));
                review.setTitle(resultSet.getString("title"));
                review.setContent(resultSet.getString("content"));
                review.setTime(resultSet.getTimestamp("time"));
                return review;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            DBUtil.close(connection, statement, resultSet);
        }

        return null;
    }

    // 4. 新增评论
    public void insert(Review review){
        Connection connection = null;
        PreparedStatement statement = null;

        try {
            connection = DBUtil.getConnection();

            String sql = "insert into review values(?, ?, ?, ?, ?, ?, ?)";
            statement = connection.prepareStatement(sql);
            statement.setInt(1, review.getId());
            statement.setString(2, review.getUsername());
            statement.setString(3, review.getTitle());
            statement.setString(4, review.getContent());
            statement.setTimestamp(5, review.getTime());
            statement.setInt(6, review.getUnderId());
            statement.setInt(7, review.getSendTo());

            int ret = statement.executeUpdate();
            if(ret == 0){
                System.out.println("新增评论失败!!!");
            } else {
                System.out.println("新增评论成功!!!");
            }

        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            DBUtil.close(connection, statement, null);
        }
    }

    // 5. 删除评论
    public void delete(int id){
        Connection connection = null;
        PreparedStatement statement = null;

        try {
            connection = DBUtil.getConnection();

            String sql = "delete from review where id = ? or underId = ?";
            statement = connection.prepareStatement(sql);
            statement.setInt(1, id);
            statement.setInt(2, id);

            int ret = statement.executeUpdate();
            if(ret == 0){
                System.out.println("删除评论失败!!!");
            } else {
                System.out.println("删除评论成功!!!");
            }

        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            DBUtil.close(connection, statement, null);
        }
    }

    // 6. 查找指向某评论的所有评论
    public List<Review> selectAppointReview(int sendTo){
        Connection connection = null;
        PreparedStatement statement = null;
        ResultSet resultSet = null;

        List<Review> res = new ArrayList<>();

        try {
            connection = DBUtil.getConnection();

            String sql = "select id, username, content, time from review where sendTo = ? order by time desc";
            statement = connection.prepareStatement(sql);
            statement.setInt(1, sendTo);

            resultSet = statement.executeQuery();
            while (resultSet.next()){
                Review review = new Review();
                review.setId(resultSet.getInt("id"));
                review.setUsername(resultSet.getString("username"));
                review.setContent(resultSet.getString("content"));
                review.setTime(resultSet.getTimestamp("time"));
                res.add(review);
            }
            return res;
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            DBUtil.close(connection, statement, resultSet);
        }
        return null;
    }

    // 6. 修改 用户名
    public void updateUserName(String newName, String priorName){
        Connection connection = null;
        PreparedStatement statement = null;

        try {
            connection = DBUtil.getConnection();

            String sql = "update review set username = ? where username = ?";
            statement = connection.prepareStatement(sql);
            statement.setString(1, newName);
            statement.setString(2, priorName);

            int ret = statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            DBUtil.close(connection, statement, null);
        }
    }

    // 测试代码
    public static void main(String[] args) {
        ReviewDao reviewDao = new ReviewDao();
        Review review = new Review();

        review.setUsername("a辉");
        review.setTitle("C#");
        review.setContent("牡丹之都");
        review.setTime(new Timestamp(System.currentTimeMillis()));
        review.setUnderId(1);
        review.setSendTo(4);

        reviewDao.insert(review);

//        List<Review> list = reviewDao.selectUserAllReview("a辉");
//        for (Review review : list) {
//            System.out.println(review);
//        }

//        reviewDao.delete(5);
    }
}
