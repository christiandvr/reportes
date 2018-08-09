package ec.com.christiandvr.pdf;

import java.io.ByteArrayOutputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.List;

import com.itextpdf.html2pdf.ConverterProperties;
import com.itextpdf.html2pdf.HtmlConverter;
import com.itextpdf.io.font.FontConstants;
import com.itextpdf.kernel.font.PdfFont;
import com.itextpdf.kernel.font.PdfFontFactory;
import com.itextpdf.kernel.geom.PageSize;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.IBlockElement;
import com.itextpdf.layout.element.IElement;

/**
 * @author cvillavicencio[Christian Villavicencio]
 * @version 1.0.0 5 ago. 2018
 */
public class Reporte {

	/**
	 * 
	 * Método que transforma los parametros del html en valores enviados por el objeto DTO..
	 */
	public static String procesar(String html, Object dto) {
		Class<?> noparams[] = {};
		Field[] atributos = dto.getClass().getDeclaredFields();
		for (int i = 0; i < atributos.length; i++) {
			String nombre = atributos[i].getName();
			Method method;
			try {
				if (!"serialVersionUID".equals(nombre)) {
					method = dto.getClass().getDeclaredMethod("get" + nombre.substring(0, 1).toUpperCase() + nombre.substring(1, nombre.length()), noparams);
					Object resultado = method.invoke(dto, null);
					if (resultado != null) {
						html = html.replace("{f:" + nombre + "}", (String) resultado);
					} else {
						html = html.replace("{f:" + nombre + "}", "&nbsp;");
					}
				}
			} catch (NoSuchMethodException e) {
				e.printStackTrace();
			} catch (SecurityException e) {
				e.printStackTrace();
			} catch (IllegalAccessException e) {
				e.printStackTrace();
			} catch (IllegalArgumentException e) {
				e.printStackTrace();
			} catch (InvocationTargetException e) {
				e.printStackTrace();
			}

		}
		return html;
	}

	//Método para escribir en un archivo el reporte generado
	public static void escribirArchivo(byte[] bFile, String fileDest) {

		try (FileOutputStream fileOuputStream = new FileOutputStream(fileDest)) {
			fileOuputStream.write(bFile);
		} catch (IOException e) {
			e.printStackTrace();
		}

	}

	//Método para crear el pdf en base al html
	public static byte[] crearDocumentoPdf(String html) throws IOException {
		ConverterProperties properties = new ConverterProperties();
		ByteArrayOutputStream file = new ByteArrayOutputStream();
		PdfWriter writer = new PdfWriter(file);
		PdfDocument pdf = new PdfDocument(writer);
		pdf.setDefaultPageSize(PageSize.A4);
		Document document = HtmlConverter.convertToDocument(html, pdf, properties);
		document.setStrokeWidth(80);
		document.close();

		return file.toByteArray();
	}

	//Método para crear el pdf en base al html y los valores de los margenes
	public static byte[] crearPdf(String html, int margenSuperior, int margenDerecho, int margenInferior, int margenIzquierdo) throws IOException {
		List<IElement> elements = HtmlConverter.convertToElements(html);
		ByteArrayOutputStream file = new ByteArrayOutputStream();
		PdfWriter writer = new PdfWriter(file);
		PdfDocument pdf = new PdfDocument(writer);
		pdf.setDefaultPageSize(PageSize.A4);
		Document document = new Document(pdf);
		document.setMargins(margenSuperior, margenDerecho, margenInferior, margenIzquierdo);
		@SuppressWarnings("deprecation")
		PdfFont font = PdfFontFactory.createFont(FontConstants.HELVETICA);
		document.setFont(font);
		for (IElement element : elements) {
			document.add((IBlockElement) element);
		}
		document.close();
		return file.toByteArray();
	}

}
