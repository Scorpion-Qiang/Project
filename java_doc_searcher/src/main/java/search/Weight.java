package search;

/**
 * Created with IntelliJ IDEA.
 * Description:
 * User: 强仔
 * Date: 2022-07-11
 * Time: 23:53
 */
// search.Weight 数据结构，将 文档id 和 文档和词的相关性权重 进行一个包裹
public class Weight {
    private int docId;

    // weight 是 表示文档和词的相关性的权重，weight 越大，这个词与文档的相关性越强
    // weight = 词在标题中出现的次数 * 10 + 词在正文中出现的次数
    private int weight;

    public int getDocId() {
        return docId;
    }

    public void setDocId(int docId) {
        this.docId = docId;
    }

    public int getWeight() {
        return weight;
    }

    public void setWeight(int weight) {
        this.weight = weight;
    }


}
