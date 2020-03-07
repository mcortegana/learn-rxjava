package com.learn.rxjava.observables;

import io.reactivex.Observable;

public class OnError {
    public static void main(String[] args) {
        Observable<String> observable = Observable.create(emitter -> {
            try {
                emitter.onNext("Alpha");
                emitter.onNext("Beta");
                emitter.onNext("Gamma");
                emitter.onNext("Delta");
                emitter.onNext("Epsilon");
            } catch (Throwable e) {
                emitter.onError(e);
            }
        });

        observable.map(String::length)
                .filter(size -> size >= 5)
                .subscribe(System.out::println, Throwable::printStackTrace);
    }
}
