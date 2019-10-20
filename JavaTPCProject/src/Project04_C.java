import java.io.FileOutputStream;

import com.itextpdf.text.Document;
import com.itextpdf.text.Image;
import com.itextpdf.text.pdf.PdfWriter;

public class Project04_C {
	public static void main(String[] args) {
       // https://cdn.inflearn.com/assets/images/main/ufo2.png
	   	Document doc=new Document();
	   	try {
			PdfWriter.getInstance(doc, new FileOutputStream("ImageDemo.pdf"));
			doc.open();
			
			String fileName="inflearn.png";
			Image image=Image.getInstance(fileName);
			doc.add(image);
			
			String url="https://cdn.inflearn.com/assets/images/main/ufo2.png";
			image=Image.getInstance(url);
			doc.add(image);
			
			System.out.println("생성완료");
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			doc.close();
		}

	}
}
