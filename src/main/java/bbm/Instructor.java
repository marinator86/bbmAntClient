package bbm;

import bbm.Api.*;
import com.google.inject.Guice;
import com.google.inject.Injector;
import org.apache.tools.ant.BuildException;
import org.apache.tools.ant.Task;
import org.apache.tools.ant.util.TaskLogger;

/**
 * Created by mario on 1/26/17.
 */
public class Instructor extends Task{

    public static final String PROPERTY_DYNORG_POSTFIX = "dynOrg.postfix";
    public static final String PROPERTY_DYNORG_BUILD_TYPE = "dynOrg.buildType";

    private String bbmUrl;
    private String repositoryUID;
    private String branchName;
    private String commitHash;

    public void setBbmUrl(String bbmUrl) {
        this.bbmUrl = bbmUrl;
    }

    public void setRepositoryUID(String repositoryUID) {
        this.repositoryUID = repositoryUID;
    }

    public void setBranchName(String branchName) {
        this.branchName = branchName;
    }

    public void setCommitHash(String commitHash) {
        this.commitHash = commitHash;
    }

    @Override
    public void execute() throws BuildException {
        super.execute();
        Injector injector = Guice.createInjector(
                new ConfigurationModule(bbmUrl, repositoryUID, branchName, commitHash),
                new ApiModule(),
                new TaskLoggerModule(new TaskLogger(this))
        );

        Api bbmApi = injector.getInstance(Api.class);
        BuildInstruction myInstructions;
        log("Retrieving build instructions for branch: " + branchName);
        log("Retrieving build instructions for repository " + repositoryUID);
        log("Retrieving build instructions from " + bbmUrl);
        try {
            myInstructions = bbmApi.getBuildInstructions();
        } catch (BBMException e) {
            log("Oops, something went wrong with bbm!");
            throw new BuildException(e);
        }
        getProject().setProperty(PROPERTY_DYNORG_POSTFIX, myInstructions.getSandbox());
        getProject().setProperty(PROPERTY_DYNORG_BUILD_TYPE, myInstructions.getBuildType().name());
    }
}
