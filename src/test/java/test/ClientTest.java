package test;

import bbm.Api.*;
import com.google.inject.Guice;
import com.google.inject.Injector;
import org.junit.Assert;
import org.junit.Test;

/**
 * Created by mario on 1/27/17.
 */
public class ClientTest {

    @Test
    public void testApi(){
        Injector injector = Guice.createInjector(
                new ConfigurationModule("test", "repo", "branch", "commitHash"),
                new TestApiModule()
        );
        Api bbmApi = injector.getInstance(Api.class);
        BuildInstruction myInstructions = null;
        try {
            myInstructions = bbmApi.getBuildInstructions();
        } catch (BBMException e) {
            Assert.fail();
        }
        Assert.assertNotNull(myInstructions);
        Assert.assertEquals(BuildType.DEPLOY, myInstructions.getBuildType());
        Assert.assertEquals("dynOrg1", myInstructions.getSandbox());
    }
}
