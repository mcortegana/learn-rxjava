package com.learn.rxjava.observables;

import io.reactivex.Observable;

import java.util.concurrent.TimeUnit;

public class ObservableInterval {

    public static void main(String[] args) {

        Observable<Long> source = Observable.interval(1, TimeUnit.SECONDS);

        source.subscribe(s -> System.out.println(s + " Mississipi"));
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
