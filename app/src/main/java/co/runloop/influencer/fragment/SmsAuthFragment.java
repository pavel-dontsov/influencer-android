package co.runloop.influencer.fragment;

import android.Manifest;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.telephony.TelephonyManager;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import com.sinch.verification.CodeInterceptionException;
import com.sinch.verification.IncorrectCodeException;
import com.sinch.verification.InitiationResult;
import com.sinch.verification.InvalidInputException;
import com.sinch.verification.PhoneNumberFormattingTextWatcher;
import com.sinch.verification.PhoneNumberUtils;
import com.sinch.verification.ServiceErrorException;
import com.sinch.verification.VerificationListener;

import java.util.Locale;

import co.runloop.influencer.App;
import co.runloop.influencer.R;
import co.runloop.influencer.data.net.auth.SmsAuthInvalidCodeException;
import co.runloop.influencer.data.net.auth.SmsAuthInvalidPhoneNumberException;
import co.runloop.influencer.data.net.auth.SmsAuthServiceException;
import co.runloop.influencer.data.net.auth.SmsVerificationListener;
import co.runloop.influencer.viewmodel.SmsAuthViewModel;

public class SmsAuthFragment extends BaseFragment {

    private static final String TAG = SmsAuthFragment.class.getSimpleName();

    private static final int READ_PHONE_STATE_PERM_RC = 1;

    public static SmsAuthFragment newInstance() {

        Bundle args = new Bundle();

        SmsAuthFragment fragment = new SmsAuthFragment();
        fragment.setArguments(args);
        return fragment;
    }

    private EditText phoneNumberEt;
    private Button submitPhoneNumberBtn;
    private EditText codeEt;
    private Button submitCodeBtn;

    private SmsAuthViewModel smsAuthViewModel;

    private Locale defLocale;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        defLocale = new Locale("", "IL");
        smsAuthViewModel = ViewModelProviders
                .of(this)
                .get(SmsAuthViewModel.class);
        smsAuthViewModel.registerVerificationListener(new SmsVerificationListener() {
            @Override
            public void onVerificationCompleted() {
            }

            @Override
            public void onVerificationFailed(Exception ex) {
            }

            @Override
            public void onCodeSent() {
                codeEt.setVisibility(View.VISIBLE);
                submitCodeBtn.setVisibility(View.VISIBLE);
            }

            @Override
            public void onCodeSentFailed(Exception ex) {
                if (ex instanceof SmsAuthInvalidPhoneNumberException) {
                    // Invalid phone number
                } else if (ex instanceof SmsAuthInvalidCodeException) {
                    // Wrong code
                } else if (ex instanceof SmsAuthServiceException) {
                    // Sinch service error
                } else {
                    //Some other problem, like UnknownHostException
                }
            }
        });
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.frag_sms_auth, container, false);

        phoneNumberEt = root.findViewById(R.id.frag_sms_auth_phone_number_et);
        TextWatcher watcher = new PhoneNumberFormattingTextWatcher(defLocale.getCountry()) {
            @Override
            public synchronized void afterTextChanged(Editable s) {
                super.afterTextChanged(s);

                if (PhoneNumberUtils.isPossibleNumber(getPreFormatedNumber(), defLocale.getCountry())) {
                    submitPhoneNumberBtn.setEnabled(true);
                    phoneNumberEt.setTextColor(Color.BLACK);
                } else {
                    submitPhoneNumberBtn.setEnabled(false);
                    phoneNumberEt.setTextColor(Color.RED);
                }
            }
        };
        phoneNumberEt.addTextChangedListener(watcher);

        submitPhoneNumberBtn = root.findViewById(R.id.frag_sms_auth_submit_phone_btn);
        submitPhoneNumberBtn.setOnClickListener(view -> {
            String number = PhoneNumberUtils.formatNumberToE164(getPreFormatedNumber(), defLocale.getCountry());
            smsAuthViewModel.submitPhoneNumber(number);
        });

        codeEt = root.findViewById(R.id.frag_sms_auth_sms_code_et);
        submitCodeBtn = root.findViewById(R.id.frag_sms_auth_submit_code_btn);
        submitCodeBtn.setOnClickListener(view -> {
            smsAuthViewModel.submitConfirmCode(codeEt.getText().toString().trim());
        });

        if (ContextCompat.checkSelfPermission(App.get().getApplicationContext(),
                Manifest.permission.READ_PHONE_STATE) == PackageManager.PERMISSION_DENIED) {
            requestPermissions(new String[]{Manifest.permission.READ_PHONE_STATE}, READ_PHONE_STATE_PERM_RC);
        } else {
            retrievePhoneNumber();
        }

        return root;
    }

    @SuppressWarnings({"MissingPermission", "HardwareIds"})
    private void retrievePhoneNumber() {
        TelephonyManager telephonyManager = (TelephonyManager)
                App.get().getApplicationContext().getSystemService(Context.TELEPHONY_SERVICE);
        phoneNumberEt.setText(telephonyManager.getLine1Number());
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == READ_PHONE_STATE_PERM_RC) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                retrievePhoneNumber();
            }
        }
    }

    private String getPreFormatedNumber() {
        String number = phoneNumberEt.getText().toString();
        if (!number.startsWith("+")) {
            number = "+" + number;
        }
        return number;
    }
}
