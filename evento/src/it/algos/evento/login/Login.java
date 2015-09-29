package it.algos.evento.login;

import com.vaadin.ui.UI;
import com.vaadin.ui.Window;
import it.algos.webbase.domain.utente.Utente;

import java.util.ArrayList;

/**
 * Main Login object (Login logic).
 */
public class Login {

    // key to store the Login object in the session
    public static String KEY_LOGIN="login";

    private ArrayList<LoginListener> loginListeners = new ArrayList<>();
    private Utente user;
    private LoginForm loginForm;


    public Login() {
        setLoginForm(new BaseLoginForm());
    }

    // displays the Login form
    public void showLoginForm(UI ui){
        if(loginForm!=null){
            Window window = loginForm.getWindow();
            window.center();
            ui.addWindow(window);
        }
    }

    private void userLogin(Utente user){
        this.user=user;
        for(LoginListener l : loginListeners){
            l.onUserLogin(user);
        }
    }

    public Utente getUser() {
        return user;
    }


    public void addLoginListener(LoginListener l){
        loginListeners.add(l);
    }

    public void setLoginForm(LoginForm loginForm) {
        this.loginForm = loginForm;
        this.loginForm.setLoginListener(new LoginListener() {
            @Override
            public void onUserLogin(Utente user) {
                userLogin(user);
            }
        });
    }
}
