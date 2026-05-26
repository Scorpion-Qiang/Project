import org.ansj.domain.Term;
import org.ansj.splitWord.analysis.ToAnalysis;

import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * Description:
 * User: 强仔
 * Date: 2022-07-10
 * Time: 21:46
 */
public class TestAnsj {
    public static void main(String[] args) {
        // 用于分词的语句
        String str = "两数之和";

        // 对于单词，分词库是不区分大小写的
        List<Term> terms = ToAnalysis.parse(str).getTerms();
        for (Term term : terms) {
            System.out.println(term.getName());
        }
    }
}
