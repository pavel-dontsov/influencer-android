package co.runloop.influencer.data.net.auth;

import android.support.annotation.NonNull;

public abstract class AbstractSmsAuthApi implements ISmsAuthApi {

    private SmsVerificationListener verificationListener;

    @Override
    public void registerVerificationListener(@NonNull SmsVerificationListener listener) {
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
