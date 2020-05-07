# Capítulo 2

## El ***Observable***

Ya vimos la forma básica de como opera un *Observable* ahora veremos como opera un *Observable* a través de sus métodos ***onNext()***, ***onComplete()*** y ***onError()***. 



### **onNext()**

Envía cada ítem uno a la vez desde la fuente *Observable* hasta el *Observer*.

### ***onComplete()***

Informa de la culminación de un evento al *Observer*, indicando que no se realizarán más llamadas *onNext()*.

### ***onError()***

Comunica un error en el flujo de datos al *Observer*, donde es el quien generalmente define cómo manejarlo. A menos que se use un operador retry () para interceptar el error, el flujo de datos desde el *Observable* generalmente termina y no se producen más emisiones de datos.



Hay distintas maneras de crear un *Observable*, definiremos las más practicas y usadas de ellas.



### **Usando Observable.create()**

Este método nos permite crear un *Observable* proporcionando una expresión lambda que reciber un "*Observable* *emitter*". Podemos hacer uso del método *onNext()* del *Observable emitter* para enviar los datos emitidos (uno a la vez) al flujo de datos, el método *onComplete()* para indicar la finalización y que no se emitirán más datos. Estas llamadas al método *onNext()* enviarán los datos hacia el *Observer* donde se imprimirá en consola cada elemento recibido.

```java
import io.reactivex.Observable;

public class ObservableCreate {
    public static void main(String[] args) {
        Observable<String> observable = Observable.create(emitter -> {
            emitter.onNext("Alpha");
            emitter.onNext("Beta");
            emitter.onNext("Gamma");
            emitter.onNext("Delta");
            emitter.onNext("Epsilon");
            emitter.onComplete();
        });

        observable.subscribe(item -> System.out.println(String.format("RECIBIDO: %s", item)));
    }
}
```

La salida en consola es:

```bash
Alpha
Beta
Gamma
Delta
Epsilon

Process finished with exit code 0
```

Si ocurriera un error dentro de nuestro bloque *create()* podemos emitirlo usando el método *onError()*. De esta forma el error será enviado al flujo de datos y manipulado por el *Observer*.

```java
import io.reactivex.Observable;

public class OnError {
    public static void main(String[] args) {
        Observable<String> observable = Observable.create(emitter -> {
            try {
                emitter.onNext("Alpha");
                emitter.onNext("Beta");
                emitter.onNext("Gamma");
                emitter.onNext("Delta");
                emitter.onNext("Epsilon");
            } catch (Throwable e) {
                emitter.onError(e);
            }
        });

        observable.map(String::length)
                .filter(size -> size >= 5)
                .subscribe(System.out::println, Throwable::printStackTrace);
    }
}
```

Como apreciamos también podemos hacer uso de uno o más operadores para filtrar la data.



## Usando **Observable.just()**

Con el método ***just()*** podemos enviar hasta 10 elementos que queramos emitir. Esto invocará el método *onNext()* por cada elemento y luego invocará al método *onComplete()* una vez todos los elementos hayan sido emitidos.

```java
import io.reactivex.Observable;

public class ObservableJust {
    public static void main(String[] args) {
        Observable<Integer> observable = Observable.just(1, 2, 3, 4, 5, 6, 7, 8, 9, 10);

        observable.filter(n -> n >= 5)
                .subscribe(System.out::println);
    }
}
```



## Usando **Observable.fromIterable()**

Este método nos permite crear un *Observable* a partir de un tipo *Iterable* se comporta igual que el método *just()* invocando al método *onNext()* por cada elemento y al método *onComplete()* una vez que todos los elementos fueron emitidos.

```java
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
```



## La interfaz ***Observer***

El tipo *Observer* es una interfaz abstracta implementada en RxJava para comunicar los eventos *onNext()*, *onComplete()* y *onError()* que también son métodos definidos en la interfaz *Observer*.

```java
    package io.reactivex;

    import io.reactivex.disposables.Disposable;

    public interface Observer<T> {
      void onSubscribe(Disposable d);
      void onNext(T value);
      void onError(Throwable e);
      void onComplete();
   }
```

Profundizaremos en el método *onSubscribe()* más adelante, por ahora nos centraremos en los otros 3 métodos.

 Los *Observer*s y *Observables* son algo relativos. Dentro de un mismo contexto un *Observable* es donde inicia el flujo y comienza la emisión de datos.

Por otro lado, cada *Observable* retornado por un operador es internamente un *Observer* que recibe, transforma y emite los datos al siguiente *Observer*. Este no sabe si el siguiente *Observer* es otro operador o el *Observer* final del todo el flujo. Cuando hablamos de *Observers* a menudo nos referimos al *Observer* final que consume el flujo iniciado por el *Observable*. Pero cada operador como el *map()* y el *filter()* implementan la interfaz *Observer* internamente.

### Implementación y Suscripción de un ***Observer***

Cuando llamamos al método *subscribe()* de un *Observable* un *Observer* es usado para consumir los 3 eventos (*onNext()*, *onComplete()* y *onError()*). En lugar de especificar cada metodo como una expresión lambda como hemos venido haciendo. Podemos implementar un *Observer* y pasar una instancia de este al método *subscribe()*.

```java
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
```

```bash
Beta
Delta
Finalizado!

Process finished with exit code 0
```

### **Abreviando Observers con expresiones lambdas**

Implementar un *Observer* puede ser muy complicado y engorroso. Afortunadamente el método *subscribe()* tiene una sobrecarga que acepta expresiones lambdas como argumentos. Probablemente esto sea lo que usemos en la mayoría de casos, podemos especificar 3 parámetros lambda separados por comas en el siguiente orden: el *onNext()*, *onError()* y finalmente el *onComplete()*. Para el ejemplo anterior podemos usar las siguientes expresiones lambdas.

```java
    Consumer<Integer> onNext = i ->  System.out.println("RECEIVED: "          + i);
    
	Consumer<Throwable> onError = Throwable::printStackTrace;
	
    Action onComplete = () -> System.out.println("Done!");
```

Podemos pasar estas expresiones lambdas como argumentos del método *subscribe()*.

```java
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
```

O pasar las expresiones lambda directamente como argumentos del método *subscribe()*

```java
    import io.reactivex.Observable;

    public class Launcher {

      public static void main(String[] args) {

        Observable<String> source =
          Observable.just("Alpha", "Beta", "Gamma", "Delta",
          "Epsilon");
        

        source.map(String::length).filter(i -> i >= 5)
          .subscribe(
            /*onNext()*/
            i -> System.out.println("RECEIVED: " + i),
            /*onError()*/
          Throwable::printStackTrace,
            /*onComplete()*/
		() ->  System.out.println("Done!"));
      }
   }
```

Podemos implementar un *Observer* omitiendo el método *onComplete()*

```java
source.map(String::length).filter(i -> i >= 5)
          .subscribe(
            /*onNext()*/
            i -> System.out.println("RECEIVED: " + i),
            /*onError()*/
          Throwable::printStackTrace,
      }
```

De igual forma podemos implementar un *Observer* solo con el método *onNext()* y omitiendo el *onError()* y *onComplete()*.

```java
source.map(String::length).filter(i -> i >= 5)
          .subscribe(
            /*onNext()*/
            i -> System.out.println("RECEIVED: " + i)
      }
```

### **Observables Fríos VS Observables Calientes**

La relación entre un *Observable* y un *Observer* puede variar dependiendo de la forma en que se ha implementado el *Observable*. Esta diferencia de implementaciones esta dada por los *Cold Observables* y los *Hot Observables*, que definen como los *Observables* se comportan cuando hay muchos *Observers*.

### **Cold Observables** (Observables Fríos)

Los Observables "fríos" son aquellos que no transmiten valores hasta que haya una suscripción activa, ya que la información es producida dentro del Observable y por tanto solo emiten valores en el momento en que se establece una nueva subscripción. Los Observables "fríos" replican toda el stream de datos a cada *Observer*, asegurándose que reciban todos los datos. La mayoría de *Observables* orientados a datos son "fríos" y estos incluyen a los métodos que crean *Observables* que vimos previamente "Observable.just()" y "Observable.fromIterable()".

En el siguiente bloque de código tenemos 2 *Observers* que están suscritos a un *Observable*. El *Observable* primero emitirá todos los datos al primer *Observer*, este una ves recibidos todos los datos invocará al evento *onComplete()*. Es ahora cuando el *Observable* emitirá nuevamente todo el stream de datos pero esta vez al segundo *Observer*  y este al finalizar también invocará al evento *onComplete*. Este es el comportamiento típico de un ***Cold Observable***.

```java
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
```

```bash
Observer 1: Alpha
Observer 1: Beta
Observer 1: Delta
Observer 1: Gamma
Observer 2: Alpha
Observer 2: Beta
Observer 2: Delta
Observer 2: Gamma

Process finished with exit code 0
```

### **Hot Observables (Observables Calientes)**

Un *Observable* "Caliente" emite el mismo stream a todos los *Observers* al mismo tiempo. Si un *Observer* se suscribe a un *Observable "Caliente"* este recibe parte del stream y si luego viene otro *Observer* este no recibirá la parte del stream ya emitido por el *Observable*, es decir consumirá el stream a partir del momento es que se suscribe.

Los *Observables Calientes* se usan a menudo para representar eventos en lugar de un conjunto de datos. Los eventos también pueden contener datos, pero hay que tener en cuenta que es un componente sensible al tiempo y que los *Observers* que se suscriban tarde pueden perder los datos que se emitieron previamente.

### ConnectableObservables

Toma cualquier *Observable* así sea un *Cold Observable* y lo convierte en un *Hot Observable* de manera que todas las emisiones son enviadas a todos los *Observers* a la vez. Para hacer esta conversión simplemente llamamos al método **publish()** en cualquier *Observable* y este instanciará un *ConnectableObservable*. Para que la emisión de los datos inicie es necesario llamar al método **connect()**

Ejemplo:

```java
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
```

El resultado en consola será el siguiente:

```bash
Observer 1: Alpha
Observer 2: ALPHA

Observer 1: Beta
Observer 2: BETA

Observer 1: Gamma
Observer 2: GAMMA

Observer 1: Delta
Observer 2: DELTA

Observer 1: Epsilon
Observer 2: EPSILON


Process finished with exit code 0
```

Notamos como el *Observer 1* recibe la cadena string tal cual es emitida y el *Observer 2* recibe la misma cadena pero la transforma a mayúsculas y ambas son imprimidas en consola de forma intercalada. Ambas suscripciones son configuradas previamente y luego el método *connect()* es llamado para *"disparar"* las emisiones.

Cada emisión va a cada *Observer* simultáneamente, es decir el Observer 1 recibe "Alpha" y el Observer 2 recibe "ALPHA", luego "beta" y "BETA" y así de forma consecutiva. Entonces usando *ConnectableObservable* forzamos a que cada emisión vaya a todos los *Observers* simultáneamente esto también es conocido como **Multicasting**.

## Otros Observables

### Observable.range()

Emite un rango consecutivo de valores enteros. *Observable.range()* emite cada número desde un valor inicial y se incrementa de uno en uno hasta que se llega al límite del contador especificado. Estos valores de valor inicial y contador límite son pasados como parámetros en la llamada al método **range()**

```java
package com.learn.rxjava.observables;

import io.reactivex.Observable;

public class ObservableRange {

    public static void main(String[] args) {
        Observable<Integer> source = Observable.range(12, 10);

        source.subscribe(i -> System.out.println("RECIBIDO: "+i));
    }

}
```

La salida en consola es la siguiente:

```bash
RECIBIDO: 12
RECIBIDO: 13
RECIBIDO: 14
RECIBIDO: 15
RECIBIDO: 16
RECIBIDO: 17
RECIBIDO: 18
RECIBIDO: 19
RECIBIDO: 20
RECIBIDO: 21

Process finished with exit code 0
```

Noten que el valor inicial declarado es 12 el cual se incrementará 10 veces luego de esto se llamará al método *onComplete()*.

### Observable.interval()

Como hemos visto, los *Observables* trabajan emitiendo datos. Estas emisiones son entregadas secuencialmente desde la fuente *Observable* hacia el *Observer*. Pero estas emisiones pueden diferir en un lapso de tiempo dependiendo de cuando la fuente *Observable* las entrega.

Veamos un ejemplo de un *Observable* basado en un temporizador usando *Observable.interval()*. Este emite valores long consecutivos (empezando por 0) según un intervalo de tiempo especificado. En el ejemplo instanciaremos un Observable<Long> que emite un valor cada segundo.

```java
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
```

La salida en consola mostrará lo siguiente:

```bash
0 Mississipi
1 Mississipi
2 Mississipi
3 Mississipi
4 Mississipi

Process finished with exit code 0
```

*Observable.interval()* emitirá valores Long infinitamente según el intervalo de tiempo especificado (en este caso 1 segundo para fines prácticos). Sin embargo ya que opera sobre un temporizador *"timer"*, necesita operar en un hilo separado. Los temas de *Concurrencia y Paralelismo* se cubrirán más adelante. Por ahora, notemos que el método *main()* da inicio al *Observable* pero no espera que este finalice; ya que el *Observable* esta emitiendo en un hilo distinto. Por lo tanto para evitar que el método *main()* termine y salga de la aplicación antes que el *Observable* pueda emitir sus valores usamos el método *sleep()* para mantener la aplicación viva 5 segundas. Esto da el tiempo suficiente al *Observable()* para emitir algunos valores antes que la aplicación termine.

Entonces un *Observable.interval()* retorna un *Hot Observable* o un *Cold Observable*? Ya que es orientado a eventos y es infinito podemos decir que es de tipo *Hot Observable*. Pero que pasaría si en el ejemplo anterior agregamos un segundo *Observer*, esperamos 5 segundos y agregamos otro. ¿Qué es lo que pasaría?

```java
package com.learn.rxjava.observables;

import io.reactivex.Observable;

import java.util.concurrent.TimeUnit;

public class ObservableInterval {

    public static void main(String[] args) {

        Observable<Long> source = Observable.interval(1, TimeUnit.SECONDS);

//        Observer 1
        source.subscribe(s -> System.out.println("Observer 1: " + s));
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
```

La salida en consola muestra lo siguiente:

```bash
Observer 1: 0
Observer 1: 1
Observer 1: 2
Observer 1: 3
Observer 1: 4
Observer 1: 5
Observer 2: 0
Observer 2: 1
Observer 1: 6
Observer 1: 7
Observer 2: 2
Observer 1: 8
Observer 2: 3
Observer 2: 4
Observer 1: 9

Process finished with exit code 0
```

Veamos que ha pasado luego del primer lapso de 5 segundos, cuando el Observer 2 entra en acción. Notamos que cada uno tiene su propio temporizador *timer* y empieza en 0, estos dos *Observers* reciben su propio flujo emitido por el *Observable* y empieza en 0. Entonces este es actualmente un *Cold Observable*. Para poner todos los *Observers* en un solo *timer* y consumiendo la misma emisión de valores tendremos que usar un *ConnectableObservable* para forzar estas emisiones a convertirse en *Hot Observable*.

```java
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
```

La salida en consola es la siguiente:

```bash
Observer 1: 0
Observer 1: 1
Observer 1: 2
Observer 1: 3
Observer 1: 4
Observer 1: 5
Observer 2: 5
Observer 1: 6
Observer 2: 6
Observer 1: 7
Observer 2: 7
Observer 1: 8
Observer 2: 8
Observer 1: 9
Observer 2: 9

Process finished with exit code 0
```

Ahora ya que el Observer 2 se unió 5 segundos tarde ha perdido las emisiones anteriores y esta sincronizado con el Observer 1 desde el segundo 6, recibiendo ambos Observers las mismas emisiones.

### Observable.future()

Ya hemos visto que los *Observables* de RxJava son mucho mas robustos y expresivos que los *Futures*. pero si tenemos una librería existente y que además devuelva *Futures* podemos fácilmente volverlos *Observables* usando *Observable.future()*.

```java
package com.learn.rxjava.observables;

import io.reactivex.Observable;

import java.util.concurrent.Future;

public class ObservableFuture {

    public static void main(String[] args) {
//        Obtener el future
        Future<String> futureValue = ...;

//        Convertir a Observable y consumirlo
        Observable.fromFuture(futureValue)
                .map(String::toUpperCase)
                .subscribe(System.out::println);
    }

}
```

### Observable.empty()

A pesar de que parezca que no tiene utilidad, en ocasiones nos resulta de gran ayuda si necesitamos crear un *Observable*  que no emita ningún valor y llame al método *onComplete()*

```java
package com.learn.rxjava.observables;

import io.reactivex.Observable;

public class ObservableEmpty {

    public static void main(String[] args) {
        Observable<String> empty = Observable.empty();

        empty.subscribe(System.out::println, Throwable::printStackTrace, () -> System.out.println("Finalizado!"));
    }

}
```

En consola se muestra:

```bash
Finalizado!

Process finished with exit code 0
```

Un Observable vacío o empty Observable es esencialmente el concepto nulo de RxJava. Es la ausencia de un valor o valores.

### Observable.never()

Un primo cercano de un Observable.empty(), la única diferencia es que en un *Observable.never()* nunca llama al método *onComplete()*, siempre espera por las emisiones pero nunca entrega nada.

```java
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
```

En consola no se mostrará nada ya que nunca se llama al método *onComplete()*

```bash

Process finished with exit code 0
```

Este Observable es principalmente usado para testing y nunca se usa en producción. Usamos el método sleep() igual que en un Observable.interval() ya que el método main() tampoco espera a que finalice.