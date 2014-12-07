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
