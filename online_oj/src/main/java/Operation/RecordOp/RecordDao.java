package Operation.RecordOp;

import Util.DBUtil;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * Description:
 * User: 强仔
 * Date: 2022-09-18
 * Time: 15:49
 */
public class RecordDao {
    // 1. 查询某个题的提交记录
    public List<Record> selectOnePro(int problemId, int userId){
        Connection connection = null;
        PreparedStatement statement = null;
        ResultSet resultSet = null;
        List<Record> list = new ArrayList<>();
        try {
            connection = DBUtil.getConnection();
            String sql = "select id, status, time from record where problemId = ? and userId = ? order by time desc";
            statement = connection.prepareStatement(sql);
            statement.setInt(1, problemId);
            statement.setInt(2, userId);

            resultSet = statement.executeQuery();
            while (resultSet.next()){
                Record record = new Record();
                record.setId(resultSet.getInt("id"));
                record.setStatus(resultSet.getInt("status"));
                record.setTime(resultSet.getTimestamp("time"));
                list.add(record);
            }
            return list;
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            DBUtil.close(connection, statement, resultSet);
        }
        return null;
    }

    // 2. 某个提交记录的内容
    public Record selectOneRe(int id){
        Connection connection = null;
        PreparedStatement statement = null;
        ResultSet resultSet = null;

        try {
            connection = DBUtil.getConnection();
            String sql = "select * from record where id = ?";
            statement = connection.prepareStatement(sql);
            statement.setInt(1, id);

            resultSet = statement.executeQuery();

            if (resultSet.next()){
                Record record = new Record();
                record.setStatus(resultSet.getInt("status"));
                record.setTime(resultSet.getTimestamp("time"));
                record.setContent(resultSet.getString("content"));
                record.setProblemId(resultSet.getInt("problemId"));
                record.setErrorReason(resultSet.getString("errorReason"));
                record.setPassExample(resultSet.getInt("passExample"));
                record.setFinalInput(resultSet.getString("finalInput"));
                return record;
            }

            return null;
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            DBUtil.close(connection, statement, resultSet);
        }
        return null;
    }

    // 3. 获取用户所有的通过的提交记录
    public List<Record> selectAllPassByUser(int userId){
        Connection connection = null;
        PreparedStatement statement = null;
        ResultSet resultSet = null;
        List<Record> list = new ArrayList<>();
        try {
            connection = DBUtil.getConnection();
            String sql = "select id, problemId, time from record where userId = ? and status = 0 order by time desc";
            statement = connection.prepareStatement(sql);
            statement.setInt(1, userId);

            resultSet = statement.executeQuery();
            while (resultSet.next()){
                Record record = new Record();
                record.setId(resultSet.getInt("id"));
                record.setProblemId(resultSet.getInt("problemId"));
                record.setTime(resultSet.getTimestamp("time"));
                list.add(record);
            }

            return list;
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            DBUtil.close(connection, statement, resultSet);
        }
        return null;
    }

    // 4. 查询 某人与某个题目的关系 0--解答正确  1--编译出错  2--运行出错  3--解答错误
    //  0--解答正确  1--做过但没通过  2--没做过
    public int selectOneRe(int problemId, int userId){
        Connection connection = null;
        PreparedStatement statement = null;
        ResultSet resultSet = null;
        int res = 2;
        try {
            connection = DBUtil.getConnection();
            String sql = "select distinct status from record where problemId= ? and userId = ? order by status asc";
            statement = connection.prepareStatement(sql);
            statement.setInt(1, problemId);
            statement.setInt(2, userId);

            resultSet = statement.executeQuery();
            if (resultSet.next()){
                res = resultSet.getInt("status") == 0 ? 0 : 1;
            }
            return res;
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            DBUtil.close(connection, statement, resultSet);
        }
        return res;
    }

    // 5. 添加提交记录
    public void insert(Record record){
        Connection connection = null;
        PreparedStatement statement = null;

        try {
            connection = DBUtil.getConnection();
            String sql = "insert into record values(null, ?, ?, ?, ?, ?, ?, ?, ?)";
            statement = connection.prepareStatement(sql);
            statement.setInt(1, record.getUserId());
            statement.setInt(2, record.getProblemId());
            statement.setInt(3, record.getStatus());
            statement.setTimestamp(4, record.getTime());
            statement.setString(5, record.getContent());
            statement.setString(6, record.getErrorReason());
            statement.setInt(7, record.getPassExample());
            statement.setString(8, record.getFinalInput());

            int ret = statement.executeUpdate();
            if(ret == 0){
                System.out.println("新增记录失败 !!");
            } else {
                System.out.println("新增记录成功 !!");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            DBUtil.close(connection, statement, null);
        }
    }
}
