package com.example.mithilesh.twitterdirectmessageapp.mvp.view_model;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;
import android.support.annotation.NonNull;

import com.example.mithilesh.twitterdirectmessageapp.data.Repository;
import com.example.mithilesh.twitterdirectmessageapp.data.local.entities.TwitterUser;
import com.example.mithilesh.twitterdirectmessageapp.di.RepositoryInjector;

import java.util.List;

public class TwitterUserViewModel extends AndroidViewModel {

    private Repository mRepository;

    private LiveData<List<TwitterUser>> mAllUsers;

    public TwitterUserViewModel(@NonNull Application application) {
        super(application);
        mRepository = RepositoryInjector.provideRepository(application);
        mAllUsers = mRepository.getAllUsers();
    }

    public LiveData<List<TwitterUser>> getAllUsers() {
        return mAllUsers;
    }
}
