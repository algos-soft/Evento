package it.algos.evento;

import it.algos.web.lib.LibImage;

import com.vaadin.server.Resource;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Image;
import com.vaadin.ui.Label;
import com.vaadin.ui.VerticalLayout;

@SuppressWarnings("serial")
public class SplashScreen extends VerticalLayout {

	public SplashScreen(Resource res) {
		super();
		Label label;

		setWidth("100%");
		setHeight("100%");

		// horizontal: label left + image + label right
		HorizontalLayout layout = new HorizontalLayout();
		layout.setWidth("100%");

		label = new Label();
		layout.addComponent(label);
		layout.setExpandRatio(label, 1.0f);

//		byte[] bytes = Pref.splashImage.getBytes();
//		if (bytes.length>0) {
//			Image img = LibImage.getImage(bytes);
//			layout.addComponent(img);			
//		}
		if (res!=null) {
			Image img = LibImage.getImage(res);
			layout.addComponent(img);			
		}

		label = new Label();
		layout.addComponent(label);
		layout.setExpandRatio(label, 1.0f);

		// vertical: label top + image layout + label bottom
		label = new Label();
		addComponent(label);
		setExpandRatio(label, 1.0f);

		addComponent(layout);

		label = new Label();
		addComponent(label);
		setExpandRatio(label, 1.0f);

	}

}
