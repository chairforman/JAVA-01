import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/*
* Method04，使用Condition
* 主线程等待子线程调用signal后，主线程才继续执行
*/
public class Method04 {
    public static String ThreadMsg = "";
    public static Lock lock = new ReentrantLock();
    public static Condition condition = lock.newCondition();

    public static void main(String[] args) {
        System.out.println("Hello World!");

        Thread t = new Thread("conditionDemo") {
            @Override
            public void run() {
                lock.lock();
                ThreadMsg = "msg from : " + Thread.currentThread().getName();
                try {
                    condition.signal();
                } catch (Exception e) {
                    e.printStackTrace();
                } finally {
                    lock.unlock();
                }
            }
        };
        t.start();

        lock.lock();
        try {
            condition.await();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            lock.unlock();
        }
        System.out.println(ThreadMsg);
    }
}
