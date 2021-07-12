package com.threadBasic;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * @created by: IntelliJ IDEA
 * @author: kevin
 * @e-mail: drzhong2015@gmail.com
 * @dateTime: 2021-07-12 22:17
 * @description:
 */
public class ThreadPool {
    public static void main(String[] args) {
        //ExecutorService executorService = Executors.newFixedThreadPool(10);
        ThreadPoolExecutor executorService = (ThreadPoolExecutor) Executors.newFixedThreadPool(10);
        //设置连接池属性
        //executorService.setCorePoolSize(15);
        //executorService.setKeepAliveTime(600, TimeUnit.SECONDS);
        //executorService.submit();// 适合使用于Callable
        executorService.execute(new NumberThread());
        executorService.execute(new NumberThread1());
        executorService.shutdown();
        //while (true) {
        //    if (executorService.isTerminated()) {
        //        System.out.println("done");
        //        break;
        //    }
        //}

    }
}
class NumberThread implements Runnable {
    @Override
    public void run() {
        for (int i = 0; i <= 1000; i++) {
            if (i % 2 == 0) {
                System.out.println(Thread.currentThread().getName() + " : " + i);
            }
        }
    }
}
class NumberThread1 implements Runnable {
    @Override
    public void run() {
        for (int i = 0; i <= 1000; i++) {
            if (i % 2 != 0) {
                System.out.println(Thread.currentThread().getName() + " : " + i);
            }
        }
    }
}
