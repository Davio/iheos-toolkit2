package gov.nist.toolkit.simulators.support;

import gov.nist.toolkit.actorfactory.SimDb;
import gov.nist.toolkit.actorfactory.client.NoSimException;
import gov.nist.toolkit.actorfactory.client.SimId;
import gov.nist.toolkit.actorfactory.client.SimulatorConfig;
import gov.nist.toolkit.errorrecording.ErrorRecorder;
import gov.nist.toolkit.errorrecording.GwtErrorRecorder;
import gov.nist.toolkit.errorrecording.GwtErrorRecorderBuilder;
import gov.nist.toolkit.errorrecording.client.ValidationStepResult;
import gov.nist.toolkit.installation.Installation;
import gov.nist.toolkit.utilities.io.Io;
import gov.nist.toolkit.validatorsSoapMessage.engine.ValidateMessageService;
import gov.nist.toolkit.valsupport.client.MessageValidationResults;
import gov.nist.toolkit.valsupport.client.ValidationContext;
import gov.nist.toolkit.valsupport.engine.MessageValidatorEngine;
import gov.nist.toolkit.valsupport.engine.ValidationStep;
import gov.nist.toolkit.valsupport.message.AbstractMessageValidator;
import gov.nist.toolkit.valsupport.message.ServiceRequestContainer;
import gov.nist.toolkit.xdsexception.client.XdsException;
import org.apache.log4j.Logger;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Enumeration;
import java.util.List;

/**
 * All simulators are passed an instance of this class in the constructor giving
 * the simulator access to all the necessary goodies.
 * @author bill
 *
 */

// NOTE: This should be limited to supporting functions for all simulators.
    // References to Regindex, RepIndex, RegistryErrorListGenerator, documentsToAttach should be refactored out
public class SimCommon {
	public SimDb db = null;
	boolean tls = false;
	MessageValidationResults mvr = null;
	public MessageValidatorEngine mvc = null;
	public ValidationContext vc = null;
	public SimulatorConfig simConfig = null;
	public HttpServletRequest request = null;
	public HttpServletResponse response = null;
	OutputStream os = null;
	ValidateMessageService vms = new ValidateMessageService(null);
	boolean faultReturned = false;
	boolean responseSent = false;
	static Logger logger = Logger.getLogger(SimCommon.class);

	public boolean isResponseSent() {
		return faultReturned || responseSent;
	}

   public SimCommon(SimDb db, SimulatorConfig simConfig, boolean tls, ValidationContext vc, MessageValidatorEngine mvc, 
      HttpServletRequest request, HttpServletResponse response) throws IOException, XdsException {
      this(db, tls, vc, mvc, response);
      this.simConfig = simConfig;
      this.request = request;
   }


	/**
	 * Build a new simulator support model
	 * @param db the simulator database model supporting this simulator
	 * @param tls is tls employed
	 * @param vc validation context used to validate the input message
	 * @param mvc message validation engine
	 * @param response HttpServletResponse model for accepting eventual output
	 * @throws IOException
	 * @throws XdsException 
	 */
	public SimCommon(SimDb db, boolean tls, ValidationContext vc, MessageValidatorEngine mvc, HttpServletResponse response) throws IOException, XdsException {

		this.db = db;
		this.tls = tls;
		this.vc = vc;
		this.mvc = mvc;
		this.response = response;
		if (response != null)
			this.os = response.getOutputStream();
        logger.info(String.format("Connection is %s secure", (tls) ? "" : "not"));
	}

    /**
     * Used only to issue soap faults, don't have enough context to do more
     * @param response
     * @throws IOException
     */

    public SimCommon(HttpServletResponse response) throws IOException {
        this.response = response;
        if (response != null)
            os = response.getOutputStream();
    }

	/**
	 * Is TLS enabled?
	 * @return
	 */
	public boolean isTls() { return tls; }

	/**
	 * Return current validation context.
	 * @return
	 */
	public ValidationContext getValidationContext() {
		return vc;
	}

	public void setValidationContext(ValidationContext vc) {
		this.vc = vc;
	}

	ErrorRecorder er = null;

	public ErrorRecorder getCommonErrorRecorder() {
		if (er == null) {
			er = new GwtErrorRecorderBuilder().buildNewErrorRecorder();
			ServiceRequestContainer val = new ServiceRequestContainer(vc);
			mvc.addMessageValidator("Default ErrorRecorder", val, er);
		}

		return er;
	}


	/**
	 * Build the collection of error/statuses/messages for the validation steps
	 * so far. 
	 */

	void buildMVR() {
		mvr = vms.getMessageValidationResults(mvc);
	}


	/**
	 * Get the collection of error/statuses/messages for the validation steps
	 * recorded so far. 
	 */

	public MessageValidationResults getMessageValidationResults() {
		buildMVR();
		return mvr;
	}

	/**
	 * Return the collection of results/status/errors
	 * @return
	 */
	public List<ValidationStepResult> getErrors() {
		buildMVR();
		return mvr.getResults();
	}

	/**
	 * Examine simulator stack - errors found?
	 * @return
	 */
	public boolean hasErrors() {
		buildMVR();
		return mvr.hasErrors();
	}

	void generateLog() throws IOException {
		if (mvc == null || db == null)
			return;
		StringBuffer buf = new StringBuffer();

		//		buf.append(mvc.toString());

		Enumeration<ValidationStep> steps = mvc.getValidationStepEnumeration();
		while (steps.hasMoreElements()) {
			ValidationStep step = steps.nextElement();
			buf.append(step).append("\n");
			ErrorRecorder er = step.getErrorRecorder();
			if (er instanceof GwtErrorRecorder) {
				GwtErrorRecorder ger = (GwtErrorRecorder) er;
				buf.append(ger);
			}
		}


		Io.stringToFile(db.getLogFile(), buf.toString());
	}

	/**
	 * Get MessageValidator off validation queue that is an instance of clas.
	 * @param clas
	 * @return Matching MessageValidator
	 */
	public AbstractMessageValidator getMessageValidatorIfAvailable(@SuppressWarnings("rawtypes") Class clas) {
		return mvc.findMessageValidatorIfAvailable(clas.getCanonicalName());
	}

	public List<String> getValidatorNames() {
		return mvc.getValidatorNames();
	}

	public static ErrorRecorder getUnconnectedErrorRecorder() {
		return new GwtErrorRecorder();
	}


	static public void deleteSim(SimId simulatorId) {
		try {
			logger.info("Delete sim " + simulatorId);
			SimDb simdb = new SimDb(Installation.instance().simDbFile(), simulatorId, null, null);
			File simdir = simdb.getIpDir();
			Io.delete(simdir);
		} catch (IOException e) {
			// doesn't exist - ok
		} catch (NoSimException e) {
			// doesn't exist - ok
		}
	}
	
	public void setLogger(Logger log) {
	   logger = log;
	}
	
	public void sendHttpFault(String em) {
	   sendHttpFault(400, em);
	}
	public void sendHttpFault(int status, String em) {
	   logger.info("HTTP Error response: " + status + " " + em);
	   try {
         response.sendError(status, em);
      } catch (IOException e) {
         logger.warn("IO error sending http response");
      }
	}


}
