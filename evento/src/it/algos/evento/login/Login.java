package it.algos.evento.login;

import com.google.gwt.user.client.Cookies;
import com.vaadin.ui.UI;
import com.vaadin.ui.Window;
import it.algos.webbase.domain.utente.Utente;
import it.algos.webbase.web.lib.ObjectCrypter;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.ShortBufferException;
import javax.xml.bind.DatatypeConverter;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
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
//        writeAuthenticationCookie();
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

    /**
     * Read the authentication cookie on the browser
     */
    private void readAuthenticationCookie(){

        // Read name from cookie
        //  String name = Cookies.getCookie("name");

    }

    /**
     * Read the authentication cookie on the browser
     */
    private void writeAuthenticationCookie(){

        byte[] pass = "www.javacodegeeks.com".getBytes();

        byte[] pKey = new byte[]{0x01, 0x23, 0x45, 0x67, (byte) 0x89, (byte) 0xab, (byte) 0xcd,(byte) 0xef};

        ObjectCrypter crypter = new ObjectCrypter(pass, pKey);

        String userpass = user.getPassword();
        String encpass="";
        try {
            byte[] bytes = crypter.encrypt(userpass);
            encpass = new String(bytes, StandardCharsets.UTF_8);
        } catch (Exception e) {
            e.printStackTrace();
        }

        Cookies.setCookie("login", user.getNickname());
        Cookies.setCookie("password", encpass);
    }

}
