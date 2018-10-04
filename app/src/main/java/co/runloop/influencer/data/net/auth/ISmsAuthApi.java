package co.runloop.influencer.data.net.auth;

public interface ISmsAuthApi {

    void requestSms(String phoneNumber);

    void submitConfirmCode(String code);

    void registerVerificationListener(SmsVerificationListener listener);

    void unregisterVerificationListener(SmsVerificationListener listener);
}
