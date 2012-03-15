package org.openmrs.jira.listeners;

import java.util.Map;

import com.atlassian.crowd.embedded.api.User;
import com.atlassian.jira.ComponentManager;
import com.atlassian.jira.event.issue.AbstractIssueEventListener;
import com.atlassian.jira.event.issue.IssueEvent;
import com.atlassian.jira.issue.Issue;
import com.atlassian.jira.issue.watchers.WatcherManager;

/**
 * Simple JIRA IssueEventListener to automatically add user to issue watcher list. This ensures that commenters/editors
 * automatically get notified of subsequent issue updates, but still allows the user to opt-out by unwatching the issue. 
 * If the user adds another comment or edits the issue again, he/she will be automatically added to the watcher list again.
 */
public class AutowatchJiraListener extends AbstractIssueEventListener {

	private static final String INCLUDE_PROJECT_KEYS = "Comma-separated project keys to include";
	private static final String EXCLUDE_PROJECT_KEYS = "Comma-separated project keys to exclude";
	private static final String[] ACCEPTED_PARAMS = { EXCLUDE_PROJECT_KEYS, INCLUDE_PROJECT_KEYS };

	/**
	 * A list of project keys to be included for autowatch feature
	 */
	private String[] includeProjectKeys = new String[0];

	/**
	 * A list of project keys to be excluded from autowatch feature
	 */
	private String[] excludeProjectKeys = new String[0];

	@Override
	public void init(@SuppressWarnings("rawtypes") Map params) {
		if (params != null) {
			if (params.containsKey(INCLUDE_PROJECT_KEYS)) {
				String value = ((String) params.get(INCLUDE_PROJECT_KEYS));
				if (value != null)
					includeProjectKeys = value.split("\\s*,\\s*"); // comma-separated, excuse whitespace
				if (includeProjectKeys == null)
					includeProjectKeys = new String[0]; // avoid null
			}
			if (params.containsKey(EXCLUDE_PROJECT_KEYS)) {
				String value = ((String) params.get(EXCLUDE_PROJECT_KEYS));
				if (value != null)
					excludeProjectKeys = value.split("\\s*,\\s*"); // comma-separated, excuse whitespace
				if (excludeProjectKeys == null)
					excludeProjectKeys = new String[0]; // avoid null
			}
		}
	}

	@Override
	public String[] getAcceptedParams() {
		return ACCEPTED_PARAMS;
	}

	@Override
	public String getDescription() {
		return "Automatically adds user to issue watcher list, ensuring that people who "
				+ "comment on or edit an issue will get notified of updates, but still have the option to opt-out "
				+ "from future updates by unwatching the issue.  To apply the feature to all projects, leave both "
				+ "lists blank.  To limit to specific projects, enter their keys in 'project keys to include'.  To "
				+ "apply the feature in all but specific projects, list the exclusions in 'project keys to exclude'. "
				+ "If both lists are used, only projects included and not excluded will have the feature.";
	}

	@Override
	public boolean isUnique() {
		return true; // there should only be one instance of this class within JIRA
	}

	@Override
	public void customEvent(IssueEvent event) {
		watchIssue(event.getUser(), event.getIssue());
	}
	
	@Override
	public void issueAssigned(IssueEvent event) {
		watchIssue(event.getUser(), event.getIssue());
	}

	@Override
	public void issueClosed(IssueEvent event) {
		watchIssue(event.getUser(), event.getIssue());
	}

	@Override
	public void issueCommented(IssueEvent event) {
		watchIssue(event.getUser(), event.getIssue());
	}

	@Override
	public void issueCommentEdited(IssueEvent event) {
		watchIssue(event.getUser(), event.getIssue());
	}

	@Override
	public void issueCreated(IssueEvent event) {
		watchIssue(event.getUser(), event.getIssue());
	}
	
	@Override
	public void issueMoved(IssueEvent event) {
		watchIssue(event.getUser(), event.getIssue());
	}
	
	@Override
	public void issueReopened(IssueEvent event) {
		watchIssue(event.getUser(), event.getIssue());
	}

	@Override
	public void issueResolved(IssueEvent event) {
		watchIssue(event.getUser(), event.getIssue());
	}

	@Override
	public void issueStarted(IssueEvent event) {
		watchIssue(event.getUser(), event.getIssue());
	}

	@Override
	public void issueStopped(IssueEvent event) {
		watchIssue(event.getUser(), event.getIssue());
	}

	@Override
	public void issueUpdated(IssueEvent event) {
		watchIssue(event.getUser(), event.getIssue());
	}

	@Override
	public void issueWorkLogged(IssueEvent event) {
		watchIssue(event.getUser(), event.getIssue());
	}
		
	private void watchIssue(User user, Issue issue) {
		// Abort if "projects to exclude" specified and issue's project is among them
		if (excludeProjectKeys.length > 0) {
			String projectKey = issue.getProjectObject().getKey();
			if (arrayContainsString(excludeProjectKeys, projectKey))
				return; // abort if project is in exclusion list
		}
		
		// Abort if "projects to include" specified and isssue's project not among them
		if (includeProjectKeys.length > 0) {
			String projectKey = issue.getProjectObject().getKey();
			if (!arrayContainsString(includeProjectKeys, projectKey))
				return;
		}

		// Add user to watchers for issue if not already in the list
		WatcherManager wm = (WatcherManager) ComponentManager.getComponentInstanceOfType(WatcherManager.class);
		if (!wm.isWatching(user, issue.getGenericValue())) {
			wm.startWatching(user, issue.getGenericValue());
		}
	}

	private boolean arrayContainsString(String[] array, String s) {
		if (s == null)
			return false;
		for (int i = 0; i < array.length; i++)
			if (s.equalsIgnoreCase(array[i]))
				return true;
		return false;
	}

}
