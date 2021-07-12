package uk.getvector.examples;

import org.apache.commons.lang3.RandomStringUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * @created by: IntelliJ IDEA
 * @author: kevin
 * @e-mail: drzhong2015@gmail.com
 * @dateTime: 2021-07-13 0:06
 * @description:
 */
public class CountDownLatchTest {
    // 计数器
    private CountDownLatch threadsSignal;
    //每个线程处理的数据量
    private static final int count = 1000;
    // 线程数
    private static final int NUMBER = 10;
    //定义线程池数量为8,每个线程处理1000条数据
    private static ExecutorService pool = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors());
    /**
     * 将一个list均分成n个list,主要通过偏移量来实现的
     *
     * @param source
     * @return
     */
    public static <T> List<List<T>> averageAssign(List<T> source, int n) {
        List<List<T>> result = new ArrayList<List<T>>();
        int remaider = source.size() % n;  //(先计算出余数)
        int number = source.size() / n;  //然后是商
        int offset = 0;//偏移量
        for (int i = 0; i < n; i++) {
            List<T> value = null;
            if (remaider > 0) {
                value = source.subList(i * number + offset, (i + 1) * number + offset + 1);
                remaider--;
                offset++;
            } else {
                value = source.subList(i * number + offset, (i + 1) * number + offset);
            }
            result.add(value);
        }
        return result;
    }

    public String multiThread() {

        long start = System.currentTimeMillis();
        long end;
        // 生成集合
        List<String> collect = Stream.generate(() -> RandomStringUtils.randomAlphanumeric(8)).limit(10 * 1024 * 1024).filter(string -> !string.isEmpty()).collect(Collectors.toList());
        threadsSignal = new CountDownLatch(NUMBER);
        List<List<String>> lists = averageAssign(collect, NUMBER);
        for (List<String> list : lists) {
            pool.submit(new printThread(threadsSignal, list));
        }


        try {
            threadsSignal.await();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        pool.shutdown();
        end = System.currentTimeMillis();
        System.out.println("time:" + (end - start) + "ms");
        return "result";
    }

    public static void main(String[] args) {
        System.out.println(new CountDownLatchTest().multiThread());
        //List<String> collect = Stream.generate(() -> RandomStringUtils.randomAlphanumeric(8)).limit(10 ).filter(string -> !string.isEmpty()).collect(Collectors.toList());
        //List<List<String>> lists = averageAssign(collect, 10);
        //System.out.println(lists);


    }
}
class printThread implements Runnable {

    CountDownLatch cdl;

    List<String> list;

    public printThread(CountDownLatch cdl, List<String> list) {
        this.cdl = cdl;
        this.list = list;
    }

    @Override
    public void run() {
        for (String string : list) {
            System.out.println(Thread.currentThread().getName() + " : " + string);
        }
        cdl.countDown();
    }
}
