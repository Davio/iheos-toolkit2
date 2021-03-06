package gov.nist.toolkit.xdstools2.client.command.command;

import gov.nist.toolkit.services.client.RawResponse;
import gov.nist.toolkit.xdstools2.client.util.ClientUtils;
import gov.nist.toolkit.xdstools2.shared.command.request.BuildRgTestOrchestrationRequest;

/**
 * Created by onh2 on 11/14/16.
 */
public abstract class BuildRGTestOrchestrationCommand extends GenericCommand<BuildRgTestOrchestrationRequest,RawResponse>{
    @Override
    public void run(BuildRgTestOrchestrationRequest var1) {
        ClientUtils.INSTANCE.getToolkitServices().buildRgTestOrchestration(var1,this);
    }
}
