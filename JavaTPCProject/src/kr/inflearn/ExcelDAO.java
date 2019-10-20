package kr.inflearn;
import java.io.*;
import java.net.*;
import java.util.*;
import org.apache.poi.hssf.usermodel.*;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.util.IOUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.*;
public class ExcelDAO {
	private List<ExcelVO> list;
	private HSSFWorkbook wb;
	public ExcelDAO() {
		list=new ArrayList<ExcelVO>();
		wb=new HSSFWorkbook();
	}
    public void excel_input() {
    	BufferedReader br=new BufferedReader(new InputStreamReader(System.in));
    	try {
            HSSFSheet firstSheet = wb.createSheet("BOOK SHEET");
            HSSFRow rowA = firstSheet.createRow(0);
            HSSFCell cellA = rowA.createCell(0);
            cellA.setCellValue(new HSSFRichTextString("å����"));
            HSSFCell cellB = rowA.createCell(1);
            cellB.setCellValue(new HSSFRichTextString("����"));
            HSSFCell cellC = rowA.createCell(2);
            cellC.setCellValue(new HSSFRichTextString("���ǻ�"));
            HSSFCell cellD = rowA.createCell(3);
            cellD.setCellValue(new HSSFRichTextString("isbn"));
            HSSFCell cellE = rowA.createCell(4);
            cellE.setCellValue(new HSSFRichTextString("�̹����̸�"));            
            HSSFCell cellF = rowA.createCell(5);
            cellF.setCellValue(new HSSFRichTextString("�̹���"));

            int i=1;
            while(true) {
	        	System.out.print("å����:");
	    		String title=br.readLine();
	    		System.out.print("å����:");
	    		String author=br.readLine();
	    		System.out.print("���ǻ�:");
	    		String company=br.readLine();
	    		
	            HSSFRow rowRal = firstSheet.createRow(i);	            
	            HSSFCell cellTitle = rowRal.createCell(0);            
	            cellTitle.setCellValue(new HSSFRichTextString(title));	            
	            HSSFCell cellAuthor = rowRal.createCell(1);
	            cellAuthor.setCellValue(new HSSFRichTextString(author));	            
	            HSSFCell cellCompany = rowRal.createCell(2);
	            cellCompany.setCellValue(new HSSFRichTextString(company));	            
	            i++;
	            
	            ExcelVO vo=new ExcelVO(title, author, company);
	            // isbn, image �˻�
	            ExcelVO data=naver_search(vo);
	            list.add(data);
	            System.out.print("����Է� �Ͻø� Y / �Է����� N:");
	            String key=br.readLine();
	            if(key.equals("N")) break;
	        }
            System.out.println("������ ������...........");
            excel_save();
		} catch (Exception e) {
            e.printStackTrace();
		}
    }		
	//�� �˻��� å ����(d_titl), ���ڸ�(d_auth), ����(d_cont), ISBN(d_isbn), ���ǻ�(d_publ) 5�� �׸� �߿��� 1�� �̻� ���� �Է��ؾ���.
public ExcelVO naver_search(ExcelVO vo) {
		try {
			String URL_STATICMAP = "https://openapi.naver.com/v1/search/book_adv.xml?d_titl="+URLEncoder.encode(vo.getTitle(), "UTF-8")+"&d_auth="+URLEncoder.encode(vo.getAuthor(), "UTF-8")+"&d_publ="+URLEncoder.encode(vo.getCompany(), "UTF-8");
	        URL url = new URL(URL_STATICMAP);
            HttpURLConnection con = (HttpURLConnection)url.openConnection();
            con.setRequestMethod("GET");
            con.setRequestProperty("X-Naver-Client-Id", "pOZrzVf2ULPCGWhdaKMk");
            con.setRequestProperty("X-Naver-Client-Secret", "R7H5MMEDEO");
            int responseCode = con.getResponseCode();
            BufferedReader br;            
            if(responseCode==200) { 
                br = new BufferedReader(new InputStreamReader(con.getInputStream(),"UTF-8"));
            } else {  
                br = new BufferedReader(new InputStreamReader(con.getErrorStream()));
            }
            String inputLine;
            StringBuffer response = new StringBuffer(); //���ڿ� �߰� ����� ���            
            while ((inputLine = br.readLine()) != null) {
                response.append(inputLine);
            }
            br.close();

            Document doc = Jsoup.parse(response.toString());
            //System.out.println(doc.toString());
            Element isbn = doc.select("isbn").first();
            System.out.println("isbn: "+ isbn.text());
            String img=doc.toString();
            String imgTag=img.substring(img.indexOf("<img>")+5);
            img=imgTag.substring(0, imgTag.indexOf("?"));
            System.out.println(img);
            vo.setIsbn(isbn.text().split(" ")[1]);
            String fileName=img.substring(img.lastIndexOf("/")+1);
            vo.setImgurl(fileName);   
            // DownloadBroker            
            Runnable dl=new DownloadBroker(img, fileName);
            Thread t=new Thread(dl);
            t.start();
         } catch (Exception e) {
            System.out.println(e);
        }
		return vo;
	}//naver_search
	
 public void excel_save() {
		  try {
		  HSSFSheet sheet = wb.getSheetAt(0);
		   if(wb != null && sheet != null) {
			  Iterator rows = sheet.rowIterator();
	          rows.next();
	          int i=0; // list�� index
		      while (rows.hasNext()) {
		    	    HSSFRow row = (HSSFRow) rows.next();
	               	HSSFCell cell=row.createCell(3);	
	               	cell.setCellType(CellType.STRING);
					cell.setCellValue(list.get(i).getIsbn());				
					
					cell=row.createCell(4);	
					cell.setCellType(CellType.STRING);
					cell.setCellValue(list.get(i).getImgurl());
					
		    	    InputStream inputStream = new FileInputStream(list.get(i).getImgurl());
		    	    byte[] bytes = IOUtils.toByteArray(inputStream);
		    	    int pictureIdx = wb.addPicture(bytes, Workbook.PICTURE_TYPE_JPEG);
		    	    inputStream.close();
				        
					CreationHelper helper = wb.getCreationHelper();
					Drawing drawing = sheet.createDrawingPatriarch();
					ClientAnchor anchor = helper.createClientAnchor();
					   
				    anchor.setCol1(5); //Column B
			    	anchor.setRow1(i+1); //Row 3
			    	anchor.setCol2(6); //Column C
			    	anchor.setRow2(i+2); //Row 4

			    	Picture pict = drawing.createPicture(anchor, pictureIdx);
			    	Cell cellImg = row.createCell(5);
			    	int widthUnits = 20*256; 
			    	sheet.setColumnWidth(5, widthUnits);
			    	short heightUnits = 120*20; // 1/20
			    	cellImg.getRow().setHeight(heightUnits);

				    i++;
		        }		
				FileOutputStream fos = new FileOutputStream("isbn.xls");
				wb.write(fos);
				fos.close();
				System.out.println("ISBN,ImageURL ���强��");
		   }
		  }catch(Exception e) {
			  e.printStackTrace();
		  }
	}
}