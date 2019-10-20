import java.io.FileOutputStream;

import com.itextpdf.text.Chunk;
import com.itextpdf.text.Document;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.pdf.PdfWriter;

public class Project04_B {
	public static void main(String[] args) {
      Document doc=new Document();
      try {
		  FileOutputStream fos=new FileOutputStream("paragraphDemo.pdf");
		  PdfWriter.getInstance(doc, fos);
		  doc.open();
		  
		  String content="Your word is a lamp to my feet and a light for my path";
		  Paragraph par1=new Paragraph(32);
		  par1.setSpacingBefore(50);
		  par1.setSpacingAfter(50);
		  
		  for(int i=0;i<20;i++) {
			  Chunk chunk=new Chunk(content);
			  par1.add(chunk);
		  }
		  doc.add(par1);
		  
		  Paragraph par2=new Paragraph();
		  for(int i=0;i<10;i++) {
				  par2.add(new Chunk(content));
		  }
		  doc.add(par2);
    	  doc.close();	  
		  
		  System.out.println("paragraphDemo »ý¼º");
	   } catch (Exception e) {
		e.printStackTrace();
	  }
	}
}
