package Operation.SettleReviewOp;
import Util.DBUtil;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * Description:
 * User: 强仔
 * Date: 2022-10-18
 * Time: 20:43
 */
public class SettleReviewDao {
    // 1. 查询某个题目所有的评论（underId = 0，一级评论；underId != 0，二级评论）
    public List<SettleReview> selectSettleReview(int settleId, int underId){
        Connection connection = null;
        PreparedStatement statement = null;
        ResultSet resultSet = null;

        List<SettleReview> res = new ArrayList<>();

        try {
            connection = DBUtil.getConnection();

            String sql = "select id, username, content, time, sendto from settlereview where settleId = ? and underId = ? order by time desc";
            statement = connection.prepareStatement(sql);
            statement.setInt(1, settleId);
            statement.setInt(2, underId);

            resultSet = statement.executeQuery();
            while (resultSet.next()){
                SettleReview review = new SettleReview();
                review.setId(resultSet.getInt("id"));
                review.setUsername(resultSet.getString("username"));
                review.setContent(resultSet.getString("content"));
                review.setTime(resultSet.getTimestamp("time"));
                review.setSendTo(resultSet.getInt("sendto"));
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

    // 2. 根据 id 查找某个评论
    public SettleReview selectOneSettleReview(int id){
        Connection connection = null;
        PreparedStatement statement = null;
        ResultSet resultSet = null;

        try {
            connection = DBUtil.getConnection();

            String sql = "select id, username, content, time from settlereview where id = ?";
            statement = connection.prepareStatement(sql);
            statement.setInt(1, id);

            resultSet = statement.executeQuery();
            if (resultSet.next()){
                SettleReview review = new SettleReview();
                review.setId(resultSet.getInt("id"));
                review.setUsername(resultSet.getString("username"));
                review.setContent(resultSet.getString("content"));
                review.setTime(resultSet.getTimestamp("time"));
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

    // 3. 删除某个评论（如果他是一级评论的话，删除它下面的二级评论）
    public void deleteSettleReview(int id){
        Connection connection = null;
        PreparedStatement statement = null;

        try {
            connection = DBUtil.getConnection();

            String sql = "delete from settlereview where id = ? or underId = ?";
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


    // 4. 新增评论
    public void insertSettleReview(SettleReview review){
        Connection connection = null;
        PreparedStatement statement = null;

        try {
            connection = DBUtil.getConnection();

            String sql = "insert into settlereview values(null, ?, ?, ?, ?, ?, ?)";
            statement = connection.prepareStatement(sql);
            statement.setString(1, review.getUsername());
            statement.setString(2, review.getContent());
            statement.setTimestamp(3, review.getTime());
            statement.setInt(4, review.getSettleId());
            statement.setInt(5, review.getUnderId());
            statement.setInt(6, review.getSendTo());

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

    // 5. 某个题目所有评论的数量
    public int selectSettleReviewCount(int settleId){
        Connection connection = null;
        PreparedStatement statement = null;
        ResultSet resultSet = null;

        try {
            connection = DBUtil.getConnection();

            String sql = "select count(*) as num from settlereview where settleId = ?";
            statement = connection.prepareStatement(sql);
            statement.setInt(1, settleId);

            resultSet = statement.executeQuery();
            if(resultSet.next()) {
                return resultSet.getInt("num");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            DBUtil.close(connection, statement, resultSet);
        }
        return 0;
    }

    // 6. 更改用户名
    public void updateUserName(String newName, String priorName){
        Connection connection = null;
        PreparedStatement statement = null;

        try {
            connection = DBUtil.getConnection();

            String sql = "update settlereview set username = ? where username = ?";
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

    public static void main(String[] args) {
        SettleReviewDao settleReviewDao = new SettleReviewDao();

        SettleReview review = new SettleReview();
        review.setUsername("a辉");
        review.setTime(new Timestamp(System.currentTimeMillis()));
        review.setContent("北华大学");
        review.setSettleId(3);
        review.setUnderId(2);
        review.setSendTo(5);

        settleReviewDao.insertSettleReview(review);
    }
}
