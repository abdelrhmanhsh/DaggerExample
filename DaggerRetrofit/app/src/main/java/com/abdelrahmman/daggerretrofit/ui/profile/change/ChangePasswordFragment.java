package com.abdelrahmman.daggerretrofit.ui.profile.change;

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
import com.abdelrahmman.daggerretrofit.models.DefaultResponse;
import com.abdelrahmman.daggerretrofit.models.LoginResponse;
import com.abdelrahmman.daggerretrofit.ui.login.LoginResource;
import com.abdelrahmman.daggerretrofit.viewmodel.ViewModelProviderFactory;

import javax.inject.Inject;

import dagger.android.support.DaggerFragment;

public class ChangePasswordFragment extends DaggerFragment implements View.OnClickListener {

    private static final String TAG = "ChangePasswordFragment";

    private ChangePasswordViewModel viewModel;

    //ui
    private ProgressBar progressBar;
    private Button btn;
    private EditText currentPassword, newPassword;

    private String email;

    @Inject
    ViewModelProviderFactory providerFactory;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_change_password, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        viewModel = ViewModelProviders.of(this, providerFactory).get(ChangePasswordViewModel.class);

        btn = view.findViewById(R.id.btn_change_password);
        currentPassword = view.findViewById(R.id.current_password_input);
        newPassword = view.findViewById(R.id.new_password_input);
        progressBar = view.findViewById(R.id.progress_bar);

        view.findViewById(R.id.btn_change_password).setOnClickListener(this);

        subscribeObserversUser();
        subscribeObservers();

    }

    private void subscribeObserversUser(){
        viewModel.getAuthenticatedUser().removeObservers(getViewLifecycleOwner());
        viewModel.getAuthenticatedUser().observe(getViewLifecycleOwner(), new Observer<LoginResource<LoginResponse>>() {
            @Override
            public void onChanged(LoginResource<LoginResponse> userAuthResource) {
                if (userAuthResource != null){
                    switch (userAuthResource.status){
                        case AUTHENTICATED:{
                            email = userAuthResource.data.getUser().getEmail();
                            break;
                        }

                        case ERROR:{
                            Toast.makeText(getActivity(), "You can't update your password now\ntry again later", Toast.LENGTH_SHORT).show();
                            break;
                        }
                    }
                }
            }
        });
    }

    private void subscribeObservers(){
        viewModel.updatedUser.removeObservers(getViewLifecycleOwner());
        viewModel.updatedUser.observe(getViewLifecycleOwner(), new Observer<LoginResource<DefaultResponse>>() {
            @Override
            public void onChanged(LoginResource<DefaultResponse> userLoginResource) {
                if (userLoginResource != null){
                    switch (userLoginResource.status){
                        case LOADING:{
                            showProgressBar(true);
                            break;
                        }

                        case AUTHENTICATED:{
                            showProgressBar(false);

                            Toast.makeText(getActivity(), "Password Changed Successfully", Toast.LENGTH_SHORT).show();

                            break;
                        }

                        case ERROR:{
                            showProgressBar(false);
                            Toast.makeText(getActivity(), userLoginResource.message, Toast.LENGTH_SHORT).show();
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

    private void showProgressBar(boolean isVisible){
        if (isVisible){
            progressBar.setVisibility(View.VISIBLE);
            btn.setEnabled(false);
        } else {
            progressBar.setVisibility(View.GONE);
            btn.setEnabled(true);
        }
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btn_change_password: {

                attemptUpdate();

                break;

            }
        }
    }

    private void attemptUpdate() {
        if (TextUtils.isEmpty(currentPassword.getText().toString())
                || TextUtils.isEmpty(newPassword.getText().toString())
                || email == null){

            return;

        }

        viewModel.UpdateUser(currentPassword.getText().toString(), newPassword.getText().toString(), email);
    }

}
