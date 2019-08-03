package com.abdelrahmman.daggerretrofit.ui.register;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import com.abdelrahmman.daggerretrofit.R;
import com.abdelrahmman.daggerretrofit.models.RegisterResponse;
import com.abdelrahmman.daggerretrofit.ui.login.LoginActivity;
import com.abdelrahmman.daggerretrofit.viewmodel.ViewModelProviderFactory;

import javax.inject.Inject;

import dagger.android.support.DaggerAppCompatActivity;

public class RegisterActivity extends DaggerAppCompatActivity implements View.OnClickListener {
    private static final String TAG = "RegisterActivity";

    private RegisterViewModel viewModel;

    // ui
    private ProgressBar progressBar;
    private Button registerButton;
    private EditText email, password, name, school;

    @Inject
    ViewModelProviderFactory providerFactory;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        progressBar = findViewById(R.id.progress_bar);
        registerButton = findViewById(R.id.btn_sign_up);
        email = findViewById(R.id.email_input);
        password = findViewById(R.id.password_input);
        name = findViewById(R.id.name_input);
        school = findViewById(R.id.school_input);

        findViewById(R.id.btn_sign_up).setOnClickListener(this);
        findViewById(R.id.login_text).setOnClickListener(this);

        viewModel = ViewModelProviders.of(this, providerFactory).get(RegisterViewModel.class);

        subscribeObservers();
    }

    private void subscribeObservers(){
        viewModel.observeCreatedUser().observe(this, new Observer<RegisterResource<RegisterResponse>>() {
            @Override
            public void onChanged(RegisterResource<RegisterResponse> userRegisterResource) {
                if (userRegisterResource != null){
                    switch (userRegisterResource.status){
                        case LOADING:{
                            showProgressBar(true);
                            break;
                        }
                        case REGISTERED:{
                            showProgressBar(false);
                            Toast.makeText(RegisterActivity.this, userRegisterResource.message, Toast.LENGTH_SHORT).show();
                            onRegisterSuccess();
                            break;
                        }
                        case ERROR:{
                            showProgressBar(false);
                            Toast.makeText(RegisterActivity.this, userRegisterResource.message, Toast.LENGTH_SHORT).show();
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

    private void onRegisterSuccess(){
        Intent intent = new Intent(this, LoginActivity.class);
        startActivity(intent);
        finish();
    }

    private void showProgressBar(boolean isVisible){
        if (isVisible){
            progressBar.setVisibility(View.VISIBLE);
            registerButton.setEnabled(false);
        } else {
            progressBar.setVisibility(View.GONE);
            registerButton.setEnabled(true);
        }
    }

    private void attemptRegister() {
        if (TextUtils.isEmpty(email.getText().toString())
                || TextUtils.isEmpty(password.getText().toString())
                || TextUtils.isEmpty(name.getText().toString())){
            return;
        }
        viewModel.RegisterUser(email.getText().toString(), password.getText().toString(), name.getText().toString(), school.getText().toString());
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btn_sign_up: {
                attemptRegister();
                break;
            }
            case R.id.login_text: {
                navToLogin();
            }
        }
    }

    private void navToLogin(){
        Intent intent = new Intent(RegisterActivity.this, LoginActivity.class);
        startActivity(intent);
        finish();
    }

}
