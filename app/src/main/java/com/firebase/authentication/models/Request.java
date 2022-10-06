package com.firebase.authentication.models;

public class Request {

    private String requester_uid;
    private boolean hasBeenRequested = false;
    private boolean hasRequestBeenGranted = false;

    public boolean isHasRequestBeenGranted() {
        return hasRequestBeenGranted;
    }

    public void setHasRequestBeenGranted(boolean hasRequestBeenGranted) {
        this.hasRequestBeenGranted = hasRequestBeenGranted;
    }

    public Request() {
    }

    public Request(String requester_uid, boolean hasBeenRequested) {
        this.requester_uid = requester_uid;
        this.hasBeenRequested = hasBeenRequested;
    }

    public String getRequester_uid() {
        return requester_uid;
    }

    public void setRequester_uid(String requester_uid) {
        this.requester_uid = requester_uid;
    }

    public boolean isHasBeenRequested() {
        return hasBeenRequested;
    }

    public void setHasBeenRequested(boolean hasBeenRequested) {
        this.hasBeenRequested = hasBeenRequested;
    }
}
