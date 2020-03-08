package com.learn.rxjava.observers;

import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;

public class Example01 {
    public static void main(String[] args) {
        Observable<String> source = Observable.just("Alpha", "Beta", "Gamma", "Delta");

        /*Creamos una instancia de Observer*/
        Observer<String> observer = new Observer<String>() {
            @Override
            public void onSubscribe(Disposable disposable) {
                /*nothing to do for the moment*/
            }

            @Override
            public void onNext(String s) {
                System.out.println(s);
            }

            @Override
            public void onError(Throwable throwable) {
                throwable.printStackTrace();
            }

            @Override
            public void onComplete() {
                System.out.println("Finalizado!");
            }
        };

        source.filter(s -> s.contains("t"))
                .subscribe(observer); /*pasamos la instancia de nuestro observer para consumir los datos*/
    }
}
