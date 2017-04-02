package bbm.Api;

import com.google.inject.Inject;
import org.apache.tools.ant.util.TaskLogger;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.MultivaluedMap;
import javax.ws.rs.core.Response;
import java.util.Base64;

/**
 * Created by mario on 1/27/17.
 */
public class ApiImpl implements Api {

    private final Configuration configuration;
    private final ApiParser parser;
    private final TaskLogger logger;

    @Inject
    public ApiImpl (Configuration configuration, ApiParser parser, TaskLogger logger){
        this.configuration = configuration;
        this.parser = parser;
        this.logger = logger;
    }

    public BuildInstruction getBuildInstructions() throws BBMException{
        Client client = ClientBuilder.newClient();
        WebTarget target = client.target(configuration.getEndpoint())
                .path(configuration.getRepositoryUID())
                .path(configuration.getBranchName())
                .path(configuration.getCommitHash());
        String authPrefix = "Basic ";
        String clearCredsString = configuration.getUsername() + ":" + configuration.getPassword();
        byte[] encodedBytes = Base64.getEncoder().encode(clearCredsString.getBytes());

        Response response = target.request(MediaType.APPLICATION_JSON_TYPE)
                .header("Authorization", authPrefix + new String(encodedBytes))
                .get();

        if(response.getStatus() != 200 || !response.hasEntity()){
            handleError(response);
        }
        return parser.parseResponse(response.readEntity(String.class));
    }

    private void handleError(Response response) throws BBMException {
        logger.error("Response status: " + response.getStatus());
        logger.error("Response headers:");
        MultivaluedMap<String, Object> headers = response.getHeaders();
        for(String header : headers.keySet())
            logger.error(header + ": " + headers.get(header));
        logger.error("Response body:");
        if(response.hasEntity()) {
            logger.error(response.readEntity(String.class));
        }
        throw new BBMException("BBM Server has not returned code 200 :-(");
    }
}
