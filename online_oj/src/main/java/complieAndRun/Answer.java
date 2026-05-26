package complieAndRun;

/**
 * Created with IntelliJ IDEA.
 * Description:
 * User: 强仔
 * Date: 2022-09-10
 * Time: 21:50
 */

// 编译 + 运行的结果
public class Answer {
    // 错误码，约定 0 是编译运行都通过(可能是解答错误或解答正确)；1 是编译出错； 2 是运行出错（抛出异常）
    private int error;
    // 出错的信息，error 是 1，就放编译的错误信息；error 为 2，就放运行时的异常
    private String reason;
    // error 为 0，运行程序的标准输出
    private String stdout;    // 放 "success" 或者是 "finally;通过测试用例数;最后一次的输入用例;实际输出的结果;预期的结果"

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

    public String getStdout() {
        return stdout;
    }

    public void setStdout(String stdout) {
        this.stdout = stdout;
    }


    @Override
    public String toString() {
        return "complieAndRun.Answer{" +
                "error=" + error +
                ", reason='" + reason + '\'' +
                ", stdout='" + stdout + '\'';
    }
}
