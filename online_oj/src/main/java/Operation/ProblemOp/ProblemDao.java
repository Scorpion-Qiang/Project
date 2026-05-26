package Operation.ProblemOp;

import Util.DBUtil;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * Description:
 * User: 强仔
 * Date: 2022-09-12
 * Time: 21:39
 */
// 对于数据库中problem表进行增删改查
public class ProblemDao {

    // 1. 新增题目
    public void insert(Problem problem){
        Connection connection = null;
        PreparedStatement statement = null;
        try {
            // 1. 获取数据库连接
            connection = DBUtil.getConnection();
            // 2. 构造sql语句
            String sql = "insert into problem values(null, ?, ?, ?, ?, ?, ?)";
            statement = connection.prepareStatement(sql);
            statement.setString(1, problem.getTitle());
            statement.setString(2, problem.getLevel());
            statement.setString(3, problem.getDescription());
            statement.setString(4, problem.getTemplatecode());
            statement.setString(5, problem.getTestcode());
            statement.setInt(6, problem.getTestExample());
            // 3. 执行 sql 语句
            int ret = statement.executeUpdate();
            if(ret == 0){
                System.out.println("新增题目失败!!");
            } else {
                System.out.println("新增题目成功!!");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            // 切记！！ 一定要关闭资源
            DBUtil.close(connection, statement, null);
        }
    }

    // 2. 删除题目
    public void delete(int id){
        Connection connection = null;
        PreparedStatement statement = null;
        try {
            // 1. 与数据库建立连接
            connection = DBUtil.getConnection();
            // 2. 构造sql语句
            String sql = "delete from problem where id = ?";
            statement = connection.prepareStatement(sql);
            statement.setInt(1, id);
            // 3. 执行sql语句
            int ret = statement.executeUpdate();
            if(ret == 0){
                System.out.println("删除题目失败!!");
            } else {
                System.out.println("删除题目成功!!");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            DBUtil.close(connection, statement, null);
        }
    }

    // 3. 查询所有的题目
    // 实现分页功能，前端使用分页器，后端使用 select limit offset
    // 查询所有的题目，主要用于所有题目列表的显示，列表只需要显示 id, title, level 即可，并不需要全列查询；
    // 尽可能不使用全表查询，数据量太大，数据库比较脆弱，容易挂掉，网络带宽也比较卡；如果查询全表，就要使用分页查询，每次查询一定数量的记录；
    // 全列查询相对于指定列查询还是降低了任务量
    public List<Problem> selectAll(){
        Connection connection = null;
        PreparedStatement statement = null;
        ResultSet resultSet = null;
        List<Problem> res = new ArrayList<>();
        try {
            // 1. 与数据库建立连接
            connection = DBUtil.getConnection();
            // 2. 构造sql语句
            String sql = "select id, title, level from problem";
            statement = connection.prepareStatement(sql);
            // 3. 执行sql语句
            resultSet = statement.executeQuery();
            // 4. 遍历结果集
            while (resultSet.next()){
                Problem problem = new Problem();
                problem.setId(resultSet.getInt(1));
                // getInt()参数可以是 列名或列的下标（第一列是1，第二列是2....）
                // problem.setId(resultSet.getInt("id"));
                problem.setTitle(resultSet.getString(2));
                problem.setLevel(resultSet.getString(3));
                res.add(problem);
            }
            return res;
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            DBUtil.close(connection, statement, resultSet);
        }
        return null;
    }

    // 4. 查询题目详情
    public Problem selectOne(int id){
        Connection connection = null;
        PreparedStatement statement = null;
        ResultSet resultSet = null;
        Problem problem = null;
        try {
            // 1. 与数据库建立连接
            connection = DBUtil.getConnection();
            // 2. 构造sql语句
            String sql = "select * from problem where id = ?";
            statement = connection.prepareStatement(sql);
            statement.setInt(1, id);
            // 3. 执行sql语句
            resultSet = statement.executeQuery();
            // 4. 遍历结果集
            if (resultSet.next()){
                problem = new Problem();
                problem.setId(resultSet.getInt(1));
                problem.setTitle(resultSet.getString(2));
                problem.setLevel(resultSet.getString(3));
                problem.setDescription(resultSet.getString(4));
                problem.setTemplatecode(resultSet.getString(5));
                problem.setTestcode(resultSet.getString(6));
                problem.setTestExample(resultSet.getInt(7));
                return problem;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            DBUtil.close(connection, statement, resultSet);
        }
        return null;
    }

    // 5. 根据题目名称 模糊匹配
    public List<Problem> selectAllLink(String name){
        Connection connection = null;
        PreparedStatement statement = null;
        ResultSet resultSet = null;
        List<Problem> res = new ArrayList<>();
        try {
            // 1. 与数据库建立连接
            connection = DBUtil.getConnection();
            // 2. 构造sql语句
            String sql = "select id, title, level from problem where title like ?";
            statement = connection.prepareStatement(sql);
            statement.setString(1, name);
            // 3. 执行sql语句
            resultSet = statement.executeQuery();
            // 4. 遍历结果集
            while (resultSet.next()){
                Problem problem = new Problem();
                problem.setId(resultSet.getInt(1));
                // getInt()参数可以是 列名或列的下标（第一列是1，第二列是2....）
                // problem.setId(resultSet.getInt("id"));
                problem.setTitle(resultSet.getString(2));
                problem.setLevel(resultSet.getString(3));
                res.add(problem);
            }
            return res;
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            DBUtil.close(connection, statement, resultSet);
        }
        return null;
    }

    // 测试
    // TODO 我赌我的代码里没有 bug，不显示，一定要即使测试
    // 最简单的测试，冒烟测试
    public static void testInsert(){
        ProblemDao problemDao = new ProblemDao();
        Problem problem = new Problem();
        problem.setTitle("跳跃游戏");
        problem.setLevel("中等");
        problem.setDescription("给你一个非负整数数组 nums ，你最初位于数组的第一个位置。\n"+
                "数组中的每个元素代表你在该位置可以跳跃的最大长度。\n" +
                "你的目标是使用最少的跳跃次数到达数组的最后一个位置。\n" +
                "假设你总是可以到达数组的最后一个位置。\n" +
                "\n" +
                "示例 1:\n" +
                "输入: nums = [2,3,1,1,4]\n" +
                "输出: 2\n" +
                "解释: 跳到最后一个位置的最小跳跃数是 2。\n" +
                "     从下标为 0 跳到下标为 1 的位置，跳 1 步，然后跳 3 步到达数组的最后一个位置。\n" +
                "示例 2:\n" +
                "输入: nums = [2,3,0,1,4]\n" +
                "输出: 2\n" +
                " \n" +
                "提示:\n" +
                "1 <= nums.length <= 104\n" +
                "0 <= nums[i] <= 1000\n" +
                "来源：力扣（LeetCode）\n" +
                "链接：https://leetcode.cn/problems/jump-game-ii\n");
        problem.setTemplatecode("import java.util.*;\n" +
                "class Solution {\n" +
                "    public int jump(int[] nums) {\n" +
                "\n" +
                "    }\n" +
                "}");
        problem.setTestcode("public static void main(String[] args) {\n" +
                "        int allcase = 6;\n" +
                "        int pass = 0;\n" +
                "\n" +
                "        Solution solution = new Solution();\n" +
                "        int[][] nums = {{2, 3, 1, 1, 4}, {2, 3, 0, 1, 4}, {2, 1, 3, 4, 5, 2, 6}, {2, 4, 1, 2, 4, 2}, {2, 3, 2, 4, 2, 1}, {1, 2, 1, 3, 1, 2}};\n" +
                "        int[] answers = {2, 2, 3, 2, 3, 3};\n" +
                "\n" +
                "        int[] num = null;\n" +
                "        int answer = 0;\n" +
                "        int res = 0;\n" +
                "        try {\n" +
                "            for (int i = 0; i < allcase; i++) {\n" +
                "                num = nums[i];\n" +
                "                answer = answers[i];\n" +
                "                res = solution.jump(num);\n" +
                "\n" +
                "                if(res != answer){\n" +
                "                    return;\n" +
                "                }\n" +
                "\n" +
                "                pass++;\n" +
                "            }\n" +
                "        } finally {\n" +
                "            if (pass == allcase) {\n" +
                "                System.out.print(\"success\");\n" +
                "            } else {\n" +
                "                String error = \"finally;\" + pass + \";\" + Arrays.toString(num) + \";\" + res + \";\" + answer;\n" +
                "                System.out.print(error);\n" +
                "            }\n" +
                "        }\n" +
                "    }");
        problem.setTestExample(6);
        problemDao.insert(problem);
    }


    public static void testSelectAll(){
        ProblemDao op = new ProblemDao();
        List<Problem> res = op.selectAll();
        System.out.println(res);
    }

    public static void testSelectOne(){
        ProblemDao op = new ProblemDao();
        Problem p = op.selectOne(2);
        System.out.println(p);
    }

    public static void testDelete(){
        ProblemDao op = new ProblemDao();
        op.delete(2);
    }
    public static void main(String[] args) {
        testInsert();;
    }
}
