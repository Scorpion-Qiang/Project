package search;

import javafx.geometry.Pos;
import org.ansj.domain.Term;
import org.ansj.splitWord.analysis.ToAnalysis;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;

/**
 * Created with IntelliJ IDEA.
 * Description:
 * User: 强仔
 * Date: 2022-07-13
 * Time: 18:34
 */
// 搜索类
public class DocSearcher {

    // TODO 暂停词
    // 暂停词表的路径
    private static final String STOP_WORD_PATH = "/root/doc_searcher_install/stop_words.txt";
    // 存放暂停词的数据结构
    private HashSet<String> stopWords = new HashSet<>();

    // 创建索引的实例
    private Index index = new Index();

    public DocSearcher(){
        // 加载索引
        index.load();
        // 加载暂停词
        loadStopWords();
    }

    // 完成整个搜索过程的方法
    // query 用户输入的内容
    // List<search.Result> 搜索结果的集合
    public List<Result> search(String query){
        // 1. 分词，对用户输入内容进行分词
        List<Term> oldTermList = ToAnalysis.parse(query).getTerms();
        List<Term> termList = new ArrayList<>();

        // 如果这个分词不在暂停词表中，保留
        for (Term term : oldTermList) {
            String word = term.getName();
            if(!stopWords.contains(word)){
                termList.add(term);
            }
        }


        // 2. 触发，查倒排索引，找到相应的 docId
        List<List<Weight>> termResult = new ArrayList<>();
        for (Term term : termList) {
            String word = term.getName();
            List<Weight> invertedList = index.getInverted(word);
            if(invertedList != null){
                termResult.add(invertedList);
            }
        }

        // 3. 归并，不同分词的倒排拉链中 相同的文档 通过权重相加 归并到一起
        List<Weight> allTermResult = mergeResult(termResult);

        // 4. 排序，按 相关性降序 排列
        allTermResult.sort(new Comparator<Weight>() {
            @Override
            // 降序排序哦
            public int compare(Weight o1, Weight o2) {
                return o2.getWeight() - o1.getWeight();
            }
        });


        // 5. 包装结果，查正排索引，得到文档的具体内容，包装成 search.Result
        List<Result> results = new ArrayList<>();
        for (Weight weight : allTermResult) {
            DocInfo doc = index.getDocInfo(weight.getDocId());
            Result result = new Result();
            result.setTitle(doc.getTitle());
            result.setUrl(doc.getUrl());
            result.setDesc(GenDesc(doc.getContent(), termList));
            results.add(result);
        }
        return results;
    }


    // 相同文件，合并权重
    private List<Weight> mergeResult(List<List<Weight>> source) {

        // 合并时，光标会指过每一个元素
        // 用 Pos 表示二维数组的每一个元素的位置
        class Pos{
            public int row;
            public int col;

            public Pos(int row, int col) {
                this.row = row;
                this.col = col;
            }
        }



        // 1. 将每一行按 docId 升序排序
        for (List<Weight> curRow : source) {
            curRow.sort(new Comparator<Weight>() {
                @Override
                public int compare(Weight o1, Weight o2) {
                    return o1.getDocId() - o2.getDocId();
                }
            });
        }

        // 2. 借助一个堆，将这些行合并
        // 设置最终结果集
        List<Weight> target = new ArrayList<>();

        // 2.1 创建一个小根堆，指定比较规则（按照 Weight 中的 docId 比较，得到最小的 docId）
        PriorityQueue<Pos> queue = new PriorityQueue<>(source.size(), new Comparator<Pos>() {
            @Override
            public int compare(Pos o1, Pos o2) {
                Weight weight1 = source.get(o1.row).get(o1.col);
                Weight weight2 = source.get(o2.row).get(o2.col);
                return weight1.getDocId() - weight2.getDocId();
            }
        });

        // 2.2 初始化堆，将每一行的第一个元素位置入堆
        for (int i = 0; i < source.size(); i++) {
            queue.offer(new Pos(i, 0));
        }

        // 2.3 循环取堆首元素
        while(!queue.isEmpty()){
            Pos pos = queue.poll();
            Weight curW = source.get(pos.row).get(pos.col);

            // 2.4 比较当前插入 Weight中的docId 与 结果集中的最后一个 Weight中的docId 是否相同
            if(target.size() > 0){
                Weight preW = target.get(target.size() - 1);
                // 相同，说明遇到了相同的文档，合并权重
                if(preW.getDocId() == curW.getDocId()){
                    preW.setWeight(preW.getWeight() + curW.getWeight());
                } else {
                    // 不相同，直接插入
                    target.add(curW);
                }
            } else {
                target.add(curW);
            }

            // 2.5 指针移动，入堆
            Pos newPos = new Pos(pos.row, pos.col + 1);
            if(newPos.col >= source.get(newPos.row).size()){
                continue;
            }
            queue.offer(newPos);
        }

        return target;
    }

    // 设置描述
    private String GenDesc(String content, List<Term> termList) {

        // 先遍历分词结果，看看哪个分词在正文中出现
        int firstPos = -1;
        for (Term term : termList) {
            String word = term.getName();

            // 分词库将分词转小写，此处查找分词时，需要将正文转小写
            // "全字匹配"，word 独立成词才能查找出来，而不是作为词的一部分
            // 使用正则表达式 \bword\b 匹配前后有其他符号（空格/标点）的字符串
            // indexOf() 不支持正则表达式
            // 使用 replaceAll() 解决问题
            content = content.toLowerCase().replaceAll("\\b" + word + "\\b", " " + word + " ");
            firstPos = content.indexOf(" " + word + " ");
            if(firstPos > -1){
                break;
            }
        }


        // firstPos == -1 这种情况就是只有标题中有分词，正文中没有分词


        // start 和 end 都是下标 [start, end)
        int start = firstPos < 60 ? 0 : firstPos - 60;
        int end = Math.min(content.length(), start + 160);

        // 实现标红逻辑
        for (Term term : termList) {
            String word = term.getName();

            // 全字匹配  当查询词是 List 的时候，不能将 ArrayList 标红
            // (?i) 表示不区分大小写替换
            content = content.replaceAll("(?i) " + word + " ", "<i> " + word + "</i> ");
        }

        return content.substring(start, end) + "...";
    }


    // 将文件中暂停词加载到内存中
    private void loadStopWords(){
        try(BufferedReader reader = new BufferedReader(new FileReader(STOP_WORD_PATH))){
            while(true) {
                // 由于文件中一个词占一行，所以一行一行的读
                String line = reader.readLine();
                if(line == null){
                    break;
                }
                stopWords.add(line);
            }
        } catch (IOException e){
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        DocSearcher searcher = new DocSearcher();
        Scanner scanner = new Scanner(System.in);
        String query = scanner.nextLine();
        List<Result> results = searcher.search(query);
        for (Result result : results) {
            System.out.println(result);
        }
    }
}
