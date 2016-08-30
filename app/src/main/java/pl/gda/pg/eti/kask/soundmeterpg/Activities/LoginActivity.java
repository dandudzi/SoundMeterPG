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
    private AsyncTask loginThread;


    @Override
    public void onPause() {
        closeActivity();
        super.onPause();
        finish();
    }

    private void closeActivity() {
        login.setText("");
        password.setText("");
        if(loginThread!=null) {
            AsyncTask.Status status = loginThread.getStatus();
            boolean isLogInRunning = isLogInTaskRunning(status);
            if (isLogInRunning) {
                manager.stopLogInProcess();
            }
        }
    }

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
                onButtonSignIn();
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

    private void onButtonSignIn(){
        AlertDialog dialog = Login.create(this, result);
        Log.i("Login activity","Show login dialog");
        dialog.show();
    }

    private void onLoginDialogResult(Object newValue) {
        if (isUserAgreeWithCookiesCondition(newValue)) {
            loginForm.setVisibility(View.GONE);
            progressBarView.setVisibility(View.VISIBLE);

            startLogIn();
            startNewTaskWhichUpdateProgressBar();
            Log.i("Login activity","Log in and start thread progress bar");
        }
        Log.i("Login activity","User cancel login dialog");
    }

    private void startLogIn() {
        String login = this.login.getText().toString();
        String password = this.password.getText().toString();
        String macAddress = InformationAboutThisApplication.getMACAddress();
        this.login.setText("");
        this.password.setText("");

        manager.logIn(login,password,macAddress);
    }

    private void startNewTaskWhichUpdateProgressBar() {
        loginThread = new LoggingTask();
        loginThread.execute("Daj");
    }

    private boolean isUserAgreeWithCookiesCondition(Object newValue) {
        return newValue instanceof Boolean && newValue.equals(Boolean.TRUE);
    }

    private boolean isLogInTaskRunning(AsyncTask.Status status) {
        return status == AsyncTask.Status.RUNNING;
    }

    private void onLogInResult(Boolean result) {
        if(result) {
            Log.i("Login activity","User log in");
            finish();
        }
        else {
            String errorMsg = manager.getErrorMessage();
            String endTaskErrorMsg = LoginActivity.this.getString(R.string.end_task_login_Activity);
            boolean isLogInEndByUser = errorMsg.equals(endTaskErrorMsg);
            if(isLogInEndByUser) {
                Log.i("Login activity","User end task");
                return;
            }
            Log.i("Login activity","User cannot log in with error " + manager.getErrorMessage());
            errorMessage.setText(errorMsg);
        }
    }

    private class LoggingTask extends AsyncTask<Object, Integer,Boolean>{

        @Override
        protected Boolean doInBackground(Object... objects) {
            while(manager.getProgress() < MyAccountManager.MAX_DURATION_OF_LOG_IN) {
                publishProgress(manager.getProgress());
                try {
                    Thread.sleep(300);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            Log.i("Login activity","End log in");
            return manager.isLogIn();
        }

        @Override
        protected void onProgressUpdate(Integer... values) {
            progressBar.setProgress(values[0]);
            Log.i("Login activity","Set progress to "+values[0]);
        }

        @Override
        protected void onPostExecute(Boolean result) {
            progressBarView.setVisibility(View.GONE);
            loginForm.setVisibility(View.VISIBLE);
            onLogInResult(result);
        }
    }

    private class MyProperty implements PropertyChangeListener{

        @Override
        public void propertyChange(PropertyChangeEvent propertyChangeEvent) {
            onLoginDialogResult(propertyChangeEvent.getNewValue());
        }
    }
}



