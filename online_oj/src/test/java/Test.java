import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * Created with IntelliJ IDEA.
 * Description:
 * User: 强仔
 * Date: 2022-09-22
 * Time: 16:59
 */
public class Test {
    private static ObjectMapper mapper = new ObjectMapper();

    public static void main(String[] args) throws JsonProcessingException {
        String s = "public static void main(String[] args) {\n" +
                "        int allcase = 10;\n" +
                "        int pass = 0;\n" +
                "\n" +
                "        Solution solution = new Solution();\n" +
                "        int[] nums = {121, -121, 10, 66, 4948, 564, 36, -686, 43234, 1};\n" +
                "        boolean[] answers = {true, false, false, true, false, false, false, false, true, true};\n" +
                "\n" +
                "        int num = 0;\n" +
                "        boolean answer = true;\n" +
                "        boolean res = false;\n" +
                "        try {\n" +
                "            for (int i = 0; i < allcase; i++) {\n" +
                "                num = nums[i];\n" +
                "                answer = answers[i];\n" +
                "                res = solution.isPalindrome(num);\n" +
                "\n" +
                "                if(res != answer){\n" +
                "                    return;\n" +
                "                }\n" +
                "\n" +
                "                pass++;\n" +
                "            }\n" +
                "        } finally {\n" +
                "            if (pass == allcase) {\n" +
                "                System.out.print(\"success\");\n" +
                "            } else {\n" +
                "                String error = \"finally;\" + pass + \";\" + num + \";\" + res + \";\" + answer;\n" +
                "                System.out.print(error);\n" +
                "            }\n" +
                "        }\n" +
                "    }";

        String body = mapper.writeValueAsString(s);
        System.out.println(body);

//        Scanner scanner = new Scanner(System.in);
//        while (scanner.hasNext()) {
//            String word1 = scanner.next();
//            String word2 = scanner.next();
//            System.out.println(minDistance(word1, word2));
//        }
    }


    public static int minDistance(String word1, String word2) {
        int n = word1.length();
        int m = word2.length();

        // 有一个字符串为空串
        if (n * m == 0) {
            return n + m;
        }

        // DP 数组
        int[][] D = new int[n + 1][m + 1];

        // 边界状态初始化
        for (int i = 0; i < n + 1; i++) {
            D[i][0] = i;
        }
        for (int j = 0; j < m + 1; j++) {
            D[0][j] = j;
        }

        // 计算所有 DP 值
        for (int i = 1; i < n + 1; i++) {
            for (int j = 1; j < m + 1; j++) {
                int left = D[i - 1][j] + 1;
                int down = D[i][j - 1] + 1;
                int left_down = D[i - 1][j - 1];
                if (word1.charAt(i - 1) != word2.charAt(j - 1)) {
                    left_down += 1;
                }
                D[i][j] = Math.min(left, Math.min(down, left_down));
            }
        }
        return D[n][m];
    }
}







