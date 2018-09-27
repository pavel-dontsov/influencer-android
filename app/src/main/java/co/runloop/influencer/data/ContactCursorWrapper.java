package co.runloop.influencer.data;

import android.content.Context;
import android.database.Cursor;
import android.database.CursorWrapper;
import android.provider.ContactsContract;
import android.provider.ContactsContract.CommonDataKinds.Phone;
import android.provider.ContactsContract.Contacts;

import co.runloop.influencer.model.Contact;

public class ContactCursorWrapper extends CursorWrapper {

    private Context context;

    public ContactCursorWrapper(Cursor cursor, Context context) {
        super(cursor);
        this.context = context;
    }

    public Contact extractContact() {
        Contact contact = new Contact();
        contact.setId(getLong(getColumnIndex(Contacts._ID)));
        contact.setName(getString(getColumnIndex(Contacts.DISPLAY_NAME)));
        contact.setThumbnailUri(getString(getColumnIndex(Contacts.PHOTO_THUMBNAIL_URI)));
        contact.setPhotoUri(getString(getColumnIndex(Contacts.PHOTO_URI)));

        int havePhoneNumber = getInt(getColumnIndex(Contacts.HAS_PHONE_NUMBER));
        if (havePhoneNumber == 1) {
            Cursor phoneCursor = context.getContentResolver().query(
                    Phone.CONTENT_URI,
                    ContactsProvider.PHONE_PROJECTION,
                    Phone.CONTACT_ID + " = ?",
                    new String[]{String.valueOf(contact.getId())},
                    null);

            if (phoneCursor != null && phoneCursor.getCount() > 0) {
                phoneCursor.moveToFirst();
                contact.setPhoneNumber(phoneCursor.getString(phoneCursor.getColumnIndex(Phone.NUMBER)));
                phoneCursor.close();
            }
        }
        return contact;
    }
}
