package co.runloop.influencer.adapter;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;
import java.util.List;

import co.runloop.influencer.R;
import co.runloop.influencer.model.Contact;

public class ContactsAdapter extends RecyclerView.Adapter<ContactsAdapter.ContactViewHolder> {

    private static final String TAG = ContactsAdapter.class.getSimpleName();

    private List<Contact> contacts;

    public ContactsAdapter() {
        contacts = new ArrayList<>();
    }

    @NonNull
    @Override
    public ContactViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int pos) {
        LayoutInflater layoutInflater = LayoutInflater.from(viewGroup.getContext());
        View itemView = layoutInflater.inflate(R.layout.item_contact, viewGroup, false);
        return new ContactViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull ContactViewHolder viewHolder, int pos) {
        viewHolder.bind(contacts.get(pos));
    }

    @Override
    public int getItemCount() {
        return contacts.size();
    }

    @Override
    public void onViewRecycled(@NonNull ContactViewHolder holder) {
        super.onViewRecycled(holder);
        holder.phoneNumberTv.setText(null);
        holder.nameTv.setText(null);
        holder.thumbnailImgv.setImageDrawable(null);
    }

    class ContactViewHolder extends RecyclerView.ViewHolder {

        private TextView nameTv;
        private TextView phoneNumberTv;
        private ImageView thumbnailImgv;

        public ContactViewHolder(@NonNull View itemView) {
            super(itemView);
            nameTv = itemView.findViewById(R.id.item_contact_person_name_tv);
            phoneNumberTv = itemView.findViewById(R.id.item_contact_phone_number_tv);
            thumbnailImgv = itemView.findViewById(R.id.item_contact_person_imgv);
        }

        public void bind(Contact contact) {
            nameTv.setText(contact.getName());
            phoneNumberTv.setText(contact.getPhoneNumber());
            if (contact.getThumbnailUri() != null) {
                Glide.with(thumbnailImgv)
                        .load(contact.getThumbnailUri())
                        .into(thumbnailImgv);
            }
        }
    }

    public List<Contact> getContacts() {
        return contacts;
    }

    public void setContacts(List<Contact> contacts) {
        this.contacts = contacts;
    }
}
