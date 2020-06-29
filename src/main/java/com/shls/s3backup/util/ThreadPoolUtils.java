package com.shls.s3backup.util;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * @author ：Killer
 * @date ：Created in 20-6-15 下午4:16
 * @description：${description}
 * @modified By：
 * @version: version
 */
public class ThreadPoolUtils {
    public static ExecutorService executorService = Executors.newCachedThreadPool();

    public static void execute(Runnable runnable) {
        executorService.execute(runnable);
    }
}

