package com.tangledwebgames.crossfade.auth;

public interface SignInListener {

    void onSuccess();
    void onError(SignInError error);

    enum SignInError {
        CANCEL,
        NETWORK_ERROR,
        UNKNOWN
    }
}
