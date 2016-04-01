package com.taihe.template.app.tmplate.activity;

import android.content.ContentResolver;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.ViewGroup;
import android.widget.TextView;

import com.taihe.template.app.R;
import com.taihe.template.app.base.AppBaseActivity;
import com.taihe.template.base.injection.Id;
import com.taihe.template.base.injection.Layout;
import com.github.ilioili.widget.CharacterChooser;

import java.util.ArrayList;

import static android.provider.ContactsContract.CommonDataKinds.Phone.CONTENT_URI;
import static android.provider.ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME;
import static android.provider.ContactsContract.CommonDataKinds.Phone.NUMBER;

@Layout(R.layout.activity_contact_list)
public class ContactListActivity extends AppBaseActivity {

    @Id(R.id.tv_cur)
    private TextView tvCur;
    @Id(R.id.charChooser)
    private CharacterChooser characterChooser;
    @Id(R.id.recyclerView)
    private RecyclerView recyclerView;
    private ArrayList<Contact> contacts;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        characterChooser.setOnCharacterSelectedListener(new CharacterChooser.OnCharacterSelectedListener() {
            @Override
            public void onCharacterSelected(char c) {
                tvCur.setText("" + c);
            }

            @Override
            public void onUnknownSelected() {
                tvCur.setText("#");
            }

            @Override
            public void onImportSelected() {
                tvCur.setText("★");
            }
        });
        contacts = getPhoneContacts();
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(new RecyclerView.Adapter() {
            @Override
            public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
                return new RecyclerView.ViewHolder(new TextView(ContactListActivity.this)) {
                    @Override
                    public String toString() {
                        return super.toString();
                    }
                };
            }

            @Override
            public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
                TextView tv = (TextView) holder.itemView;
                Contact contact = contacts.get(position);
                tv.setText(contact.name+"   "+contact.number);
            }

            @Override
            public int getItemCount() {
                return contacts.size();
            }
        });
    }
    private static final String[] PHONES_PROJECTION = new String[] {DISPLAY_NAME, NUMBER};
    /**
     * 得到手机通讯录联系人信息
     **/
    private ArrayList<Contact> getPhoneContacts() {
        ContentResolver resolver = getContentResolver();
        Cursor phoneCursor = resolver.query(CONTENT_URI, PHONES_PROJECTION, null, null, null);
        ArrayList<Contact> contacts = new ArrayList<>();
        if (phoneCursor != null) {
            while (phoneCursor.moveToNext()) {
                Contact contact = new Contact();
                contact.number = phoneCursor.getString(1);
                contact.name = phoneCursor.getString(0);
                contacts.add(contact);
            }
            phoneCursor.close();
        }
        return contacts;
    }

    class Contact{
        String number;
        String name;
    }
}
