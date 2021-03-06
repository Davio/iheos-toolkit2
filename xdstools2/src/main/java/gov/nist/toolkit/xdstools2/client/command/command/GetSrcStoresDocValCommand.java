package gov.nist.toolkit.xdstools2.client.command.command;

import gov.nist.toolkit.results.client.Result;
import gov.nist.toolkit.xdstools2.client.util.ClientUtils;
import gov.nist.toolkit.xdstools2.shared.command.request.GetSrcStoresDocValRequest;

import java.util.List;

/**
 * Created by onh2 on 10/31/16.
 */
public abstract class GetSrcStoresDocValCommand extends GenericCommand<GetSrcStoresDocValRequest,List<Result>>{
    @Override
    public void run(GetSrcStoresDocValRequest request) {
        ClientUtils.INSTANCE.getToolkitServices().srcStoresDocVal(request,this);
    }
}
