package com.rxjava.cap1.classes;

import io.reactivex.Observable;

public class Launcher {
    public static void main(String[] args) {
        /*Observable que contiene un stream fijo de 5 strings*/
        Observable<String> strings = Observable.just("Alpha", "Beta", "Gamma", "Delta", "Epsilon");

        /*Observer que se suscribirá al Observable creado arriba y manipulara los items emitidos*/
        strings.subscribe(s -> System.out.println(s));

        /*Observer con un operador intermedio que transforma los datos entregados por el Observable*/
        strings.map(s -> s.length()).subscribe(x -> System.out.println(x));
    }
}
