package com.rxjava.cap1.classes;

import io.reactivex.Observable;

import java.util.concurrent.TimeUnit;

public class Events {
    public static void main(String[] args) {
        Observable<Long> ejecutarCadaSegundo = Observable.interval(1, TimeUnit.SECONDS);

        ejecutarCadaSegundo.subscribe(x -> System.out.println(x));
        
        sleep(5000);
    }

    private static void sleep(long milisegundos) {
        try {
            Thread.sleep(milisegundos);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
