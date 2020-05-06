package com.learn.rxjava.observables;

import io.reactivex.Observable;
import io.reactivex.observables.ConnectableObservable;

public class ConnectableObservableExample {

    public static void main(String[] args) {
        ConnectableObservable<String> source = Observable.just("Alpha", "Beta", "Gamma", "Delta", "Epsilon")
                .publish();

//        Observer 1
        source.subscribe(s -> System.out.print("Observer 1: " + s + "\n"));

//        Observer 2
        source.map(String::toUpperCase)
                .subscribe(s -> System.out.println("Observer 2: " + s + "\n"));

//        Conectar y recibir el flujo del stream
        source.connect();
    }

}
