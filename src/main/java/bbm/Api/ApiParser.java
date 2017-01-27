package bbm.Api;

/**
 * Created by mario on 1/27/17.
 */
public interface ApiParser {
    BuildInstruction parseResponse(String response) throws BBMException;
}
