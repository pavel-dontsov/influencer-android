package co.runloop.influencer.viewmodel;

import android.arch.lifecycle.ViewModel;
import android.text.TextUtils;

import com.sinch.verification.VerificationListener;

import co.runloop.influencer.data.net.auth.FirebaseSmsAuth;
import co.runloop.influencer.data.net.auth.ISmsAuthApi;
import co.runloop.influencer.data.net.auth.SinchSmsAuth;
import co.runloop.influencer.data.net.auth.SmsVerificationListener;

public class SmsAuthViewModel extends ViewModel {

    private ISmsAuthApi smsAuthApi;
    private SmsVerificationListener verificationListener;

    public SmsAuthViewModel() {
        smsAuthApi = new SinchSmsAuth();
    }

    public void submitPhoneNumber(String phoneNumber) {
        smsAuthApi.requestSms(phoneNumber);
    }

    public void submitConfirmCode(String code) {
        smsAuthApi.submitConfirmCode(code);
    }

    public void registerVerificationListener(SmsVerificationListener listener) {
        this.verificationListener = listener;
        smsAuthApi.registerVerificationListener(listener);
    }

    @Override
    protected void onCleared() {
        smsAuthApi.unregisterVerificationListener(verificationListener);
    }
}
