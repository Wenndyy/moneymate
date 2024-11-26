package com.example.moneymate.View.Dashboard;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.res.ResourcesCompat;
import androidx.fragment.app.Fragment;

import com.example.moneymate.Interface.MessageListener;
import com.example.moneymate.R;
import com.example.moneymate.Controller.UserController;
import com.example.moneymate.View.Account.Login.LoginActivity;

import www.sanju.motiontoast.MotionToast;
import www.sanju.motiontoast.MotionToastStyle;


public class ProfileFragment extends Fragment implements MessageListener {
    private Button logoutBtn;
    private LinearLayout layoutProfile, layoutProgress;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        return inflater.inflate(R.layout.fragment_profile, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        logoutBtn = view.findViewById(R.id.logoutButton);
        layoutProfile = view.findViewById(R.id.layoutProfile);
        layoutProgress = view.findViewById(R.id.layoutProgress);
        logoutBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                UserController account = new UserController("", "", "", "", "", null, null);
                account.setMessageListener(ProfileFragment.this);
                account.logout();
            }
        });
    }

    @Override
    public void onMessageSuccess(String message) {
        SharedPreferences sharedPreferences = getContext().getSharedPreferences("MyPrefs", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.clear();
        editor.apply();
        Intent intent = new Intent(getContext(), LoginActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
        getContext().startActivity(intent);
        showMotionToast("Logout Account",message, MotionToastStyle.SUCCESS);
    }

    @Override
    public void onMessageFailure(String message) {
        showMotionToast("Logout Account",message, MotionToastStyle.ERROR);
    }

    @Override
    public void onMessageLoading(boolean isLoading) {
        if (isLoading){
            layoutProgress.setVisibility(View.VISIBLE);
            layoutProfile.setVisibility(View.GONE);
        }else{
            layoutProgress.setVisibility(View.GONE);
            layoutProfile.setVisibility(View.VISIBLE);
        }
    }

    private void showMotionToast(String title, String message, MotionToastStyle style) {
        MotionToast.Companion.createColorToast(
                getActivity(),
                title,
                message,
                style,
                MotionToast.GRAVITY_BOTTOM,
                MotionToast.LONG_DURATION,
                ResourcesCompat.getFont(getContext(), R.font.poppins_regular)
        );
    }


}