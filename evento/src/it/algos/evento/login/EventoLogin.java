package it.algos.evento.login;

import it.algos.webbase.web.lib.LibSession;
import it.algos.webbase.web.login.Login;

/**
 * Created by alex on 29-09-2015.
 */
public class EventoLogin extends Login {

    public static EventoLogin getLogin() {
        Object obj = LibSession.getAttribute(KEY_LOGIN);
        EventoLogin login;
        if(obj == null) {
            login = new EventoLogin();
            LibSession.setAttribute(KEY_LOGIN, login);
        } else {
            login = (EventoLogin)obj;
        }

        return login;
    }

}
