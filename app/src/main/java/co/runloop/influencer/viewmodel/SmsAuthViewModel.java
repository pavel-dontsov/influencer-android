package co.runloop.influencer.viewmodel;

import android.arch.lifecycle.ViewModel;

import com.sinch.verification.VerificationListener;

import co.runloop.influencer.data.net.SmsAuthManager;

public class SmsAuthViewModel extends ViewModel {

    private SmsAuthManager smsAuthManager;
    private VerificationListener verificationListener;

    public SmsAuthViewModel() {
        smsAuthManager = new SmsAuthManager();
    }

    public void submitPhoneNumber(String phoneNumber) {
        smsAuthManager.smsVerification(phoneNumber);
    }

    public void submitConfirmCode(String code) {
        smsAuthManager.submitConfirmCode(code);
    }

    public void registerVerificationListener(VerificationListener listener) {
        this.verificationListener = listener;
        smsAuthManager.registerVerificationListener(listener);
    }

    @Override
    protected void onCleared() {
        smsAuthManager.unregisterVerificationListener(verificationListener);
    }
}
