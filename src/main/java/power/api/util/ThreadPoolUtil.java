package power.api.util;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ThreadPoolUtil {
    // 创建一个当前cpu可运行的最大线程
    private static ExecutorService fixedThreadPool = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors());

    static void execute(Runnable runnable) {
        fixedThreadPool.execute(runnable);
    }
}
