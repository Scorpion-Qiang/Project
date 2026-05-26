import javax.swing.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * Description:
 * User: 强仔
 * Date: 2022-09-11
 * Time: 21:04
 */

/*
* 书写测试用例代码：
* 如果运行错误或解答错误，最后打印   "finally;测试用例数;最后一次输入的测试用例;实际输出结果;预期结果"
* 如果解答正确，最后打印 success
*
* */

// 测试用例代码
// 用户提交的代码中，拼接main()方法，main()方法中实例化Solution，调用方法，不同的测试用例作为参数，返回结果与参考答案作对比
public class Solution {

    // 1. 两数之和

    // 用户提交的代码
    public int[] twoSum(int[] nums, int target) {
        int[] a = {0, 1};
        return a;
    }

    // 测试用例代码
    public static void main1(String[] args){
        // 所有的测试用例数
        int allcase = 6;
        // 通过的测试用例数
        int pass = 0;

        Solution solution = new Solution();
        // 实际的测试用例
        int[][] nums = {{2, 7, 11}, {3, 2, 4}, {3, 3}, {2, 8, 9, 4, 5, 100, 56, 48, 1, 56, 78, 450}, {2, 8, 9, 48, 56}, {2, 10, 9, 48, 4, 1}};
        int[] targets = {9, 6, 6, 80, 10, 14};
        // 标准答案
        int[][] answers = {{0, 1}, {1, 2}, {0, 1}, {0, 10}, {0, 1}, {1, 4}};

        int[] num = null;
        int target = 0;
        int[] answer = null;
        int[] res = null;
        try {
            // 调用用户提交的 twoSum（）方法，对每一个测试用例使用此方法运行，得到的结果与标准答案对比，得出通过测试用例数
            for (int i = 0; i < allcase; i++) {
                num = nums[i];
                target = targets[i];
                answer = answers[i];
                res = solution.twoSum(num, target);

                if (res.length != answer.length) {
                    return;
                }
                for (int j = 0; j < answer.length; j++) {
                    if (answer[j] != res[j]) {
                        return;
                    }
                }
                pass++;
            }
            // 如果编译运行错误，代码直接结束；编译的标准错误内容在 complieErr.txt文件中；运行的标准错误内容在stderr.txt文件中
        } finally {
            // 通过所有的测试用例，stdout标准输出内容为 success
            if (pass == allcase) {
                System.out.print("success");
            } else {
                // 如果没有通过全部的测试用例，stdout标准输出内容为
                // finally; 通过的测试用例数; 解答错误的测试用例体现（比如 {2, 7, 11} 9）; 带入解答错误的测试用例后得到的错误答案; 解答错误的测试用例的标准答案
                String input = Arrays.toString(num) + " " + target;
                String error = "finally;" + pass + ";" + input + ";" + Arrays.toString(res) + ";" + Arrays.toString(answer);
                System.out.print(error);
            }
        }

    }

    // 2. 有效的括号
    public boolean isValid(String s) {
        return false;
    }
    public static void main2(String[] args){
        int allcase = 10;
        int pass = 0;

        Solution solution = new Solution();
        String[] strings = {"()", "()[]{}", "(]", "([{}])", "([]{})", "[(])", "{[](})[]", "((]][", ")(", "([)]"};
        boolean[] answers = {true, true, false, true, true, false, false, false, false, false};

        String s = null;
        boolean answer = false;
        boolean res = false;
        try {
            for (int i = 0; i < allcase; i++) {
                s = strings[i];
                answer = answers[i];
                res = solution.isValid(s);
                if(res != answer){
                    return;
                }
                pass++;
            }
        } finally {
            if (pass == allcase) {
                System.out.print("success");
            } else {
                String error = "finally;" + pass + ";" + s + ";" + res + ";" + answer;
                System.out.print(error);
            }
        }

    }

    // 3. 跳跃游戏
    public int jump(int[] nums) {
        return 1;
    }

    public static void main3(String[] args) {
        int allcase = 6;
        int pass = 0;

        Solution solution = new Solution();
        int[][] nums = {{2, 3, 1, 1, 4}, {2, 3, 0, 1, 4}, {2, 1, 3, 4, 5, 2, 6}, {2, 4, 1, 2, 4, 2}, {2, 3, 2, 4, 2, 1}, {1, 2, 1, 3, 1, 2}};
        int[] answers = {2, 2, 3, 2, 3, 3};

        int[] num = null;
        int answer = 0;
        int res = 0;
        try {
            for (int i = 0; i < allcase; i++) {
                num = nums[i];
                answer = answers[i];
                res = solution.jump(num);

                if(res != answer){
                    return;
                }

                pass++;
            }
        } finally {
            if (pass == allcase) {
                System.out.print("success");
            } else {
                String error = "finally;" + pass + ";" + Arrays.toString(num) + ";" + res + ";" + answer;
                System.out.print(error);
            }
        }
    }

    // 4. 买卖股票的最佳时机
    public int maxProfit(int[] prices) {
        return 1;
    }

    public static void main4(String[] args) {
        int allcase = 6;
        int pass = 0;

        Solution solution = new Solution();
        int[][] nums = {{7,1,5,3,6,4}, {7,6,4,3,1}, {10,4,9,18,26,3,24}, {6,4,16,8,9,15}, {1,1,2,4,2,6,8}, {1,4,2,8,6}};
        int[] answers = {5, 0, 22, 12, 7, 7};

        int[] num = null;
        int answer = 0;
        int res = 0;
        try {
            for (int i = 0; i < allcase; i++) {
                num = nums[i];
                answer = answers[i];
                res = solution.maxProfit(num);

                if(res != answer){
                    return;
                }

                pass++;
            }
        } finally {
            if (pass == allcase) {
                System.out.print("success");
            } else {
                String error = "finally;" + pass + ";" + Arrays.toString(num) + ";" + res + ";" + answer;
                System.out.print(error);
            }
        }
    }

    // 5. 编辑距离

    public int minDistance(String word1, String word2) {
        return 0;
    }

    public static void main5(String[] args) {
        int allcase = 10;
        int pass = 0;

        Solution solution = new Solution();
        String[][] nums = {{"horse", "ros"}, {"intention", "execution"}, {"kitten", "sitting"}, {"love", "like"}, {"class", "sleep"}, {"code", "diligent"}, {"prosperity", "spectacular"}, {"science", "technology"}, {"welcome", "qingdao"}, {"dance", "byte"}};
        int[] answers = {3, 5, 3, 2, 4, 7, 10, 9, 7, 4};

        String[] num = null;
        int answer = 0;
        int res = 0;
        try {
            for (int i = 0; i < allcase; i++) {
                num = nums[i];
                answer = answers[i];
                String word1 = num[0];
                String word2 = num[1];
                res = solution.minDistance(word1, word2);

                if(res != answer){
                    return;
                }

                pass++;
            }
        } finally {
            if (pass == allcase) {
                System.out.print("success");
            } else {
                String input = "\"" + num[0] + "\"" + "  " + "\"" + num[1] + "\"";
                String error = "finally;" + pass + ";" + input + ";" + res + ";" + answer;
                System.out.print(error);
            }
        }
    }


    // 6.回文数
    public boolean isPalindrome(int x) {
        return false;
    }

    public static void main(String[] args) {
        int allcase = 10;
        int pass = 0;

        Solution solution = new Solution();
        int[] nums = {121, -121, 10, 66, 4948, 564, 36, -686, 43234, 1};
        boolean[] answers = {true, false, false, true, false, false, false, false, true, true};

        int num = 0;
        boolean answer = true;
        boolean res = false;
        try {
            for (int i = 0; i < allcase; i++) {
                num = nums[i];
                answer = answers[i];
                res = solution.isPalindrome(num);

                if(res != answer){
                    return;
                }

                pass++;
            }
        } finally {
            if (pass == allcase) {
                System.out.print("success");
            } else {
                String error = "finally;" + pass + ";" + num + ";" + res + ";" + answer;
                System.out.print(error);
            }
        }
    }

}
