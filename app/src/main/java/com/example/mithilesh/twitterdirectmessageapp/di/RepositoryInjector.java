package com.example.mithilesh.twitterdirectmessageapp.di;

import android.content.Context;

import com.example.mithilesh.twitterdirectmessageapp.data.Repository;
import com.example.mithilesh.twitterdirectmessageapp.data.local.LocalDataSource;
import com.example.mithilesh.twitterdirectmessageapp.data.remote.RemoteDataSource;

public class RepositoryInjector {

    public static Repository provideRepository(Context context) {
        return Repository.getInstance(LocalDataSource.getInstance(context), RemoteDataSource.getInstance(context));
    }
}
