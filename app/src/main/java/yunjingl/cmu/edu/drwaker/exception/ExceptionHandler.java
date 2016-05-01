package yunjingl.cmu.edu.drwaker.exception;

/**
 * Created by yapeng on 4/30/2016.
 */
public class ExceptionHandler {
    public void handleNullDatabase(int errNo){
        System.out.println("Error" + errNo + ": Cannot get database.");
    }
//    public void handleNullInsertion(int errNo){
//        System.out.println("Error" + errNo + ": Cannot get data from input.");
//    }
}
