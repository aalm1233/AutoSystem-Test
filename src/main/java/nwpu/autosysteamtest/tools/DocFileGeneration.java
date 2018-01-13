package nwpu.autosysteamtest.tools;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;

import org.apache.poi.xwpf.usermodel.XWPFDocument;
import org.apache.poi.xwpf.usermodel.XWPFParagraph;
import org.apache.poi.xwpf.usermodel.XWPFRun;

/**
 * 
 * @author Dengtong
 * @version 0.0,12/01/2018
 */
public class DocFileGeneration {
	File wordocx;
	XWPFDocument document;

	void createDocxfile(String path) {
		File wordoc = new File(path);
		if (!wordoc.exists()) {
			document = new XWPFDocument();
			try {
				OutputStream os = new FileOutputStream(wordoc);
				document.write(os);
				document.close();
			} catch (FileNotFoundException e) {

				e.printStackTrace();
			} catch (IOException e) {

				e.printStackTrace();
			} finally {
			}
		}

	}

	public static void main(String[] args) {
		File wordoc = new File("F:\\pdf\\123456.docx");
		if (!wordoc.exists()) {
			XWPFDocument document = new XWPFDocument();
			try {
				OutputStream os = new FileOutputStream(wordoc);
				document.write(os);
				document.close();
			} catch (FileNotFoundException e) {

				e.printStackTrace();
			} catch (IOException e) {

				e.printStackTrace();
			} finally {

			}
		}
	}
}
