package pl.gda.pg.eti.kask.soundmeterpg;

import android.content.Intent;
import android.media.MediaRecorder;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import android.os.Handler;

import java.io.IOException;

public class MainActivity extends AppCompatActivity {
    private TextView mStatusView;
    private TmpRecorder tmpRecorder; //this class must be reworked so its temporary solution

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mStatusView = (TextView) findViewById(R.id.textView);


        tmpRecorder = new TmpRecorder();
    }

    public void onResume()
    {
        super.onResume();
        tmpRecorder.startRecorder();
    }

    public void onPause()
    {
        super.onPause();
        tmpRecorder.stopRecorder();

    }

    public void insertData(View btn) throws IOException {
        new Insert(getBaseContext()).execute(tmpRecorder.soundDb(1.0));
    }
}
