package co.runloop.influencer.data;

import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.provider.ContactsContract;
import android.provider.ContactsContract.CommonDataKinds;
import android.provider.ContactsContract.CommonDataKinds.Phone;
import android.provider.ContactsContract.Contacts;
import android.util.Log;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import co.runloop.influencer.model.Contact;

public class ContactsProvider {

    private static final String TAG = ContactsProvider.class.getSimpleName();

    public static final String[] CONTACTS_PROJECTION = {
            Contacts._ID,
            Contacts.DISPLAY_NAME,
            Contacts.PHOTO_URI,
            Contacts.PHOTO_THUMBNAIL_URI,
            Contacts.HAS_PHONE_NUMBER,
    };
    public static final String[] PHONE_PROJECTION = {
            Phone.NUMBER
    };

    private Context context;

    public ContactsProvider(Context context) {
        this.context = context;
    }

    public Contact getById(long id) {
        ContentResolver cr = context.getContentResolver();
        Cursor cursor = cr.query(Contacts.CONTENT_URI, CONTACTS_PROJECTION,
                Contacts._ID + " = ?",
                new String[]{String.valueOf(id)},
                null);

        if (cursor != null && cursor.getCount() > 0) {
            ContactCursorWrapper contactCw = new ContactCursorWrapper(cursor, context);
            try {
                contactCw.moveToFirst();
                return contactCw.extractContact();
            } finally {
                contactCw.close();
            }
        }
        return null;
    }

    public List<Contact> getAll() {
        List<Contact> contacts = new ArrayList<>();

        ContentResolver cr = context.getContentResolver();
        Cursor cursor = cr.query(Contacts.CONTENT_URI, CONTACTS_PROJECTION,
                null,
                null,
                null);

        if (cursor != null && cursor.getCount() > 0) {
            ContactCursorWrapper contactCw = new ContactCursorWrapper(cursor, context);
            try {
                contactCw.moveToFirst();
                while (!contactCw.isAfterLast()) {
                    contacts.add(contactCw.extractContact());
                    contactCw.moveToNext();
                }
            } finally {
                contactCw.close();
            }
        }
        return contacts;
    }

    public List<Contact> getByContainsName(String str) {
        return Collections.emptyList();
    }
}
