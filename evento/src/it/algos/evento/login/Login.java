package it.algos.evento.login;

import com.vaadin.ui.UI;
import com.vaadin.ui.Window;
import it.algos.webbase.domain.utente.Utente;
import it.algos.webbase.web.lib.LibCookie;
import it.algos.webbase.web.lib.LibSession;

import javax.servlet.http.Cookie;
import java.util.ArrayList;

/**
 * Main Login object (Login logic).
 */
public class Login {

    private static final int EXPIRY_TIME_SEC=604800;    // 1 week

    // key to store the Login object in the session
    public static String KEY_LOGIN="login";
    public static String KEY_PASSWORD="password";
    public static String KEY_REMEMBER="rememberlogin";

    private ArrayList<LoginListener> loginListeners = new ArrayList<>();
    private Utente user;
    private LoginForm loginForm;


    public Login() {
        setLoginForm(new BaseLoginForm());
    }

    // displays the Login form
    public void showLoginForm(UI ui){
        if(loginForm!=null){


            String username="";
            String password="";
            boolean remember=false;

            // retrieve login data from the cookies
            Cookie remCookie=LibCookie.getCookie(KEY_REMEMBER);
            if(remCookie!=null){
                String str=remCookie.getValue();
                if(str.equalsIgnoreCase("true")){

                    Cookie userCookie= LibCookie.getCookie(KEY_LOGIN);
                    if(userCookie!=null){
                        username=userCookie.getValue();
                        if(!username.equals("")){

                            Cookie passCookie=LibCookie.getCookie(KEY_PASSWORD);
                            if(passCookie!=null){
                                password=passCookie.getValue();
                            }

                            remember=true;

                        }
                    }

                }
            }


            loginForm.setUsername(username);
            loginForm.setPassword(password);
            loginForm.setRemember(remember);

            Window window = loginForm.getWindow();
            window.center();
            ui.addWindow(window);
        }
    }

    /**
     * Invoked after a successful login happened using the Login form
     */
    protected void userLogin(Utente user, boolean remember){

        // register user
        this.user=user;

        if(remember){
            // create/update the cookies
            LibCookie.setCookie(KEY_LOGIN, user.getNickname(), EXPIRY_TIME_SEC);
            LibCookie.setCookie(KEY_PASSWORD, user.getPassword(), EXPIRY_TIME_SEC);
            LibCookie.setCookie(KEY_REMEMBER, "true", EXPIRY_TIME_SEC);
        }else{
            // delete the cookies
            LibCookie.deleteCookie(KEY_LOGIN);
            LibCookie.deleteCookie(KEY_PASSWORD);
            LibCookie.deleteCookie(KEY_REMEMBER);
        }


        // notify the listeners
        for(LoginListener l : loginListeners){
            l.onUserLogin(user, remember);
        }
    }

    public Utente getUser() {
        return user;
    }



    public void setLoginForm(LoginForm loginForm) {
        this.loginForm = loginForm;
        this.loginForm.setLoginListener(new LoginListener() {
            @Override
            public void onUserLogin(Utente user, boolean remember) {
                userLogin(user, remember);
            }
        });
    }


    /**
     * Attempts a login from the cookies.
     * @return true if success
     */
    public boolean loginFromCookies(){
        boolean success=false;
        Cookie userCookie = LibCookie.getCookie(KEY_LOGIN);
        if(userCookie!=null){
            Cookie passCookie = LibCookie.getCookie(KEY_PASSWORD);
            if (passCookie!=null){
                String username=userCookie.getValue();
                String password=passCookie.getValue();
                if((!username.equals("")) && (!password.equals(""))){
                    user = Utente.validate(username,password);
                    if(user!=null){
                        success=true;
                    }
                }
            }
        }

        // if not success, delete the cookies if existing
        if(!success){
            LibCookie.deleteCookie(KEY_LOGIN);
            LibCookie.deleteCookie(KEY_PASSWORD);
            LibCookie.deleteCookie(KEY_REMEMBER);
        }

        return success;
    }


    /**
     * Adds a LoginListener.
     */
    public void addLoginListener(LoginListener l){
        loginListeners.add(l);
    }

    /**
     * Removes all the login listeners
     */
    public void removeAllListeners(){
        loginListeners.clear();
    }

    /**
     * Registers a unique LoginListener.
     * All the previous LoginListeners are deleted
     */
    public void setLoginListener(LoginListener l){
        removeAllListeners();
        addLoginListener(l);
    }





//    private void writeCookie(){
//
//
//
//        String name = user.getNickname();
//
//        // See if name cookie is already set
//        Cookie nameCookie = getCookieByName("login");
//
//        if (nameCookie != null) {
//            String oldName = nameCookie.getValue();
//            nameCookie.setValue(name);
//            Notification.show("Updated name in cookie from " + oldName + " to " + name);
//
//        } else {
//            // Create a new cookie
//            nameCookie = new Cookie("login", name);
//            nameCookie .setComment("Cookie for storing the name of the user");
//            Notification.show("Stored name " + name + " in cookie");
//        }
//
//        // Make cookie expire in 2 minutes
//        nameCookie.setMaxAge(120);
//
//        // Set the cookie path.
//        nameCookie.setPath(VaadinService.getCurrentRequest() .getContextPath());
//
//        // Save cookie
//        VaadinService.getCurrentResponse().addCookie(nameCookie);
//
//
////        String value = this.user.getNickname();
////        createCookie("login", value, 600);
//
////        byte[] pass = "www.javacodegeeks.com".getBytes();
////
////        byte[] pKey = new byte[]{0x01, 0x23, 0x45, 0x67, (byte) 0x89, (byte) 0xab, (byte) 0xcd,(byte) 0xef};
////
////        ObjectCrypter crypter = new ObjectCrypter(pass, pKey);
////
////        String userpass = user.getPassword();
////        String encpass="";
////        try {
////            byte[] bytes = crypter.encrypt(userpass);
////            encpass = new String(bytes, StandardCharsets.UTF_8);
////        } catch (Exception e) {
////            e.printStackTrace();
////        }
////
////        Cookies.setCookie("login", user.getNickname());
//////        Cookies.setCookie("password", encpass);
//    }

    /**
     * Recupera l'oggetto Login dalla sessione.
     * Se manca lo crea ora e lo registra nella sessione.
     */
    public static Login getLogin() {
        Login login;
        Object obj = LibSession.getAttribute(Login.KEY_LOGIN);
        if (obj == null) {
            login = new EventoLogin();
            LibSession.setAttribute(Login.KEY_LOGIN, login);
        } else {
            login = (Login) obj;
        }
        return login;
    }


}
