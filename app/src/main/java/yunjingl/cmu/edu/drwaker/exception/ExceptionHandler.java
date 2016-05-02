package yunjingl.cmu.edu.drwaker.exception;

/**
 * handle exception thrown.
 */
public class ExceptionHandler {
    public void handleNullDatabase(int errNo){
        System.out.println("Error" + errNo + ": Cannot get database.");
    }

}
