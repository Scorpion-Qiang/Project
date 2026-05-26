package Searcher.SearchReview;

import Operation.ReviewOp.Review;
import org.ansj.domain.Term;
import org.ansj.splitWord.analysis.ToAnalysis;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created with IntelliJ IDEA.
 * Description:
 * User: 强仔
 * Date: 2022-10-20
 * Time: 20:35
 */
// TODO 倒排索引
public class Index {
    // 1. 倒排索引
    // TODO key--关键词  value--[评论+权重]的集合（此关键词出现的评论及对应权重，可以反映关键词在不同评论中出现的频率大小）
    // TODO 静态变量，属于类本身，可使用类名（Index）直接调用（继承自Index的子类也可以，别的类名不可以）；在类加载到内存的时候，系统会给静态变量赋初始值，静态变量在内存中永远只有一份
    public static HashMap<String, List<ReviewWeight>> invertedIndex = new HashMap<>();

    // TODO 某评论中，某关键词在题目中出现的次数（titleCnt）；某关键词在内容中出现的次数（contentCnt）；可以计算某关键词在此评论中权重
    class WordCnt {
        int titleCnt;
        int contentCnt;
    }

    // 2. 向倒排索引中添加一个 Review
    public void insertForwardIndex(Review review){
        // TODO key--此评论（review）中关键词（分词得）； value--此关键词出现在 题目次数+内容次数
        HashMap<String, WordCnt> map = new HashMap<>();

        // 2.1. 关于题目
        String title = review.getTitle();
        List<Term> titleTerm = ToAnalysis.parse(title).getTerms();
        for (Term term : titleTerm) {
            String word = term.getName();
            WordCnt wordCnt = map.get(word);
            if(wordCnt != null){
                wordCnt.titleCnt++;
            } else {
                WordCnt w = new WordCnt();
                w.titleCnt = 1;
                map.put(word, w);
            }
        }

        // 2.2. 关于内容
        String content = review.getContent();
        List<Term> contentTerm = ToAnalysis.parse(content).getTerms();
        for (Term term : contentTerm) {
            String word = term.getName();
            WordCnt wordCnt = map.get(word);
            if(wordCnt != null){
                wordCnt.contentCnt++;
            } else {
                WordCnt w = new WordCnt();
                w.contentCnt = 1;
                map.put(word, w);
            }
        }

        // 2.3. 将 map 添加到 invertedIndex 中
        for (Map.Entry<String, WordCnt> entry : map.entrySet()) {
            String word = entry.getKey();
            WordCnt wordCnt = entry.getValue();

            // TODO 计算得某关键词在此评论（review）中的权重
            int weight = wordCnt.titleCnt * 10 + wordCnt.contentCnt;
            ReviewWeight reviewWeight = new ReviewWeight();
            reviewWeight.setReview(review);
            reviewWeight.setWeight(weight);

            List<ReviewWeight> list = invertedIndex.get(word);
            if(list == null){
                List<ReviewWeight> l = new ArrayList<>();
                l.add(reviewWeight);
                invertedIndex.put(word, l);
            } else {
                list.add(reviewWeight);
            }
        }

    }

    // 3. 根据 Term, 查倒排索引
    public List<ReviewWeight> get(String word){
        return invertedIndex.get(word);
    }
}
