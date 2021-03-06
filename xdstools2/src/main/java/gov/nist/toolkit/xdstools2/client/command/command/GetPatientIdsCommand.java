package gov.nist.toolkit.xdstools2.client.command.command;

import gov.nist.toolkit.configDatatypes.client.Pid;
import gov.nist.toolkit.xdstools2.client.util.ClientUtils;
import gov.nist.toolkit.xdstools2.shared.command.request.PatientIdsRequest;

import java.util.List;

/**
 * Created by onh2 on 11/7/16.
 */
public abstract class GetPatientIdsCommand extends GenericCommand<PatientIdsRequest,List<Pid>>{
    @Override
    public void run(PatientIdsRequest var1) {
        ClientUtils.INSTANCE.getToolkitServices().getPatientIds(var1,this);
    }
}
