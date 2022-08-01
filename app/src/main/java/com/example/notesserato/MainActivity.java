package com.example.notesserato;

import static com.example.notesserato.Note.*;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.DialogFragment;
import androidx.loader.app.LoaderManager;
import androidx.loader.content.CursorLoader;
import androidx.loader.content.Loader;

import android.content.ContentResolver;
import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Date;

public class MainActivity extends AppCompatActivity implements EditNoteDialogFragment.EditNoteDialogListener, LoaderManager.LoaderCallbacks<Cursor> {
    ArrayList<Note> notes;
    NotesAdapter notes_adapter;
    NotesOpenHelper helper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        helper = new NotesOpenHelper(this, NotesOpenHelper.DATABASE_NAME,
                null, NotesOpenHelper.DATABASE_VERSION);
//        SQLiteDatabase db = helper.getWritableDatabase();
//        Cursor cursor = db.query(NotesOpenHelper.DATABASE_TABLE, null, null, null, null, null, null);

        setListAdapterMethod();
        btnAddListenerMethod();
        etNoteEnterListenerMethod();

        LoaderManager.getInstance(this).initLoader(0, null, this);

    }

    @Override
    protected void onResume() {
        super.onResume();
        LoaderManager.getInstance(this).restartLoader(0, null, this);
    }

    private void etNoteEnterListenerMethod() {
        EditText etNote = findViewById(R.id.etNote);
        etNote.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View view, int i, KeyEvent keyEvent) {
                if (keyEvent.getAction() == KeyEvent.ACTION_DOWN ||
                        keyEvent.getAction() == KeyEvent.KEYCODE_ENTER ||
                        keyEvent.getAction() == KeyEvent.KEYCODE_NUMPAD_ENTER) {
                    addNoteMethod();
                    return true;
                }
                return false;
            }
        });
    }

    public void addNoteMethod(){
        EditText etNote = findViewById(R.id.etNote);
        CheckBox cbImportant = findViewById(R.id.cbImportant);
        String note = etNote.getText().toString();
        etNote.setText("");
        boolean important = cbImportant.isChecked();

        ContentValues cv = new ContentValues();
        cv.put(KEY_NOTE_COLUMN, note);
        cv.put(KEY_NOTE_CREATED_COLUMN, System.currentTimeMillis());
        cv.put(KEY_NOTE_IMPORTANT_COLUMN, important ? 1 : 0);

        ContentResolver cr = getContentResolver();
        Uri uri = cr.insert(NoteContentProvider.CONTENT_URI, cv);
        String rowID = uri.getPathSegments().get(1);
//        SQLiteDatabase db = helper.getWritableDatabase();
//        int id = (int) db.insert(NotesOpenHelper.DATABASE_TABLE, null, cv);

        Note n = new Note(note);
        n.id = Integer.parseInt(rowID);
        n.important = important;
        notes.add(n);
        notes_adapter.notifyDataSetChanged();
    }

    private void btnAddListenerMethod() {
        Button btnAdd = findViewById(R.id.btnAdd);
        btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addNoteMethod();

            }
        });
    }

    private void setListAdapterMethod() {
        ListView lvList = findViewById(R.id.lvList);
        notes = new ArrayList<>();
        notes_adapter = new NotesAdapter(getBaseContext(), R.layout.note_layout, notes, getSupportFragmentManager(), helper);
        lvList.setAdapter(notes_adapter);
    }

    @Override
    public void onEditListenerMethod(DialogFragment dialog) {
        notes_adapter.onEditListenerMethod(dialog);
    }

    @Override
    public void onCancelListenerMethod(DialogFragment dialog) {
        notes_adapter.onCancelListenerMethod(dialog);
    }

    @NonNull
    @Override
    public Loader<Cursor> onCreateLoader(int id, @Nullable Bundle args) {
        CursorLoader loader = new CursorLoader(this, NoteContentProvider.CONTENT_URI, null, null, null, null);
        return loader;
    }

    @Override
    public void onLoadFinished(@NonNull Loader<Cursor> loader, Cursor cursor) {
        int INDEX_NOTE = cursor.getColumnIndexOrThrow(KEY_NOTE_COLUMN);
        int INDEX_ID = cursor.getColumnIndexOrThrow(KEY_ID);
        int INDEX_CREATED = cursor.getColumnIndexOrThrow(KEY_NOTE_CREATED_COLUMN);
        int INDEX_IMPORTANT = cursor.getColumnIndexOrThrow(KEY_NOTE_IMPORTANT_COLUMN);
        while (cursor.moveToNext()) {
            String note = cursor.getString(INDEX_NOTE);
            int id = cursor.getInt(INDEX_ID);
            long date = cursor.getLong(INDEX_CREATED);
            int int_important = cursor.getInt(INDEX_IMPORTANT);
            Note n = new Note(note);
            n.id = id;
            n.important = int_important == 1;
            n.setCreated(new Date(date));
            notes.add(n);
        }
    }

    @Override
    public void onLoaderReset(@NonNull Loader<Cursor> loader) {

    }
}