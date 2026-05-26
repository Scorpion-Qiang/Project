package Searcher.SearchSettle;

import Operation.SettleOp.Settle;
import Operation.SettleOp.SettleDao;
import org.ansj.domain.Term;
import org.ansj.splitWord.analysis.ToAnalysis;

import java.util.*;

/**
 * Created with IntelliJ IDEA.
 * Description:
 * User: 强仔
 * Date: 2022-10-21
 * Time: 21:19
 */
class Pos {
    int row;
    int col;

    public Pos(int row, int col) {
        this.row = row;
        this.col = col;
    }
}

// TODO 根据参数搜索某个题的题解，只搜题解题目，不搜内容；因为是模糊匹配搜索，即使一个分词在某个题解的题目中出现多次，此分词对应的此题解的权重只能是 1；
public class SearchSettle {
    public List<SettleWeight> search(int problemId, String query){
        // 1. 分词
        List<Term> terms = ToAnalysis.parse(query).getTerms();
        // 2. 查找
        SettleDao settleDao = new SettleDao();

        List<List<Settle>> l = new ArrayList<>();

        for (Term term : terms) {
            String word = term.getName();
            // TODO 因为是模糊匹配搜索，即使一个分词在某个题解的题目中出现多次，此分词对应的此题解的权重只能是 1；此处的 Settle 相当于 SettleWeight<Settle, 1>
            List<Settle> list = settleDao.selectAllLink(problemId, "%" + word + "%");
            if (list.size() > 0) {
                l.add(list);
            }
        }

        // 3. 将相同Settle的权重相加
        List<SettleWeight> list = addWeight(l);

        // 4. 将res按照权重排序
        list.sort(new Comparator<SettleWeight>() {
            @Override
            public int compare(SettleWeight o1, SettleWeight o2) {
                return o2.getWeight() - o1.getWeight();
            }
        });

        return list;
    }


    // 将相同Settle的权重相加
    // TODO 将不同分词对应的 Settle 权重相加，得到整个参数对应的 Settle+权重
    // TODO 此处的 Settle 相当于 SettleWeight<Settle, 1>
    private List<SettleWeight> addWeight(List<List<Settle>> l) {
        List<SettleWeight> res = new ArrayList<>();

        // 1. 将 l 每一行按照 id 排序
        // TODO 将每个分词中 对应的 Settle 按照 id 从小到大排序
        for (List<Settle> list : l) {
            list.sort(new Comparator<Settle>() {
                @Override
                public int compare(Settle o1, Settle o2) {
                    return o1.getId() - o2.getId();
                }
            });
        }

        // 2. 创建一个小根堆, 找到每一行的第一列 的id最小的Settle, 通过与 res 的最后一个元素比较，合并或是新加入
        // TODO 小根堆 可以找到 id 最小的 Settle
        PriorityQueue<Pos> queue = new PriorityQueue<>(new Comparator<Pos>() {
            @Override
            public int compare(Pos o1, Pos o2) {
                int o1Id = l.get(o1.row).get(o1.col).getId();
                int o2Id = l.get(o2.row).get(o2.col).getId();
                return o1Id - o2Id;
            }
        });

        for (int i = 0; i < l.size(); i++) {
            queue.offer(new Pos(i, 0));
        }

        while (!queue.isEmpty()) {
            Pos pos = queue.poll();
            Settle settle = l.get(pos.row).get(pos.col);

            if (res.size() == 0) {
                SettleWeight settleWeight = new SettleWeight();
                settleWeight.setSettle(settle);
                settleWeight.setWeight(1);
                res.add(settleWeight);
            } else {
                SettleWeight finalSettle = res.get(res.size() - 1);
                if(finalSettle.getSettle().equals(settle)){
                    finalSettle.setWeight(finalSettle.getWeight() + 1);
                } else {
                    SettleWeight settleWeight = new SettleWeight();
                    settleWeight.setSettle(settle);
                    settleWeight.setWeight(1);
                    res.add(settleWeight);
                }
            }

            int size = l.get(pos.row).size();
            if(pos.col + 1 < size){
                queue.offer(new Pos(pos.row, pos.col + 1));
            }

        }

        return res;
    }
}
