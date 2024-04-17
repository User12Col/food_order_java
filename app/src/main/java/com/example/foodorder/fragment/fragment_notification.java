package com.example.foodorder.fragment;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.foodorder.R;
import com.example.foodorder.adapter.NotificationAdapter;
import com.example.foodorder.api.NotificationApiService;
import com.example.foodorder.models.Food;
import com.example.foodorder.models.Notification;
import com.example.foodorder.models.ResponeObject;
import com.example.foodorder.storage.DataLocalManager;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.List;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.annotations.NonNull;
import io.reactivex.rxjava3.core.Observer;
import io.reactivex.rxjava3.disposables.Disposable;
import io.reactivex.rxjava3.schedulers.Schedulers;

public class fragment_notification extends Fragment {
    private RecyclerView rclNotification;
    private List<Notification> notifications;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_notification, container, false);
        reference(view);
        String userID = DataLocalManager.getUser().getUserID();

        NotificationApiService.notificationApiService.getNotificationByUserID(userID)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<ResponeObject>() {
                    @Override
                    public void onSubscribe(@NonNull Disposable d) {

                    }

                    @Override
                    public void onNext(@NonNull ResponeObject responeObject) {
                        Object data = responeObject.getData();
                        if(data instanceof List<?>){
                            Gson gson = new Gson();
                            Type listType = new TypeToken<List<Notification>>() {}.getType();
                            notifications = gson.fromJson(gson.toJson(data), listType);
                        }
                    }

                    @Override
                    public void onError(@NonNull Throwable e) {

                    }

                    @Override
                    public void onComplete() {
                        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity().getApplicationContext(), LinearLayoutManager.VERTICAL, false);
                        NotificationAdapter notificationAdapter = new NotificationAdapter(getActivity().getApplicationContext(), notifications);
                        rclNotification.setLayoutManager(linearLayoutManager);
                        rclNotification.setAdapter(notificationAdapter);
                    }
                });
        return view;
    }

    private void reference(View view){
        rclNotification = view.findViewById(R.id.rclNotification);
    }
}