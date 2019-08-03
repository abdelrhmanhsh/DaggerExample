package com.abdelrahmman.daggerretrofit.ui.profile.delete;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import com.abdelrahmman.daggerretrofit.BaseFragment;
import com.abdelrahmman.daggerretrofit.R;
import com.abdelrahmman.daggerretrofit.models.DefaultResponse;
import com.abdelrahmman.daggerretrofit.models.LoginResponse;
import com.abdelrahmman.daggerretrofit.ui.login.LoginActivity;
import com.abdelrahmman.daggerretrofit.ui.login.LoginResource;
import com.abdelrahmman.daggerretrofit.viewmodel.ViewModelProviderFactory;

import javax.inject.Inject;


public class DeleteAccountFragment extends BaseFragment implements View.OnClickListener {

    private static final String TAG = "DeleteAccountFragment";

    private DeleteAccountViewModel viewModel;

    //ui
    private ProgressBar progressBar;
    private Button btn;

    private int id;

    @Inject
    ViewModelProviderFactory providerFactory;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_delete_account, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        viewModel = ViewModelProviders.of(this, providerFactory).get(DeleteAccountViewModel.class);

        btn = view.findViewById(R.id.btn_delete_account);
        progressBar = view.findViewById(R.id.progress_bar);

        view.findViewById(R.id.btn_delete_account).setOnClickListener(this);

        subscribeObserversUser();
        subscribeObservers();
    }

    private void subscribeObserversUser() {
        viewModel.getAuthenticatedUser().removeObservers(getViewLifecycleOwner());
        viewModel.getAuthenticatedUser().observe(getViewLifecycleOwner(), new Observer<LoginResource<LoginResponse>>() {
            @Override
            public void onChanged(LoginResource<LoginResponse> userAuthResource) {
                if (userAuthResource != null) {
                    switch (userAuthResource.status) {
                        case AUTHENTICATED: {
                            id = userAuthResource.data.getUser().getId();
                            break;
                        }

                        case ERROR: {
                            Toast.makeText(getActivity(), "You can't update your password now\ntry again later", Toast.LENGTH_SHORT).show();
                            break;
                        }
                    }
                }
            }
        });
    }

    private void subscribeObservers() {
        viewModel.deletedUser.removeObservers(getViewLifecycleOwner());
        viewModel.deletedUser.observe(getViewLifecycleOwner(), new Observer<LoginResource<DefaultResponse>>() {
            @Override
            public void onChanged(LoginResource<DefaultResponse> userLoginResource) {
                if (userLoginResource != null) {
                    switch (userLoginResource.status) {
                        case LOADING: {
                            showProgressBar(true);
                            break;
                        }

                        case AUTHENTICATED: {
                            showProgressBar(false);

                            Toast.makeText(getActivity(), "Account Deleted!", Toast.LENGTH_SHORT).show();

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
            case R.id.btn_delete_account: {
                attemptDelete();
            }
        }
    }

    private void attemptDelete() {
        if (id == 0) {
            return;

        }

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle("This action is irreversible")
                .setMessage("Are you sure you want to delete your account?")
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        viewModel.DeleteUser(id);
                        navToLogout();

                    }
                }).setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });

        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }

    private void navToLogout(){
        sessionManager.logOut();

        Intent intent = new Intent(getActivity(), LoginActivity.class);
        startActivity(intent);
    }

}
