package com.example.notesserato;

import static com.example.notesserato.Note.*;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteQueryBuilder;
import android.net.Uri;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

public class NoteContentProvider extends ContentProvider {
    public static final Uri CONTENT_URI = Uri.parse("content://com.example.notesserato.notesprovider/notes");

    public static final int ALL_ROWS = 1;
    public static final int SINGLE_ROW = 2;

    private NotesOpenHelper helper;
    public static final UriMatcher matcher;

    static{
        matcher = new UriMatcher(UriMatcher.NO_MATCH);
        matcher.addURI("com.example.notesserato.notesprovider", "notes", ALL_ROWS);
        matcher.addURI("com.example.notesserato.notesprovider", "notes/#", SINGLE_ROW);
    }

    @Override
    public boolean onCreate() {
        helper = new NotesOpenHelper(getContext(), NotesOpenHelper.DATABASE_NAME, null, NotesOpenHelper.DATABASE_VERSION);
        return true;
    }

    @Nullable
    @Override
    public Cursor query(@NonNull Uri uri, @Nullable String[] projection, @Nullable String selection, @Nullable String[] selectionArgs, @Nullable String sortOrder) {
        SQLiteDatabase db = helper.getWritableDatabase();
        SQLiteQueryBuilder builder = new SQLiteQueryBuilder();
        switch (matcher.match(uri)){
            case SINGLE_ROW:
                String rowID = uri.getPathSegments().get(1);
                builder.appendWhere(KEY_ID + "=" + rowID);
            default:
                break;
        }

        builder.setTables(NotesOpenHelper.DATABASE_TABLE);
        Cursor cursor = builder.query(db, projection, selection, selectionArgs,null, null, sortOrder);
        return cursor;
    }

    @Nullable
    @Override
    public String getType(@NonNull Uri uri) {
        return null;
    }

    @Nullable
    @Override
    public Uri insert(@NonNull Uri uri, @Nullable ContentValues values) {
        return null;
    }

    @Override
    public int delete(@NonNull Uri uri, @Nullable String selection, @Nullable String[] selectionArgs) {
        return 0;
    }

    @Override
    public int update(@NonNull Uri uri, @Nullable ContentValues values, @Nullable String selection, @Nullable String[] selectionArgs) {
        return 0;
    }
}
