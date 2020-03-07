package com.learn.rxjava.observables;

import io.reactivex.Observable;

public class ObservableJust {
    public static void main(String[] args) {
        Observable<Integer> observable = Observable.just(1, 2, 3, 4, 5, 6, 7, 8, 9, 10);

        observable.filter(n -> n >= 5)
                .subscribe(System.out::println);
    }
}
