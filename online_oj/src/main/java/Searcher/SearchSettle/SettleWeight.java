package Searcher.SearchSettle;

import Operation.SettleOp.Settle;

import java.util.Objects;

/**
 * Created with IntelliJ IDEA.
 * Description:
 * User: 强仔
 * Date: 2022-10-21
 * Time: 20:22
 */
// TODO 某关键词在某题解中的权重（反应关键词在此题解中的出现频率）
public class SettleWeight {
    private Settle settle;
    private int weight;

    public Settle getSettle() {
        return settle;
    }

    public void setSettle(Settle settle) {
        this.settle = settle;
    }

    public int getWeight() {
        return weight;
    }

    public void setWeight(int weight) {
        this.weight = weight;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        SettleWeight that = (SettleWeight) o;
        return weight == that.weight &&
                Objects.equals(settle, that.settle);
    }

    @Override
    public int hashCode() {
        return Objects.hash(settle, weight);
    }

    @Override
    public String toString() {
        return "SettleWeight{" +
                "settle=" + settle +
                ", weight=" + weight +
                '}';
    }
}
