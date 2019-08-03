package com.abdelrahmman.daggerretrofit.ui.profile.update;

import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import com.abdelrahmman.daggerretrofit.R;
import com.abdelrahmman.daggerretrofit.models.LoginResponse;
import com.abdelrahmman.daggerretrofit.models.User;
import com.abdelrahmman.daggerretrofit.ui.login.LoginResource;
import com.abdelrahmman.daggerretrofit.viewmodel.ViewModelProviderFactory;

import javax.inject.Inject;

import dagger.android.support.DaggerFragment;

public class UpdateProfileFragment extends DaggerFragment implements View.OnClickListener {
    private static final String TAG = "UpdateProfileFragment";

    private UpdateProfileViewModel viewModel;

    //ui
    private ProgressBar progressBar;
    private Button btn;
    private EditText email, name, school;


    private int id;

    @Inject
    ViewModelProviderFactory providerFactory;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_update_profile, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        viewModel = ViewModelProviders.of(this, providerFactory).get(UpdateProfileViewModel.class);

        progressBar = view.findViewById(R.id.progress_bar);
        btn = view.findViewById(R.id.btn_update_settings);
        email = view.findViewById(R.id.email_input);
        name = view.findViewById(R.id.name_input);
        school = view.findViewById(R.id.school_input);

        view.findViewById(R.id.btn_update_settings).setOnClickListener(this);

        subscribeObservers();
    }

    private void subscribeObservers() {
        viewModel.getAuthenticatedUser().removeObservers(getViewLifecycleOwner());
        viewModel.getAuthenticatedUser().observe(getViewLifecycleOwner(), new Observer<LoginResource<LoginResponse>>() {
            @Override
            public void onChanged(LoginResource<LoginResponse> userLoginResource) {
                if (userLoginResource != null) {
                    switch (userLoginResource.status) {
                        case LOADING: {
                            showProgressBar(true);
                            break;
                        }

                        case AUTHENTICATED: {
                            showProgressBar(false);

                            setUserDetails(userLoginResource.data.getUser());

//                            Log.d(TAG, "onChanged: UPDATE SUCCESS: " + userLoginResource.data.getUser().getEmail());
//                            Log.d(TAG, "onChanged: UPDATE SUCCESS: " + userLoginResource.data.getUser().getName());
//                            Log.d(TAG, "onChanged: UPDATE SUCCESS: " + userLoginResource.data.getUser().getSchool());
//                            Log.d(TAG, "onChanged: UPDATE SUCCESS: " + userLoginResource.data.getUser().getId());

                            break;
                        }

                        case ERROR: {
                            showProgressBar(false);
                            Toast.makeText(getActivity(), userLoginResource.message, Toast.LENGTH_SHORT).show();
                            break;
                        }

                        case NOT_AUTHENTICATED: {
                            showProgressBar(false);
                            break;
                        }

                    }
                }
            }
        });

    }

    private void setUserDetails(User data) {
        email.setText(data.getEmail());
        name.setText(data.getName());
        school.setText(data.getSchool());
        id = data.getId();
    }

    private void showProgressBar(boolean isVisible) {
        if (isVisible) {
            progressBar.setVisibility(View.VISIBLE);
            btn.setEnabled(false);
        } else {
            progressBar.setVisibility(View.GONE);
            btn.setEnabled(true);
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_update_settings: {

                attemptUpdate();
                break;

            }
        }
    }

    private void attemptUpdate() {
        if (TextUtils.isEmpty(email.getText().toString())
                || TextUtils.isEmpty(name.getText().toString())
                || TextUtils.isEmpty(school.getText().toString()) || id == 0) {

            return;

        }

        viewModel.UpdateUser(id, email.getText().toString(), name.getText().toString(), school.getText().toString());
    }
}
