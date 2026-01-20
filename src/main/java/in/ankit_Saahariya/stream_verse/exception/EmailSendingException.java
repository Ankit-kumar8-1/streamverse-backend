package in.ankit_Saahariya.stream_verse.exception;

public class EmailSendingException extends RuntimeException{
    public EmailSendingException(String message,Throwable cause){
        super(message,cause);
    }
}
