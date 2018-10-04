package co.runloop.influencer.data.net.auth;

import android.os.Handler;
import android.os.Looper;
import android.support.annotation.NonNull;

import com.google.firebase.FirebaseException;
import com.google.firebase.FirebaseTooManyRequestsException;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;

import java.util.concurrent.Executor;
import java.util.concurrent.TimeUnit;


public class FirebaseSmsAuth extends AbstractSmsAuthApi {

    private static final String TAG = FirebaseSmsAuth.class.getSimpleName();

    private static final long AUTO_RETRIEVAL_TIMEOUT = 30;

    private PhoneAuthProvider smsApi;
    private PhoneAuthProvider.OnVerificationStateChangedCallbacks verificationCallback;
    private Executor executor;
    private FirebaseAuth auth;
    private Handler handler;

    private String phoneNumber;
    private String verificationId;
    private PhoneAuthProvider.ForceResendingToken resendingToken;

    public FirebaseSmsAuth() {
        smsApi = PhoneAuthProvider.getInstance();
        verificationCallback = new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
            @Override
            public void onVerificationCompleted(PhoneAuthCredential phoneAuthCredential) {
                // This callback will be invoked in two situations:
                // 1 - Instant verification. In some cases the phone number can be instantly
                //     verified without needing to send or enter a verification code.
                // 2 - Auto-retrieval. On some devices Google Play services can automatically
                //     detect the incoming verification SMS and perform verification without
                //     user action.
                signIn(phoneAuthCredential);
            }

            @Override
            public void onVerificationFailed(FirebaseException e) {
                // This callback is invoked in an invalid request for verification is made,
                // for instance if the the phone number format is not valid.
                if (getVerificationListener() != null) {
                    if (e instanceof FirebaseAuthInvalidCredentialsException) {
                        getVerificationListener().onCodeSentFailed(new SmsAuthInvalidPhoneNumberException());
                    } else if (e instanceof FirebaseTooManyRequestsException) {
                        getVerificationListener().onCodeSentFailed(new SmsAuthTooMuchTriesException());
                    } else {
                        getVerificationListener().onCodeSentFailed(new SmsAuthServiceException());
                    }
                }
            }

            @Override
            public void onCodeSent(String verificationId,
                                   PhoneAuthProvider.ForceResendingToken forceResendingToken) {
                FirebaseSmsAuth.this.verificationId = verificationId;
                FirebaseSmsAuth.this.resendingToken = forceResendingToken;
                if (getVerificationListener() != null) {
                    getVerificationListener().onCodeSent();
                }
            }

            @Override
            public void onCodeAutoRetrievalTimeOut(String s) {
            }
        };
        handler = new Handler(Looper.getMainLooper());
        executor = (Runnable command) -> {
            handler.post(command);
        };
        auth = FirebaseAuth.getInstance();
    }

    @Override
    public void requestSms(@NonNull String phoneNumber) {
        if (phoneNumber.equals(this.phoneNumber) && resendingToken != null) {
            smsApi.verifyPhoneNumber(phoneNumber,
                    AUTO_RETRIEVAL_TIMEOUT,
                    TimeUnit.SECONDS,
                    executor,
                    verificationCallback,
                    resendingToken);
        } else {
            this.phoneNumber = phoneNumber;
            smsApi.verifyPhoneNumber(phoneNumber,
                    AUTO_RETRIEVAL_TIMEOUT,
                    TimeUnit.SECONDS,
                    executor,
                    verificationCallback);
        }

    }

    @Override
    public void submitConfirmCode(@NonNull String code) {
        PhoneAuthCredential credential = PhoneAuthProvider.getCredential(verificationId, code);
        signIn(credential);
    }

    public void signIn(PhoneAuthCredential credential) {
        auth.signInWithCredential(credential).addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                getVerificationListener().onVerificationCompleted();
            } else {
                if (task.getException() instanceof FirebaseAuthInvalidCredentialsException) {
                    getVerificationListener().onVerificationFailed(new SmsAuthInvalidCodeException());
                } else {
                    getVerificationListener().onVerificationFailed(new SmsAuthServiceException());
                }
            }
        });
    }
}
