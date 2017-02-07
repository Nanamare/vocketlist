package com.vongtome.android.logger.network.service;

public interface ErrorChecker<T> {
    void checkError(T data) throws RuntimeException;
}
