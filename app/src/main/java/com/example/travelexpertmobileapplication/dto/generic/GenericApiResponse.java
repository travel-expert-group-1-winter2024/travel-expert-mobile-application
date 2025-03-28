package com.example.travelexpertmobileapplication.dto.generic;

import java.util.List;

public class GenericApiResponse<T> {
    private T data;
    private List<ErrorInfo> errors;

    public GenericApiResponse() {
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }

    public List<ErrorInfo> getErrors() {
        return errors;
    }

    public void setErrors(List<ErrorInfo> errors) {
        this.errors = errors;
    }
}
