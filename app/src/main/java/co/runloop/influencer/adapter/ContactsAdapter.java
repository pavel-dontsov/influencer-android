package co.runloop.influencer.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import co.runloop.influencer.R;
import co.runloop.influencer.model.Contact;

public class ContactsAdapter extends RecyclerView.Adapter<ContactsAdapter.ContactViewHolder> {

    private List<Contact> contacts;
    private LayoutInflater layoutInflater;
    private Context context;

    public ContactsAdapter() {
        contacts = new ArrayList<>();
    }

    @Override
    public void onAttachedToRecyclerView(@NonNull RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
        context = recyclerView.getContext();
        layoutInflater = LayoutInflater.from(context);
    }

    @NonNull
    @Override
    public ContactViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int pos) {
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

    class ContactViewHolder extends RecyclerView.ViewHolder {

        private TextView nameTv;
        private TextView phoneNumberTv;
        private ImageView photoImgv;

        public ContactViewHolder(@NonNull View itemView) {
            super(itemView);
            nameTv = itemView.findViewById(R.id.item_contact_person_name_tv);
            phoneNumberTv = itemView.findViewById(R.id.item_contact_phone_number_tv);
            photoImgv = itemView.findViewById(R.id.item_contact_person_imgv);
        }

        public void bind(Contact contact) {
            nameTv.setText(contact.getName());
            phoneNumberTv.setText(contact.getPhoneNumber());
            if (contact.getPhotoUrl() != null) {
                //TODO load img
            }
        }
    }
}
