package Searcher.SearchProblem;

import Operation.ProblemOp.Problem;

/**
 * Created with IntelliJ IDEA.
 * Description:
 * User: 强仔
 * Date: 2022-10-19
 * Time: 21:29
 */
/*
* 搜索题(只搜针对题目搜)
* 分词 ---> link 搜索 ---> 计算权重
* url: '/searcherproblem?query='
* 响应: Problem
*
*
* */
public class Weight {
    private Problem problem;
    private int count;

    public Problem getProblem() {
        return problem;
    }

    public void setProblem(Problem problem) {
        this.problem = problem;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }
}
