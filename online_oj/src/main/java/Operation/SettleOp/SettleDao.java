package Operation.SettleOp;

import Util.DBUtil;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * Description:
 * User: 强仔
 * Date: 2022-09-18
 * Time: 20:31
 */
public class SettleDao {
    // 1. 新增题解
    public void insert(Settle settle) {
        Connection connection = null;
        PreparedStatement statement = null;

        try {
            connection = DBUtil.getConnection();
            String sql = "insert into settle values(null, ?, ?, ?, ?, ?)";
            statement = connection.prepareStatement(sql);
            statement.setString(1, settle.getUsername());
            statement.setString(2, settle.getTitle());
            statement.setString(3, settle.getContent());
            statement.setTimestamp(4, settle.getTime());
            statement.setInt(5, settle.getProblemId());

            int ret = statement.executeUpdate();
            if (ret == 0) {
                System.out.println("新增题解失败!!");
            } else {
                System.out.println("新增题解成功!!");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            DBUtil.close(connection, statement, null);
        }
    }

    // 2. 查看指定题目的题解
    public List<Settle> selectProblemSettle(int problemId) {
        Connection connection = null;
        PreparedStatement statement = null;
        ResultSet resultSet = null;
        List<Settle> list = new ArrayList<>();

        try {
            connection = DBUtil.getConnection();
            String sql = "select id, username, title, content, time from settle where problemId = ? order by time desc";
            statement = connection.prepareStatement(sql);
            statement.setInt(1, problemId);

            resultSet = statement.executeQuery();
            while (resultSet.next()) {
                Settle settle = new Settle();
                settle.setId(resultSet.getInt("id"));
                settle.setUsername(resultSet.getString("username"));
                settle.setTitle(resultSet.getString("title"));
                settle.setContent(resultSet.getString("content"));
                settle.setTime(resultSet.getTimestamp("time"));
                list.add(settle);
            }

            return list;
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            DBUtil.close(connection, statement, null);
        }
        return null;
    }

    // 3. 查看某个用户的题解
    public List<Settle> selectUserSettle(String username) {
        Connection connection = null;
        PreparedStatement statement = null;
        ResultSet resultSet = null;
        List<Settle> list = new ArrayList<>();

        try {
            connection = DBUtil.getConnection();
            String sql = "select id, title, time, problemId from settle where username = ? order by time desc";
            statement = connection.prepareStatement(sql);
            statement.setString(1, username);

            resultSet = statement.executeQuery();
            while (resultSet.next()) {
                Settle settle = new Settle();
                settle.setId(resultSet.getInt("id"));
                settle.setTitle(resultSet.getString("title"));
                settle.setTime(resultSet.getTimestamp("time"));
                settle.setProblemId(resultSet.getInt("problemId"));
                list.add(settle);
            }

            return list;
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            DBUtil.close(connection, statement, null);
        }
        return null;
    }

    // 查看某个用户新发表的这个评论的id
    public int selectUserSettleOneId(String username) {
        Connection connection = null;
        PreparedStatement statement = null;
        ResultSet resultSet = null;
        List<Settle> list = new ArrayList<>();

        try {
            connection = DBUtil.getConnection();
            String sql = "select max(id) from settle where username = ?";
            statement = connection.prepareStatement(sql);
            statement.setString(1, username);

            resultSet = statement.executeQuery();
            if(resultSet.next()){
                return resultSet.getInt("max(id)");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            DBUtil.close(connection, statement, null);
        }
        return 0;
    }


    // 4. 查看题解详情
    public Settle selectOneSettle(int id){
        Connection connection = null;
        PreparedStatement statement = null;
        ResultSet resultSet = null;

        try {
            connection = DBUtil.getConnection();
            String sql = "select id, username, title, content, time from settle where id = ?";
            statement = connection.prepareStatement(sql);
            statement.setInt(1, id);

            resultSet = statement.executeQuery();
            if (resultSet.next()){
                Settle settle = new Settle();
                settle.setId(resultSet.getInt("id"));
                settle.setUsername(resultSet.getString("username"));
                settle.setTitle(resultSet.getString("title"));
                settle.setContent(resultSet.getString("content"));
                settle.setTime(resultSet.getTimestamp("time"));
                return settle;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            DBUtil.close(connection, statement, null);
        }
        return null;
    }

    // 5. 删除题解
    public void delete(int id){
        Connection connection = null;
        PreparedStatement statement = null;

        try {
            connection = DBUtil.getConnection();
            String sql = "delete from settle where id = ?";
            statement = connection.prepareStatement(sql);
            statement.setInt(1, id);

            int ret = statement.executeUpdate();
            if(ret == 0){
                System.out.println("删除题解失败!!!");
            } else {
                System.out.println("删除题解成功!!!");
            }

        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            DBUtil.close(connection, statement, null);
        }
    }

    // 6. 根据题解题目 link 查询相应的题解
    public List<Settle> selectAllLink(int problemId, String name){
        Connection connection = null;
        PreparedStatement statement = null;
        ResultSet resultSet = null;
        List<Settle> res = new ArrayList<>();
        try {
            // 1. 与数据库建立连接
            connection = DBUtil.getConnection();
            // 2. 构造sql语句
            String sql = "select id, username, title, content, time from settle where problemId = ? and title like ?";
            statement = connection.prepareStatement(sql);
            statement.setInt(1, problemId);
            statement.setString(2, name);
            // 3. 执行sql语句
            resultSet = statement.executeQuery();
            // 4. 遍历结果集
            while (resultSet.next()){
                Settle settle = new Settle();
                settle.setId(resultSet.getInt("id"));
                settle.setUsername(resultSet.getString("username"));
                settle.setTitle(resultSet.getString("title"));
                settle.setContent(resultSet.getString("content"));
                settle.setTime(resultSet.getTimestamp("time"));

                res.add(settle);
            }
            return res;
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            DBUtil.close(connection, statement, resultSet);
        }
        return null;
    }

    // 7. 更改用户名
    public void updateUserName(String newName, String priorName){
        Connection connection = null;
        PreparedStatement statement = null;

        try {
            connection = DBUtil.getConnection();

            String sql = "update settle set username = ? where username = ?";
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

    // 测试
    public static void main(String[] args) {
        SettleDao settleDao = new SettleDao();
        Settle settle = new Settle();
        settle.setUsername("阿强");
        settle.setTitle("去哪呢呢?");
        settle.setContent("七彩云南");
        settle.setTime(new Timestamp(System.currentTimeMillis()));
        settle.setProblemId(4);

        settleDao.insert(settle);


//        List<Settle> list = settleDao.selectProblemSettle(13);
//        for (Settle settle : list) {
//            System.out.println(settle);
//        }

//        settleDao.delete(1);
    }
}
