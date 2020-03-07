# Programación Reactiva

La programación reactiva es mucho más que una nueva tecnología o una especificación, podria decirse que es una nueva forma o mentalidad de como resolver problemas. La razón por la que es tan efectiva y revolucionaria es porque no estructura todo como una serie de estados, sino como algo que esta en constante cambio. Al ser capaz de capturar la complejidad y la naturaleza dinámica de las cosas, en lugar de representarlas como estados, abre nuevas y poderosas posibilidades de como representar las cosas con código.

La idea fundamental de **ReactiveX** es que *"Los Eventos son datos y los datos son Eventos*".



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
        Observable<String> observable = Observable.create(emitter -> 		 {
            emitter.onNext("Alpha");
            emitter.onNext("Beta");
            emitter.onNext("Gamma");
            emitter.onNext("Delta");
            emitter.onNext("Epsilon");
            emitter.onComplete();
        });

        observable.subscribe(item -> System.out.println(item));
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
                .subscribe(System.out::println, 		   										Throwable::printStackTrace);
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
        Observable<Integer> observable = Observable.just(1, 2, 3, 4, 												5, 6, 7, 8, 9, 10);

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
        List<String> nombres = Arrays.asList("Miguel", "Juan", 								"Alex", "Elizabeth", "Zulema", "Veronica");
        Observable<String> observable = 													Observable.fromIterable(nombres);

        observable.filter(x -> x.startsWith("E"))
                .subscribe(System.out::println);
    }
}
```

