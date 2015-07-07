package it.algos.evento.entities.scuola;

import it.algos.web.dialog.BaseDialog;
import it.algos.web.importexport.ExportConfiguration;
import it.algos.web.importexport.ExportManager;
import it.algos.web.module.ModulePop;
import it.algos.web.table.TablePortal;
import it.algos.web.toolbar.TableToolbar;
import it.algos.evento.entities.scuola.ScuolaImport.DoneListener;

import com.vaadin.ui.MenuBar;
import com.vaadin.ui.MenuBar.MenuItem;

@SuppressWarnings("serial")
public class ScuolaTablePortal extends TablePortal {

	public ScuolaTablePortal(ModulePop modulo) {
		super(modulo);
	}// end of constructor

	public TableToolbar createToolbar() {
		final TableToolbar toolbar = super.createToolbar();

		MenuBar.MenuItem item = toolbar.addButton("Altro...", null);
		item.addItem("Importa...", null, new MenuBar.Command() {
			public void menuSelected(MenuItem selectedItem) {
				DoneListener listener = new DoneListener() {

					@Override
					public void done(BaseDialog dialog) {
						dialog.show(getUI());
					}
				};
				new ScuolaImport(getTable(), listener);

				// ConfirmDialog dialog = new ConfirmDialog(null);
				// dialog.show(getUI());
			}
		});// end of anonymous class

		item.addItem("Esporta...", null, new MenuBar.Command() {
			public void menuSelected(MenuItem selectedItem) {
				ExportConfiguration conf = ExportManager.createExportConfiguration(Scuola.class);
				conf.setContainer(getTable().getJPAContainer()); // used only if "only records in table" is selected
				new ExportManager(conf).show(getUI());
			}
		});// end of anonymous class

//		item.addItem("Pdf...", null, new MenuBar.Command() {
//			public void menuSelected(MenuItem selectedItem) {
//				JasperReportBuilder report = InsegnanteModulo.createPdfReport();
//				ReportDownloadDialog dialog = new ReportDownloadDialog(report, "Testreport.pdf");
//				dialog.show(getUI());
//			}
//		});// end of anonymous class

		return toolbar;
	}// end of method

}// end of class
