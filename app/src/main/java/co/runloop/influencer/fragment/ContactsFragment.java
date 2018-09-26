package co.runloop.influencer.fragment;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import co.runloop.influencer.R;

public class ContactsFragment extends BaseFragment {

    private static final String TAG = ContactsFragment.class.getSimpleName();

    public static ContactsFragment newInstance() {
        Bundle args = new Bundle();
        ContactsFragment fragment = new ContactsFragment();
        fragment.setArguments(args);

        return fragment;
    }

    private RecyclerView recyclerView;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getAppCompatActivity().getSupportActionBar().setTitle(R.string.contacts);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.frag_contacts_list, container, false);

        recyclerView = root.findViewById(R.id.frag_contacts_list_recyclerview);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        return root;
    }
}
