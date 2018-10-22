package co.runloop.influencer.data.net.auth;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

public interface ISmsAuthApi {

    void requestSms(@NonNull String phoneNumber);

    void submitConfirmCode(@Nullable String code);

    void registerVerificationListener(@NonNull SmsVerificationListener listener);

    void unregisterVerificationListener(@NonNull SmsVerificationListener listener);
}
