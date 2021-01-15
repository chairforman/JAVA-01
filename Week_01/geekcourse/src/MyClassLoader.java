import java.io.FileInputStream;
import java.io.InputStream;
import java.lang.reflect.Method;

public class MyClassLoader extends ClassLoader {

    private static MyClassLoader myClassLoader;
    private static final String CLASSNAME = "Hello";
    private static final String METHODNAME = "hello";
    private static final String XLASSFILEPATH = "filesforhomework/Hello.xlass";

    public static void main(String[] args) {
        myClassLoader = new MyClassLoader();
        try {
            Class<?> mClass = myClassLoader.findClass(CLASSNAME);
            Object mObj = mClass.newInstance();
            Method mMethod = mClass.getMethod(METHODNAME);
            mMethod.invoke(mObj);
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    @Override
    protected Class<?> findClass(String name) throws ClassNotFoundException {
        byte[] bytes = myClassLoader.readXlassFile();
        return defineClass(name, bytes, 0, bytes.length);
    }

    private byte[] readXlassFile() {
        byte[] retBytes = null;

        try {
            InputStream is = new FileInputStream(XLASSFILEPATH);
            int fileSize = is.available();
            retBytes = new byte[fileSize];
            is.read(retBytes);
            is.close();

            for (int i = 0; i < fileSize; i++) {
                retBytes[i] = (byte)(255 - retBytes[i]);
            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }

        return retBytes;
    }
}
