package it.asteria.cultura.test;

import static org.junit.Assert.assertEquals;
import it.algos.webbase.web.lib.LibDate;
import it.algos.webbase.web.lib.Mese;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;

import org.joda.time.DateTime;
import org.junit.Test;

public class AlgosLibTest {

	@Test
	public void getGiorni() {
		int previsto = 0;
		int ottenuto = 0;
		int numMese = 0;

		// gennaio
		numMese = 1;
		previsto = 31;
		ottenuto = Mese.getGiorni(numMese);
		assertEquals(ottenuto, previsto);

		// febbraio
		numMese = 2;
		previsto = 28;
		ottenuto = Mese.getGiorni(numMese);
		assertEquals(ottenuto, previsto);

		// marzo
		numMese = 3;
		previsto = 31;
		ottenuto = Mese.getGiorni(numMese);
		assertEquals(ottenuto, previsto);

		// aprile
		numMese = 4;
		previsto = 30;
		ottenuto = Mese.getGiorni(numMese);
		assertEquals(ottenuto, previsto);

		// maggio
		numMese = 5;
		previsto = 31;
		ottenuto = Mese.getGiorni(numMese);
		assertEquals(ottenuto, previsto);

		// giugno
		numMese = 6;
		previsto = 30;
		ottenuto = Mese.getGiorni(numMese);
		assertEquals(ottenuto, previsto);

		// luglio
		numMese = 7;
		previsto = 31;
		ottenuto = Mese.getGiorni(numMese);
		assertEquals(ottenuto, previsto);

		// agosto
		numMese = 8;
		previsto = 31;
		ottenuto = Mese.getGiorni(numMese);
		assertEquals(ottenuto, previsto);

		// settembre
		numMese = 9;
		previsto = 30;
		ottenuto = Mese.getGiorni(numMese);
		assertEquals(ottenuto, previsto);

		// ottobre
		numMese = 10;
		previsto = 31;
		ottenuto = Mese.getGiorni(numMese);
		assertEquals(ottenuto, previsto);

		// novembre
		numMese = 11;
		previsto = 30;
		ottenuto = Mese.getGiorni(numMese);
		assertEquals(ottenuto, previsto);

		// dicembre
		numMese = 12;
		previsto = 31;
		ottenuto = Mese.getGiorni(numMese);
		assertEquals(ottenuto, previsto);
	}// end of single test

	// @Test
	// public void testMeseMese() {
	// Mese previsto = null;
	// Mese ottenuto = null;
	// String nomeMese = "";
	//
	// // gennaio
	// nomeMese = "gennaio";
	// previsto = Mese.gennaio;
	// ottenuto = Mese.getMese(nomeMese);
	// assertEquals(ottenuto, previsto);
	//
	// // febbraio
	// nomeMese = "febbraio";
	// previsto = Mese.febbraio;
	// ottenuto = Mese.getMese(nomeMese);
	// assertEquals(ottenuto, previsto);
	//
	// // marzo
	// nomeMese = "marzo";
	// previsto = Mese.marzo;
	// ottenuto = Mese.getMese(nomeMese);
	// assertEquals(ottenuto, previsto);
	//
	// // aprile
	// nomeMese = "aprile";
	// previsto = Mese.aprile;
	// ottenuto = Mese.getMese(nomeMese);
	// assertEquals(ottenuto, previsto);
	//
	// // maggio
	// nomeMese = "maggio";
	// previsto = Mese.maggio;
	// ottenuto = Mese.getMese(nomeMese);
	// assertEquals(ottenuto, previsto);
	//
	// // giugno
	// nomeMese = "giugno";
	// previsto = Mese.giugno;
	// ottenuto = Mese.getMese(nomeMese);
	// assertEquals(ottenuto, previsto);
	//
	// // luglio
	// nomeMese = "luglio";
	// previsto = Mese.luglio;
	// ottenuto = Mese.getMese(nomeMese);
	// assertEquals(ottenuto, previsto);
	//
	// // agosto
	// nomeMese = "agosto";
	// previsto = Mese.agosto;
	// ottenuto = Mese.getMese(nomeMese);
	// assertEquals(ottenuto, previsto);
	//
	// // settembre
	// nomeMese = "settembre";
	// previsto = Mese.settembre;
	// ottenuto = Mese.getMese(nomeMese);
	// assertEquals(ottenuto, previsto);
	//
	// // ottobre
	// nomeMese = "ottobre";
	// previsto = Mese.ottobre;
	// ottenuto = Mese.getMese(nomeMese);
	// assertEquals(ottenuto, previsto);
	//
	// // novembre
	// nomeMese = "novembre";
	// previsto = Mese.novembre;
	// ottenuto = Mese.getMese(nomeMese);
	// assertEquals(ottenuto, previsto);
	//
	// // dicembre
	// nomeMese = "dicembre";
	// previsto = Mese.dicembre;
	// ottenuto = Mese.getMese(nomeMese);
	// assertEquals(ottenuto, previsto);
	// }// end of single test

	@Test
	public void getOrd() {
		int previsto = 0;
		int ottenuto = 0;
		String nomeMese = "";

		// gennaio
		nomeMese = "gennaio";
		previsto = 1;
		ottenuto = Mese.getOrd(nomeMese);
		assertEquals(ottenuto, previsto);

		// febbraio
		nomeMese = "febbraio";
		previsto = 2;
		ottenuto = Mese.getOrd(nomeMese);
		assertEquals(ottenuto, previsto);

		// marzo
		nomeMese = "marzo";
		previsto = 3;
		ottenuto = Mese.getOrd(nomeMese);
		assertEquals(ottenuto, previsto);

		// aprile
		nomeMese = "aprile";
		previsto = 4;
		ottenuto = Mese.getOrd(nomeMese);
		assertEquals(ottenuto, previsto);

		// maggio
		nomeMese = "maggio";
		previsto = 5;
		ottenuto = Mese.getOrd(nomeMese);
		assertEquals(ottenuto, previsto);

		// giugno
		nomeMese = "giugno";
		previsto = 6;
		ottenuto = Mese.getOrd(nomeMese);
		assertEquals(ottenuto, previsto);

		// luglio
		nomeMese = "luglio";
		previsto = 7;
		ottenuto = Mese.getOrd(nomeMese);
		assertEquals(ottenuto, previsto);

		// agosto
		nomeMese = "agosto";
		previsto = 8;
		ottenuto = Mese.getOrd(nomeMese);
		assertEquals(ottenuto, previsto);

		// settembre
		nomeMese = "settembre";
		previsto = 9;
		ottenuto = Mese.getOrd(nomeMese);
		assertEquals(ottenuto, previsto);

		// ottobre
		nomeMese = "ottobre";
		previsto = 10;
		ottenuto = Mese.getOrd(nomeMese);
		assertEquals(ottenuto, previsto);

		// novembre
		nomeMese = "novembre";
		previsto = 11;
		ottenuto = Mese.getOrd(nomeMese);
		assertEquals(ottenuto, previsto);

		// dicembre
		nomeMese = "dicembre";
		previsto = 12;
		ottenuto = Mese.getOrd(nomeMese);
		assertEquals(ottenuto, previsto);
	}// end of single test

	@Test
	public void getShortLong() {
		String previsto = "";
		String ottenuto = "";
		int numMese = 0;

		// gennaio
		numMese = 1;
		previsto = "gen";
		ottenuto = Mese.getShort(numMese);
		assertEquals(ottenuto, previsto);
		previsto = "gennaio";
		ottenuto = Mese.getLong(numMese);
		assertEquals(ottenuto, previsto);

		// febbraio
		numMese = 2;
		previsto = "feb";
		ottenuto = Mese.getShort(numMese);
		assertEquals(ottenuto, previsto);
		previsto = "febbraio";
		ottenuto = Mese.getLong(numMese);
		assertEquals(ottenuto, previsto);

		// marzo
		numMese = 3;
		previsto = "mar";
		ottenuto = Mese.getShort(numMese);
		assertEquals(ottenuto, previsto);
		previsto = "marzo";
		ottenuto = Mese.getLong(numMese);
		assertEquals(ottenuto, previsto);

		// aprile
		numMese = 4;
		previsto = "apr";
		ottenuto = Mese.getShort(numMese);
		assertEquals(ottenuto, previsto);
		previsto = "aprile";
		ottenuto = Mese.getLong(numMese);
		assertEquals(ottenuto, previsto);

		// maggio
		numMese = 5;
		previsto = "mag";
		ottenuto = Mese.getShort(numMese);
		assertEquals(ottenuto, previsto);
		previsto = "maggio";
		ottenuto = Mese.getLong(numMese);
		assertEquals(ottenuto, previsto);

		// giugno
		numMese = 6;
		previsto = "giu";
		ottenuto = Mese.getShort(numMese);
		assertEquals(ottenuto, previsto);
		previsto = "giugno";
		ottenuto = Mese.getLong(numMese);
		assertEquals(ottenuto, previsto);

		// luglio
		numMese = 7;
		previsto = "lug";
		ottenuto = Mese.getShort(numMese);
		assertEquals(ottenuto, previsto);
		previsto = "luglio";
		ottenuto = Mese.getLong(numMese);
		assertEquals(ottenuto, previsto);

		// agosto
		numMese = 8;
		previsto = "ago";
		ottenuto = Mese.getShort(numMese);
		assertEquals(ottenuto, previsto);
		previsto = "agosto";
		ottenuto = Mese.getLong(numMese);
		assertEquals(ottenuto, previsto);

		// settembre
		numMese = 9;
		previsto = "set";
		ottenuto = Mese.getShort(numMese);
		assertEquals(ottenuto, previsto);
		previsto = "settembre";
		ottenuto = Mese.getLong(numMese);
		assertEquals(ottenuto, previsto);

		// ottobre
		numMese = 10;
		previsto = "ott";
		ottenuto = Mese.getShort(numMese);
		assertEquals(ottenuto, previsto);
		previsto = "ottobre";
		ottenuto = Mese.getLong(numMese);
		assertEquals(ottenuto, previsto);

		// novembre
		numMese = 11;
		previsto = "nov";
		ottenuto = Mese.getShort(numMese);
		assertEquals(ottenuto, previsto);
		previsto = "novembre";
		ottenuto = Mese.getLong(numMese);
		assertEquals(ottenuto, previsto);

		// dicembre
		numMese = 12;
		previsto = "dic";
		ottenuto = Mese.getShort(numMese);
		assertEquals(ottenuto, previsto);
		previsto = "dicembre";
		ottenuto = Mese.getLong(numMese);
		assertEquals(ottenuto, previsto);
	}// end of single test

	@Test
	public void getAllShortString() {
		String previsto = "gen, feb, mar, apr, mag, giu, lug, ago, set, ott, nov, dic";
		String ottenuto = Mese.getAllShortString();
		assertEquals(ottenuto, previsto);
	}// end of single test

	@Test
	public void getAllLongString() {
		String previsto = "gennaio, febbraio, marzo, aprile, maggio, giugno, luglio, agosto, settembre, ottobre, novembre, dicembre";
		String ottenuto = Mese.getAllLongString();
		assertEquals(ottenuto, previsto);
	}// end of single test

	@SuppressWarnings({ "rawtypes", "unchecked" })
	@Test
	public void getAllShortList() {
		ArrayList<String> previsto = new ArrayList(Arrays.asList("gen", "feb", "mar", "apr", "mag", "giu", "lug",
				"ago", "set", "ott", "nov", "dic"));
		ArrayList<String> ottenuto = Mese.getAllShortList();
		assertEquals(ottenuto, previsto);
	}// end of single test

	@SuppressWarnings({ "rawtypes", "unchecked" })
	@Test
	public void getAllLongList() {
		ArrayList<String> previsto = new ArrayList(Arrays.asList("gennaio", "febbraio", "marzo", "aprile", "maggio",
				"giugno", "luglio", "agosto", "settembre", "ottobre", "novembre", "dicembre"));
		ArrayList<String> ottenuto = Mese.getAllLongList();
		assertEquals(ottenuto, previsto);
	}// end of single test

	@Test
	public void toStringDDMMYYYY() {
		Date date = null;
		String previsto = "";
		String ottenuto = "";
		// Date dateTmp = new Date(114, 2, 8, 7, 12);
		// long lungoTmp = dateTmp.getTime();
		long lungoData = 1413868320000L; // 21 ottobre 2014, 7 e 12
		long lungoData2 = 1398057120000L; // 21 aprile 2014, 7 e 12
		long lungoData3 = 1412485920000L; // 5 aprile 2014, 7 e 12
		long lungoData4 = 1394259120000L; // 8 marzo 2014, 7 e 12

		// metodo toStringDDMMYYYY
		date = new Date(lungoData);
		previsto = "21-10-2014";
		ottenuto = LibDate.toStringDDMMYYYY(date);
		assertEquals(ottenuto, previsto);

		date = new Date(lungoData2);
		previsto = "21-04-2014";
		ottenuto = LibDate.toStringDDMMYYYY(date);
		assertEquals(ottenuto, previsto);

		date = new Date(lungoData3);
		previsto = "05-10-2014";
		ottenuto = LibDate.toStringDDMMYYYY(date);
		assertEquals(ottenuto, previsto);

		date = new Date(lungoData4);
		previsto = "08-03-2014";
		ottenuto = LibDate.toStringDDMMYYYY(date);
		assertEquals(ottenuto, previsto);

		// metodo toStringDMYYYY
		date = new Date(lungoData);
		previsto = "21-10-2014";
		ottenuto = LibDate.toStringDMYYYY(date);
		assertEquals(ottenuto, previsto);

		date = new Date(lungoData2);
		previsto = "21-4-2014";
		ottenuto = LibDate.toStringDMYYYY(date);
		assertEquals(ottenuto, previsto);

		date = new Date(lungoData3);
		previsto = "5-10-2014";
		ottenuto = LibDate.toStringDMYYYY(date);
		assertEquals(ottenuto, previsto);

		date = new Date(lungoData4);
		previsto = "8-3-2014";
		ottenuto = LibDate.toStringDMYYYY(date);
		assertEquals(ottenuto, previsto);
	}// end of method

	@Test
	public void toStringDDMMYYYYHHMM() {
		Date date = null;
		String previsto = "";
		String ottenuto = "";
		// Date dateTmp = new Date(114, 2, 8, 7, 12);
		// long lungoTmp = dateTmp.getTime();
		long lungoData = 1413868320000L; // 21 ottobre 2014, 7 e 12

		date = new Date(lungoData);
		previsto = "21-10-2014 07:12";
		ottenuto = LibDate.toStringDDMMYYYYHHMM(date);
		assertEquals(ottenuto, previsto);
	}// end of method

	@Test
	public void toStringHHMM() {
		Date date = null;
		String previsto = "";
		String ottenuto = "";
		// Date dateTmp = new Date(114, 2, 8, 7, 12);
		// long lungoTmp = dateTmp.getTime();
		long lungoData = 1413868320000L; // 21 ottobre 2014, 7 e 12

		date = new Date(lungoData);
		previsto = "07:12";
		ottenuto = LibDate.toStringHHMM(date);
		assertEquals(ottenuto, previsto);
	}// end of method

	@Test
	public void toStringDMMMYY() {
		Date date = null;
		String previsto = "";
		String ottenuto = "";
		// Date dateTmp = new Date(114, 2, 8, 7, 12);
		// long lungoTmp = dateTmp.getTime();
		long lungoData = 1394259120000L; // 8 marzo 2014, 7 e 12

		date = new Date(lungoData);
		previsto = "8-mar-14";
		ottenuto = LibDate.toStringDMMMYY(date);
		assertEquals(ottenuto, previsto);
	}// end of method

	@Test
	public void fromInizioMeseAnno() {
		Date date = null;
		String mese = "";
		int anno = 0;
		String ottenuto = "";
		String previsto = "";

		mese = "agosto";
		anno = 2014;
		previsto = "01-08-2014 00:00";
		date = LibDate.fromInizioMeseAnno(mese, anno);
		ottenuto = LibDate.toStringDDMMYYYYHHMM(date);
		assertEquals(ottenuto, previsto);

		mese = "dicembre";
		anno = 2014;
		previsto = "1-dic-14";
		date = LibDate.fromInizioMeseAnno(mese, anno);
		ottenuto = LibDate.toStringDMMMYY(date);
		assertEquals(ottenuto, previsto);

		mese = "gennaio";
		anno = 2015;
		previsto = "1-gen-15";
		date = LibDate.fromInizioMeseAnno(mese, anno);
		ottenuto = LibDate.toStringDMMMYY(date);
		assertEquals(ottenuto, previsto);

		mese = "febbraio";
		anno = 2015;
		previsto = "1-feb-15";
		date = LibDate.fromInizioMeseAnno(mese, anno);
		ottenuto = LibDate.toStringDMMMYY(date);
		assertEquals(ottenuto, previsto);

	}// end of method

	@Test
	public void fromFineMeseAnno() {
		Date date = null;
		String mese = "";
		int anno = 0;
		String ottenuto = "";
		String previsto = "";

		mese = "novembre";
		anno = 2014;
		previsto = "30-11-2014 23:59:59";
		date = LibDate.fromFineMeseAnno(mese, anno);
		ottenuto = new DateTime(date).toString("dd-MM-YYYY HH:mm:ss");
		assertEquals(ottenuto, previsto);

		mese = "dicembre";
		anno = 2014;
		previsto = "31-dic-14";
		date = LibDate.fromFineMeseAnno(mese, anno);
		ottenuto = LibDate.toStringDMMMYY(date);
		assertEquals(ottenuto, previsto);

		mese = "gennaio";
		anno = 2015;
		previsto = "31-gen-15";
		date = LibDate.fromFineMeseAnno(mese, anno);
		ottenuto = LibDate.toStringDMMMYY(date);
		assertEquals(ottenuto, previsto);

		mese = "febbraio";
		anno = 2015;
		previsto = "28-feb-15";
		date = LibDate.fromFineMeseAnno(mese, anno);
		ottenuto = LibDate.toStringDMMMYY(date);
		assertEquals(ottenuto, previsto);
	}// end of method

	@Test
	public void toStringDMYY() {
		Date date = null;
		String previsto = "";
		String ottenuto = "";
		// Date dateTmp = new Date(114, 2, 8, 7, 12);
		// long lungoTmp = dateTmp.getTime();
		long lungoData = 1394259120000L; // 8 marzo 2014, 7 e 12

		date = new Date(lungoData);
		previsto = "8-3-14";
		ottenuto = LibDate.toStringDMYY(date);
		assertEquals(ottenuto, previsto);

		date = new Date(lungoData);
		previsto = "8-3-14 07:12";
		ottenuto = LibDate.toStringDMYYHHMM(date);
		assertEquals(ottenuto, previsto);
}// end of method

}// end of test class