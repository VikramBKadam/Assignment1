package com.example.assignment.view.activities;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.viewpager.widget.ViewPager;

import android.Manifest;
import android.content.ContentResolver;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.example.assignment.R;
import com.example.assignment.adapters.MyFragmentAdapter;
import com.example.assignment.utils.AndroidContactsChangeListener;
import com.example.assignment.utils.SyncNativeContacts;
import com.example.assignment.view.CreateEntryFragment;
import com.example.assignment.view.UserListFragment;
import com.example.assignment.view.ContactListFragment;
import com.example.assignment.view.ViewContactDetailsFragment;
import com.example.assignment.viewmodel.MyViewModel;
import com.google.android.material.tabs.TabLayout;

import dagger.hilt.android.AndroidEntryPoint;

@AndroidEntryPoint
public class MainActivity extends AppCompatActivity {
    ContentResolver contentResolver;


    TabLayout tabLayout;
    Toolbar toolbar;
    ViewPager viewPager;
    MyFragmentAdapter adapter;
    private final int READ_CONTACT_REQUEST_CODE = 100;
    SyncNativeContacts syncNativeContacts;

    MyViewModel fragmentMyViewModel;


    AndroidContactsChangeListener.IChangeListener contactChangeListener = new AndroidContactsChangeListener.IChangeListener() {
        @Override
        public void onContactsChanged() {
          // fragmentMyViewModel.completeContactSync();

            fragmentMyViewModel.deltaContactSync();
        }
    };



    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        fragmentMyViewModel =new ViewModelProvider(this).get(MyViewModel.class);
       // fragmentMyViewModel = ViewModelProviders.of(this).get(MyViewModel.class);
        init();
    }

    @Override
    protected void onResume() {
        super.onResume();
        AndroidContactsChangeListener.getInstance(this).startContactsObservation(contactChangeListener);
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    private void init() {



        toolbar=findViewById(R.id.tool_bar);
        toolbar.setTitle("User Info");
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        tabLayout = findViewById(R.id.tab_layout);
        viewPager = findViewById(R.id.view_pager);
        adapter = new MyFragmentAdapter(getSupportFragmentManager());
        viewPager.setAdapter(adapter);
        tabLayout.setupWithViewPager(viewPager);
        checkPermissionSyncContacts();


    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    private void checkPermissionSyncContacts() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.READ_CONTACTS) !=
                PackageManager.PERMISSION_GRANTED)
            requestPermissions(new String[]{Manifest.permission.READ_CONTACTS}, READ_CONTACT_REQUEST_CODE);
        else
            syncContacts();
    }

    private void syncContacts() {
        fragmentMyViewModel.completeContactSync();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == READ_CONTACT_REQUEST_CODE) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED)
                syncContacts();
            else
                Toast.makeText(this, "Contact Sync failed. Please grant contacts permission", Toast.LENGTH_SHORT).show();
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        MenuInflater inflater =getMenuInflater();

        MyViewModel.getIsMultiSelectOn().observe(this, new Observer<Boolean>() {
            @Override
            public void onChanged(Boolean aBoolean) {
                menu.clear();

                if (aBoolean)
                    inflater.inflate(R.menu.multi_select_delete, menu);
                else
                    inflater.inflate(R.menu.menu_search, menu);

            }
        });



        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        if (item.getItemId()==R.id.search){
            Toast.makeText(this, "Search Clicked", Toast.LENGTH_SHORT).show();
            SearchView searchView =(SearchView)item.getActionView();
            searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
                @Override
                public boolean onQueryTextSubmit(String query) {
                    searchView.clearFocus();
                    MyViewModel.setQueryString(query);

                    return false;
                }

                @Override
                public boolean onQueryTextChange(String newText) {
                    MyViewModel.setQueryString(newText);


                    return false;
                }
            });
        }



        return super.onOptionsItemSelected(item);
    }
    public void  switchTodetailfragment(int id){
        FragmentManager manager=getSupportFragmentManager();

        FragmentTransaction transaction=manager.beginTransaction();
        transaction.replace(R.id.tab1fragment, CreateEntryFragment.Tab2Instance(id)).addToBackStack("tag")
                .commit();
    }
    public void  switchToTab1fragment(){
        FragmentManager manager=getSupportFragmentManager();

        FragmentTransaction transaction=manager.beginTransaction();
        transaction.replace(R.id.tab2_fragment, UserListFragment.newInstance())
                .commit();
    }

    public  void switchToViewContactFragment(String id){
        FragmentManager manager=getSupportFragmentManager();

        FragmentTransaction transaction=manager.beginTransaction();
        transaction.replace(R.id.contact_list_fragment, ViewContactDetailsFragment.newInstance(id))
                .commit();

    }
    public  void switchToContactListfragment(){
        Log.e("TAG","In Switch To Contact List Fragment");
        FragmentManager manager=getSupportFragmentManager();

        FragmentTransaction transaction=manager.beginTransaction();
        ContactListFragment contactListFragment =new ContactListFragment();
        transaction.replace(R.id.view_contact_fragment, contactListFragment)
                .commit();

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        AndroidContactsChangeListener.getInstance(this).stopContactsObservation();
    }
}