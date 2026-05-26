package search;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.ansj.domain.Term;
import org.ansj.splitWord.analysis.ToAnalysis;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created with IntelliJ IDEA.
 * Description:
 * User: 强仔
 * Date: 2022-07-11
 * Time: 23:34
 */
// 通过这个类在内存中构建出索引结构
public class Index {

    // 索引文件所在的目录
    private static final String INDEX_PATH = "/root/doc_searcher_install/";

    // Jackson库 的类
    ObjectMapper mapper = new ObjectMapper();

    // 正排索引    根据文档id（docId）找到相应的文档
    // 使用数组下标表示 docId
    private ArrayList<DocInfo> forwardIndex = new ArrayList<DocInfo>();

    // 倒排索引    根据词找到与之相关联的文档
    // key 就是 词
    // value 就是 相关联的文章
    private HashMap<String, ArrayList<Weight>> invertedIndex = new HashMap<String, ArrayList<Weight>>();

    // 创建两把锁
    private Object lacker1 = new Object();
    private Object lacker2 = new Object();


    //  TODO 1. 给定一个 docId，在正排索引中，查询文档的详细信息
    public DocInfo getDocInfo(int docId){
        return forwardIndex.get(docId);
    }

    //  TODO 2. 给定一个词，在倒排索引中，查询与这个词相关的文档
    // 如果单纯的返回 docId（List<Integer>），不太好；无法体现 词和文档 的相关性
    public List<Weight> getInverted(String term){
        return invertedIndex.get(term);
    }

    // TODO 3. 在索引中新增一个文档
    public void addDoc(String title, String url, String content){
        // 新增文档操作，需要同时向正排索引和倒排索引中添加信息
        DocInfo doc = new DocInfo();
        doc.setTitle(title);
        doc.setUrl(url);
        doc.setContent(content);

        // （1）、往正排索引中添加信息
        // 加锁，保证线程安全
        synchronized (lacker1) {
            doc.setDocId(forwardIndex.size());
            forwardIndex.add(doc);
        }

        // （2）、往倒排索引中添加信息
        buildInverted(doc);
    }

    private void buildInverted(DocInfo doc) {
        class WordCnt{
            // 在标题中出现的次数
            public int titleCnt;
            // 在正文中出现的次数
            public int contentCnt;
        }

        // 统计分词在标题和正文中的出现次数
        HashMap<String, WordCnt> wordCntHashMap = new HashMap<String, WordCnt>();

        // 1. 针对文章标题进行分词
        List<Term> titleTerms = ToAnalysis.parse(doc.getTitle()).getTerms();
        // 2. 遍历分词的结果，统计每个词出现的次数
        //    分词库将分词自动转换为小写；真实的搜索引擎，对于用户输入的内容并不区分大小写
        for (Term term : titleTerms) {
            String word = term.getName();
            WordCnt wordCnt = wordCntHashMap.get(word);

            if(wordCnt == null){
                WordCnt newWordCnt = new WordCnt();
                newWordCnt.titleCnt = 1;
                newWordCnt.contentCnt = 0;
                wordCntHashMap.put(word, newWordCnt);
            } else {
                wordCnt.titleCnt++;
            }
        }

        // 3. 针对正文进行分词
        List<Term> contentTerms = ToAnalysis.parse(doc.getContent()).getTerms();
        // 4. 遍历分词的结果，统计每个词出现的次数
        for (Term term : contentTerms) {
            String word = term.getName();
            WordCnt wordCnt = wordCntHashMap.get(word);

            if(wordCnt == null){
                WordCnt newWordCnt = new WordCnt();
                newWordCnt.contentCnt = 1;
                wordCntHashMap.put(word, newWordCnt);
            } else {
                wordCnt.contentCnt++;
            }
        }

        // 5. 把上面的结果汇总到一个 HashMap 中
        //    最终文档的权重 = 标题中词出现的次数 * 10 + 正文中词出现的次数

        // 6. 遍历 HashMap，更新倒排索引中的结构
        for (Map.Entry<String, WordCnt> entry : wordCntHashMap.entrySet()) {

            synchronized (lacker2) {
                String word = entry.getKey();
                WordCnt wordCnt = entry.getValue();

                // 先根据这里的词到倒排索引中查一下
                // invertedList 叫做 倒排拉链
                ArrayList<Weight> invertedList = invertedIndex.get(word);

                if (invertedList == null) {
                    // 如果倒排索引中没有这个词，就插入一个新的键值对
                    ArrayList<Weight> newInvertedList = new ArrayList<Weight>();

                    // 把当前文档构建成 weight 对象
                    Weight weight = new Weight();
                    weight.setDocId(doc.getDocId());
                    // 权重计算
                    weight.setWeight(wordCnt.titleCnt * 10 + wordCnt.contentCnt);

                    newInvertedList.add(weight);
                    invertedIndex.put(word, newInvertedList);
                } else {
                    // 把当前文档构建成 weight 对象，放到倒排拉链中
                    Weight weight = new Weight();
                    weight.setDocId(doc.getDocId());
                    weight.setWeight(wordCnt.titleCnt * 10 + wordCnt.contentCnt);

                    invertedList.add(weight);
                }
            }
        }
    }



    // TODO 4. 将内存中的索引结构保存到指定文件（磁盘）中
    // 借助  Jackon库 中的 ObjectMapper 类
    public void save(){
        long begin = System.currentTimeMillis();

        System.out.println("保存索引开始!!");
        // 判断目录是否存在，如果不存在，创建
        File indexFilePath = new File(INDEX_PATH);
        if(!indexFilePath.exists()){
            indexFilePath.mkdirs();
        }

        File forwardIndexFile = new File(INDEX_PATH + "ForwordIndex.txt");
        File invertedIndexFile = new File(INDEX_PATH + "InvertedIndex.txt");

        // 将 正排和倒排索引结构 写入相应的文件中
        try {
            mapper.writeValue(forwardIndexFile, forwardIndex);
            mapper.writeValue(invertedIndexFile, invertedIndex);
        } catch (IOException e) {
            e.printStackTrace();
        }

        long end = System.currentTimeMillis();
        System.out.println("保存索引结束!!  消耗时间是: " + (end - begin) + "ms");
    }

    // TODO 5. 把磁盘中的内存结构加载到内存中
    public void load(){
        long begin = System.currentTimeMillis();

        System.out.println("加载索引开始!!");

        File forwardIndexFile = new File(INDEX_PATH + "ForwordIndex.txt");
        File invertedIndexFile = new File(INDEX_PATH + "InvertedIndex.txt");

        try {
            forwardIndex = mapper.readValue(forwardIndexFile, new TypeReference<ArrayList<DocInfo>>() {});
            invertedIndex = mapper.readValue(invertedIndexFile, new TypeReference<HashMap<String, ArrayList<Weight>>>() {});
        } catch (IOException e) {
            e.printStackTrace();
        }

        long end = System.currentTimeMillis();
        System.out.println("加载索引结束!!  消耗时间是: " + (end - begin) + "ms");
    }

    public static void main(String[] args) {
        Index index = new Index();
        index.load();
    }
}
