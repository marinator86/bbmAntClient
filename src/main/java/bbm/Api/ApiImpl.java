package bbm.Api;

import com.google.inject.Inject;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

/**
 * Created by mario on 1/27/17.
 */
public class ApiImpl implements Api {

    private final Configuration configuration;
    private final ApiParser parser;

    @Inject
    public ApiImpl (Configuration configuration, ApiParser parser){
        this.configuration = configuration;
        this.parser = parser;
    }

    public BuildInstruction getBuildInstructions() throws BBMException{
        Client client = ClientBuilder.newClient();
        WebTarget target = client.target(configuration.getEndpoint())
                .path(configuration.getRepositoryUID())
                .path(configuration.getBranchName());

        Response response = target.request(MediaType.APPLICATION_JSON_TYPE).get();
        if(response.getStatus() != 200)
            throw new BBMException("BBM Server has not returned code 200 :-(");
        return parser.parseResponse(response.readEntity(String.class));
    }
}
