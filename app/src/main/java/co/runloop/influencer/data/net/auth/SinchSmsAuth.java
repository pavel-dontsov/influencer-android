package co.runloop.influencer.data.net.auth;

import com.sinch.verification.CodeInterceptionException;
import com.sinch.verification.Config;
import com.sinch.verification.IncorrectCodeException;
import com.sinch.verification.InitiationResult;
import com.sinch.verification.InvalidInputException;
import com.sinch.verification.ServiceErrorException;
import com.sinch.verification.SinchVerification;
import com.sinch.verification.Verification;
import com.sinch.verification.VerificationListener;

import co.runloop.influencer.App;

public class SinchSmsAuth extends AbstractSmsAuthApi {

    private static final String API_KEY = "23dbb4ac-996b-4caa-a27a-3ea215b2d0f1";

    private VerificationListener verificationListener;
    private Config config;

    private String curPhoneNumber;
    private Verification curVerification;

    public SinchSmsAuth() {
        config = SinchVerification.config()
                .context(App.get().getApplicationContext())
                .applicationKey(API_KEY)
                .build();
        verificationListener = new VerificationListener() {
            @Override
            public void onInitiated(InitiationResult initiationResult) {
                if (getVerificationListener() != null) {
                    getVerificationListener().onCodeSent();
                }
            }

            @Override
            public void onInitiationFailed(Exception e) {
                if (getVerificationListener() != null) {
                    if (e instanceof InvalidInputException) {
                        // Incorrect phone number
                        getVerificationListener().onCodeSentFailed(new SmsAuthInvalidPhoneNumberException());
                    } else if (e instanceof ServiceErrorException) {
                        // Sinch service error
                        getVerificationListener().onCodeSentFailed(new SmsAuthServiceException());
                    } else {
                        //Some other problem, like UnknownHostException
                    }
                }
            }

            @Override
            public void onVerified() {
                curVerification = null;
                curPhoneNumber = null;
                if (getVerificationListener() != null) {
                    getVerificationListener().onVerificationCompleted();
                }
            }

            @Override
            public void onVerificationFailed(Exception e) {
                if (getVerificationListener() != null) {
                    if (e instanceof InvalidInputException) {
                        // Invalid phone number
                        getVerificationListener().onVerificationFailed(new SmsAuthInvalidPhoneNumberException());
                    } else if (e instanceof IncorrectCodeException) {
                        // Wrong code
                        getVerificationListener().onVerificationFailed(new SmsAuthInvalidCodeException());
                    } else if (e instanceof CodeInterceptionException) {
                        // Auto code handle failed, code should be written by user
                    } else if (e instanceof ServiceErrorException) {
                        // Sinch service error
                        getVerificationListener().onVerificationFailed(new SmsAuthServiceException());
                    } else {
                        //Some other problem, like UnknownHostException
                        getVerificationListener().onVerificationFailed(new SmsAuthServiceException());
                    }
                }
            }

            @Override
            public void onVerificationFallback() {
            }
        };
    }

    @Override
    public void requestSms(String phoneNumber) {
        if (curVerification == null
                || !phoneNumber.equals(curPhoneNumber)) {
            curPhoneNumber = phoneNumber;
            curVerification = SinchVerification.createSmsVerification(
                    config,
                    phoneNumber,
                    verificationListener);
        }
        curVerification.initiate();
    }

    @Override
    public void submitConfirmCode(String code) {
        curVerification.verify(code);
    }
}
