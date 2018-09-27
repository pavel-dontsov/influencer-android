package co.runloop.influencer.fragment;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import co.runloop.influencer.R;
import co.runloop.influencer.adapter.ContactsAdapter;
import co.runloop.influencer.data.ContactsProvider;

public class ContactsFragment extends BaseFragment {

    private static final String TAG = ContactsFragment.class.getSimpleName();

    private static final int READ_CONTACT_PERM_RC = 101;

    public static ContactsFragment newInstance() {
        Bundle args = new Bundle();
        ContactsFragment fragment = new ContactsFragment();
        fragment.setArguments(args);

        return fragment;
    }

    private RecyclerView recyclerView;
    private ContactsAdapter adapter;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        adapter = new ContactsAdapter();
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
        recyclerView.setAdapter(adapter);

        if (contactsIsAvailable()) {
            loadContacts();
        }

        return root;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            loadContacts();
        } else {
            requestReadContactsPermission();
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        if (!contactsIsAvailable()) {
            requestReadContactsPermission();
        } else {
            loadContacts();
        }
    }

    private void loadContacts() {
        ContactsProvider contactsProvider = new ContactsProvider(getActivity());
        adapter.setContacts(contactsProvider.getAll());
        adapter.notifyDataSetChanged();
    }

    private boolean contactsIsAvailable() {
        return PackageManager.PERMISSION_GRANTED == ContextCompat.checkSelfPermission(
                getActivity(),
                Manifest.permission.READ_CONTACTS);
    }

    private void requestReadContactsPermission() {
        requestPermissions(new String[]{Manifest.permission.READ_CONTACTS}, READ_CONTACT_PERM_RC);
    }
}
