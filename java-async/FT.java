import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

public class FT {
    /*
        Notes :
        1. Future is an object that will be filled eventually.
        2. Future get() method is blocking and blocks main thread. // important drawback
        3. because we fall again into blocking mode, this may not scale well (Use CompletableFuture instead).

     */

    static ExecutorService executorService = Executors.newFixedThreadPool(3);
    public static void main(String[] args) throws InterruptedException, ExecutionException {
        int limit = 10;
        long start = System.currentTimeMillis();
        for (int i = 0; i < limit; i++) {
            doSynchrous();
        }
        System.out.println("Synchronous task " + (System.currentTimeMillis() - start) / 1000 + " sec");

        start = System.currentTimeMillis();
        for (int i = 0; i < limit; i++) {
            doAsynchrouns();
        }
        executorService.shutdown();
        System.out.println("Asynchronous task " + (System.currentTimeMillis() - start) / 1000 + " sec");
    }

    public static void doSynchrous() throws InterruptedException {
        boolean didGetSomething = getSomething();
        boolean didGetAnotherThing = getAnotherThing();

        // some operation based on those two values
    }

    public static void doAsynchrouns() throws ExecutionException, InterruptedException {
        Future<Boolean> futureSomething = executorService.submit(FT::getSomething);
        Future<Boolean> futureAnotherThing = executorService.submit(FT::getAnotherThing);

        boolean didGetSomething = futureSomething.get();
        boolean didGetAnotherThing = futureAnotherThing.get();

        // some operation based on those two values
    }

    public static boolean getSomething() throws InterruptedException {
        // consider this as an api call
        Thread.sleep(1000);
        return true;
    }


    public static boolean getAnotherThing() throws InterruptedException {
        // consider this as an api call
        Thread.sleep(500);
        return true;
    }
}

