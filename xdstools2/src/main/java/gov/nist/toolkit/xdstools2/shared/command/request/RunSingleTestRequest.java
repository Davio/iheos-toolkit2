package gov.nist.toolkit.xdstools2.shared.command.request;

import gov.nist.toolkit.sitemanagement.client.Site;
import gov.nist.toolkit.xdstools2.shared.command.CommandContext;

/**
 * Created by onh2 on 11/10/16.
 */
public class RunSingleTestRequest extends CommandContext{
    private Site site;
    private int testId;

    public RunSingleTestRequest(){}
    public RunSingleTestRequest(CommandContext context, Site site, int testId){
        copyFrom(context);
        this.site=site;
        this.testId=testId;
    }

    public Site getSite() {
        return site;
    }

    public void setSite(Site site) {
        this.site = site;
    }

    public int getTestId() {
        return testId;
    }

    public void setTestId(int testId) {
        this.testId = testId;
    }
}
