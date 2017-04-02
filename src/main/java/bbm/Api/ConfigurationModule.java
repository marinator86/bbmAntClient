package bbm.Api;

import com.google.inject.AbstractModule;
import com.google.inject.Provides;

/**
 * Created by mario on 1/27/17.
 */
public class ConfigurationModule extends AbstractModule {

    private final String endpoint;
    private final String repositoryUID;
    private final String branchName;
    private final String commitHash;

    public ConfigurationModule(String endpoint, String repositoryUID, String branchName, String commitHash){
        this.endpoint = endpoint;
        this.repositoryUID = repositoryUID;
        this.branchName = branchName;
        this.commitHash = commitHash;
    }

    protected void configure() {
        //
    }

    @Provides
    Configuration getConfiguration(){
        return new Configuration() {
            public String getEndpoint() {
                return endpoint;
            }

            public String getRepositoryUID() {
                return repositoryUID;
            }

            public String getBranchName() {
                return branchName;
            }

            @Override
            public String getCommitHash() {
                return commitHash;
            }
        };
    }
}
