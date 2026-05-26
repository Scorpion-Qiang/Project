package ServletAPI.SubmitAPI;

/**
 * Created with IntelliJ IDEA.
 * Description:
 * User: 强仔
 * Date: 2022-09-13
 * Time: 17:49
 */
// 用户提交的id + 代码是JSON格式，为了解析请求的 body，构造一个类对应JSON结构
// 注意!!! 成员变量的名字要和 JSON 结构的变量名一致
class ComplieReq {
    private int id;
    private String code;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    @Override
    public String toString() {
        return "subCode{" +
                "id=" + id +
                ", code=" + code +
                '}';
    }
}
