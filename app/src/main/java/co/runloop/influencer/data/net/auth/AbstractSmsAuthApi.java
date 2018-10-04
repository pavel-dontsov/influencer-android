package co.runloop.influencer.data.net.auth;

public abstract class AbstractSmsAuthApi implements ISmsAuthApi {

    private SmsVerificationListener verificationListener;

    public AbstractSmsAuthApi() {

    }

    public abstract void requestSms(String phoneNumber);

    public abstract void submitConfirmCode(String code);

    @Override
    public void registerVerificationListener(SmsVerificationListener listener) {
        if (listener == null) {
            throw new IllegalArgumentException("Listener cannot be null!");
        }
        verificationListener = listener;
    }

    @Override
    public void unregisterVerificationListener(SmsVerificationListener listener) {
        verificationListener = null;
    }

    public SmsVerificationListener getVerificationListener() {
        return verificationListener;
    }
}
