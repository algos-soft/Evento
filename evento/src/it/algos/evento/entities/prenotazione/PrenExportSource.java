package it.algos.evento.entities.prenotazione;

import com.vaadin.data.Item;
import com.vaadin.data.Property;
import it.algos.webbase.web.importexport.ExportConfiguration;
import it.algos.webbase.web.importexport.ExportProvider;
import it.algos.webbase.web.updown.ExportStreamSource;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import com.vaadin.data.Container;


import java.awt.*;
import java.util.Collection;

/**
 * Custom ExportStreamSource needed to append totals row to the worksheet
 */
public class PrenExportSource extends ExportStreamSource {

    public PrenExportSource(ExportConfiguration config) {
        super(config);
    }

    @Override
    protected void populateWorkbook() {
        super.populateWorkbook();

        Cell cell;
        Row row;

        // add totals

        row = addRow();

        cell = row.createCell(0);
        cell.setCellType(Cell.CELL_TYPE_STRING);
        cell.setCellValue("Totale posti prenotati");

        cell = row.createCell(1);
        cell.setCellType(Cell.CELL_TYPE_NUMERIC);
        cell.setCellValue(11);

        row = addRow();

        cell = row.createCell(0);
        cell.setCellType(Cell.CELL_TYPE_STRING);
        cell.setCellValue("Totale importo");

        cell = row.createCell(1);
        cell.setCellType(Cell.CELL_TYPE_NUMERIC);
        cell.setCellValue(99);


//        Container container=getExportConfiguration().getContainer();
//        Collection ids =  container.getItemIds();
//
//        for(Object itemId : ids){
//            Item item = container.getItem(itemId);
//            Property prop = item.getItemProperty(Prenotazione_.numTotali);
//            Object obj = prop.getValue();
//            int a = 87;
//            int b=1;
//        }



    }
}
