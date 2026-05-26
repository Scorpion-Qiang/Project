package Operation.UserOp;

import Util.DBUtil;

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
 * Date: 2022-09-15
 * Time: 20:34
 */

public class UserDao {

    // 1. 新增用户
    public void insert(User user){
        Connection connection = null;
        PreparedStatement statement = null;
        try {
            // 1. 与数据库建立连接
            connection = DBUtil.getConnection();
            // 2. 构建 sql 语句
            String sql = "insert into user values(null, ?, ?, ?, ?, 0, 0, 0, 0)";
            statement = connection.prepareStatement(sql);
            statement.setString(1, user.getUsername());
            statement.setString(2, user.getPassword());
            statement.setString(3, user.getPhoto());
            statement.setString(4, user.getIntroduce());
            // 3. 执行 sql 语句
            int ret = statement.executeUpdate();
            if(ret == 0){
                System.out.println("新增用户失败!");
            } else {
                System.out.println("新增用户成功!");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            DBUtil.close(connection, statement, null);
        }
    }

    // 2. 根据用户名查询用户
    public User selectOne(String username){
        Connection connection = null;
        PreparedStatement statement = null;
        ResultSet resultSet = null;
        try {
            // 1. 与数据库建立连接
            connection = DBUtil.getConnection();
            // 2. 构建 sql 语句
            String sql = "select * from user where username = ?";
            statement = connection.prepareStatement(sql);
            statement.setString(1, username);
            // 3. 执行 sql 语句
            resultSet = statement.executeQuery();
            if(resultSet.next()){
                User user = new User();
                user.setId(resultSet.getInt("id"));
                user.setUsername(resultSet.getString("username"));
                user.setPassword(resultSet.getString("password"));
                user.setPhoto(resultSet.getString("photo"));
                user.setIntroduce(resultSet.getString("introduce"));
                user.setSubmitcount(resultSet.getInt("submitcount"));
                user.setPasscount(resultSet.getInt("passcount"));
                user.setPassproblemcount(resultSet.getInt("passproblemcount"));
                user.setStatus(resultSet.getInt("status"));
                return user;
            }
            return null;
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            DBUtil.close(connection, statement, resultSet);
        }
        return null;
    }

    // 根据 id 查询用户
    public User selectOneById(int id){
        Connection connection = null;
        PreparedStatement statement = null;
        ResultSet resultSet = null;
        try {
            // 1. 与数据库建立连接
            connection = DBUtil.getConnection();
            // 2. 构建 sql 语句
            String sql = "select * from user where id = ?";
            statement = connection.prepareStatement(sql);
            statement.setInt(1, id);
            // 3. 执行 sql 语句
            resultSet = statement.executeQuery();
            if(resultSet.next()){
                User user = new User();
                user.setId(resultSet.getInt("id"));
                user.setUsername(resultSet.getString("username"));
                user.setPassword(resultSet.getString("password"));
                user.setPhoto(resultSet.getString("photo"));
                user.setIntroduce(resultSet.getString("introduce"));
                user.setSubmitcount(resultSet.getInt("submitcount"));
                user.setPasscount(resultSet.getInt("passcount"));
                user.setPassproblemcount(resultSet.getInt("passproblemcount"));
                user.setStatus(resultSet.getInt("status"));
                return user;
            }
            return null;
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            DBUtil.close(connection, statement, resultSet);
        }
        return null;
    }


    // 3. 查询所有用户
    public List<User> selectAll(String username) {
        Connection connection = null;
        PreparedStatement statement = null;
        ResultSet resultSet = null;
        try {
            // 1. 与数据库建立连接
            connection = DBUtil.getConnection();
            // 2. 构建 sql 语句
            String sql = "select * from user";
            statement = connection.prepareStatement(sql);
            // 3. 执行 sql 语句
            List<User> res = new ArrayList<>();
            resultSet = statement.executeQuery();
            while (resultSet.next()) {
                User user = new User();
                user.setId(resultSet.getInt("id"));
                user.setUsername(resultSet.getString("username"));
                user.setPassword(resultSet.getString("password"));
                user.setPhoto(resultSet.getString("photo"));
                user.setIntroduce(resultSet.getString("introduce"));
                user.setSubmitcount(resultSet.getInt("submitcount"));
                user.setPasscount(resultSet.getInt("passcount"));
                user.setPassproblemcount(resultSet.getInt("passproblemcount"));
                user.setStatus(resultSet.getInt("status"));
                res.add(user);
            }
            return res;
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            DBUtil.close(connection, statement, resultSet);
        }
        return null;
    }

    // 4. 修改用户信息
    public void update(User user){
        Connection connection = null;
        PreparedStatement statement = null;

        try {
            connection = DBUtil.getConnection();

            String sql = "update user set username = ?, password = ?, introduce = ?, photo = ? where id = ?";
            statement = connection.prepareStatement(sql);
            statement.setString(1, user.getUsername());
            statement.setString(2, user.getPassword());
            statement.setString(3, user.getIntroduce());
            statement.setString(4, user.getPhoto());
            statement.setInt(5, user.getId());

            int ret = statement.executeUpdate();
            if(ret == 1){
                System.out.println("修改成功");
            } else {
                System.out.println("修改失败");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            DBUtil.close(connection, statement, null);
        }
    }

    // 5. 修改用户的提交次数及通过题目个数次数
    public void updateII(User user){
        Connection connection = null;
        PreparedStatement statement = null;

        try {
            connection = DBUtil.getConnection();

            String sql = "update user set submitcount = ?, passcount = ?, passproblemcount = ? where id = ?";
            statement = connection.prepareStatement(sql);
            statement.setInt(1, user.getSubmitcount());
            statement.setInt(2, user.getPasscount());
            statement.setInt(3, user.getPassproblemcount());
            statement.setInt(4, user.getId());

            int ret = statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            DBUtil.close(connection, statement, null);
        }
    }

//    // 按照通过题目次数排名
//    public List<Integer> selectByGrade(){
//        Connection connection = null;
//        PreparedStatement statement = null;
//
//        try {
//            connection = DBUtil.getConnection();
//            String sql = "select username from user grade by passproblemcount desc";
//        } catch (SQLException e) {
//            e.printStackTrace();
//        }
//
//    }

    // 测试
    public static void main(String[] args) {
        User user = new User();
        UserDao userDao = new UserDao();

        user.setUsername("阿强");
        user.setPassword("123456");

        userDao.insert(user);
    }
}
