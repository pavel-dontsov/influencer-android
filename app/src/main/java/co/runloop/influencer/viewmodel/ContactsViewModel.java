package co.runloop.influencer.viewmodel;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;

import java.util.List;

import co.runloop.influencer.data.ContactsProvider;
import co.runloop.influencer.model.Contact;
import co.runloop.influencer.model.Resource;
import co.runloop.influencer.utils.DataObserver;
import io.reactivex.Observable;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.schedulers.Schedulers;

public class ContactsViewModel extends AndroidViewModel {

    private static final String TAG = ContactsViewModel.class.getSimpleName();

    private MutableLiveData<Resource<List<Contact>>> contacts;
    private ContactsProvider provider;
    private DataObserver observer;
    private CompositeDisposable disposables;
    private boolean shouldRegisterObserver;

    public ContactsViewModel(Application app) {
        super(app);
        contacts = new MutableLiveData<>();
        provider = new ContactsProvider(app.getApplicationContext().getContentResolver());
        observer = () -> {
            loadAll();
        };
        disposables = new CompositeDisposable();
        shouldRegisterObserver = true;
    }

    private void registerObserver() {
        provider.unregisrerContactsChangesObserver();
        provider.registerContactsChangesObserver(observer);
    }

    @Override
    protected void onCleared() {
        provider.unregisrerContactsChangesObserver();
        disposables.clear();
    }

    public void loadAll() {
        contacts.postValue(new Resource<>(Resource.Status.Progress));
        disposables.add(
                Observable.create((ObservableOnSubscribe<List<Contact>>) emitter -> {
                    emitter.onNext(provider.getAll());
                    emitter.onComplete();
                })
                        .subscribeOn(Schedulers.io())
                        .subscribe(list -> {
                            contacts.postValue(new Resource<>(list, Resource.Status.Completed));
                            if (shouldRegisterObserver) {
                                registerObserver();
                                shouldRegisterObserver = false;
                            }
                        }, err -> {
                            contacts.postValue(new Resource<>(err));
                            if (err instanceof SecurityException) {
                                shouldRegisterObserver = true;
                            }
                        }));

    }

    public LiveData<Resource<List<Contact>>> getContacts() {
        return contacts;
    }
}
