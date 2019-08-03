package com.abdelrahmman.daggerretrofit.ui.login;

import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.abdelrahmman.daggerretrofit.R;
import com.abdelrahmman.daggerretrofit.models.LoginResponse;

import com.abdelrahmman.daggerretrofit.ui.main.MainActivity;
import com.abdelrahmman.daggerretrofit.ui.register.RegisterActivity;
import com.abdelrahmman.daggerretrofit.viewmodel.ViewModelProviderFactory;

import javax.inject.Inject;

import dagger.android.support.DaggerAppCompatActivity;

public class LoginActivity extends DaggerAppCompatActivity implements View.OnClickListener {
    private static final String TAG = "LoginActivity";

    private LoginViewModel viewModel;

    // ui
    private ProgressBar progressBar;
    private Button loginButton;
    private EditText email, password;

    @Inject
    ViewModelProviderFactory providerFactory;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        findViewById(R.id.btn_login).setOnClickListener(this);
        findViewById(R.id.create_account_text).setOnClickListener(this);

        progressBar = findViewById(R.id.progress_bar);
        loginButton = findViewById(R.id.btn_login);
        email = findViewById(R.id.email_input);
        password = findViewById(R.id.password_input);

        viewModel = ViewModelProviders.of(this, providerFactory).get(LoginViewModel.class);

        subscribeObservers();
    }

    private void subscribeObservers(){
        viewModel.observeAuthUser().observe(this, new Observer<LoginResource<LoginResponse>>() {
            @Override
            public void onChanged(LoginResource<LoginResponse> userLoginResource) {
                if (userLoginResource != null){
                    switch (userLoginResource.status){
                        case LOADING:{
                            showProgressBar(true);
                            break;
                        }
                        case AUTHENTICATED:{
                            showProgressBar(false);
                            onLoginSuccess();

                            break;
                        }
                        case ERROR:{
                            showProgressBar(false);
                            Toast.makeText(LoginActivity.this, userLoginResource.message, Toast.LENGTH_SHORT).show();
                            break;
                        }
                        case NOT_AUTHENTICATED:{
                            showProgressBar(false);
                            break;
                        }
                    }
                }
            }
        });

    }

    private void onLoginSuccess(){
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
        finish();
    }

    private void showProgressBar(boolean isVisible){
        if (isVisible){
            progressBar.setVisibility(View.VISIBLE);
            loginButton.setEnabled(false);
        } else {
            progressBar.setVisibility(View.GONE);
            loginButton.setEnabled(true);
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btn_login: {
                attemptLogin();
                break;
            }
            case R.id.create_account_text: {
                navToRegister();
            }
        }
    }

    private void navToRegister(){
        Intent intent = new Intent(LoginActivity.this, RegisterActivity.class);
        startActivity(intent);
        finish();
    }

    private void attemptLogin() {
        if (TextUtils.isEmpty(email.getText().toString()) || TextUtils.isEmpty(password.getText().toString())){
            return;
        }
        viewModel.authenticateUser(email.getText().toString(), password.getText().toString());
    }

}
