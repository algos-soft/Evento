package it.asteria.cultura.test;

import it.algos.evento.DemoDataGenerator;
import it.algos.evento.EventoSession;
import it.algos.evento.entities.company.Company;
import it.algos.evento.multiazienda.EQuery;
import it.algos.web.entity.BaseEntity;
import it.algos.evento.entities.prenotazione.Prenotazione;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import com.vaadin.ui.Notification;

public class StressTest implements Runnable {

	@Override
	public void run() {
		//Company comp=EventoApp.COMPANY;
		Company comp= EventoSession.getCompany();
		if (comp!=null) {
			ArrayList<Prenotazione> prenotazioni = testCreate();
			testRead();
			testModify();
		}else{
			Notification.show("Per eseguire lo stress test occorre avere una Company attiva.", Notification.TYPE_ERROR_MESSAGE);
		}
		//testDelete(prenotazioni);
	}
	
	// test create
	private ArrayList<Prenotazione> testCreate(){
		ArrayList<Prenotazione> prenotazioni = new ArrayList<Prenotazione>();
		prenotazioni.addAll(DemoDataGenerator.creaPrenotazioni(null));
		return prenotazioni;
	}
	
	// test delete
	private void testDelete(ArrayList<Prenotazione> prenotazioni){
		for(Prenotazione pren : prenotazioni){
			System.out.println("delete -> "+pren);
			pren.delete();
		}
	}


	
	// test read
	private void testRead(){
		int iterations = 2000;
		List<Prenotazione> lista = (List<Prenotazione>)EQuery.getList(Prenotazione.class);
		for(int i=0; i<iterations; i++){
			Prenotazione pren = (Prenotazione)getEntityRandom(lista);
			System.out.println("read "+i+" -> "+pren);
		}
	}

	
	// test modify
	private void testModify(){
		int iterations = 200;
		List<Prenotazione> lista = (List<Prenotazione>)EQuery.getList(Prenotazione.class);
		for(int i=0; i<iterations; i++){
			Prenotazione pren = (Prenotazione)getEntityRandom(lista);
			pren.setTelRiferimento(""+i);
			pren.save();
			System.out.println("modify "+i+"  -> "+pren);
		}
		
	}
	
	private static BaseEntity getEntityRandom(List lista){
	    int min = 0;
	    int max = lista.size()-1;
	    int randomNum = new Random().nextInt((max - min) + 1) + min;
	    return (BaseEntity)lista.get(randomNum);
	}


	

}
