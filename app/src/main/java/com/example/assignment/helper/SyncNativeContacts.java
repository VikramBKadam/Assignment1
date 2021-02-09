package com.example.assignment.helper;



import android.content.Context;
import android.database.Cursor;
import android.provider.ContactsContract;
import android.util.Log;


import com.example.assignment.model.Contact;
import com.example.assignment.repository.LocalRepository;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.CompletableObserver;
import io.reactivex.annotations.NonNull;
import io.reactivex.disposables.Disposable;

public class SyncNativeContacts {

    Context context;
    LocalRepository localRepository;

    public SyncNativeContacts(Context context) {
        this.context = context;
        localRepository = new LocalRepository(context);
    }

    private List<Contact> contactList = new ArrayList<>();

    public void syncNativeContacts() {

        getContactArrayList();

        addContactListToDB();
    }

    private void addContactListToDB() {
//        for (Contact eachContact : contactList) {
//            localRepository.addContact(eachContact).subscribe();
//        }
        localRepository.addListOfContact(contactList)
                .subscribe(new CompletableObserver() {
                    @Override
                    public void onSubscribe(@NonNull Disposable d) {
                        Log.d("TAG", "Inside onSubscribe of addContactListDB in SyncNativeContacts");
                    }

                    @Override
                    public void onComplete() {
                        Log.d("TAG", "Inside onComplete of addContactListDB in SyncNativeContacts");
                    }

                    @Override
                    public void onError(@NonNull Throwable e) {
                        Log.d("TAG", "Inside onError of addContactListDB in SyncNativeContacts.: " + e.getMessage());
                    }
                });
    }

    private void getContactArrayList() {
        int count = 0;

        Cursor cursor = context.getContentResolver().query(ContactsContract.Contacts.CONTENT_URI,
                null, null, null, null);

        if ((cursor != null ? cursor.getCount() : 0) > 0) {
            while (cursor != null && cursor.moveToNext()) {

                String id = cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts._ID));
                String name = cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME));

                List<String> numberList = new ArrayList<>();

                if (cursor.getInt(cursor.getColumnIndex(ContactsContract.Contacts.HAS_PHONE_NUMBER)) > 0) {
                    Cursor phoneCursor = context.getContentResolver().query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
                            null, ContactsContract.CommonDataKinds.Phone.CONTACT_ID + "=?",
                            new String[]{id}, null);
                    while (phoneCursor.moveToNext()) {
                        String number = phoneCursor.getString(phoneCursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
                        numberList.add(number);
                        Log.d("TAG", "Name is: " + name);
                        Log.d("TAG", "Number is: " + number);
                        count++;
                    }
                    phoneCursor.close();
                }

                Contact contact = new Contact(id, name, numberList);
                contactList.add(contact);
            }
        }
        if (cursor != null)
            cursor.close();
        Log.d("TAG", "Total Count: " + count);

    }
}

