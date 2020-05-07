package com.learn.rxjava.observables;

import io.reactivex.Observable;

public class ObservableNever {

    public static void main(String[] args) {
        Observable<String> never = Observable.never();

        never.subscribe(System.out::println, Throwable::printStackTrace, () -> System.out.println("Finalizado!"));
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
