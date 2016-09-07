package it.algos.evento.entities.insegnante;

import it.algos.webbase.web.table.ATable;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Personalizzazione per Extrateatro
 * Mette tutti i dati aggiuntivi nel campo Note
 */
public class InsegnanteImportExtrateatro extends InsegnanteImport {


    public InsegnanteImportExtrateatro(ATable table, DoneListener listener) {
        super(table, listener);
    }

    @Override
    public String[] getColumnNames() {

        String[] baseNames = super.getColumnNames();
        ArrayList<String> names = new ArrayList<String>();
        for(String name : baseNames){
            names.add(name);
        }

        String[] extraNames = Columns.getColumnNames();
        for(String name : extraNames){
            names.add(name);
        }

        return names.toArray(new String[0]);
    }

    @Override
    public Insegnante insegnanteFromExcel(HashMap<String, String> valueMap) {
        Insegnante ins = super.insegnanteFromExcel(valueMap);
        if (ins != null) {

            String s;
            StringBuilder sb = new StringBuilder();

            s = valueMap.get(Columns.tipo.getTitoloColonna()).trim();
            append(sb, "Tipo:", s);

            s = valueMap.get(Columns.scuola.getTitoloColonna()).trim();
            append(sb, "Scuola:", s);

            s = valueMap.get(Columns.plessosede.getTitoloColonna()).trim();
            append(sb, "Plesso/Sede:", s);

            s = valueMap.get(Columns.fax.getTitoloColonna()).trim();
            append(sb, "Fax:", s);

            s = valueMap.get(Columns.note.getTitoloColonna()).trim();
            append(sb, "Note:", s);

            s = valueMap.get(Columns.stor1516.getTitoloColonna()).trim();
            append(sb, "2015-2016:", s);

            s = valueMap.get(Columns.stor1415.getTitoloColonna()).trim();
            append(sb, "2014-2015:", s);

            s = valueMap.get(Columns.stor1314.getTitoloColonna()).trim();
            append(sb, "2013-2014:", s);

            s = valueMap.get(Columns.stor1213.getTitoloColonna()).trim();
            append(sb, "2012-2013:", s);

            s = valueMap.get(Columns.stor1112.getTitoloColonna()).trim();
            append(sb, "2011-2012:", s);

            s = sb.toString();
            ins.setNote(s);


        }
        return ins;
    }

    private void append(StringBuilder sb, String t, String s) {
        if (!s.isEmpty()) {
            if (sb.length() > 0) {
                sb.append("\n");
            }
            sb.append(t+" "+s);
        }
    }


    /**
     * Colonne aggiuntive specifiche di Extrateatro
     */
    private enum Columns {

        tipo("TIPO"),

        scuola("SCUOLA"),

        plessosede("PLESSO/SEDE"),

        note("NOTE"),

        fax("FAX"),

        stor1516("STOR-2015-2016"),

        stor1415("STOR-2014-2015"),

        stor1314("STOR-2013-2014"),

        stor1213("STOR-2012-2013"),

        stor1112("STOR-2011-2012"),;

        private String titoloColonna;

        Columns(String name) {
            this.titoloColonna = name;
        }

        public String getTitoloColonna() {
            return titoloColonna;
        }

        public static String[] getColumnNames() {
            String[] columnNames = new String[Columns.values().length];
            for (int i = 0; i < columnNames.length; i++) {
                Columns c = Columns.values()[i];
                columnNames[i] = c.titoloColonna;
            }
            return columnNames;
        }



    }
}
