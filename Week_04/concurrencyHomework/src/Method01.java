/*
* Method01，使用join
* main线程调用子线程join，阻塞了main线程，等待子线程执行完毕
*/
public class Method01 {
    public static String ThreadMsg = "";

    public static void main(String[] args) {
        System.out.println("Hello World!");

        Thread t = new Thread("Method01_Thread") {
            @Override
            public void run() {
                ThreadMsg = "msg from : " + this.getName();
            }
        };
        t.start();

        try {
            t.join();
        } catch (Exception e) {
            e.printStackTrace();
        }

        System.out.println(ThreadMsg);
    }
}
/*
    public static String ThreadMsg = "";

    public static void main(String[] args) {
        System.out.println("Hello World!");
    }
*/