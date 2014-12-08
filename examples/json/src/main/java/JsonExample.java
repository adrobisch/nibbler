import de.androbit.nibbler.RestHttpServerConfiguration;
import de.androbit.nibbler.RestServiceBuilder;
import de.androbit.nibbler.json.JacksonConverter;
import de.androbit.nibbler.netty.NettyHttpServer;

import static de.androbit.nibbler.json.JsonSupport.json;

public class JsonExample extends RestServiceBuilder {
  @Override
  public void define() {
    path("/json").get((in, out) -> {
      return out.body(json("Hello World!"));
    });
  }

  public static void main(String[] args) {
    RestHttpServerConfiguration restHttpServerConfiguration = new RestHttpServerConfiguration()
      .withService(new JsonExample())
      .withConverter(new JacksonConverter());

    new NettyHttpServer()
      .startAndWait(restHttpServerConfiguration);
  }
}
