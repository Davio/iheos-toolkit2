Multiple Responding Gateways

<h2>Multiple Responding Gateways</h2>

<h3>Purpose / Configuration</h3>
<p>A retrieve request is sent to the the Initiating Image Gateway
for two studies in separate communities using one transfer syntax.
One study is located in Community A, and the second study is located
in Community B.
The Initiating Imaging Gateway is expected to submit retrieve
requests to both communities and provide a consolidated result.
</p>
<table border="1">
 <tr><td>RIG Home Community ID (A)</td><td>urn:oid:1.3.6.1.4.1.21367.13.70.101</td><tr>
 <tr><td>IDS Repository Unique ID (A1)</td><td>1.3.6.1.4.1.21367.13.71.101</td></tr>
 <tr><td>RIG Home Community ID (B)</td><td>urn:oid:1.3.6.1.4.1.21367.13.70.102</td><tr>
 <tr><td>IDS Repository Unique ID (B1)</td><td>1.3.6.1.4.1.21367.13.71.102</td></tr>
 <tr><td>Transfer Syntax UID</td><td>1.2.840.10008.1.2.1</td></tr>
</table>

<h2>Retrieve from multiple Responding Gateways</h2>

<h3>Test Steps</h3>
<ol>
<li/>The Image Document Consumer simulator (IDC sim) sends a Retrieve Image 
Document Set Request (RAD-69 Request) to the Initiating Imaging Gateway 
system under test (IIG SUT), requesting two imaging studies.
<li/>The IIG SUT is expected to process the RAD-69 Request and send a
corresponding Cross Community Retrieve Image Document Set Request
(RAD-75 Request) to the Responding Imaging Gateway A simulator
(RIG A sim) and to the Responding Imaging Gateway B simulator (RIG B sim).
<li/>The RIG A sim will process the RAD-75 Request, generating a 
corresponding RAD-69 request to the Imaging Document Source A1 simulator
(IDS A1 sim).
<li/>The RIG B sim will process the RAD-75 Request, generating a 
corresponding RAD-69 request to the Imaging Document Source B1 simulator
(IDS B1 sim).
<li/>The IDS A1 sim will process the RAD-69 request, generating a response
containing the requested images.
<li/>The IDS B1 sim will process the RAD-69 request, generating a response
containing the requested images.
<li/>The RIG A sim and RIG B sim will process the RAD-69 response,
each generating a RAD-75 response. Those separate RAD-75 responses
are returned to the IIG SUT.
<li/>The IIG SUT is expected to process the RAD-75 responses, generating
one RAD-69 response and returning it to the IDC sim.
<li/>The IDC sim will process the RAD-69 response and store the received
images.
</ol>
</p>

<h2>Validate-A RAD-75 from SUT to rig_a</h2>

<p>
The following validations are performed:
<ol>
<li>Correct format of RAD-75 request generated by the IIG SUT to Responding
Image Gateway simulator a, including home community ID, repository unique ID, 
and Transfer Syntax UID(s).
<li/>Correct images requested and correct content.
</ol>
</p>

<h2>Validate-B RAD-75 from SUT to rig_b</h2>

<p>
The following validations are performed:
<ol>
<li>Correct format of RAD-75 request generated by the IIG SUT to Responding
Image Gateway simulator b, including home community ID, repository unique ID, 
and Transfer Syntax UID(s).
<li/>Correct images requested and correct content.
</ol>
</p>

<h2>Validate-R</h2>

<p>
The following validations are performed:
<ol>
<li>Correct format of RAD-69 response generated by the IIG SUT.
<li>Correct content of RAD-75 requests and RAD-69 response, including
home community ID, repository unique ID, mime type and status.
</ol>
</p>

<h2>Validate-Img Image documents returned in RAD-69 response generated by the
IIG SUT.</h2>

<p>
The following validations are performed:
<ol>
<li/>Returned image(s) match those that were requested.
<li/>Correct image content in DICOM tags:<ul>
<li/>SOPClassUID
<li/>SOPInstanceUID
<li/>PatientID
<li/>PatientBirthDate
<li/>PatientSex
<li/>StudyInstanceUID
<li/>SeriesInstanceUID
</ul>
</ol>
</p>