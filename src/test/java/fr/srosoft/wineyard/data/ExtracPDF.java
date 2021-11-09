package fr.srosoft.wineyard.data;

import java.io.File;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.text.PDFTextStripper;

public class ExtracPDF {
	public static void main (String args[]) {
		try {
			String s = "Dénomination Couleur de la baie Synonyme(s) \r\n" + 
					"Abondant (6) Blanche  \r\n" + 
					"Abouriou Noire  \r\n" + 
					"Agiorgitiko (4) Noire  \r\n" + 
					"Aléatico(4) Noire  \r\n" + 
					"Alicante Henri Bouschet (3) Noire  \r\n" + 
					"Aligoté (5) Blanche  \r\n" + 
					"Altesse (5) Blanche  \r\n" + 
					"Alvarinho Blanche Albariño ";
			
			System.out.println(s.split("Noire").length);
			
			
			PDDocument document = PDDocument.load(new File("C:\\z_data\\wineyard\\AnnexeBOagrimars20.pdf"));
			if (!document.isEncrypted()) {
			    PDFTextStripper stripper = new PDFTextStripper();
			    stripper.setEndPage(7);
			    String text = stripper.getText(document);
			    String texts[] = text.split("\r\n");
			    //System.out.println(text);
			    for (int i = 0; i < texts.length; i++) {
					
					boolean bool = false;
				    if (texts[i].startsWith("Dénomination Couleur de la baie Synonyme(s)"))
				    {
				    	   System.out.println("Text:" + texts[i]);
				    	   if (!bool) {
				    		   bool=true;
				    		   continue;
				    	   }
				    }
				    
				    //if (bool) 
				    {
				    	   
				    	   
				    	   if ( texts[i].contains("Noire"))
				    	   {
				    		  
				    		   String clean =  texts[i].replace ("(*)","")
				    				   						.replace ("(1)","")
				    				   						.replace ("(2)","")
				    				   						.replace ("(3)","")
				    				   						.replace ("(4)","")
				    				   						.replace ("(5)","")
				    				   						.replace ("(6)","");
				    		   int index = clean.indexOf("Noire");
				    		   System.out.println(clean.substring(0,index).trim()+";red;"+clean.substring(index+5).trim());
				    		   
				    	   }
				    	   else  if ( texts[i].contains("Blanche"))
				    	   {
				    		  
				    		   String clean =  texts[i].replace ("(*)","")
				   						.replace ("(1)","")
				   						.replace ("(2)","")
				   						.replace ("(3)","")
				   						.replace ("(4)","")
				   						.replace ("(5)","")
				   						.replace ("(6)","");
				    		   int index = clean.indexOf("Blanche");
				    		   System.out.println(clean.substring(0,index).trim()+";white;"+clean.substring(index+7).trim());
				    		   
				    	   }
				    	   else  if ( texts[i].contains("Grise"))
				    	   {
				    		  
				    		   String clean =  texts[i].replace ("(*)","").replace ("(*)","")
				   						.replace ("(1)","")
				   						.replace ("(2)","")
				   						.replace ("(3)","")
				   						.replace ("(4)","")
				   						.replace ("(5)","")
				   						.replace ("(6)","");
				    		   int index = clean.indexOf("Grise");
				    		   System.out.println(clean.substring(0,index).trim()+";grey;"+clean.substring(index+5).trim());
				    		   
				    	   }
				    	   
				    }
				    			 
				    
				    //System.out.println(i);
			    }
			}
			document.close();
			
		}catch (Exception e) {
			e.printStackTrace();
			
		}
	}
}
