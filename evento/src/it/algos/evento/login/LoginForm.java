package it.algos.evento.login;

import com.vaadin.ui.Component;
import com.vaadin.ui.Window;
import it.algos.webbase.domain.utente.Utente;

/**
 * Interface for a LoginForm.
 */
public interface LoginForm  {
    public void setLoginListener(LoginListener listener);
    public Window getWindow();
    public void setUsername(String name);
    public void setPassword(String password);
}
