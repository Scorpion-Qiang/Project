package search;

import java.io.*;
import java.util.ArrayList;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Created with IntelliJ IDEA.
 * Description:
 * User: 强仔
 * Date: 2022-07-11
 * Time: 14:50
 */
public class Parser {
    // 指定一个加载文档的路径
    private static final String INPUT_PATH = "C:/Users/86188/Desktop/docs/api";

    // search.Index 实现索引的数据结构
    private Index index = new Index();

    // run() 整个 search.Parser 类的入口
    public void run(){
        System.out.println("开始制作索引!!");
        long begin = System.currentTimeMillis();

        // TODO 1. 根据上面指定的路径，枚举出路径中所有的文档（html），这个过程需要把所有子目录中的文档都能获取到
        ArrayList<File> fileList = new ArrayList<File>();
        enumFile(INPUT_PATH, fileList);

        long endEnumFile = System.currentTimeMillis();
        System.out.println("获取文件消耗时间: " + (endEnumFile - begin) + "ms");

        // TODO 2. 针对上面罗列出的文件的路径，打开文件，读取文件内容，进行解析，构建索引
        for (File f : fileList) {
            parseHtml(f);
        }

        long endParseHtml = System.currentTimeMillis();
        System.out.println("解析文件，构建索引消耗时间: " + (endParseHtml - endEnumFile) + "ms");

        // TODO 3.把在内存中构建好的索引的数据结构，保存到文件中
        index.save();

        long end = System.currentTimeMillis();
        System.out.println("保存文件消耗时间: " + (end - endParseHtml) + "ms");
        System.out.println("索引制作完毕!! 消耗时间: " + (end - begin) + "ms");


    }

    public void runByThread(){
        System.out.println("索引制作开始");
        long begin = System.currentTimeMillis();

        // 1. 枚举所有的文件
        ArrayList<File> fileList = new ArrayList<File>();
        enumFile(INPUT_PATH, fileList);

        // 2. 解析文件，制作索引结构
        // 引入线程池

        // 设置 "裁判" ，明确 "运动员" 数量
        final CountDownLatch latch = new CountDownLatch(fileList.size());

        ExecutorService service = Executors.newFixedThreadPool(4);
        for (final File f : fileList) {
            // 将任务提交到线程池中
            service.submit(new Runnable() {
                @Override
                public void run() {
                    System.out.println("开始解析" + f.getAbsolutePath());
                    parseHtml(f);
                    // 任务完成  "撞线"
                    latch.countDown();
                }
            });
        }

        // "裁判" 死等，等所有的 "运动员" "撞线"，才保存索引
        try {
            latch.await();

            // 将线程池中的线程全部干掉
            service.shutdown();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        // 必须要等到所有线程把所有文档任务处理完，才能保存索引
        // 3. 保存索引
        index.save();

        long end = System.currentTimeMillis();
        System.out.println("索引制作结束，消耗时间: " + (end - begin) + "ms");


    }

    private void parseHtml(File f) {
        System.out.println("开始解析: " + f.getAbsolutePath());
        // 1. 解析 HTML 文件中的标题
        String title = parseTitle(f);
        // 2. 解析 HTML 文件中的URL
        String url = parseUrl(f);

        // 3. 解析 HTML 文件中的正文
        String content = parseContentByRegex(f);
        // 4. TODO 把解析出来的这些信息，加入到索引当中
        index.addDoc(title, url, content);
    }




    // 根据正则表达式解析正文
    public String parseContentByRegex(File f){
        String content = readFile(f);
        // 去除 <script></script> 包裹的内容
        content = content.replaceAll("<script.*?>(.*?)</script>", " ");
        // 去除 普通的 html 标签
        content = content.replaceAll("<.*?>", " ");
        // 把多个空格合成一个空格
        content = content.replaceAll("\\s+", " ");
        return content;
    }
    // 将文件的内容原封不动的读到字符串中
    public String readFile(File f){
        try(BufferedReader reader = new BufferedReader(new FileReader(f))){
            StringBuilder content = new StringBuilder();
            while(true){
                int ret = reader.read();
                if(ret == -1){
                    break;
                }
                char ch = (char)ret;
                if(ch == '\n' || ch == '\r'){
                    ch = ' ';
                }
                content.append(ch);
            }

            return content.toString();
        } catch(IOException e){
            e.printStackTrace();
        }
        return "";
    }


    // 手动解析正文
    public String parseContent(File f) {
        // 按照一个字符一个字符来读取，< 和 > 作为 拷贝数据的开关（isCopy）
        // < --- isCopy 为 false，暂停数据的拷贝；
        // > --- isCopy 为 true，恢复数据的拷贝
        try (BufferedReader buffered = new BufferedReader(new FileReader(f), 1021 * 1024)){
            // BufferedReader 设置缓冲区，将一部分内容预读到内存中，减少首次读取文件时访问磁盘的次数

            // 用于保存结果
            StringBuilder content = new StringBuilder();
            // 拷贝数据的开关
            boolean isCopy = true;

            while (true) {

                // read() 一次返回一个字符，返回值是 int 类型，如果返回为 -1，表示读取到文件结尾了
                int ret = buffered.read();
                // 读到文件末尾
                if(ret == -1){
                    break;
                }
                // 如果返回的不是 -1，就是一个合法字符了
                char ch = (char)ret;
                if(isCopy){
                    if(ch == '<'){
                        isCopy = false;
                        continue;
                    }
                    if(ch == '\n' || ch == '\r'){
                        // 将换行替换为空格
                        ch = ' ';
                    }
                    content.append(ch);
                }else {
                    if(ch == '>'){
                        isCopy = true;
                    }
                }
            }
            return content.toString();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return "";
    }

    private String parseUrl(File f) {
        String part1 = "https://docs.oracle.com/javase/8/docs/api/";
        // substring(int beginIndex) 截取 [beginIndex, s.length())
        String part2 = f.getAbsolutePath().substring(INPUT_PATH.length());
        String path = part1 + part2;
        return path;
    }

    private String parseTitle(File f) {
        String name = f.getName();
        return name.substring(0, name.length() - ".html".length());
        // return name.substring(0, name.indexOf('.'));
    }

    // 通过递归获取到指定路径的所有文档
    // 第一个参数表示 从哪个目录开始递归遍历
    // 第二个参数表示 递归得到的结果
    private void enumFile(String inputPath, ArrayList<File> fileList) {
        File rootPath = new File(inputPath);
        // listFiles() 可以得到 rootPath 目录下的所有的目录/文件，只能看到一级目录，看不到子目录中的目录/文件
        File[] files = rootPath.listFiles();
        for (File f : files) {
            // 根据 f 类型，判断是否需要递归
            // 如果 f 是目录，递归，获取此目录下的所有目录/文件
            // 如果 f 是普通文件，直接添加到 fileList 中
            if(f.isDirectory()){
                enumFile(f.getAbsolutePath(), fileList);
            } else {
                // 排除非 html 文件
                if(f.getAbsolutePath().endsWith(".html")) {
                    fileList.add(f);
                }
            }
        }
    }

    public static void main(String[] args) {
        // 通过 main 方法实现制作索引的过程
        Parser parser = new Parser();
        parser.runByThread();
    }
}
