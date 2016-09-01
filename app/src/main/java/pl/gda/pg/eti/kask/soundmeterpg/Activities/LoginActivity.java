package pl.gda.pg.eti.kask.soundmeterpg.Activities;

import android.os.AsyncTask;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;

import android.os.Bundle;
import android.text.method.LinkMovementMethod;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

import pl.gda.pg.eti.kask.soundmeterpg.Dialogs.Login;
import pl.gda.pg.eti.kask.soundmeterpg.InformationAboutThisApplication;
import pl.gda.pg.eti.kask.soundmeterpg.Interfaces.AccountManager;
import pl.gda.pg.eti.kask.soundmeterpg.Internet.MyAccountManager;
import pl.gda.pg.eti.kask.soundmeterpg.R;

public class LoginActivity extends AppCompatActivity {
    private Button loginButton;
    private Button skipButton;
    private EditText login;
    private EditText password;
    private AccountManager manager;
    private MyProperty result;
    private View loginForm;
    private View progressBarView;
    private ProgressBar progressBar;
    private TextView errorMessage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_activity);

        setUpPrivateVariable();
        makeRegistrationHyperlinkWorkable();

        loginButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.i("Login activity","Login button press");
                logIn();
            }
        });

        skipButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.i("Login activity","Skip button press");
                finish();
            }
        });
    }

    private void makeRegistrationHyperlinkWorkable() {
        TextView text = (TextView) findViewById(R.id.registration_text_view_login_activity);
        text.setMovementMethod(LinkMovementMethod.getInstance());
    }

    private void setUpPrivateVariable() {
        loginButton = (Button) findViewById(R.id.login_button_login_activity);
        skipButton = (Button) findViewById(R.id.skip_button_login_activity);
        login = (EditText) findViewById(R.id.login_edit_text_login_activity);
        password = (EditText) findViewById(R.id.password_edit_text_login_activity);
        loginForm = findViewById(R.id.login_form);
        progressBarView = findViewById(R.id.progress_bar_view);
        progressBar = (ProgressBar) findViewById(R.id.progress_bar);
        errorMessage = (TextView) findViewById(R.id.error_message_login_activity);

        manager =  new MyAccountManager(this);

        result = new MyProperty();

        progressBar.setMax(MyAccountManager.MAX_DURATION_OF_LOG_IN);
    }

    private void logIn() {
        AlertDialog dialog = Login.create(this, result);
        dialog.show();
    }

    private void resultListener(Object newValue) {
        if (newValue instanceof Boolean && newValue.equals(Boolean.TRUE)) {
            loginForm.setVisibility(View.GONE);
            progressBarView.setVisibility(View.VISIBLE);
            AsyncTask loginThread = new LoggingTask();

            String login = this.login.getText().toString();
            String password = this.password.getText().toString();
            String macAddress = InformationAboutThisApplication.getMACAddress();
            this.login.setText("");
            this.password.setText("");

            manager.logIn(login, password, macAddress);
            loginThread.execute("Daj");
        }
    }


    private class LoggingTask extends AsyncTask<Object, Integer,Boolean>{

        @Override
        protected Boolean doInBackground(Object... objects) {
            int duration = 0;
            while (!manager.isLogIn()) {
                publishProgress(manager.getProgress());
                if (duration >= MyAccountManager.MAX_DURATION_OF_LOG_IN)
                    break;
                try {
                    Thread.sleep(300);
                    duration += 300;
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            return manager.isLogIn();
        }

        @Override
        protected void onProgressUpdate(Integer... values) {
            progressBar.setProgress(values[0]);
        }

        @Override
        protected void onPostExecute(Boolean result) {
            progressBarView.setVisibility(View.GONE);
            loginForm.setVisibility(View.VISIBLE);
            if (result)
                finish();
            else
                errorMessage.setText(manager.getErrorMessage());
        }
    }

    private class MyProperty implements PropertyChangeListener{

        @Override
        public void propertyChange(PropertyChangeEvent propertyChangeEvent) {
            resultListener(propertyChangeEvent.getNewValue());
        }
    }
}



