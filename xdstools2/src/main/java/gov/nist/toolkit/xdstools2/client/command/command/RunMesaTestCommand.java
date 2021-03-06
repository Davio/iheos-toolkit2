package gov.nist.toolkit.xdstools2.client.command.command;

import gov.nist.toolkit.results.client.Result;
import gov.nist.toolkit.xdstools2.client.util.ClientUtils;
import gov.nist.toolkit.xdstools2.shared.command.request.RunTestRequest;

import java.util.List;

/**
 * Created by onh2 on 11/7/16.
 */
public abstract class RunMesaTestCommand extends GenericCommand<RunTestRequest,List<Result>>{
    @Override
    public void run(RunTestRequest var1) {
        ClientUtils.INSTANCE.getToolkitServices().runMesaTest(var1,this);
    }
}
