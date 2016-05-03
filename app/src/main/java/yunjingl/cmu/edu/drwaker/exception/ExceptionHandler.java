package yunjingl.cmu.edu.drwaker.exception;

/**
 * Handle exception thrown.
 */
public class ExceptionHandler {
    public void handleNullDatabase(int errNo){
        System.out.println("Error" + errNo + ": Cannot get database.");
    }

}
