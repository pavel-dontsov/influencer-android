package co.runloop.influencer.viewmodel;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.ViewModel;

import java.util.List;

import co.runloop.influencer.data.ContactsProvider;
import co.runloop.influencer.model.Contact;

public class ContactsViewModel extends AndroidViewModel {

    private MutableLiveData<List<Contact>> contacts;
    private ContactsProvider provider;
    private boolean wasLoaded;

    public ContactsViewModel(Application app) {
        super(app);
        contacts = new MutableLiveData<>();
        provider = new ContactsProvider(app);
        provider.registerContactsChangesObserver(() -> {
            loadAll();
        });
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

    public boolean isWasLoaded() {
        return wasLoaded;
    }

    public void setWasLoaded(boolean wasLoaded) {
        this.wasLoaded = wasLoaded;
    }
}
