package gov.nist.toolkit.tookitApi;

import gov.nist.toolkit.toolkitServicesCommon.DocumentContent;
import gov.nist.toolkit.toolkitServicesCommon.RefList;

/**
 *
 */
public interface DocumentRegRep  extends AbstractActorInterface /*SimConfig*/ {

    RefList findDocumentsForPatientID(String patientID) throws ToolkitServiceException;
    String getDocEntry(String id) throws ToolkitServiceException;
    DocumentContent getDocument(String uniqueId) throws ToolkitServiceException;
}