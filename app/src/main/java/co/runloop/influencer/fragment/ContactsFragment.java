package co.runloop.influencer.fragment;

import android.Manifest;
import android.arch.lifecycle.ViewModelProviders;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import java.util.List;

import co.runloop.influencer.R;
import co.runloop.influencer.adapter.ContactsAdapter;
import co.runloop.influencer.model.Contact;
import co.runloop.influencer.model.Resource;
import co.runloop.influencer.viewmodel.ContactsViewModel;

public class ContactsFragment extends BaseFragment {

    private static final String TAG = ContactsFragment.class.getSimpleName();

    private static final int READ_CONTACT_PERM_RC = 101;

    public static ContactsFragment newInstance() {
        return new ContactsFragment();
    }

    private RecyclerView recyclerView;
    private ProgressBar progressBar;

    private ContactsAdapter adapter;
    private ContactsViewModel contactsViewModel;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        adapter = new ContactsAdapter();
        if (isActionBarAvailable()) {
            getBaseActivity().getSupportActionBar().setTitle(R.string.contacts);
        }
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

        progressBar = root.findViewById(R.id.frag_contacts_list_progressbar);

        contactsViewModel = ViewModelProviders
                .of(this)
                .get(ContactsViewModel.class);
        contactsViewModel.getContacts().observe(this, (@Nullable Resource<List<Contact>> contactsRes) -> {
            if (contactsRes == null) {
                progressBar.setVisibility(View.GONE);
                return;
            }
            switch (contactsRes.getStatus()) {
                case Progress:
                    progressBar.setVisibility(View.VISIBLE);
                    break;
                case Completed:
                    adapter.setContacts(contactsRes.getData());
                    adapter.notifyDataSetChanged();
                    progressBar.setVisibility(View.GONE);
                    break;
                case Error:
                    if (contactsRes.getError() instanceof SecurityException && !contactsIsAvailable()) {
                        requestReadContactsPermission();
                    }
                    progressBar.setVisibility(View.GONE);
                    break;
            }
        });

        if (contactsViewModel.getContacts().getValue() == null
                || contactsViewModel.getContacts().getValue().getStatus() != Resource.Status.Completed) {
            contactsViewModel.loadAll();
        }
        return root;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        if (requestCode == READ_CONTACT_PERM_RC) {
            if (contactsIsAvailable()) {
                contactsViewModel.loadAll();
            }
        }
    }

    private boolean contactsIsAvailable() {
        if (getContext() == null) {
            return false;
        }
        return PackageManager.PERMISSION_GRANTED == ContextCompat.checkSelfPermission(
                getContext(),
                Manifest.permission.READ_CONTACTS);
    }

    private void requestReadContactsPermission() {
        requestPermissions(new String[]{Manifest.permission.READ_CONTACTS}, READ_CONTACT_PERM_RC);
    }
}
