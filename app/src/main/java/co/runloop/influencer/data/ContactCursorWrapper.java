package co.runloop.influencer.data;

import android.content.ContentResolver;
import android.database.Cursor;
import android.database.CursorWrapper;
import android.provider.ContactsContract.CommonDataKinds.Phone;
import android.provider.ContactsContract.Contacts;

import co.runloop.influencer.model.Contact;

class ContactCursorWrapper extends CursorWrapper {

    public static final String[] PHONE_PROJECTION = {
            Phone.NUMBER
    };

    private ContentResolver contentResolver;

    public ContactCursorWrapper(Cursor cursor, ContentResolver contentResolver) {
        super(cursor);
        this.contentResolver = contentResolver;
    }

    public Contact extractContact() {
        Contact contact = new Contact();
        contact.setId(getLong(getColumnIndex(Contacts._ID)));
        contact.setName(getString(getColumnIndex(Contacts.DISPLAY_NAME)));
        contact.setThumbnailUri(getString(getColumnIndex(Contacts.PHOTO_THUMBNAIL_URI)));
        contact.setPhotoUri(getString(getColumnIndex(Contacts.PHOTO_URI)));

        int hasPhoneNumber = getInt(getColumnIndex(Contacts.HAS_PHONE_NUMBER));
        if (hasPhoneNumber == 1) {
            Cursor phoneCursor = contentResolver.query(
                    Phone.CONTENT_URI,
                    PHONE_PROJECTION,
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
