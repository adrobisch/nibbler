![nibbler](docs/images/nibbler_big.png)

nibbler
=======

nibbler is a HTTP *micro*-service DSL + library for **Java 8** or higher based on [RxNetty](https://github.com/ReactiveX/RxNetty),
the [RxJava](https://github.com/ReactiveX/RxJava) adapter for [Netty](http://netty.io/) developed by Netflix.

<a href="https://travis-ci.org/adrobisch/nibbler"><img src="https://travis-ci.org/adrobisch/nibbler.png?branch=master" /></a>

Download
========

The current version is available in the [maven central repository](http://search.maven.org/#search%7Cga%7C1%7Cnibbler)

Hello World
===========

```java
import de.androbit.nibbler.RestHttpServer;
import de.androbit.nibbler.RestServiceBuilder;
import de.androbit.nibbler.json.JacksonConverter;

import static de.androbit.nibbler.json.JsonSupport.json;

public class JsonExample extends RestServiceBuilder {
  @Override
  public void define() {
    path("/json").get((in, out) -> {
      return out.body(json("Hello World!"));
    });
  }

  public static void main(String[] args) {
    new RestHttpServer()
      .withService(new JsonExample())
      .withConverter(new JacksonConverter())
      .startAndWait();
  }
}
```

Documentation
=============

Check [Read The Docs](http://nibbler.readthedocs.org) for examples and documentation.

Versioning
==========

Starting with version 1.0.0, nibbler will follow [semantic versioning](http://semver.org). During the 0.x releases, the minor (.x) releases may include breaking changes.

License
=======

nibbler is published under the terms of the Apache 2.0 License.
See the [LICENSE](LICENSE) file.