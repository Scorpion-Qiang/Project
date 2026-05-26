import complieAndRun.Question;
import complieAndRun.Task;

/**
 * Created with IntelliJ IDEA.
 * Description:
 * User: 强仔
 * Date: 2022-09-13
 * Time: 18:21
 */
public class TestSubmit {
    public static void main(String[] args) {
        Question question = new Question();
        question.setCode("import java.util.Arrays;" +
                "public class Solution {\n" +
                "    public int[] twoSum(int[] nums, int target) {\n" +
                "        int[] a = {0, 1};\n" +
                "        return a;\n" +
                "    }\n" +
                "    //\n" +
                "    public static void main(String[] args) throws Exception {\n" +
                "        Solution solution = new Solution();\n" +
                "        // testcase1\n" +
                "        int[] nums1 = {2, 7, 11};\n" +
                "        int target1 = 9;\n" +
                "        // testcase1的参考答案\n" +
                "        int[] answer1 = {0, 1};\n" +
                "        // 返回的结果\n" +
                "        int[] res1 = solution.twoSum(nums1, target1);\n" +
                "        if(Arrays.toString(res1).equals(Arrays.toString(answer1))){\n" +
                "            \n" +
                "        } else {\n" +
                "            throw new Exception(\"testcase1 filed\");\n" +
                "        }\n" +
                "\n" +
                "        // testcase2\n" +
                "        int[] nums2 = {3,2,4};\n" +
                "        int target2 = 6;\n" +
                "        // testcase2的参考答案\n" +
                "        int[] answer2 = {1, 2};\n" +
                "        // 返回的结果\n" +
                "        int[] res2 = solution.twoSum(nums2, target2);\n" +
                "        if(Arrays.toString(res2).equals(Arrays.toString(answer2))){\n" +
                "            \n" +
                "        } else {\n" +
                "            throw new Exception(\"testcase1 filed\");\n" +
                "        }\n" +
                "    }\n" +
                "}");

//        question.setCode("public class Solution{\n" +
//                "    public static void main(String[] args) {\n" +
//                "        System.out.println(\"hello, world!\");\n" +
//                "    }\n" +
//                "}");


        Task task = new Task();
        task.complieAndRun(question);
    }


    public static void main1(String[] args) {
        String s = "\\nyydsdssssa\\ndsds";
        System.out.println(s);
        s = s.replaceAll("\\\\n", "a");
        System.out.println(s);
    }
}
