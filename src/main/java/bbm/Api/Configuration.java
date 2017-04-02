package bbm.Api;

/**
 * Created by mario on 1/27/17.
 */
public interface Configuration {
    String getEndpoint();
    String getRepositoryUID();
    String getBranchName();
    String getCommitHash();
    String getUsername();
    String getPassword();
}
