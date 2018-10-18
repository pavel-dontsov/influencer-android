package co.runloop.influencer.model;

import io.reactivex.annotations.NonNull;

public class Resource<T> {

    public enum Status {
        Completed,
        Progress,
        Error
    }

    private T data;
    private Status status;
    private Throwable error;

    public Resource(@NonNull Status status) {
        this.status = status;
    }

    public Resource(T data, @NonNull Status status) {
        this.data = data;
        this.status = status;
    }

    public Resource(Throwable error) {
        this.error = error;
        status = Status.Error;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(@NonNull Status status) {
        this.status = status;
    }

    public Throwable getError() {
        return error;
    }

    public void setError(Throwable error) {
        this.error = error;
    }
}
