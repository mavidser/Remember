package mavidser.remember;

import android.app.Activity;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.TaskStackBuilder;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;


public class CreateNote extends ActionBarActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_note);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
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
            Intent upIntent = NavUtils.getParentActivityIntent(this);
            onBackPressed();
            return true;
        }
        if (id == R.id.action_archive) {
            NotificationManager mNotifyMgr = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
            mNotifyMgr.cancel(1);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    public void notify(View view) {
        NotificationCompat.BigTextStyle notiStyle = new
                NotificationCompat.BigTextStyle();
        notiStyle.setBigContentTitle("Big Picture Expanded");
        notiStyle.bigText("Nice big picjg kdfjgh fkdghdfkjgh dfkjghkjgh kdfjgh dkjfhgkdfjgh\n fture.\n" +
                "jkdfkjgh kghdf kgh dfkjghf kdjghdfkjgh dfkjgh dfkjghkfjdgh fdhgjdfhkgjdfh gkhdfg k\n" +
                "dfhg dsjf sjgdjsgf dh fdgfdsfhgfk hsdkjfhkdhg ushgkdjgh kduhg kjhg ");
        NotificationCompat.Builder mBuilder =
                new NotificationCompat.Builder(this)
                        .setSmallIcon(R.drawable.ic_launcher)
                        .setContentTitle("My notification")
                        .setContentText("Hello World! hejhf kshf ksdf jdhf khg hkdghkjhfsdjgh fjdsgf jdg f")
                        .setOngoing(true)
                        .setStyle(notiStyle);

        NotificationManager mNotifyMgr = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        mNotifyMgr.notify(001, mBuilder.build());
    }
}
