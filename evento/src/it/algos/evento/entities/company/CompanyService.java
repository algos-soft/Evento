package it.algos.evento.entities.company;

import com.vaadin.ui.Notification;
import it.algos.evento.demo.DemoDataGenerator;
import it.algos.webbase.domain.utente.Utente;

/**
 * Created by Alex on 03/10/15.
 */
public class CompanyService {

    /**
     * Activate a Company.
     * - create a corrisponding user with the given password
     * - create demo data for the new company
     */
    public static boolean activateCompany(Company company, String password, boolean createData){

        // create a user
        Utente user = new Utente();
        user.setNickname(company.getCompanyCode());
        user.setPassword(password);
        user.setEnabled(true);
        user.save();

        // create demo data in background
        if(createData){
            new Thread(new Runnable() {
                @Override
                public void run() {
                    DemoDataGenerator.createDemoData(company);
                    Notification.show("Creazione dati demo completata per "+company+".");
                }
            }).start();
        }

        return true;
    }



}
