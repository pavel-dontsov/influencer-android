package co.runloop.influencer.data.net.auth;

public interface SmsVerificationListener {

    void onVerificationCompleted();

    void onVerificationFailed(Exception ex);

    void onCodeSent();

    void onCodeSentFailed(Exception ex);
}
