package co.runloop.influencer.viewmodel;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;

import java.util.List;

import co.runloop.influencer.data.ContactsProvider;
import co.runloop.influencer.model.Contact;
import co.runloop.influencer.utils.DataObserver;

public class ContactsViewModel extends AndroidViewModel {

    private MutableLiveData<List<Contact>> contacts;
    private ContactsProvider provider;
    private DataObserver observer;

    public ContactsViewModel(Application app) {
        super(app);
        contacts = new MutableLiveData<>();
        provider = new ContactsProvider(app.getApplicationContext().getContentResolver());
        observer = () -> {
            loadAll();
        };
    }

    public void onPermissionGranted() {
        provider.unregisrerContactsChangesObserver();
        provider.registerContactsChangesObserver(observer);
    }

    @Override
    protected void onCleared() {
        provider.unregisrerContactsChangesObserver();
    }

    public void loadAll() {
        contacts.postValue(provider.getAll());
    }

    public LiveData<List<Contact>> getContacts() {
        return contacts;
    }
}
