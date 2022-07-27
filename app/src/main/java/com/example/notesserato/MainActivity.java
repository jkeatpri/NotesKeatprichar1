package com.example.notesserato;

import static com.example.notesserato.Note.*;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.DialogFragment;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements EditNoteDialogFragment.EditNoteDialogListener {
    ArrayList<Note> notes;
    NotesAdapter notes_adapter;
    NotesOpenHelper helper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        setListAdapterMethod();
        btnAddListenerMethod();
        etNoteEnterListenerMethod();

        helper = new NotesOpenHelper(this, NotesOpenHelper.DATABASE_NAME,
                null, NotesOpenHelper.DATABASE_VERSION);
        SQLiteDatabase db = helper.getWritableDatabase();
        Cursor cursor = db.query(NotesOpenHelper.DATABASE_TABLE, null, null, null, null, null, null);

        int INDEX_NOTE = cursor.getColumnIndexOrThrow(KEY_NOTE_COLUMN);
        while(cursor.moveToNext()){
            String note = cursor.getString(INDEX_NOTE);
            Note n = new Note(note);
            notes.add(n);
        }

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
        String note = etNote.getText().toString();
        notes.add(new Note(note));
        notes_adapter.notifyDataSetChanged();
        etNote.setText("");

        ContentValues cv = new ContentValues();
        cv.put(KEY_NOTE_COLUMN, note);

        SQLiteDatabase db = helper.getWritableDatabase();
        db.insert(NotesOpenHelper.DATABASE_TABLE, null, cv);
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
        notes.add(new Note("First Note"));
        notes.add(new Note("Second Note"));

        notes_adapter = new NotesAdapter(getBaseContext(), R.layout.note_layout, notes, getSupportFragmentManager());
        lvList.setAdapter(notes_adapter);

        notes.add(new Note("Jay Vince Serato"));
    }

    @Override
    public void onEditListenerMethod(DialogFragment dialog) {
        notes_adapter.onEditListenerMethod(dialog);
    }

    @Override
    public void onCancelListenerMethod(DialogFragment dialog) {
        notes_adapter.onCancelListenerMethod(dialog);
    }
}