package it.algos.evento.entities.lettera;

import com.vaadin.ui.Image;
import com.vaadin.ui.Table;
import it.algos.evento.EventoApp;
import it.algos.evento.multiazienda.ETable;
import it.algos.web.lib.LibResource;
import it.algos.web.module.ModulePop;

@SuppressWarnings("serial")
public class LetteraTable extends ETable {

    // id della colonna generata "tipo modello di lettera"
    private static final String COL_TIPO = "tipo";

    public LetteraTable(ModulePop modulo) {
        super(modulo);
        setColumnAlignment(COL_TIPO, Align.CENTER);
    }// end of constructor

    @Override
    protected void createAdditionalColumns() {
        addGeneratedColumn(COL_TIPO, new TipoColumnGenerator());
    }// end of method

    @Override
    protected Object[] getDisplayColumns() {
        return new Object[]{Lettera_.sigla, Lettera_.oggetto, COL_TIPO, Lettera_.allegati};
    }// end of method

    /**
     * Genera la colonna del Tipo.
     */
    private class TipoColumnGenerator implements ColumnGenerator {
        @Override
        public Object generateCell(Table source, Object itemId, Object columnId) {
            Image image;
            String sigla;
            Lettera lettera = Lettera.find((long) itemId);
            String locImgName = "";
            String description = "";

            if (lettera != null) {
                sigla = lettera.getSigla();
                if (ModelliLettere.getAllDbCode().contains(sigla)) {
                    locImgName = "lock-icon.png";
                    description = "Lettera standard";
                } else {
                    locImgName = "lock-open-icon.png";
                    description = "Lettera extra";
                }// fine del blocco if-else
            }// fine del blocco if

            image = new Image(null, LibResource.getImgResource(EventoApp.IMG_FOLDER_NAME, locImgName));
            image.setDescription(description);

            return image;
        }// end of method
    }// end of inner class

}// end of class
