package Operation.ProblemOp;

import java.util.Objects;

/**
 * Created with IntelliJ IDEA.
 * Description:
 * User: 强仔
 * Date: 2022-09-12
 * Time: 21:36
 */
// Problem 类的一个实例就对应数据库中problem表中的一条记录
public class Problem {
    private int id;
    private String title;
    private String level;
    private String description;
    private String templatecode;
    private String testcode;
    private int testExample;

    public int getTestExample() {
        return testExample;
    }

    public void setTestExample(int testExample) {
        this.testExample = testExample;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getLevel() {
        return level;
    }

    public void setLevel(String level) {
        this.level = level;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getTemplatecode() {
        return templatecode;
    }

    public void setTemplatecode(String templatecode) {
        this.templatecode = templatecode;
    }

    public String getTestcode() {
        return testcode;
    }

    public void setTestcode(String testcode) {
        this.testcode = testcode;
    }

    @Override
    public String toString() {
        return "Problem{" +
                "id=" + id +
                ", title='" + title + '\'' +
                ", level='" + level + '\'' +
                ", description='" + description + '\'' +
                ", templatecode='" + templatecode + '\'' +
                ", testcode='" + testcode + '\'' +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Problem problem = (Problem) o;
        return id == problem.id &&
                testExample == problem.testExample &&
                Objects.equals(title, problem.title) &&
                Objects.equals(level, problem.level) &&
                Objects.equals(description, problem.description) &&
                Objects.equals(templatecode, problem.templatecode) &&
                Objects.equals(testcode, problem.testcode);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, title, level, description, templatecode, testcode, testExample);
    }
}
