import java.io.*;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.util.IOUtils;

public class Project03_B {
	public static void main(String[] args) {
		
		try {
			Workbook wb=new HSSFWorkbook();
			Sheet sheet=wb.createSheet("My Sample Excel");
			InputStream is=new FileInputStream("pic.jpg");
			byte[] bytes=IOUtils.toByteArray(is);
			int pictureIdx=wb.addPicture(bytes, Workbook.PICTURE_TYPE_JPEG);
			is.close();
			
			CreationHelper helper=wb.getCreationHelper();
			Drawing drawing=sheet.createDrawingPatriarch();
			ClientAnchor anchor=helper.createClientAnchor();
			anchor.setCol1(1);
			anchor.setRow1(2);			
			anchor.setCol2(2);
			anchor.setRow2(3);
		
			Picture pict=drawing.createPicture(anchor, pictureIdx);
			
			Cell cell=sheet.createRow(2).createCell(1);
			int w=20*256;
			sheet.setColumnWidth(1, w);
			
			short h=120*20;
			cell.getRow().setHeight(h);
			
			FileOutputStream fileOut=new FileOutputStream("myFile.xls");
			wb.write(fileOut);
			fileOut.close();
			System.out.println("이미지 생성 성공");
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

}
