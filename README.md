Autowatch Jira
==============
A simple plugin for [Atlassian](http://atlassian.com) [JIRA](http://atlassian.com/jira/) that
automatically adds users to the list of watchers for a ticket when they touch the ticket, changing 
watching from an opt-in into an opt-out (users who, for some reason, don't want to watch an issue
after editing it can still opt-out by unwatching with a single click).

Installation
------------
Use JIRA's awesome plugin manager to install from the plugin repository.

Configuration
-------------
Within JIRA with Autowatch Jira installed, navigate to `Administration > System > Advanced > Listeners` 
and you should see an `Autowatch Jira Listener` entry.  If you click on the Edit link for this listener, you 
will see two settings:

1. <b>Comma-separated project keys to exclude</b>.  Enter the project key(s) for any project(s) that you do
   not want automatic watching to affect.  If there is more than one, place commas between them.  If you enter 
   keys in this list, projects not in this list will still have automatic watching enabled.
2. <b>Comma-separated project keys to include</b>.  If you only want a automatic watching for a few of your 
   projects, then enter the key(s) for those project(s) here and all other projects (not in this list) will 
   not have automatic watching.

In general, you should be using either one of the above settings, but not both.  If you want automatic watching 
for most projects, list the exclusions; if you have lots of projects and only want a few to use automatic watching, 
then list the projects to include (all others will be excluded).
