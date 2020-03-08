package com.learn.rxjava.observers;


import io.reactivex.Observable;
import io.reactivex.functions.Action;
import io.reactivex.functions.Consumer;

public class Example02 {
    public static void main(String[] args) {
        Observable<String> observable = Observable.just("Alpha", "Beta", "Delta", "Gamma");

        Consumer<String> onNext = System.out::println;
        Consumer<Throwable> onError = Throwable::printStackTrace;
        Action onComplete = () -> System.out.println("Finalizado!");

        observable.filter(s -> s.contains("l"))
                .subscribe(onNext, onError, onComplete);
    }
}
