package ServletAPI.RecordAPI;

import com.fasterxml.jackson.annotation.JsonFormat;

import java.sql.Timestamp;

/**
 * Created with IntelliJ IDEA.
 * Description:
 * User: 强仔
 * Date: 2022-09-26
 * Time: 19:59
 */
public class RecordDetailResp {
    private int problemId;
    private String problemtitle;
    private int status;   // 0--解答正确  1--编译出错  2--运行出错  3--解答错误
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm", timezone = "GMT+8")
    private Timestamp time;
    private String content;
    private String errorReason;
    private int allExample;
    private int passExample;
    private String finalInput;



    public int getProblemId() {
        return problemId;
    }

    public void setProblemId(int problemId) {
        this.problemId = problemId;
    }

    public int getAllExample() {
        return allExample;
    }

    public void setAllExample(int allExample) {
        this.allExample = allExample;
    }

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


    public String getProblemtitle() {
        return problemtitle;
    }

    public void setProblemtitle(String problemtitle) {
        this.problemtitle = problemtitle;
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

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }
}
