package com.example.assignment.repository;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.room.migration.Migration;
import androidx.sqlite.db.SupportSQLiteDatabase;

import com.example.assignment.model.Contact;
import com.example.assignment.model.User;

@Database(entities = {User.class,Contact.class}, version = 2)
public abstract class UserDatabase extends RoomDatabase {

    private static UserDatabase instance;
    static Migration migration = new Migration(1,2) {
        @Override
        public void migrate(@NonNull SupportSQLiteDatabase database) {

           // database.execSQL("alter table 'user' add 'phonenumber1' text ,  add 'phonenumber2' text");

           // database.execSQL("alter table 'user' add 'date' integer ");
          /*  database.execSQL("CREATE TABLE Contacts (id INTEGER, "
                    + "name TEXT,"+"contactNo TEXT , PRIMARY KEY(id))");*/

          // database.execSQL("ALTER TABLE 'user table' ADD COLUMN 'column' TEXT NOT NULL DEFAULT''");

        }
    };

    public static UserDatabase getInstance(Context context) {
        if (instance == null) {
            instance = Room.databaseBuilder(
                    context.getApplicationContext(),
                    UserDatabase.class,
                    "userdatabase")
                   .addMigrations(migration)
                    .build();
        }
        return instance;
    }

    public abstract UserDao userDao();
    public abstract ContactDao contactDAO();
}