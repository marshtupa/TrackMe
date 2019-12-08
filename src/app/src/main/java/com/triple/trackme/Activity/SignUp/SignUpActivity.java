package com.triple.trackme.Activity.SignUp;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.TextView;

import com.triple.trackme.R;

public class SignUpActivity extends AppCompatActivity {

    private boolean firstNameValid = false;
    private boolean secondNameValid = false;
    private boolean emailValid = false;
    private boolean passwordValid = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        updateWindow();
        setContentView(R.layout.activity_sign_up);
        setFieldsValidation();
    }

    private void updateWindow() {
        getWindow().getDecorView().setSystemUiVisibility(
                View.SYSTEM_UI_FLAG_LAYOUT_STABLE | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);
        setWindowFlag(this, WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS, false);
        getWindow().setStatusBarColor(Color.TRANSPARENT);
    }

    private void setWindowFlag(final Activity activity, final int bits, final boolean on) {
        Window win = activity.getWindow();
        WindowManager.LayoutParams winParams = win.getAttributes();
        if (on) {
            winParams.flags |= bits;
        } else {
            winParams.flags &= ~bits;
        }
        win.setAttributes(winParams);
    }

    private void setFieldsValidation() {
        updateUI();

        EditText firstName = findViewById(R.id.firstNameText);
        firstName.addTextChangedListener(new TextValidator(firstName) {
            @Override public void validate(TextView textView, String text) {
                firstNameValid = !text.isEmpty();
                updateUI();
            }
        });

        final EditText secondName = findViewById(R.id.secondNameText);
        secondName.addTextChangedListener(new TextValidator(secondName) {
            @Override public void validate(TextView textView, String text) {
                secondNameValid = !text.isEmpty();
                updateUI();
            }
        });

        EditText email = findViewById(R.id.emailText);
        email.addTextChangedListener(new TextValidator(email) {
            @Override public void validate(TextView textView, String text) {
                String regex = "^[\\w-_\\.+]*[\\w-_\\.]\\@([\\w]+\\.)+[\\w]+[\\w]$";
                emailValid = text.matches(regex);
                updateUI();
            }
        });

        EditText password = findViewById(R.id.passwordText);
        password.addTextChangedListener(new TextValidator(password) {
            @Override public void validate(TextView textView, String text) {
                passwordValid = !text.isEmpty() && text.length() >= 6;
                updateUI();
            }
        });
    }

    private void updateUI() {
        if (firstNameValid && secondNameValid && emailValid && passwordValid) {
            findViewById(R.id.signUpButton).setEnabled(true);
        } else {
            findViewById(R.id.signUpButton).setEnabled(false);
        }
    }

    public void signUp(final View view) {

    }
}
