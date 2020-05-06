package com.learn.rxjava.observables;

import io.reactivex.Observable;

public class ObservableRange {

    public static void main(String[] args) {
        Observable<Integer> source = Observable.range(12, 10);

        source.subscribe(i -> System.out.println("RECIBIDO: "+i));
    }

}
