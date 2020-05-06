# Programación Reactiva

La programación reactiva es mucho más que una nueva tecnología o una especificación, podria decirse que es una nueva forma o mentalidad de como resolver problemas. La razón por la que es tan efectiva y revolucionaria es porque no estructura todo como una serie de estados, sino como algo que esta en constante cambio. Al ser capaz de capturar la complejidad y la naturaleza dinámica de las cosas, en lugar de representarlas como estados, abre nuevas y poderosas posibilidades de como representar el mundo real con código.

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