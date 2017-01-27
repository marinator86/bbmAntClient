package test;

import bbm.Api.Api;
import bbm.Api.ApiParser;
import bbm.Api.ApiParserImpl;
import com.google.inject.AbstractModule;

/**
 * Created by mario on 1/27/17.
 */
public class TestApiModule extends AbstractModule {
    protected void configure() {
        bind(Api.class).to(TestApiImpl.class);
        bind(ApiParser.class).to(ApiParserImpl.class);
    }
}
