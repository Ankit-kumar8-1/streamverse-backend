package in.ankit_Saahariya.stream_verse.service;

public interface EmailService {

    void sendVerificationEmail(String toEmail,String token);

    void sendPasswordResetEmail(String toEmail,String token);
}
