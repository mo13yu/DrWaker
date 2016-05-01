package yunjingl.cmu.edu.drwaker.exception;

/**
 * Created by yapeng on 4/30/2016.
 */
public class DatabaseException extends Exception implements Fix {
    private int errNo;
    private String errMess;

    public DatabaseException(int errNo) {
        this.errNo = errNo;
    }

    public String getErrMess() {
        return errMess;
    }

    public int getErrNo() {
        return errNo;
    }

    @Override
    public void fix(int errNo) {
        ExceptionHandler handler=new ExceptionHandler();
        switch(errNo){
            case 1:
                errMess="Cannot get writtable database";
                handler.handleNullDatabase(1);
                break;
//            case 2:
//                errMess="Cannot get input data";
//                handler.handleNullInsertion(2);
//                break;
        }
    }
}
