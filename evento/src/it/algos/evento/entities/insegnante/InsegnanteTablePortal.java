package it.algos.evento.entities.insegnante;

import com.vaadin.server.ThemeResource;
import com.vaadin.ui.MenuBar;
import com.vaadin.ui.MenuBar.MenuItem;
import it.algos.evento.entities.insegnante.InsegnanteImport.DoneListener;
import it.algos.webbase.web.dialog.BaseDialog;
import it.algos.webbase.web.importexport.ExportConfiguration;
import it.algos.webbase.web.importexport.ExportManager;
import it.algos.webbase.web.module.ModulePop;
import it.algos.webbase.web.table.TablePortal;
import it.algos.webbase.web.toolbar.TableToolbar;
import it.algos.webbase.web.updown.ReportDownloadDialog;
import net.sf.dynamicreports.jasper.builder.JasperReportBuilder;

@SuppressWarnings("serial")
public class InsegnanteTablePortal extends TablePortal {

	public static final String CMD_IMPORT = "Importa...";
	public static final ThemeResource ICON_IMPORT = new ThemeResource("img/action_import.png");

	public static final String CMD_EXPORT = "Esporta...";
	public static final ThemeResource ICON_EXPORT = new ThemeResource("img/action_export.png");

	public InsegnanteTablePortal(ModulePop modulo) {
		super(modulo);
	}// end of constructor

	public TableToolbar createToolbar() {
		final TableToolbar toolbar = super.createToolbar();

		// bottone Altro...
		MenuBar.MenuItem item = toolbar.addButton("Altro...", new ThemeResource("img/action_more.png"), null);

		item.addItem(CMD_IMPORT, ICON_IMPORT, new MenuBar.Command() {
			public void menuSelected(MenuItem selectedItem) {
				DoneListener listener = new DoneListener() {

					@Override
					public void done(BaseDialog dialog) {
						dialog.show(getUI());
					}// end of method
				};
				new InsegnanteImport(getTable(), listener);
			}// end of method
		});// end of anonymous class
		
		item.addItem(CMD_EXPORT, ICON_EXPORT, new MenuBar.Command() {
			public void menuSelected(MenuItem selectedItem) {
				ExportConfiguration conf = ExportManager.createExportConfiguration(Insegnante.class);
				conf.setContainer(getTable().getJPAContainer()); // used only if "only records in table" is selected
				new ExportManager(conf).show(getUI());
			}// end of method
		});// end of anonymous class

		item.addItem("Pdf...", null, new MenuBar.Command() {
			public void menuSelected(MenuItem selectedItem) {
				JasperReportBuilder report = InsegnanteModulo.createPdfReport();
				ReportDownloadDialog dialog = new ReportDownloadDialog(report, "Testreport.pdf");
				dialog.show(getUI());
			}// end of method
		});// end of anonymous class

		return toolbar;
	}// end of method

}// end of class
