package actionoverride;

import com.sforce.soap.metadata.*;
import com.sforce.soap.metadata.Error;
import com.sforce.soap.partner.LoginResult;
import com.sforce.soap.partner.PartnerConnection;
import com.sforce.ws.ConnectionException;
import com.sforce.ws.ConnectorConfig;
import org.apache.tools.ant.BuildException;
import org.apache.tools.ant.Task;
import org.apache.tools.ant.util.StringUtils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by mario on 1/27/17.
 */
public class DefaultTask extends Task {
    private String username;
    private String password;
    private String fields;

    public void setPassword(String password) {
        this.password = password;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void setFields(String fields) {
        this.fields = fields;
    }

    @Override
    public void execute() throws BuildException {
        super.execute();
        ConnectorConfig partnerConfig = new ConnectorConfig();

        partnerConfig.setManualLogin(true);
        partnerConfig.setAuthEndpoint("https://test.salesforce.com/services/Soap/u/38.0");
        partnerConfig.setServiceEndpoint("https://test.salesforce.com/services/Soap/u/38.0");

        PartnerConnection partnerConnection = null;
        LoginResult loginResult;
        try {
            partnerConnection = com.sforce.soap.partner.Connector.newConnection(partnerConfig);
            loginResult = partnerConnection.login(username, password);
        } catch (ConnectionException e) {
            log("Problem with connection!");
            throw new BuildException(e);
        }

        // shove the partner's session id into the metadata configuration then connect
        ConnectorConfig metadataConfig = new ConnectorConfig();
        metadataConfig.setSessionId(loginResult.getSessionId());
        metadataConfig.setServiceEndpoint(loginResult.getMetadataServerUrl().replace('u', 'T'));
        MetadataConnection metadataConnection = null;
        try {
            metadataConnection = com.sforce.soap.metadata.Connector.newConnection(metadataConfig);
        } catch (Exception e) {
            log("Problem with connection!");
            throw new BuildException(e);
        }
        // "AllotmentSet__c","CollectiveBill__c","Contact","Contract__c","Country__c","Medium__c","ParkingSpaceTypeModifier__c"
        String[] objects = fields.split(",");
        List<List<String>> objectNames = new ArrayList<>();
        List<String> currentBlock = new ArrayList<>();
        objectNames.add(currentBlock);
        for (String n : objects) {
            currentBlock.add(n.trim());
            if(currentBlock.size() == 10) {
                currentBlock = new ArrayList<>();
                objectNames.add(currentBlock);
            }
        }

        for(List<String> queryBlock : objectNames){
            log("Looking at block");
            updateBlock(metadataConnection, queryBlock);
        }
    }

    private void updateBlock(MetadataConnection metadataConnection, List<String> currentBlock) {
        ReadResult readResult = null;
        try {
            log("Querying for: " + String.join(",", currentBlock.toArray(new String[]{})));
            readResult = metadataConnection.readMetadata("CustomObject", currentBlock.toArray(new String[]{}));
        } catch (ConnectionException ce) {
            throw new BuildException(ce);
        }
        Metadata[] mdInfo = readResult.getRecords();
        List<Metadata> mdToUpdate = new ArrayList<>();
        log("Number of component info returned: " + mdInfo.length);
        for (Metadata md : mdInfo) {
            if(md == null) continue;
            CustomObject obj = (CustomObject) md;
            mdToUpdate.add(obj);
            log("Custom object full name: " + obj.getFullName());
            for(ActionOverride override : obj.getActionOverrides()){
                //log("Defaulting override: " + override.getActionName());
                override.setType(ActionOverrideType.Default);
                override.setContent(null);
                override.setComment(null);
            }
        }
        try {
            log("Uploading block!");
            SaveResult[] saveResults = metadataConnection.updateMetadata(mdToUpdate.toArray(new Metadata[]{}));
            for(SaveResult result : saveResults){
                log(result.getFullName() + ": " + result.getSuccess());
                Error[] errors = result.getErrors();
                if(errors == null) continue;
                for(Error e : errors)
                    log(e.getMessage());
            }

        } catch (ConnectionException ce) {
            ce.printStackTrace();
        }
    }
}
