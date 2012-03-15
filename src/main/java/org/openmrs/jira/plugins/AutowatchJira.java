package org.openmrs.jira.plugins;

import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.InitializingBean;

import webwork.dispatcher.ActionResult;

import com.atlassian.core.ofbiz.CoreFactory;
import com.atlassian.core.util.map.EasyMap;
import com.atlassian.jira.action.ActionNames;
import com.atlassian.jira.event.ListenerManager;
import com.atlassian.jira.extension.Startable;

/**
 * AutowatchJira component responsible for registering our listener.
 */
public class AutowatchJira implements InitializingBean, DisposableBean, Startable {

	private static final String LISTENER_NAME = "Autowatch Jira Listener";
	
	private static boolean started = false;
	
	ListenerManager listenerManager;
	
	public AutowatchJira(ListenerManager listenerManager) {
		this.listenerManager = listenerManager;
	}
	
	/**
	 * Called when JIRA has finished starting up
	 */
	public void start() {
		started = true;
		register();
	}
	
	public void register() {
		if (!started)
			return; // abort if JIRA hasn't finished starting up
		try {
			if (!listenerManager.getListeners().containsKey(LISTENER_NAME)) {
				ActionResult aResult = CoreFactory.getActionDispatcher().execute(ActionNames.LISTENER_CREATE,
						EasyMap.build("name", LISTENER_NAME, "clazz", "org.openmrs.jira.listeners.AutowatchJiraListener"));
			}
		} catch (Exception e) {
			System.out.println("Exception while trying to register listener for Autowatch Jira plugin.");
		}
	}

	/**
	 * Called when plugin is enabled.  Known problem: listener manager contains entry for our listener
	 * even after it has been removed (by disabling the plugin), so re-enabling the plugin doesn't 
	 * re-register the listener.
	 */
	public void afterPropertiesSet() throws Exception {
		register();
	}
	
	/**
	 * Called when plugin is disabled
	 */
	public void destroy() throws Exception {
		try {
			if (listenerManager.getListeners().containsKey(LISTENER_NAME)) {
				ActionResult aResult = CoreFactory.getActionDispatcher().execute(ActionNames.LISTENER_DELETE,
						EasyMap.build("name", LISTENER_NAME, "clazz", "org.openmrs.jira.listeners.AutowatchJiraListener"));
			}
		} catch (Exception e) {
			System.out.println("Exception while trying to remove listener for Autowatch Jira plugin.");
		}
	}
}
