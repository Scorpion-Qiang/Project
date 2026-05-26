import Operation.ReviewOp.Review;
import Searcher.SearchReview.Index;
import Searcher.SearchReview.ReviewWeight;
import Searcher.SearchReview.Search;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

/**
 * Created with IntelliJ IDEA.
 * Description:
 * User: 强仔
 * Date: 2024-05-21
 * Time: 9:27
 */

// TODO 对 SearchReview 中 Search 的测试
// 1. 构造 Review 实例，构造倒排索引 insertedIndex
// 2. 使用 Search 中 search()方法搜索，注意注释掉暂停词那一部分
// 3. 用不到 Parse 类
public class TestSearch {
    public static void main1(String[] args) {
        Index index = new Index();
        List<Review> list = new ArrayList<>();

        produceReview(10,"中国海洋大学","航天工程", list);
        produceReview(6,"西北航天大学","航天科学与技术", list);
        produceReview(2,"北京航天大学","航天材料与航天元器件加工", list);
        produceReview(4,"中国航天工业集团", "航空航天技术", list);
        produceReview(3,"郑州大学", "材料与化工", list);
        produceReview(100,"哈尔滨材料大学", "控制工程", list);
        produceReview(102,"武汉材料大学","无机非金属材料工程", list);
        produceReview(8,"北京材料科技大学","材料科学与工程", list);
        produceReview(9,"山东大学","数学", list);
        produceReview(11,"中国石油大学","石油工程", list);
        produceReview(12,"中国矿业大学","采矿工程", list);


        // 将 review 放到 invertedIndex 中
        for (int i = 0; i < list.size(); i++) {
            index.insertForwardIndex(list.get(i));
        }

        // 打印 insertedIndex
        int count = 1;
        for (Map.Entry<String, List<ReviewWeight>> entry: Index.invertedIndex.entrySet()) {
            System.out.print(count++ + ". " +entry.getKey() + "---");
            List<ReviewWeight> l = entry.getValue();
            for (int i = 0; i < l.size(); i++) {
                System.out.print(l.get(i).getReview().getTitle() + "---" + l.get(i).getWeight() + "  ");
            }
            System.out.println();
        }



        Search search = new Search();
        Scanner scanner = new Scanner(System.in);
        while(scanner.hasNextLine()){
            String query = scanner.nextLine();
            List<Review> res = search.search(query);
            for (int i = 0; i < res.size(); i++) {
                System.out.println(res.get(i));
            }
        }
    }

    // 构造 Review 实例
    public static void produceReview(int id, String title, String content, List<Review> list){
        Review review = new Review();
        review.setId(id);
        review.setTitle(title);
        review.setContent(content);
        list.add(review);
    }
}
