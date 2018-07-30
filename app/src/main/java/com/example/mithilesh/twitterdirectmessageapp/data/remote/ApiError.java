package com.example.mithilesh.twitterdirectmessageapp.data.remote;

import com.twitter.sdk.android.core.TwitterApiException;

public class ApiError {
    public int errorCode;
    public String msgError;

    public static ApiError getApiError(Throwable t) {
        TwitterApiException exception = (TwitterApiException) t;
        ApiError apiError = new ApiError();

        if (exception != null) {
            apiError.errorCode = exception.getStatusCode();
            apiError.msgError = exception.getErrorMessage();
        } else {

            apiError.errorCode = 0;
            apiError.msgError = "Error";

        }

        return apiError;
    }

    @Override
    public String toString() {
        return "ApiError{" + "errorCode=" + errorCode + ", msgError=" + msgError + '}';
    }
}