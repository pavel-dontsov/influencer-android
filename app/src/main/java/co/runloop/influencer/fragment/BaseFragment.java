package co.runloop.influencer.fragment;

import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;

import co.runloop.influencer.activity.BaseActivity;

public abstract class BaseFragment extends Fragment {

    public boolean isActionBarAvailable() {
        return getBaseActivity().getSupportActionBar() != null;
    }

    public AppCompatActivity getAppCompatActivity() {
        return (AppCompatActivity) getActivity();
    }

    public BaseActivity getBaseActivity() {
        return (BaseActivity) getActivity();
    }
}
