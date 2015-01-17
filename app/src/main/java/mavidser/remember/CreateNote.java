package mavidser.remember;

import android.app.Activity;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.TaskStackBuilder;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

public class CreateNote extends ActionBarActivity {

    private boolean EDITING = false;
    private String _ID;
    private boolean _PINNED = false;
    private boolean _ARCHIVED = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_note);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        Intent intent = getIntent();
        String content = intent.getStringExtra("content");
        String pinned = intent.getStringExtra("pinned");
        String archived = intent.getStringExtra("archived");
        _ID = intent.getStringExtra("id");
        if (content != null) {
            EDITING = true;
            EditText note_content = (EditText) findViewById(R.id.note_text);
            note_content.setText(content);
            Button pinBtn = (Button) findViewById(R.id.pinbutton);
            if (pinned.equals("1")) {
                _PINNED = true;
                pinBtn.setText("Unpin");
                pinBtn.setBackgroundResource(R.color.red_300);
            }
            else {
                _PINNED = false;
            }
            if (archived.equals("1")) {
                _ARCHIVED = true;
            }
            else {
                _ARCHIVED = false;
            }
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.create_note, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }
        if (id == android.R.id.home) {
//            Intent upIntent = NavUtils.getParentActivityIntent(this);
            onBackPressed();
            return true;
        }
        if (id == R.id.action_archive) {
            archiveNote();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    public void unpin(int n) {
        NotificationManager mNotifyMgr = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        mNotifyMgr.cancel(Integer.parseInt(_ID));
        NoteInfo note = generateNote(false);
        note.id = _ID;
        new NotesContract().updateNote(getBaseContext(), note);
    }

    public NoteInfo generateNote(boolean pinned) {
        NoteInfo note = new NoteInfo();
        EditText note_content = (EditText) findViewById(R.id.note_text);
        if (pinned)
            note.pinned = "1";
        else
            note.pinned = "0";
        if (_ARCHIVED)
            note.archived = "1";
        else
            note.archived = "0";
        System.out.println(note.archived);
        note.content = note_content.getText().toString().trim();

        DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
        Date date = new Date();
        note.date = dateFormat.format(date);
        return note;
    }

    public void saveNote(boolean pinned) {
        NoteInfo note = generateNote(pinned);
        if (note.content.isEmpty()) {
            archiveNote();
            return;
        }
        _ID = new NotesContract().createNote(getBaseContext(), note);
        EDITING = true;
        _PINNED = pinned;
    }

//    public void saveNote(View view) {
//        saveNote(false);
//    }

    public void updateNote(boolean pinned) {
        NoteInfo note = generateNote(pinned);
        if (note.content.isEmpty()) {
            archiveNote();
            return;
        }
        note.id = _ID;
//        System.out.println("Setting pin as "+note.pinned);
        new NotesContract().updateNote(getBaseContext(), note);
    }

    public void archiveNote() {
        try {
            NotificationManager mNotifyMgr = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
            mNotifyMgr.cancel(Integer.parseInt(_ID));
        } catch (Exception e) {}
        NoteInfo note = generateNote(false);
        if(EDITING || !note.content.isEmpty()) {
            note.archived = "1";
            note.id = _ID;
            _ARCHIVED = true;
            new NotesContract().updateNote(getBaseContext(), note);
        }
        onBackPressed();
    }

    public void pin(String note) {
        if (note.isEmpty())
            return;
        NotificationCompat.BigTextStyle notiStyle = new
                NotificationCompat.BigTextStyle();

        notiStyle.setBigContentTitle("Remember ");
        notiStyle.bigText(note);
        NotificationCompat.Builder mBuilder =
                new NotificationCompat.Builder(this)
                        .setSmallIcon(R.drawable.ic_remember_r)
                        .setContentTitle("Remember")
                        .setContentText(note)
                        .setOngoing(true)
                        .setStyle(notiStyle);

        NotificationManager mNotifyMgr = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        mNotifyMgr.notify(Integer.parseInt(_ID), mBuilder.build());
    }

    public void pinPressed(View view) {
        EditText note = (EditText) findViewById(R.id.note_text);
        Button pinBtn = (Button) findViewById(R.id.pinbutton);
        if (pinBtn.getText().toString().equals("Pin")) {

            if (EDITING) {
                updateNote(true);
                _PINNED = true;
            } else {
                saveNote(true);
            }

            pin(note.getText().toString());
            pinBtn.setText("Unpin");
            pinBtn.setBackgroundResource(R.color.red_300);
        } else {

            unpin(1);
            _PINNED = false;
            pinBtn.setText("Pin");
            pinBtn.setBackgroundResource(R.color.light_green_300);
        }

    }

    @Override
    public void onPause() {
        super.onPause();  // Always call the superclass method first
//        System.out.println("Uh huh");

        if (EDITING) {
            if (_PINNED)
                updateNote(true);
            else
                updateNote(false);
        }
        else {
            saveNote(false);
        }
    }
}