package com.objective.kse.ui.base;

import static io.restassured.RestAssured.delete; 

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.sql.ResultSet;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Iterator; 
import java.util.List;

import org.json.JSONException;
import org.json.JSONObject;

import com.objective.kse.ui.utils.DbHelper;

import io.restassured.response.Response;

public class AbstractEventBase extends StakeholderPageBase {
	// Hardcode the milliseconds to zero because that's what we get back from JDBC
	protected DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.'000'X");
	protected ZonedDateTime past = ZonedDateTime.now(ZoneId.of("UTC")).minusMinutes(1);
	protected ZonedDateTime future = ZonedDateTime.now(ZoneId.of("UTC")).plusDays(1);

	protected String formXML;
	protected String formQuery;
	protected String autoForm = "AutoForm";

	/**
     * Removes the test events created during test execution.
     *
     * @param idList The list of event ids created during test execution.
     */
    protected static void cleanUpEventsOld(List idList) {
        if (currentLoggedInUserId == null || !currentLoggedInUserId.equals(adminUser))
            authenticateToApi(adminUser, adminPassword);
        if (idList.size() > 0) {
            LOG.info("{} new events(s) have been created during the test execution. Deleting {}", idList.size(), idList.toString());
            for (Object eventId : idList) {
                Response dc = delete("/events/" + eventId);
                if (dc.getStatusCode() == 204) {
                    LOG.info("Deleted event with ID {}", eventId);
                } else {
                    LOG.info("Could not delete event with ID {} - reason - {}", eventId, dc.statusLine());
                }
            }
        } else {
            LOG.info("No new events have been created during the test execution.");
        }
    }

    
    protected static void cleanUpEvents(List idList) throws Exception {
        if (currentLoggedInUserId == null || !currentLoggedInUserId.equals(adminUser))
            authenticateToApi(adminUser, adminPassword);
        if (idList.size() > 0) {
        	String idCSList = csList(idList);
        	
        	LOG.info("{} new forms(s) have been created during the test execution. Deleting {}", idList.size(), idList.toString());
            DbHelper.executeUpdateQuery("DELETE from     publisher_representation where     representation_event_id in (" + idCSList + ")" );
            DbHelper.executeUpdateQuery("DELETE from     event_submission where     event_submission.event_id in (" + idCSList + ")");
            DbHelper.executeUpdateQuery("DELETE from     publisher_event where event_customer_id = 3 AND   event_id  in (" + idCSList + ")");
        } else {
            LOG.info("No new forms have been created during the test execution.");
        }
    }

    protected static void cleanUpForms(List idList) throws Exception {
        if (currentLoggedInUserId == null || !currentLoggedInUserId.equals(adminUser))
            authenticateToApi(adminUser, adminPassword);
        if (idList.size() > 0) {
        	String idCSList = csList(idList);
        	
        	LOG.info("{} new forms(s) have been created during the test execution. Deleting {}", idList.size(), idList.toString());
            DbHelper.executeUpdateQuery(
            	"delete from publisher_content where content_id in (" + idCSList + ")"
            );
        } else {
            LOG.info("No new forms have been created during the test execution.");
        }
    }

    protected static void cleanUpReps(List idList) throws Exception {
        if (idList.size() > 0) {
        	String idCSList = csList(idList);
        	
        	LOG.info("{} new representation(s) have been created during the test execution. Deleting {}", idList.size(), idList.toString());
            DbHelper.executeUpdateQuery(
            	"delete from publisher_representation where representation_id in (" + idCSList + ")"
            );
        } else {
            LOG.info("No new representations have been created during the test execution.");
        }
    }

	protected static void cleanUpTestUsers(List<String> userList) {
		if (currentLoggedInUserId == null || !currentLoggedInUserId.equals(adminUser))
			authenticateToApi(adminUser, adminPassword);
		if (userList.size() > 0) {
			LOG.info("{} new users have been created during the test execution. Deleting {}", userList.size(), userList.toString());
			for (String name : userList) {
				try {
                    DbHelper.executeUpdateQuery("DELETE FROM person_activation WHERE person_id in (select person_id from publisher_person WHERE person_username = '" +  name + "')");
                    DbHelper.executeUpdateQuery("DELETE FROM publisher_customer_person_lnk WHERE link_person_id in (select person_id from publisher_person WHERE person_username = '" +  name + "')");
					DbHelper.executeUpdateQuery("DELETE FROM publisher_person WHERE person_username = '" +  name + "'");
					LOG.info("The person Id is deleted from the table");
				} catch (Exception e) {
					e.printStackTrace();
				}
				LOG.info("Deleted user with Id {}", id);
			}
			userList.clear();
		} else {
			LOG.info("No new Consultees have been created the test execution.");
		}
	}

    // Turns a list into a comma separated string
	private static String csList(List<?> idList) {
		StringBuilder strbul  = new StringBuilder();
		Iterator<?> iter = idList.iterator();
		while(iter.hasNext())
		{
		    strbul.append(iter.next());
		   if(iter.hasNext()){
		    strbul.append(",");
		   }
		}
		String idCSList = strbul.toString();
		return idCSList;
	}

	protected String getPollURL(int pEventID) {
		return System.getProperty("testEndpoint") + "/kse/event/" + pEventID + "/poll";
	}

	protected String getConsulteeURL() {
		return System.getProperty("consulteeEndpoint") + "/kse/auth";
	}

    /**
     * creates body for new event creation request
     *
     * @param name     name of the event to be created
     * @return a request body as a String
     */
	

	
    protected String newEventRequestBody(String name, String eventType, Integer folderId, String liveStatus, ZonedDateTime startReading,  
    		ZonedDateTime archiveDate, ZonedDateTime startDate, ZonedDateTime endDate, String inviteStatus, Integer imageId,
    		String standaloneStatus, String privacyStatus) {
        JSONObject requestBody = new JSONObject();
        try {
            requestBody.put("name", name);
            if (eventType != null) {
            	requestBody.put("eventType", eventType);
            }
            if (liveStatus != null) {
            	requestBody.put("liveStatus", liveStatus);
            }
            if (folderId != null) {
                JSONObject parentFolder = new JSONObject();
                parentFolder.put("id", folderId);
                parentFolder.put("$ref", "/resources/folders/" + folderId);
                requestBody.put("parentFolder", parentFolder);
            }
            if (startReading != null) {
            	requestBody.put("startReading", startReading.format(formatter));
            }
            if (archiveDate != null) {
            	requestBody.put("archiveRepresentationsDate", archiveDate.format(formatter));
            }
            if (startDate != null) {
            	requestBody.put("startEvent", startDate.format(formatter));
            }
            if (endDate != null) {
            	requestBody.put("endEvent", endDate.format(formatter));
            }
            requestBody.put("inviteStatus", inviteStatus);
/*            if (privacyStatus != null) {
            	requestBody.put("privacyStatus", privacyStatus);
            }*/
            requestBody.put("standaloneStatus", standaloneStatus);
            requestBody.put("imageId", imageId);

        } catch (JSONException e) {
            System.out.println("Failed to create new event request body. See stackTrace:");
            e.printStackTrace();
        }

        return requestBody.toString();
    }


    protected String newEventRequestBody(String name, String eventType, Integer folderId, String liveStatus, String privacyStatus) {
    	// Give the startReading time a one minute fudge factor to allow for clock drift between the db server and the
    	// app server
        return newEventRequestBody(name, eventType, folderId, liveStatus, ZonedDateTime.now(ZoneId.of("UTC")).minusMinutes(1), null, 
        		ZonedDateTime.now(ZoneId.of("UTC")).minusDays(1), ZonedDateTime.now(ZoneId.of("UTC")).plusDays(1), "public", null, "both", privacyStatus);
    }

    protected String newEventRequestBody(String name, String eventType, Integer folderId, String liveStatus, Integer imageId, String privacyStatus) {
    	// Give the startReading time a one minute fudge factor to allow for clock drift between the db server and the
    	// app server
        return newEventRequestBody(name, eventType, folderId, liveStatus, ZonedDateTime.now(ZoneId.of("UTC")).minusMinutes(1), null, 
        		ZonedDateTime.now(ZoneId.of("UTC")).minusDays(1), ZonedDateTime.now(ZoneId.of("UTC")).plusDays(1), "public", imageId, "both", privacyStatus);
    }

    protected String newEventRequestBody(String name, String eventType, Integer folderId, String liveStatus, ZonedDateTime startReading, String privacyStatus) {
    	// Give the startReading time a one minute fudge factor to allow for clock drift between the db server and the
    	// app server
        return newEventRequestBody(name, eventType, folderId, liveStatus, startReading,ZonedDateTime.now(ZoneId.of("UTC")).plusDays(1), 
        		ZonedDateTime.now(ZoneId.of("UTC")).minusDays(1), ZonedDateTime.now(ZoneId.of("UTC")).plusDays(1), "public", null, "both", privacyStatus);
    }

	protected void setEventForm(Integer eventId, Integer formId) {
		try {
			DbHelper.executeUpdateQuery(
					"update publisher_event set event_questionnaire_id = " + formId + ", event_privacy_status = 'anonymous' where event_id = " + eventId
			);
	
		} catch (Exception e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
	}
 
	protected void setUserActive(String pUserName) {
		try {
			DbHelper.executeUpdateQuery(
					"update publisher_person set person_active=1 where  person_username='" + pUserName + "'"
			);
		} catch (Exception e1) {
			e1.printStackTrace();
		}
	}

	protected Integer createForm(Integer folderId, String name) {
		// formType - onepage, twopage, threepage, empty
		try {
			formQuery = readFile("src/test/java/com/objective/kse/ui/utils/sql/FormCreateQuery.sql");
			
			ResultSet rs = DbHelper.executeQuery(
					"declare @folderid int = " + folderId
					+ "  declare @name nvarchar(255) = '" + name + "'"
					+ "	 DECLARE	@return_value int " 
					+ "	 DECLARE   @sequence_id bigint "
					+ "  DECLARE    @now datetime " 
					+ "  DECLARE    @formxml xml "
					+ formXML
					+ formQuery);
			
			if (rs.next()) {
				return rs.getInt(1);
			}
			else {
				return null;
			}
		} catch (Exception e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
			return null;
		}
	}

	protected String readFile(String fileName) throws IOException {
	    BufferedReader br = new BufferedReader(new FileReader(fileName));
	    try {
	        StringBuilder sb = new StringBuilder();
	        String line = br.readLine();
	
	        while (line != null) {
	            sb.append(line);
	            sb.append("\n");
	            line = br.readLine();
	        }
	        return sb.toString();
	    } finally {
	        br.close();
	    }
	}

	
}
