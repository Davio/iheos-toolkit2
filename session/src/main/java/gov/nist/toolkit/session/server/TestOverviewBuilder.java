package gov.nist.toolkit.session.server;

import gov.nist.toolkit.session.client.SectionOverviewDTO;
import gov.nist.toolkit.session.client.StepOverviewDTO;
import gov.nist.toolkit.session.client.TestOverviewDTO;
import gov.nist.toolkit.session.server.markdown.Markdown;
import gov.nist.toolkit.session.server.serviceManager.XdsTestServiceManager;
import gov.nist.toolkit.testenginelogging.TestLogDetails;
import gov.nist.toolkit.testenginelogging.client.LogFileContentDTO;
import gov.nist.toolkit.testenginelogging.client.TestStepLogContentDTO;
import gov.nist.toolkit.testkitutilities.ReadMe;
import gov.nist.toolkit.testkitutilities.TestDefinition;
import gov.nist.toolkit.testkitutilities.TestkitBuilder;

import java.io.File;
import java.io.IOException;
import java.util.List;

/**
 *
 */
public class TestOverviewBuilder {
    private TestLogDetails testLogDetails;
    private String testId;
    private File testDir;
    private TestOverviewDTO testOverview = new TestOverviewDTO();
    private XdsTestServiceManager testServiceManager;

    public TestOverviewBuilder(Session session, TestLogDetails testLogDetails) throws Exception {
        this.testLogDetails = testLogDetails;
        this.testId = testLogDetails.getTestInstance().getId();
        this.testDir = TestkitBuilder.getTestDir(testId);
        testServiceManager = new XdsTestServiceManager(session);
    }

    public TestOverviewDTO build() throws Exception {
        testOverview.setPass(true);  // will be updated by addSections()
        testOverview.setName(testId);
        ReadMe readme = new TestDefinition(testDir).getTestReadme();
        testOverview.setTitle(stripHeaderMarkup(readme.line1));
        testOverview.setDescription(Markdown.toHtml(readme.rest));
        addSections();
        return testOverview;
    }

    // Strip off markdown format header markings (#, ##, ###)
    private String stripHeaderMarkup(String in) {
        in = in.trim();
        if (in.charAt(0) != '#') return in;
        while (in.length() > 0 && in.charAt(0) == '#')
            in = in.substring(1);
        if (in.length() > 0)
            return in.trim();
        return in;
    }

    private void addSections() throws Exception {
        List<String> sectionNames = null;
        try {
            sectionNames = testServiceManager.getTestIndex(testId);
        } catch (Exception e) {
            return;
        }
        if (testId.equals("12360")) {
            // No test results available - scan
            // test definition to get section names
            TestDefinition testDefinition = new TestDefinition(testDir);
            try {
                List<String> sections = testDefinition.getSectionIndex();
                for (String section : sections) {
                    LogFileContentDTO logFileContentDTO = testLogDetails.sectionLogMapDTO.get(section);
                    SectionOverviewDTO sectionOverview = addSection(section, logFileContentDTO);
                    if (logFileContentDTO == null)
                        sectionOverview.setRun(false);
                    if (!sectionOverview.isPass())
                        testOverview.setPass(false);
                    try {
                        String sectionReadme = testDefinition.getFullSectionReadme(section);
                        sectionOverview.setDescription(Markdown.toHtml(sectionReadme));
                    } catch (IOException e) {
                        sectionOverview.setDescription("");
                    }
//                    testOverview.addSection(sectionOverview);
                }
                return;
            } catch (Exception e) {
                // no sections defined - maybe single un-named section
                if (testLogDetails.getTestPlanLogs().get("log.xml") != null) {
                    LogFileContentDTO logFileContentDTO = testLogDetails.getTestPlanLogs().get("log.xml");
                    SectionOverviewDTO sectionOverview = addSection("", logFileContentDTO);
                    if (!sectionOverview.isPass())
                        testOverview.setPass(false);
                }
                return;
            }
        }
//        for (String sectionName : sectionNames) {
//            LogFileContentDTO logFileContentDTO = testLogDetails.sectionLogMapDTO.get(sectionName);
//            SectionOverviewDTO sectionOverview = addSection(sectionName, logFileContentDTO);
//            if (!sectionOverview.isPass()) {
//                testOverview.setPass(false);
//            }
//        }
    }

    private SectionOverviewDTO addSection(String sectionName, LogFileContentDTO logFileContentDTO) {
        SectionOverviewDTO sectionOverview = new SectionOverviewDTO();

        sectionOverview.setName(sectionName);
        if (logFileContentDTO == null) {
            sectionOverview.setRun(false);
            testOverview.addSection(sectionOverview);
            return sectionOverview;
        }
        sectionOverview.setPass(logFileContentDTO.isSuccess());
        for (String stepName : logFileContentDTO.getStepMap().keySet()) {
            TestStepLogContentDTO stepContent = logFileContentDTO.getStepLog(stepName);
            addStep(stepName, stepContent, sectionOverview);
        }

        testOverview.addSection(sectionOverview);
        return sectionOverview;
    }

    private void addStep(String stepName, TestStepLogContentDTO stepContent, SectionOverviewDTO sectionOverview) {
        StepOverviewDTO stepOverview = new StepOverviewDTO();

        stepOverview.setName(stepContent.getId());
        stepOverview.setPass(stepContent.isSuccess());
        stepOverview.setDetails(stepContent.getDetails());
        stepOverview.addErrors(stepContent.getSoapFaults());
        stepOverview.addErrors(stepContent.getErrors());
        stepOverview.addErrors(stepContent.getAssertionErrors());
        sectionOverview.addStep(stepName, stepOverview);
    }
}