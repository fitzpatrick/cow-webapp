/**
 * Approved for Public Release: 10-4800. Distribution Unlimited.
 * Copyright 2011 The MITRE Corporation,
 * Licensed under the Apache License,
 * Version 2.0 (the "License");
 *
 * You may not use this file except in compliance with the License.
 * You may obtain a copy of the License at http://www.apache.org/licenses/LICENSE-2.0
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND,
 * either express or implied.
 *
 * See the License for the specific language governing permissions and limitations under the License.
 */

package org.wiredwidgets.cow.webapp.client.page;

import java.util.ArrayList;
import java.util.HashMap;

import org.wiredwidgets.cow.webapp.client.BpmServiceMain;
import org.wiredwidgets.cow.webapp.client.PageManager;
import org.wiredwidgets.cow.webapp.client.bpm.Parse;

import com.google.gwt.user.client.rpc.AsyncCallback;
import com.smartgwt.client.widgets.Label;
import com.smartgwt.client.widgets.events.ClickEvent;
import com.smartgwt.client.widgets.events.ClickHandler;
import com.smartgwt.client.widgets.layout.LayoutSpacer;
import com.smartgwt.client.widgets.layout.VLayout;

public class ViewActiveWorkflows extends PageWidget {
	HashMap<String, String> map;

	public ViewActiveWorkflows() {
		map = new HashMap<String, String>();
		BpmServiceMain.sendGet("/processInstances/active", new AsyncCallback<String>() {
			public void onFailure(Throwable arg0) {
				createPage(new Label("Couldn't access list of active workflows"), PageWidget.PAGE_VIEWACTIVEWORKFLOWS);
			}
			public void onSuccess(String arg0) {
				ArrayList<String> names = Parse.parseTemplateInstances(arg0);
				ArrayList<String> ids = Parse.parseTemplateInstancesIds(arg0);
				for(int i = 0; i < names.size(); i++) {
					map.put(names.get(i), ids.get(i));
				}
				generateBody(names);
			}
		});
	}
	
	protected void generateBody(ArrayList<String> names) {
		if(names.size() == 0) {
			createPage(new Label("No active workflows"), PageWidget.PAGE_VIEWACTIVEWORKFLOWS);
		} else {
			VLayout layout = new VLayout();
			layout.setHeight100();
			layout.setWidth100();
			for(int i = 0; i < names.size(); i++) {
				Label l = new Label(names.get(i));
				l.setStyleName("linkLabel");
				l.addClickHandler(new ClickHandler() {
					public void onClick(ClickEvent event) {
						PageManager.getInstance().setPage(new ViewWorkflow(map.get(((Label)event.getSource()).getTitle()), true));
					}
				});
				layout.addMember(l);
			}
			LayoutSpacer spacer = new LayoutSpacer();
			spacer.setHeight100();
			layout.addMember(spacer);
			createPage(layout, PageWidget.PAGE_VIEWACTIVEWORKFLOWS);
		}
	}

	public void refresh() {
		PageManager.getInstance().setPage(new ViewActiveWorkflows());
	}

}
