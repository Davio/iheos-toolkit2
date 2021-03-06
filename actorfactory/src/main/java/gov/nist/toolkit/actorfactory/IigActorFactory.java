package gov.nist.toolkit.actorfactory;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import gov.nist.toolkit.actorfactory.AbstractActorFactory;
import gov.nist.toolkit.actorfactory.SimManager;
import gov.nist.toolkit.actorfactory.client.SimId;
import gov.nist.toolkit.actorfactory.client.Simulator;
import gov.nist.toolkit.actorfactory.client.SimulatorConfig;
import gov.nist.toolkit.actortransaction.client.ActorType;
import gov.nist.toolkit.actortransaction.client.ParamType;
import gov.nist.toolkit.configDatatypes.SimulatorProperties;
import gov.nist.toolkit.configDatatypes.client.TransactionType;
import gov.nist.toolkit.sitemanagement.client.Site;
import gov.nist.toolkit.sitemanagement.client.TransactionBean;
import gov.nist.toolkit.sitemanagement.client.TransactionBean.RepositoryType;

/**
 * Factory for Initiating Imaging Gateway
 * 
 * @author Ralph Moulton / MIR WUSTL IHE Development Project <a
 * href="mailto:moultonr@mir.wustl.edu">moultonr@mir.wustl.edu</a>
 *
 */
public class IigActorFactory extends AbstractActorFactory {
   SimId newID = null;


   static final List <TransactionType> incomingTransactions =
      Arrays.asList(TransactionType.RET_IMG_DOC_SET_GW);

   protected Simulator buildNew(SimManager simm, SimId newID, boolean configureBase) {
      this.newID = newID;

      ActorType actorType = ActorType.INITIATING_IMAGING_GATEWAY;
      SimulatorConfig sc;
      if (configureBase) sc = configureBaseElements(actorType, newID);
      else sc = new SimulatorConfig();
      
      SimId simId = sc.getId();

      addFixedEndpoint(sc, SimulatorProperties.idsrIigEndpoint, actorType,
         TransactionType.RET_IMG_DOC_SET_GW, false);
      addFixedEndpoint(sc, SimulatorProperties.idsrIigTlsEndpoint, actorType,
         TransactionType.RET_IMG_DOC_SET_GW, true);
      addEditableConfig(sc, SimulatorProperties.respondingImagingGateways,
         ParamType.SELECTION, new ArrayList <String>(), true);


      return new Simulator(sc);
   }

   protected void verifyActorConfigurationOptions(SimulatorConfig sc) {

   }

   public Site getActorSite(SimulatorConfig sc, Site site) {
      String siteName = sc.getDefaultName();

      if (site == null) site = new Site(siteName);

      site.user = sc.getId().user; // labels this site as coming from a sim

      boolean isAsync = false;
     
      site.addTransaction(new TransactionBean(
         TransactionType.RET_IMG_DOC_SET_GW.getCode(), RepositoryType.NONE,
         sc.get(SimulatorProperties.idsrIigEndpoint).asString(), false, isAsync));
      site.addTransaction(new TransactionBean(
         TransactionType.RET_IMG_DOC_SET_GW.getCode(), RepositoryType.NONE,
         sc.get(SimulatorProperties.idsrIigTlsEndpoint).asString(), true, isAsync));
      return site;
   }

   public List <TransactionType> getIncomingTransactions() {
      return incomingTransactions;
   }

}
