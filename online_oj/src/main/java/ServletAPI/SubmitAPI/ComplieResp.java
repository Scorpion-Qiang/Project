package ServletAPI.SubmitAPI;

/**
 * Created with IntelliJ IDEA.
 * Description:
 * User: 强仔
 * Date: 2022-09-13
 * Time: 18:34
 */
// 创建一个类，对应响应的JSON结构
// 注意!!! 类成员变量名要和JSON结构的变量名一致
// 虽然ComplieResp 和 Answer 类很像，但是不能够只用一个类
// 一个类只做一件事，只有一个角色，是代表一个概念；ComplieResp是web请求的响应；Answer是子进程编译运行的结果
public class ComplieResp {
    private int error;     // 0--解答成功  1--编译错误  2--运行错误  3--解答错误  4--题目未找到
    private String reason;  // error为1--放编译错误原因  error为2--放运行错误原因  error为3、4--null

    private int passExampleCount;  // 通过的测试用例的数目
    private String finalInput;     // 最后输入的测试用例  error=0、1时，为null
    private String output;         // 实际的输出   error=0、1、2时，为null
    private String preOutput;      // 预期输出  error=0、1、2时，为null



    public int getPassExampleCount() {
        return passExampleCount;
    }

    public void setPassExampleCount(int passExampleCount) {
        this.passExampleCount = passExampleCount;
    }

    public String getFinalInput() {
        return finalInput;
    }

    public void setFinalInput(String finalInput) {
        this.finalInput = finalInput;
    }

    public String getOutput() {
        return output;
    }

    public void setOutput(String output) {
        this.output = output;
    }

    public String getPreOutput() {
        return preOutput;
    }

    public void setPreOutput(String preOutput) {
        this.preOutput = preOutput;
    }

    public int getError() {
        return error;
    }

    public void setError(int error) {
        this.error = error;
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }

}
