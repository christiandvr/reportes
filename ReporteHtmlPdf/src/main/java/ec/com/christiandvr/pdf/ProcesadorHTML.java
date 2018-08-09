package ec.com.christiandvr.pdf;

import java.io.IOException;
/**
 * @author cvillavicencio[Christian Villavicencio]
 * @version 1.0.0 5 ago. 2018
 */

public class ProcesadorHTML {

	public static void main(String[] args) {
		try {
			//DTO de prueba
			DtoPrueba prueba = new DtoPrueba();
			//seteo de datos al DTO
			prueba.setId("1");
			prueba.setNombre("Ejemplo");
			//puede agregarse tablas en formato html para hacer el reporte dinámico
			prueba.setTabla("<table border='1'>" + "<tr> <td><h3>hola</h3></td> </tr>" + "</table>");
			
			//html de prueba donde se pone los parametros con la etiqueta {p:<parametro>}
			//colocar entre llaves los nombres de los atributos del DTO tal cual como estan escritos, los atributos además deben ser siempre String
			String html = "<html> <table border='1' style='border-collapse: collapse' >" + "<tr> <td><h1>{f:id}</h1></td> </tr>" + "<tr> <td><h2>{f:nombre}</h2></td> </tr>" + "<tr> <td>{f:tabla}</td> </tr>" + "</table></html>";
			//Método que reemplaza las etiquetas del html con los valores seteados en el DTO
			html = Reporte.procesar(html, prueba);
			//Método que crea el pdf con el html y los margenes del reporte
			byte[] array = Reporte.crearPdf(html, 5, 5, 5, 5);

			//escribe el reporte en un archivo físico como ejemplo
			Reporte.escribirArchivo(array, "c:\\a.pdf");
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

}
