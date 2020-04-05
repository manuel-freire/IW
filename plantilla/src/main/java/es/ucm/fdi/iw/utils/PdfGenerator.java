package es.ucm.fdi.iw.utils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.MalformedURLException;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;

import com.google.common.collect.Table.Cell;
import com.itextpdf.text.BaseColor;
import com.itextpdf.text.Chunk;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Font;
import com.itextpdf.text.FontFactory;
import com.itextpdf.text.Image;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;

import es.ucm.fdi.iw.LocalData;
import es.ucm.fdi.iw.constants.ConstantsFromFile;
import es.ucm.fdi.iw.control.UserController;
import es.ucm.fdi.iw.model.StClass;
import es.ucm.fdi.iw.model.User;

public class PdfGenerator {
	
	private static final Logger log = LogManager.getLogger(PdfGenerator.class);

	public static String generateQrClassFile(List<User> users, StClass stClass) throws DocumentException, MalformedURLException, IOException {

		String id, name, img;
		User u;
		Image qrCode;
		int numPages;
		int cellCount = 0;
		
		File directory = new File(ConstantsFromFile.QR_DIR);
	    if (! directory.exists()){
	        directory.mkdir();
	    }
		
		String qrFile = ConstantsFromFile.QR_DIR + ConstantsFromFile.QR_FILE + "." + ConstantsFromFile.PDF;
		Document document = new Document();
		PdfWriter.getInstance(document, new FileOutputStream(qrFile));
		Font font = FontFactory.getFont(FontFactory.COURIER, ConstantsFromFile.QR_FONT_SIZE, BaseColor.BLACK);
		PdfPTable table;
		
		document.open();	
		
		if (users.size() % ConstantsFromFile.NUM_ROWS == 0) {
			numPages = users.size() / ConstantsFromFile.NUM_ROWS;
		} else {
			numPages = (users.size() / ConstantsFromFile.NUM_ROWS) + 1;
		}

		for (int i=0; i < numPages; i++) {
			for (int j=0; j < Math.min(ConstantsFromFile.NUM_ROWS, users.size()-i*ConstantsFromFile.NUM_ROWS); j++) {
				cellCount = ConstantsFromFile.NUM_ROWS*i+j;
				u = users.get(cellCount);
				id = Long.toString(u.getId());
				name = u.getFirstName() + ",\n" + u.getLastName();
				img = ConstantsFromFile.QR_DIR + ConstantsFromFile.QR_IMG + u.getUsername() + "." + ConstantsFromFile.PNG;
				
				log.info("Creando QR para el usuario {}", cellCount);
				table = new PdfPTable(ConstantsFromFile.NUM_COLS);
				table.getDefaultCell().setFixedHeight(ConstantsFromFile.CELL_H);
				table.setWidths(new float[] {ConstantsFromFile.NAME_W, ConstantsFromFile.USER_W, ConstantsFromFile.QR_W});
				table.addCell(new Phrase(name, font));
				table.addCell(new Phrase(img, font));
				QrGenerator.generateQrCode(id, u.getUsername());
				qrCode = Image.getInstance(img);
				table.addCell(qrCode);
				table.completeRow();
				document.add(table);
			}
			
			document.newPage();
		}
		
		document.close();
		
		return qrFile;
	}
	
	
}