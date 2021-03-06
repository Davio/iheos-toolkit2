package gov.nist.toolkit.xdstools2.client.command.command;

import gov.nist.toolkit.xdstools2.client.util.ClientUtils;
import gov.nist.toolkit.xdstools2.shared.command.CommandContext;

import java.util.Collection;

/**
 * Created by onh2 on 10/14/16.
 */
public abstract class GetSitesForTestSessionCommand extends GenericCommand<CommandContext,Collection<String>>{
    @Override
    public void run(CommandContext context){
        ClientUtils.INSTANCE.getToolkitServices().getSitesForTestSession(context,this);
    }
}
