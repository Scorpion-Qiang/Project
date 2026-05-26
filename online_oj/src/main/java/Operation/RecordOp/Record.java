package Operation.RecordOp;

import com.fasterxml.jackson.annotation.JsonFormat;

import java.sql.Timestamp;

/**
 * Created with IntelliJ IDEA.
 * Description:
 * User: 强仔
 * Date: 2022-09-18
 * Time: 15:47
 */
// status  0--解答正确  1--编译出错  2--运行出错  3--解答错误
// content 提交的代码
// errorReason 错误信息
// passExample 通过的测试用例数
// finalInput 最后一次的输入数据
public class Record {
    private int id;
    private int userId;
    private int problemId;
    private int status;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm", timezone = "GMT+8")
    private Timestamp time;
    private String content;

    private String errorReason;
    private int passExample;
    private String finalInput;


    public String getErrorReason() {
        return errorReason;
    }

    public void setErrorReason(String errorReason) {
        this.errorReason = errorReason;
    }

    public int getPassExample() {
        return passExample;
    }

    public void setPassExample(int passExample) {
        this.passExample = passExample;
    }

    public String getFinalInput() {
        return finalInput;
    }

    public void setFinalInput(String finalInput) {
        this.finalInput = finalInput;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public int getProblemId() {
        return problemId;
    }

    public void setProblemId(int problemId) {
        this.problemId = problemId;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public Timestamp getTime() {
        return time;
    }

    public void setTime(Timestamp time) {
        this.time = time;
    }
}
