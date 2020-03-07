package com.learn.rxjava.observables;

import io.reactivex.Observable;

public class ObservableCreate {
    public static void main(String[] args) {
        Observable<String> observable = Observable.create(emitter -> {
            emitter.onNext("Alpha");
            emitter.onNext("Beta");
            emitter.onNext("Gamma");
            emitter.onNext("Delta");
            emitter.onNext("Epsilon");
            emitter.onComplete();
        });

        observable.subscribe(item -> System.out.println(String.format("RECIBIDO: %s", item)));
    }
}
