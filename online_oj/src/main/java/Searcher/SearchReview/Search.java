package Searcher.SearchReview;

import Operation.ReviewOp.Review;
import org.ansj.domain.Term;
import org.ansj.splitWord.analysis.ToAnalysis;

import java.io.*;
import java.util.*;

/**
 * Created with IntelliJ IDEA.
 * Description:
 * User: 强仔
 * Date: 2022-10-20
 * Time: 21:05
 */
public class Search {
    // 暂停词表所在的路径
    private static final String STOP_WORD_PATH = "C:\\Users\\86188\\Documents\\代码\\Java\\online_OJ\\src\\main\\webapp\\stopwords.txt";
    // 暂停词列表
    private Set<String> stopWordList = new HashSet<>();
    // 倒排索引
    private Index index = new Index();

    public Search(){
        // 加载暂停词到列表中
        loadStopWord();
    }

    // 从文件中加载暂停词到内存中
    private void loadStopWord() {
        // TODO Reader--字符输入流（读）； Writer--字符输出流（写）； BufferedReader 继承自 Reader，比 Reader多一个按行读取的方法 readLine（）
        try (BufferedReader reader = new BufferedReader(new FileReader(STOP_WORD_PATH))){
            while (true){
                String word = reader.readLine();
                if(word == null){
                    break;
                }
                stopWordList.add(word);
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // 核心方法，搜索
    public List<Review> search(String query){
        // 1. 将传来的参数进行分词
        List<Term> queryTerms = ToAnalysis.parse(query.trim()).getTerms();

        // 2. 针对每一个分词，查倒排索引
        // TODO 整个参数中所有分词分别所对应的 评论+权重
        List<List<ReviewWeight>> lists = new ArrayList<>();
        for (Term term : queryTerms){
            // 如果这个分词是暂停词，不用管它了
            String word = term.getName();

            if(stopWordList.contains(word)){
                continue;
            }

            List<ReviewWeight> list = index.get(word);
            if(list != null){
                lists.add(list);
            }
        }

        // 3. 将相同的 Review 合并权重
        // TODO 处理 各个分词 对应的 评论+权重，得到 整个参数 对应的 评论+权重
        List<ReviewWeight> res = mergeWeight(lists);

        // 4. 根据权重大小排序
        // 按照权重大小排序，从大到小，碰到权重相同的，id小的在前
        res.sort(new Comparator<ReviewWeight>() {
            @Override
            public int compare(ReviewWeight o1, ReviewWeight o2) {
                if(o1.getWeight() == o2.getWeight()){
                    return o1.getReview().getId() - o2.getReview().getId();
                }
                return o2.getWeight() - o1.getWeight();
            }
        });


        // 5. 转换成 Review 的列表

        List<Review> reviews = new ArrayList<>();
        for (int i = 0; i < res.size(); i++) {
            Review review = res.get(i).getReview();
            reviews.add(review);
        }

        return reviews;
    }


    // 将相同的评论权重相加
    // TODO 将相同的评论权重相加，得到的即为 整个参数对应的评论+权重 List<ReviewWeight>
    // TODO 还可以用小根堆，参考 java_doc_searcher 及 SearchSettle
    private List<ReviewWeight> mergeWeight(List<List<ReviewWeight>> lists){
        // TODO key--评论的id； value--评论+权重
        HashMap<Integer, ReviewWeight> map = new HashMap<>();

        for (List<ReviewWeight> list : lists) {
            for (ReviewWeight weight : list) {
                int id = weight.getReview().getId();
                ReviewWeight reviewWeight = map.get(id);

                if(reviewWeight == null){
                    map.put(id, weight);
                } else {
                    reviewWeight.setWeight(reviewWeight.getWeight() + weight.getWeight());
                }
            }
        }

        List<ReviewWeight> res = new ArrayList<>();
        for (Map.Entry<Integer, ReviewWeight> entry : map.entrySet()) {
            res.add(entry.getValue());
        }

        return res;
    }
}
