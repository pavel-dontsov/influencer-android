package co.runloop.influencer.fragment;

import android.arch.lifecycle.ViewModelProviders;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import co.runloop.influencer.R;
import co.runloop.influencer.viewmodel.SmsAuthViewModel;

public class SmsAuthFragment extends BaseFragment {

    private EditText phoneNumerEt;
    private Button sumbitPhoneNumberBtn;
    private EditText codeEt;
    private Button submitCodeBtn;

    private SmsAuthViewModel smsAuthViewModel;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        smsAuthViewModel = ViewModelProviders
                .of(this)
                .get(SmsAuthViewModel.class);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.frag_sms_auth, container, false);

        phoneNumerEt = root.findViewById(R.id.frag_sms_auth_phone_number_et);
        sumbitPhoneNumberBtn = root.findViewById(R.id.frag_sms_auth_submit_phone_btn);
        sumbitPhoneNumberBtn.setOnClickListener(view -> {
            smsAuthViewModel.submitPhoneNumber(phoneNumerEt.getText().toString().trim());
        });

        codeEt = root.findViewById(R.id.frag_sms_auth_sms_code_et);
        submitCodeBtn = root.findViewById(R.id.frag_sms_auth_submit_code_btn);
        submitCodeBtn.setOnClickListener(view -> {
            smsAuthViewModel.submitConfirmCode(codeEt.getText().toString().trim());
        });

        return root;
    }
}
