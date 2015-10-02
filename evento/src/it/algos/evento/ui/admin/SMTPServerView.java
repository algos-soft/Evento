package it.algos.evento.ui.admin;

import it.algos.evento.config.SMTPServerConfigComponent;

public class SMTPServerView extends AbsMenuBarView {

	public static final String NAME ="smtp";

	public SMTPServerView() {
		super();
		SMTPServerConfigComponent smtpComp = new SMTPServerConfigComponent();
		smtpComp.loadContent();
		addComponent(smtpComp);
	}

}
