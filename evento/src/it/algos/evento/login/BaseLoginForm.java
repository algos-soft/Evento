package it.algos.evento.login;

import com.vaadin.ui.FormLayout;
import com.vaadin.ui.Notification;
import com.vaadin.ui.Window;
import it.algos.webbase.domain.utente.Utente;
import it.algos.webbase.web.AlgosApp;
import it.algos.webbase.web.dialog.ConfirmDialog;
import it.algos.webbase.web.field.PasswordField;
import it.algos.webbase.web.field.TextField;
import it.algos.webbase.web.form.AFormLayout;

/**
 * Base Login form (UI).
 */
public class BaseLoginForm extends ConfirmDialog implements LoginForm {

    private TextField nameField;
    private PasswordField passField;
    private LoginListener loginListener;

    /**
     * Constructor
     */
    public BaseLoginForm() {
        super(null);
        init();
    }// end of constructor

    /**
     * Initialization <br>
     */
    private void init() {
        FormLayout layout = new AFormLayout();
        layout.setSpacing(true);

        // crea i campi
        nameField = new TextField("Username");
        passField = new PasswordField("Password");

        // aggiunge i campi al layout
        layout.addComponent(nameField);
        layout.addComponent(passField);

        addComponent(layout);
    }// end of method

    private Utente controlloPassword(String nome, String password) {
        Utente utente = null;
        boolean valida = false;
        String pass = "";
        String company = "";

        if (AlgosApp.USE_COMPANY) {
            utente = Utente.read(company, nome);
        } else {
            utente = Utente.read(nome);
        }// end of if/else cycle

        if (utente != null) {
            if (utente.isEnabled()) {
                pass = utente.getPassword();
                if (pass.equals(password)) {
                    valida = true;
                }// end of if cycle
            }// end of if cycle
        }// end of if cycle

        if (!valida) {
            utente = null;
        }// end of if cycle

        return utente;
    }// end of method

    @Override
    protected void onConfirm() {
        String nome = nameField.getValue();
        String password = passField.getValue();
        Utente utente = controlloPassword(nome, password);

        if (utente != null) {
            super.onConfirm();
            utenteLoggato(utente);
        } else {
            utente = Utente.read(nome);
            if (utente == null) {
                Notification.show("Nickname errato", Notification.Type.WARNING_MESSAGE);
            } else {
                Notification.show("Password errata", Notification.Type.WARNING_MESSAGE);
            }// fine del blocco if-else
        }// end of if/else cycle
    }// end of method


    /**
     * Evento generato quando si modifica l'utente loggato <br>
     * <p>
     * Informa (tramite listener) chi è interessato <br>
     */
    private void utenteLoggato(Utente utente) {
        if(loginListener!=null){
            loginListener.onUserLogin(utente);
        }
    }


    @Override
    public void setLoginListener(LoginListener listener) {
        this.loginListener = listener;
    }

    @Override
    public Window getWindow() {
        return this;
    }


}

