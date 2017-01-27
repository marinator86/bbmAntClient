package bbm.Api;

import com.google.inject.AbstractModule;

/**
 * Created by mario on 1/27/17.
 */
public class ApiModule extends AbstractModule {
    protected void configure() {
        bind(Api.class).to(ApiImpl.class);
        bind(ApiParser.class).to(ApiParserImpl.class);
    }
}
