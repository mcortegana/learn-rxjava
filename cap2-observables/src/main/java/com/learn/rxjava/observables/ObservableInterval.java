package com.learn.rxjava.observables;

import io.reactivex.Observable;
import io.reactivex.observables.ConnectableObservable;

import java.util.concurrent.TimeUnit;

public class ObservableInterval {

    public static void main(String[] args) {

        ConnectableObservable<Long> source = Observable.interval(1, TimeUnit.SECONDS).publish();

//        Observer 1
        source.subscribe(s -> System.out.println("Observer 1: " + s));
        source.connect();
        sleep(5000);

//        Observer 2
        source.subscribe(s -> System.out.println("Observer 2: " + s));
        sleep(5000);
    }

    public static void sleep(int milisegundos) {
        try {
            Thread.sleep(milisegundos);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
