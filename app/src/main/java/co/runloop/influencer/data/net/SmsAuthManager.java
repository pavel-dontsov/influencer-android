package co.runloop.influencer.data.net;

import android.telephony.PhoneNumberUtils;

import com.sinch.verification.Config;
import com.sinch.verification.InitiationResult;
import com.sinch.verification.SinchVerification;
import com.sinch.verification.Verification;
import com.sinch.verification.VerificationListener;

import java.util.ArrayList;
import java.util.List;

import co.runloop.influencer.App;

public class SmsAuthManager {

    private static final String API_KEY = "efab3591-e09f-4d37-a861-062925571d71";

    private List<VerificationListener> listeners;
    private VerificationListener verificationListener;
    private Config config;
    private Verification curVerification;

    public SmsAuthManager() {
        listeners = new ArrayList<>();
        config = SinchVerification.config()
                .context(App.get())
                .applicationKey(API_KEY)
                .build();
        verificationListener = new VerificationListener() {
            @Override
            public void onInitiated(InitiationResult initiationResult) {
                for (VerificationListener listener : listeners) {
                    listener.onInitiated(initiationResult);
                }
            }

            @Override
            public void onInitiationFailed(Exception e) {
                for (VerificationListener listener : listeners) {
                    listener.onInitiationFailed(e);
                }
            }

            @Override
            public void onVerified() {
                for (VerificationListener listener : listeners) {
                    listener.onVerified();
                }
            }

            @Override
            public void onVerificationFailed(Exception e) {
                for (VerificationListener listener : listeners) {
                    listener.onVerificationFailed(e);
                }
            }

            @Override
            public void onVerificationFallback() {
                for (VerificationListener listener : listeners) {
                    listener.onVerificationFallback();
                }
            }
        };
    }

    public void smsVerification(String phoneNumber) {
        curVerification = SinchVerification.createSmsVerification(
                config,
                phoneNumber,
                verificationListener);
        curVerification.initiate();
    }

    public void flashCallVerification(String phoneNumber) {
        curVerification = SinchVerification.createFlashCallVerification(
                config,
                phoneNumber,
                verificationListener);
        curVerification.initiate();
    }

    public void submitConfirmCode(String code) {
        curVerification.verify(code);
    }

    public void registerVerificationListener(VerificationListener listener) {
        if (listener == null) {
            throw new IllegalArgumentException("Listener cannot be null!");
        }
        listeners.add(listener);
    }

    public void unregisterVerificationListener(VerificationListener listener) {
        listeners.remove(listener);
    }
}
