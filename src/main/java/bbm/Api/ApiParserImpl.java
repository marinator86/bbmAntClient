package bbm.Api;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

/**
 * Created by mario on 1/27/17.
 */
public class ApiParserImpl implements ApiParser {
    public BuildInstruction parseResponse(String response) throws BBMException {
        JsonParser parser = new JsonParser();
        JsonObject root = parser.parse(response).getAsJsonObject();
        Boolean success = root.get("success").getAsBoolean();
        if(!success)
            throw new BBMException("BBM Server responded error! Cannot build!");

        JsonObject payLoad = root.get("payload").getAsJsonObject();
        String buildType = payLoad.get("buildType").getAsString();
        final BuildType buildTypeT = getBuildType(buildType);
        final String sandBox = payLoad.get("sandbox").getAsString();
        return getBuildInstruction(buildTypeT, sandBox);
    }

    private BuildInstruction getBuildInstruction(final BuildType buildTypeT, final String sandBox) {
        return new BuildInstruction() {
            public BuildType getBuildType() {
                return buildTypeT;
            }

            public String getSandbox() {
                return sandBox;
            }
        };
    }

    private BuildType getBuildType(String buildType) {
        if(BuildType.DEPLOY.name().equals(buildType))
            return BuildType.DEPLOY;
        if(BuildType.CLEANDEPLOY.name().equals(buildType))
            return BuildType.CLEANDEPLOY;
        return BuildType.NOBUILD;
    }
}
