package poker;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import javafx.scene.control.Toggle;
import javafx.scene.web.WebEngine;

public class Html {
	Score score = new Score(); 
	   
	public String writeFile(ArrayList<ArrayList<Card>> hand, int step, Toggle showOthers) {
	String html = "<html><head><style> "
						+ "body {background-image: url(\"tableGreen2.jpg\");  }"
						+ "img:hover {transition: all .4s; transform: scale(1.2); }	"
						+ "p:hover { transform: rotateX(10deg);  background-color: lightgreen;  border-style: 1px solid; border-radius: 25px; }"
						+ " </style></head><body>";
		 
		for(int i=hand.size()-1; i>=0; i--) {
			html += "<p>" ;
			for(int j=0; j<5; j++) {
				String fileName = "";
				boolean showCard =  showOthers.isSelected();
				if(showCard || step==1 || i==0) {
					fileName = hand.get(i).get(j).getPip() + "" + hand.get(i).get(j).getSuit() + ".jpg";
					}
				else {
					fileName = "back.png";
				}
				html +=      "<img src=\"myCards\\" +  fileName + "\" width=90 height=125  align=\"middle\" hspace=\"10\">";
			}
				html += "<font size=\"6\"> "+ score.getName(i) + "</font></p> </body></html>";
		}
		
		Path path = Paths.get("pokerFiles\\allCards.html");
		try {
			Files.write(path, html.getBytes());
		} catch (IOException e) { 
			e.printStackTrace();
		}
		return html;
	}
	
	 public void loadFileHtml(String nomeFile, WebEngine webEngine) {
	     	File file = new File(nomeFile);
	     	URL url=null;
				try {
					url = file.toURI().toURL();
				} catch (MalformedURLException e) {
					e.printStackTrace();
				}
	     	webEngine.load(url.toString());
		 }

	
}
