package test;

import bbm.Api.*;
import com.google.inject.Inject;

/**
 * Created by mario on 1/27/17.
 */
public class TestApiImpl implements Api{

    private final ApiParser parser;

    @Inject
    public TestApiImpl(ApiParser parser){
        this.parser = parser;
    }

    public BuildInstruction getBuildInstructions() throws BBMException {
        return parser.parseResponse("{\"success\":true,\"payload\":{\"sandbox\":\"dynOrg1\",\"buildType\":\"DEPLOY\"}}");
    }
}
