package exception;

public class MergerException  extends Exception{
    
    public MergerException () {

    }

    public MergerException (String message) {
        super (message);
    }

    public MergerException (Throwable cause) {
        super (cause);
    }

    public MergerException (String message, Throwable cause) {
        super (message, cause);
    }

}
