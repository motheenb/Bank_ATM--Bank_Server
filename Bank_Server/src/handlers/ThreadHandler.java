package handlers;

import java.util.concurrent.*;

/**
 * @author Motheen Baig
 */
public class ThreadHandler {

    private ExecutorService executorService;

    private static ThreadHandler threadManager = null;

    private ThreadHandler() {
        executorService = Executors.newCachedThreadPool();
    }

    public static ThreadHandler get() {
        if (threadManager == null) {
            threadManager = new ThreadHandler();
        }
        return threadManager;
    }

    public void execute(final Runnable runnable) {
        executorService.execute(runnable);
    }

    public <T> Future<T> submit(final Callable callable) {
        return executorService.submit(callable);
    }

    public void shutdown() {
        executorService.shutdown();
        while (!executorService.isTerminated()) {
            try {
                TimeUnit.SECONDS.sleep(1);
            } catch (final InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    public ExecutorService getExecutorService() {
        return executorService;
    }

}
