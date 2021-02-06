/*
 * Method02，使用同步块+wait
 * 执行子线程，main线程先于子线程获得子线程对象锁，
 * main线程调用子线程wait，阻塞了main线程，
 * 子线程获得了this的锁，子线程执行完，发出notifyAll，
 * main线程重新获得子线程的对象锁
 */
public class Method02 {
    public static String ThreadMsg = "";

    public static void main(String[] args) {
        System.out.println("Hello World!");

        Thread t = new Thread("Method02_Thread") {
            @Override
            public void run() {
                synchronized (this) {
                    ThreadMsg = "msg from : " + this.getName();
                }
            }
        };
        t.start();

        synchronized (t) {
            try {
                t.wait();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        System.out.println(ThreadMsg);
    }
}
