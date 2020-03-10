package com.learn.rxjava.observables;

import io.reactivex.Observable;

import java.util.Arrays;
import java.util.List;

public class ColdObservable {
    public static void main(String[] args) {
        List<String> stringList = Arrays.asList("Alpha", "Beta", "Delta", "Gamma");
        Observable<String> observable = Observable.fromIterable(stringList);

//        Primer Observer
        observable.subscribe(s -> System.out.println("Observer 1: "+s));

//        Segundo Observer
        observable.subscribe(s -> System.out.println("Observer 2: "+s));

    }
}
