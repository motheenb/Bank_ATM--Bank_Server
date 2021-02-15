import java.util.concurrent.*;

/**
 * @author Motheen Baig
 */
public class ThreadManager {

    private ExecutorService executorService;

    private static ThreadManager threadManager = null;

    private ThreadManager() {
        executorService = Executors.newCachedThreadPool();
    }

    public static ThreadManager get() {
        if (threadManager == null) {
            threadManager = new ThreadManager();
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
