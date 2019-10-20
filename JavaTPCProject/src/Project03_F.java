import java.io.BufferedReader;
import java.io.InputStreamReader;

import kr.inflearn.ExcelDAO;
public class Project03_F {			
	public static void main(String[] args) {
		ExcelDAO dao=new ExcelDAO();		
		BufferedReader br=new BufferedReader(new InputStreamReader(System.in));
		try {
		    System.out.print("입력처리(I)/종료(E):");
    	    String sw=br.readLine();
    	    switch (sw) {
			case "I":
				dao.excel_input();	
				break;
            case "E":
            	System.out.println("프로그램종료");
				System.exit(0);
				break;         
			default:				
			    System.out.println("I or E input");	
			}    	    	
		}catch(Exception e) {
			e.printStackTrace();
		}
	}
}

