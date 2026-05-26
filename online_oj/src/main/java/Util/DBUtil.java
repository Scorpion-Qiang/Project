package Util;
import com.mysql.jdbc.jdbc2.optional.MysqlDataSource;
import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Created with IntelliJ IDEA.
 * Description:
 * User: 强仔
 * Date: 2022-09-12
 * Time: 21:10
 */
public class DBUtil {
    private static final String URL = "jdbc:mysql://127.0.0.1:3306/online_oj?characterEncoding=utf8&useSSL=false";
    private static final String USERNAME = "root";
    private static final String PASSWORD = "cuige2001";

    private static final DataSource dataSource = new MysqlDataSource();

    static {
        ((MysqlDataSource) dataSource).setUrl(URL);
        ((MysqlDataSource) dataSource).setUser(USERNAME);
        ((MysqlDataSource) dataSource).setPassword(PASSWORD);
    }


    public static Connection getConnection() throws SQLException {
        return dataSource.getConnection();
    }


    public static void close(Connection connection, PreparedStatement statement, ResultSet resultSet){
        if(resultSet != null){
            try {
                resultSet.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }

        if(statement != null){
            try {
                statement.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }

        if(connection != null){
            try {
                connection.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }
}
