package com.learn.rxjava.observables;

import io.reactivex.Observable;

public class ObservableEmpty {

    public static void main(String[] args) {
        Observable<String> empty = Observable.empty();

        empty.subscribe(System.out::println, Throwable::printStackTrace, () -> System.out.println("Finalizado!"));
    }

}
