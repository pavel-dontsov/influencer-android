package co.runloop.influencer.activity;

import android.app.Activity;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;

import co.runloop.influencer.R;
import co.runloop.influencer.fragment.ContactsFragment;
import co.runloop.influencer.fragment.SmsAuthFragment;

public class MainActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        FragmentManager fm = getSupportFragmentManager();
        Fragment fragment = fm.findFragmentById(R.id.act_main_frag_container);
        if (fragment == null) {
            fragment = SmsAuthFragment.newInstance();
            fm.beginTransaction()
                    .add(R.id.act_main_frag_container, fragment)
                    .commitAllowingStateLoss();
        }
    }

    @Override
    protected int getContentLayoutId() {
        return R.layout.act_main;
    }
}
