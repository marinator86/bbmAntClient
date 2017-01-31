package test;

import actionoverride.DefaultTask;
import org.junit.Test;

/**
 * Created by mario on 1/28/17.
 */
public class OverrideDefaultTest {

    @Test
    public void testDefaultTask() {
        DefaultTask task = new DefaultTask();
        task.setUsername("logicline-techuser@logicline.de.dynOrg15");
        task.setPassword("ApcoaTechuser2015!P7elfKoiPxsLg6xAvdwO3z5rd");
        task.setFields("AllotmentSet__c, CollectiveBill__c,Contact ,Contract__c,Country__c,Medium__c,ParkingSpaceTypeModifier__c");
        task.execute();
    }

}
