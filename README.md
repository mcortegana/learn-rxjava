# Programación Reactiva

La programación reactiva es mucho más que una nueva tecnología o una especificación, podria decirse que es una nueva forma o mentalidad de como resolver problemas. La razón por la que es tan efectiva y revolucionaria es porque no estructura todo como una serie de estados, sino como algo que esta en constante cambio. Al ser capaz de capturar la complejidad y la naturaleza dinámica de las cosas, en lugar de representarlas como estados, abre nuevas y poderosas posibilidades de como representar las cosas con código.

La idea fundamental de **ReactiveX** es que *"Los Eventos son datos y los datos son Eventos*".

# Capítulo 1

## Rápida introducción a RxJava

En RxJava el tipo base con el que trabajaremos será el ***Observable***. Esencialmente un **Observable** envía ítems de un tipo determinado **T** a través de una serie de operadores hasta que llegan a un ***Observer*** que consumirá estos ítems.

Por ejemplo creemos una clase Launcher.java con el siguiente código:

```java
import io.reactivex.Observable;
public class Launcher {
      public static void main(String[] args) {
        Observable<String> myStrings =
          Observable.just("Alpha", "Beta", "Gamma", "Delta", 
"Epsilon");
      }
}
```

En el método *main()* tenemos un **Observable<Strings>** que entregará 5 objetos de tipo *String*. Un **Observable** puede entregar datos o eventos desde cualquier fuente virtual, puede ser una consulta a base de datos, datos en tiempo real de tweets de Twitter, etc. En este caso usamos el método **Observable.just()** para entregar una lista fija de 5 strings.

Si ejecutamos el método *main()* no ocurrirá nada ya que solo hemos declarado un ***Observable<String>***. Para hacer que el *Observable* entregue los 5 strings (llamaremos a esto una **emisión** de datos), necesitamos un ***Observer*** que se suscriba a nuestro *Observable* y reciba los items. Crearemos y conectaremos rápidamente un *Observer* y mediante una expresión *Lambda* especificaremos que es lo que hará con cada ítem recibido.

```java
import io.reactivex.Observable;
public class Launcher {
    public static void main(String[] args) {
        /*Observable*/
        Observable<String> strings = Observable.just("Alpha", "Beta", 								"Gamma", "Delta", "Epsilon");
		
        /*Observer*/
        strings.subscribe(s -> System.out.println(s));
    }
}
```

Nuestro *Observable* ha entregado cada objeto de tipo *String* (uno a la vez) a nuestro *Observer* y mediante una expresión *Lambda* hemos impreso en consola cada ítem recibido en el *Observer*.

```bash
"C:\Program Files\Java\jdk1.8.0_211\bin\java.exe"...
Alpha
Beta
Gamma
Delta
Epsilon

Process finished with exit code 0
```



> Una expresión ***Lambda*** es esencialmente una mini función que permite pasar instrucciones sobre que acción se realizará con cada ítem entrante. Todo lo que este a la izquierda de la **->** son parámetros y todo lo que esta a la derecha son acciones.

Podemos usar más de un operador entre el *Observable* y el *Observer* para transformar cada ítem entregado o manipularlos de alguna otra forma. Cada operador retorna un nuevo *Observable* derivado del anterior pero reflejando el cambio realizado en el actual operador. por ejemplo usaremos el operador *map()* para convertir cada *String* en su propia longitud (*lenght()*).

```java
/*Observer con un operador intermedio que transforma los datos entregados por el Observable*/
        strings.map(s -> s.length()).subscribe(x -> 				   									System.out.println(x));
```

En consola nos mostrara el resultado

```bash
Alpha
Beta
Gamma
Delta
Epsilon
5
4
5
5
7

Process finished with exit code 0
```

Como mencionamos anteriormente un *Observable* no solo puede entregar datos sino también *Eventos*. Por ejemplo:

```java
import io.reactivex.Observable;
import java.util.concurrent.TimeUnit;

public class Events {
    public static void main(String[] args) {
        Observable<Long> ejecutarCadaSegundo = Observable.interval(1, 									TimeUnit.SECONDS);
        
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
```

En el código arriba descrito hemos creado un *Observable* que dispara un evento cada segundo que entrega un valor *Long* en cada intervalo especificado (Cada segundo).

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

