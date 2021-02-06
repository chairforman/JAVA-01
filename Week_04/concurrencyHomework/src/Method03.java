import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.FutureTask;

/*
* Method03，使用FutureTask
*/
public class Method03 {
    public static void main(String[] args) {
        System.out.println("Hello World!");

        CallableDemo callableDemo = new CallableDemo();
        FutureTask<String> task = new FutureTask<String>(callableDemo);
        Thread t = new Thread(task, "sonThread");
        t.start();
        try {
            System.out.println(task.get());
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
    }
}

class CallableDemo implements Callable<String> {
    @Override
    public String call() throws Exception {
        return Thread.currentThread().getName() + "----msg from : CallableDemo";
    }
}