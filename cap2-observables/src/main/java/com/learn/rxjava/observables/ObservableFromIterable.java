package com.learn.rxjava.observables;

import io.reactivex.Observable;

import java.util.Arrays;
import java.util.List;

public class ObservableFromIterable {
    public static void main(String[] args) {
        List<String> nombres = Arrays.asList("Miguel", "Juan", "Alex", "Elizabeth", "Zulema", "Veronica");
        Observable<String> observable = Observable.fromIterable(nombres);

        observable.filter(x -> x.startsWith("E"))
                .subscribe(System.out::println);
    }
}
