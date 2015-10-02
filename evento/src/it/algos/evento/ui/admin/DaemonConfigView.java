package it.algos.evento.ui.admin;

import it.algos.evento.config.GeneralDaemonConfigComponent;

public class DaemonConfigView extends AbsMenuBarView{

	public static final String NAME ="daemon";

	public DaemonConfigView() {
		super();
		GeneralDaemonConfigComponent daemonComp = new GeneralDaemonConfigComponent();
		daemonComp.loadContent();
		addComponent(daemonComp);
	}


}
