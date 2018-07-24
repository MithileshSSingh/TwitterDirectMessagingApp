package com.example.mithilesh.twitterdirectmessageapp.mvp.view_model;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;
import android.support.annotation.NonNull;

import com.example.mithilesh.twitterdirectmessageapp.data.Repository;
import com.example.mithilesh.twitterdirectmessageapp.data.local.entities.Message;
import com.example.mithilesh.twitterdirectmessageapp.di.RepositoryInjector;

import java.util.List;

public class MessageViewModel extends AndroidViewModel {

    private Repository mRepository;

    private LiveData<List<Message>> mAllMessages;
    private LiveData<List<Message>> mAllMessagesById;
    private LiveData<List<Message>> mAllMessagesByIds;
    private LiveData<List<Message>> mAllUnseenMessages;

    public MessageViewModel(@NonNull Application application) {
        super(application);

        mRepository = RepositoryInjector.provideRepository(application);

        mRepository.getAllMessagesFromDb();
    }

    public LiveData<List<Message>> getAllMessages() {
        mAllMessages = mRepository.getAllMessagesFromDb();
        return mAllMessages;
    }

    public LiveData<List<Message>> getmAllMessagesById(long userId) {
        mAllMessagesById = mRepository.getAllMessagesByIdFromDb(userId);
        return mAllMessagesById;
    }

    public LiveData<List<Message>> getAllMessagesByIds(long userId1, long userId2) {
        mAllMessagesByIds = mRepository.getAllMessagesByIdsFromDb(userId1, userId2);
        return mAllMessagesByIds;
    }

    public LiveData<List<Message>> getAllUnSeenMessages(boolean isSeen) {
        mAllUnseenMessages = mRepository.getAllUnseenMessages(isSeen);
        return mAllUnseenMessages;
    }

    public void insertMessages(List<Message> messages) {
        mRepository.insertIntoDb(messages);
    }

    public void deleteAllMessages() {
        mRepository.deleteAllFromDb();
    }
}
