package gold.context;

public class BaseContext {

    public static ThreadLocal<Long> threadLocal = new ThreadLocal<>();
    public static void setCurrentId(Long id) {
        threadLocal.set(id);
    }
    public static Long getCurrentId() {
        return threadLocal.get();
    }
    public static void removeCurrentId() {
        threadLocal.remove();
    }


    public static ThreadLocal<String> threadLocalString = new ThreadLocal<>();
    public static void setCurrentEmail(String email) {
        threadLocalString.set(email);
    }
    public static String getCurrentEmail() {
        return threadLocalString.get();
    }

}
