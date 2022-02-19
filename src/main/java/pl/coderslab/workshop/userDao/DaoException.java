package pl.coderslab.workshop.userDao;

public class DaoException extends RuntimeException{
    public DaoException (String msg, Exception cause){super(msg, cause);}
}
