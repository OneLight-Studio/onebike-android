package com.onelightstudio.onebike;

import android.app.Activity;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Patterns;
import android.view.Menu;
import android.view.MenuItem;
import android.view.Window;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.Toast;

import java.util.Properties;

import javax.mail.Authenticator;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

public class FeedbackActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_INDETERMINATE_PROGRESS);
        setContentView(R.layout.activity_feedback);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.feedback, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_send:
                sendFeedback();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void sendFeedback() {
        String email = ((EditText) findViewById(R.id.feedback_email)).getText().toString();
        String content = ((EditText) findViewById(R.id.feedback_content)).getText().toString();

        if (email.isEmpty() || content.isEmpty()) {
            Toast.makeText(this, R.string.feedback_missing_fields, Toast.LENGTH_SHORT).show();
        } else if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            Toast.makeText(this, R.string.feedback_wrong_email, Toast.LENGTH_SHORT).show();
        } else {
            Properties props = new Properties();
            props.put("mail.smtp.host", Constants.EMAIL_SMTP_HOST);
            Session session = Session.getDefaultInstance(props, new Authenticator() {
            });
            final Message msg = new MimeMessage(session);
            try {
                msg.setFrom(new InternetAddress(Constants.EMAIL_SENDER));
                msg.setRecipient(Message.RecipientType.TO, new InternetAddress(Constants.EMAIL_RECIPIENT));
                msg.setSubject(Constants.EMAIL_SUBJECT);
                msg.setText("Message de " + email + " :\n\n" + content);
                new AsyncTask<Void, Void, Boolean>() {

                    @Override
                    protected Boolean doInBackground(Void... voids) {
                        try {
                            Transport.send(msg);
                            return true;
                        } catch (MessagingException e) {
                            Log.e("Could not send email", e);
                            return false;
                        }
                    }

                    @Override
                    protected void onPreExecute() {
                        setProgressBarIndeterminateVisibility(true);
                    }

                    @Override
                    protected void onPostExecute(Boolean success) {
                        setProgressBarIndeterminateVisibility(false);
                        if (success) {
                            Toast.makeText(FeedbackActivity.this, R.string.feedback_thanks, Toast.LENGTH_SHORT).show();
                            finish();
                        } else {
                            Toast.makeText(FeedbackActivity.this, R.string.feedback_error, Toast.LENGTH_SHORT).show();
                        }
                    }
                }.execute();
            } catch (MessagingException e) {
                Log.e("Could not send email", e);
                Toast.makeText(this, R.string.feedback_error, Toast.LENGTH_SHORT).show();
            }
        }
    }
}
