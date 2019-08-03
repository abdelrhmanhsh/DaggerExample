package com.abdelrahmman.daggerretrofit.ui.main;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import com.abdelrahmman.daggerretrofit.BaseActivity;
import com.abdelrahmman.daggerretrofit.R;
import com.abdelrahmman.daggerretrofit.models.LoginResponse;
import com.abdelrahmman.daggerretrofit.ui.profile.ProfileSettingsActivity;
import com.abdelrahmman.daggerretrofit.ui.login.LoginResource;
import com.abdelrahmman.daggerretrofit.viewmodel.ViewModelProviderFactory;

import javax.inject.Inject;

public class MainActivity extends BaseActivity {

    private static final String TAG = "MainActivity";

    private TextView name, email, school;

    private MainViewModel viewModel;

    @Inject
    ViewModelProviderFactory providerFactory;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        name = findViewById(R.id.text_view_name);
        email = findViewById(R.id.text_view_email);
        school = findViewById(R.id.text_view_school);

        viewModel = ViewModelProviders.of(this, providerFactory).get(MainViewModel.class);

        subscribeObservers();
    }

    private void subscribeObservers(){
        viewModel.getAuthenticatedUser().observe(this, new Observer<LoginResource<LoginResponse>>() {
            @Override
            public void onChanged(LoginResource<LoginResponse> userAuthResource) {
                if (userAuthResource != null){
                    switch (userAuthResource.status){
                        case AUTHENTICATED:{
                            setUserDetails(userAuthResource.data);
                            break;
                        }

                        case ERROR:{
                            setErrorDetails(userAuthResource.message);
                            break;
                        }
                    }
                }
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){

            case R.id.profile_settings:
                navToProfileSettings();
                return true;

            case R.id.logout:
                sessionManager.logOut();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void navToProfileSettings(){
        Intent intent = new Intent(MainActivity.this, ProfileSettingsActivity.class);
        startActivity(intent);
    }

    private void setUserDetails(LoginResponse data){
        name.setText(data.getUser().getName());
        email.setText(data.getUser().getEmail());
        school.setText(data.getUser().getSchool());
    }

    private void setErrorDetails(String message){
        name.setText(message);
        email.setText("error");
        school.setText("error");
    }
}
