package com.example.notesserato;

import static com.example.notesserato.Note.*;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

public class NotesOpenHelper extends SQLiteOpenHelper {
    public static final String DATABASE_NAME = "myDatabase.db";
    public static final String DATABASE_TABLE = "Notes";
    public static final int DATABASE_VERSION = 4;

    private static final String DATABASE_CREATE = "CREATE TABLE " +
            DATABASE_TABLE +  " (" +
            KEY_ID + " integer primary key autoincrement, " +
            KEY_NOTE_COLUMN + " text, " +
            KEY_NOTE_CREATED_COLUMN + " long);";

    public NotesOpenHelper(@Nullable Context context, @Nullable String name, @Nullable SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(DATABASE_CREATE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS "+ DATABASE_TABLE);
        onCreate(db);
    }
}
