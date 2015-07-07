package it.algos.evento.entities.lettera.allegati;

import it.algos.evento.multiazienda.EModulePop;
import it.algos.evento.multiazienda.EQuery;
import it.algos.web.Application;
import it.algos.web.form.AForm;
import it.algos.web.lib.LibFile;
import it.algos.web.table.ATable;
import it.algos.web.updown.FileUploader;
import it.algos.web.updown.FileUploader.UploadFinishedListener;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import javax.activation.DataSource;
import javax.mail.util.ByteArrayDataSource;
import javax.persistence.metamodel.Attribute;

import org.apache.commons.beanutils.BeanComparator;
import org.apache.commons.collections.Transformer;
import org.apache.commons.collections.comparators.TransformingComparator;

import com.vaadin.data.Item;
import com.vaadin.ui.Notification;
import com.vaadin.ui.UI;

import eu.medsea.mimeutil.MimeType;

@SuppressWarnings("serial")
public class AllegatoModulo extends EModulePop {

	private ArrayList<AllegatoListener> listeners = new ArrayList<AllegatoListener>();

	/**
	 * Table high-level events
	 */
	public interface AllegatoListener {
		public void added_(); // attachment added

		public void deleted_(); // attachment deleted

		public void renamed_(); // attachment renamed
	}// end of interface

	public void addAllegatoListener(AllegatoListener listener) {
		listeners.add(listener);
	}// end of method

	private void fire(AllegatoEvent event) {
		for (AllegatoListener l : listeners) {
			switch (event) {
			case added:
				l.added_();
				break;
			case deleted:
				l.deleted_();
				break;
			case renamed:
				l.renamed_();
				break;
			default:
				break;
			}// end of switch cycle
		}// end of for cycle
	}// end of method

	public enum AllegatoEvent {
		added, deleted, renamed;
	}// end of enumeration

	public AllegatoModulo() {
		super(Allegato.class);
	}// end of constructor

	protected Attribute<?, ?>[] creaFieldsAll() {
		return new Attribute[] { Allegato_.name };
	}// end of method

	@Override
	public ATable createTable() {
		return (new AllegatoTable(this));
	}// end of method

	@Override
	public AForm createForm(Item item) {
		return (new AllegatoForm(this, item));
	}// end of method

	/**
	 * Aggiunge un allegato.
	 * <p>
	 * Lo carica dal client al server, carica il file nel database, cancella il file dal server. Al termine lancia un
	 * evento ai listener registrati
	 */
	public void addAllegato() {

		FileUploader uploader = new FileUploader(Application.UPLOAD_FOLDER_NAME);
		uploader.setTitle("Importazione allegati");
		uploader.setButtonText("importa");
		uploader.setOverwriteExisting(true);

		uploader.addUploadFinishedListener(new UploadFinishedListener() {

			@Override
			public void uploadFinished(final Path path) {
				byte[] bytes;
				File file = path.toFile();
				long length = file.length();
				
				if (length<=2048000) {
					
					String name = file.getName();
					Object result = EQuery.queryFirst(Allegato.class, Allegato_.name, name);
					if (result == null) {
						try {
					    	Allegato allegato = fileToAllegato(file);
							allegato.save();
							fire(AllegatoEvent.added);
						} catch (IOException e) {
							Notification.show(e.getMessage());
							e.printStackTrace();
						}
					} else {
						Notification.show("Il file " + name + " esiste già");
					}

				} else {
					Notification.show("La dimensione massima per gli allegati è 2MB");
				}
				
				// always deletes the temporary uploaded file
				file.delete();

			}// end of method
			
		});// end of anonymous class

		uploader.show(UI.getCurrent());

	}

	/**
	 * Cancella un allegato
	 * <p>
	 * 
	 * @param name
	 *            il nome dell'allegato da cancellare
	 */
	public void deleteAllegato(String name) {
		Allegato allegato = getAllegato(name);
		if (allegato != null) {
			allegato.delete();
			fire(AllegatoEvent.deleted);
		}
	}

	/**
	 * Rinomina un allegato
	 * <p>
	 * 
	 * @param name
	 *            il nuovo nome
	 */
	public void renameAllegato(String oldName, String newName) {
		Allegato allegato = getAllegato(newName);
		if (allegato == null) {
			allegato = getAllegato(oldName);
			if (allegato != null) {
				allegato.setName(newName);
				allegato.save();
				fire(AllegatoEvent.renamed);
			}
		} else {
			Notification.show("Questo nome esiste già.");
		}

	}

	/**
	 * Recupera un allegato
	 * <p>
	 * 
	 * @param name
	 *            il nome del file
	 * @return l'allegato corrispondente
	 */
	public static Allegato getAllegato(String name) {
		Allegato allegato = null;
		Object result = EQuery.queryFirst(Allegato.class, Allegato_.name, name);
		if ((result != null) && (result instanceof Allegato)) {
			allegato = (Allegato) result;
		}
		return allegato;
	}

	/**
	 * Recupera un allegato come DataSource
	 * <p>
	 * 
	 * @param name
	 *            il nome del file
	 * @return il DataSource corrispondente
	 */
	public static DataSource getDataSource(String name) {
		ByteArrayDataSource bds = null;
		Allegato allegato = getAllegato(name);
		if (allegato != null) {
			bds = new ByteArrayDataSource(allegato.getContent(), allegato.getMimeType());
			bds.setName(allegato.getName());
		}
		return bds;
	}

	/**
	 * Ritorna la lista di tutti gli allegati ordinata per nome
	 */
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public static ArrayList<Allegato> getList() {
		ArrayList<Allegato> allegati = new ArrayList<Allegato>();
		List<?> lista = EQuery.getList(Allegato.class);
		for (Object obj : lista) {
			if ((obj != null) && (obj instanceof Allegato)) {
				Allegato allegato = (Allegato) obj;
				allegati.add(allegato);
			}

		}

		Transformer transformer = new Transformer() {
			public Object transform(Object input) {
				return ((String) input).toLowerCase();
			}
		};
		
		Comparator comparator = new TransformingComparator(transformer);
		Collections.sort(allegati, new BeanComparator(Allegato_.name.getName(), comparator));

		// Collections.sort(allegati, comparator);
		return allegati;
	}
	
	/**
	 * Ritorna una lista con tutti gli allegati demo
	 */
	public static ArrayList<Allegato> getDemoData(){
		ArrayList<Allegato> lista = new ArrayList<Allegato>();
		
		Path path = Paths.get(Application.getDemoDataFolderPath().toString(),"allegati");
		File folder = path.toFile();
		if (folder.isDirectory()) {
			File[] files = folder.listFiles();
			for (File file : files) {
			    if (file.isFile()) {
			    	if (!file.isHidden()) {
						try {
							Allegato allegato = fileToAllegato(file);
							lista.add(allegato);
						} catch (IOException e) {
							e.printStackTrace();
						}
					}
			    }
			}
		}

		return lista;
	}
	
	
	/**
	 * Legge un file e crea un oggetto Allegato
	 * <p>
	 * @param file il file
	 * @return l'allegato
	 */
	private static Allegato fileToAllegato(File file) throws IOException {
		Allegato allegato = null;
		
    	Path path = file.toPath();

		String strMime = "";
		MimeType type = LibFile.getMimeType(file);
		if (type != null) {
			strMime = type.toString();
		}

		byte[] bytes;
		bytes = Files.readAllBytes(path);
		allegato = new Allegato();
		allegato.setName(file.getName());
		allegato.setContent(bytes);
		allegato.setMimeType(strMime);
		allegato.setBytes(file.length());

		return allegato;
	}

}
