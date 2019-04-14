set @now = GETDATE()

EXEC	@return_value = [dbo].[get_content_id_nextval]
		@sequence_id = @sequence_id OUTPUT


EXEC	@return_value = [dbo].[createContent]
		@content_completed_date = NULL,
		@content_completed_date_set = NULL,
		@content_content_type = 'text/xml',
		@content_xml = @formxml,
		@content_created_by = 57438,
		@content_created_on = @now,
		@content_creation_language = 'en_GB',
		@content_customer_id = 3,
		@content_effective_date = NULL,
		@content_effective_date_set = NULL,
		@content_expiry_date = NULL,
		@content_expiry_date_set = NULL,
		@content_finished_on = NULL,
		@content_folder_id = @folderid,
		@content_live_status = 'private',
		@content_locked_by = NULL,
		@content_metadata = '<metadata xmlns="http://www.limehousesoftware.co.uk"/>',
		@content_modified_by = 57438,
		@content_modified_on = @now,
		@content_name = @name,
		@content_owner = NULL,
		@content_owner_group = NULL,
		@content_read_only = 0,
		@content_representation_id = NULL,
		@content_review_date = NULL,
		@content_review_date_set = NULL,
		@content_short_name = @name,
		@content_started_on = NULL,
		@content_subtype = NULL,
		@content_type = 'questionnaire',
		@content_version_number = '0.1',
		@content_workflow_xml = '<workflowDefinition_content xmlns="http://www.limehousesoftware.co.uk" type="allusers">
  <contentId>1215199</contentId>
</workflowDefinition_content>',
		@content_concurrency_version = 1,
        @content_edited_by=NULL,
        @content_edited_on=NULL,
        @content_edited_reason=NULL,
		@content_id = @sequence_id

SELECT	@sequence_id