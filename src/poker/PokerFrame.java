package poker;

import java.io.*;
import java.net.MalformedURLException;
import java.net.URL; 
import java.util.ArrayList;
import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ToggleButton;
import javafx.scene.control.Tooltip;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color; 
import javafx.scene.shape.Circle;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebView;
import javafx.stage.Stage;
 
public class PokerFrame extends Application {
		private Score score = new Score(); 
		private Html html = new Html();
		final WebView browser = new WebView();
		final WebEngine webEngine = browser.getEngine();
	    private int paneWidth = 700  ;
		private int paneHeight = 600;  
		private ArrayList<ArrayList<Card>> first = new ArrayList<>();
		private ArrayList<ArrayList<Card>> second = new ArrayList<>();
		private boolean [] change = new boolean [5];
		private Button [] chooseCards = new Button[5];
		private Circle [] circle = new Circle[5];
		private int step = 0; 
		private Button ok = new Button("OK");
		private Button newGame = new Button("New Game");
		private Button ranking = new Button("Ranking");
		private ToggleButton displayAllCards = new ToggleButton("Show me"); 
		private BorderPane root = new BorderPane();
		private GridPane top = new GridPane();
		private GridPane bottom = new GridPane();
		private Label message = new Label();

		/**Event Hadler for the five bottom buttons*/
		EventHandler<ActionEvent> choose = new EventHandler<ActionEvent>() {
	    	public void handle(ActionEvent event) {
	    		if(step==0){
		    		String text = ((Button)event.getSource()).getText();
		    		int number = Integer.parseInt(text);
		    		score.setNumberPlayer(number); 
	    		}
	    		
	    		if(step==1) {
	    			int buttonNr =   Integer.parseInt(((Button)event.getSource()).getText()) -1 ; 
	    			change[buttonNr] =  !change [buttonNr];
	    			if(change[buttonNr]) {
	    				 circle[buttonNr].setFill(Color.YELLOW);
	    				}
	    			else {
	    				 circle[buttonNr].setFill(Color.LIGHTGRAY);
	    				}
	    		}
	    	} 
		};
		
    public void start(final Stage stage) {
    	 
    	html.loadFileHtml("pokerFiles\\pokerHtml.html", webEngine);
    	
	     //New Game button
	     newGame.setOnAction(
	        new EventHandler<ActionEvent>() {
	           	public void handle(ActionEvent event)  { 
	           		html.loadFileHtml("pokerFiles\\pokerHtml.html", webEngine);
	           		stage.setWidth(paneWidth);
	           		stage.setHeight(paneHeight);					
	           		message.setText("");
	           		
	           		first.clear(); 
	        		second.clear();
	        		change = new boolean [5];
	        		step = 0;
	        		score.setNumberPlayer(0);
	        		
	        		 for(int i=0; i<5; i++) {
	        			 circle[i].setFill(Color.LIGHTGRAY);
	        	        } 
	           	}
	        });
	     
	     //Ranking button 
	        ranking.setOnAction(new EventHandler<ActionEvent>() {
	            @Override
	            public void handle(ActionEvent event) {
	            	Image ranking;
	            	ImageView rView = new ImageView();
	            	
					try {
						ranking = new Image   (new FileInputStream("pokerFiles\\Wertung.png"));
						rView = new ImageView(ranking)  ;
					} catch (FileNotFoundException e) {
						e.printStackTrace();
					}
	    			
	                StackPane rankingPane = new StackPane();
	                rankingPane.getChildren().add(rView);
	 
	                Scene secondScene = new Scene(rankingPane, 230, 100);
	 
	                Stage wertungWindow = new Stage();       
	                wertungWindow.setTitle("RANKING");
	                wertungWindow.setScene(secondScene);
	                wertungWindow.setHeight(570);
	                wertungWindow.setWidth(510);
	                wertungWindow.setX(stage.getX() + 200);
	                wertungWindow.setY(stage.getY() + 100);
	                wertungWindow.setResizable(false);
	                wertungWindow.show();
	                
	            }
	        });
	        
	       
      //OK Button
        ok.setOnAction(
        		new EventHandler<ActionEvent>() {
        	    	public void handle(ActionEvent event)  {
        	    		
        	    		if(step==0) {
        	    			stage.setHeight((score.getNumberPlayer() + 3) * 90);
        	    			stage.setWidth(paneWidth + 40);
        		    		first =  score.firstHand(); 
        		    		html.writeFile(first, step,displayAllCards); 
        		    		html.loadFileHtml("pokerFiles\\allCards.html", webEngine);	 		
        		    		message.setText("Du kannst deine Karte wechseln");
        		    		step = 1;
        	    			}
        	    					
        	    		else if(step==1) {
        	    			
        	    			score.setChange(change);
        	    			second = score.seconHand(); 
        	    			html.writeFile(second, step,displayAllCards); 
        	    			html.loadFileHtml("pokerFiles\\allCards.html", webEngine);	       	    			
        	    			message.setText(score.winners(second));
        	    			step=2;
        	    			}
        	    		}
      
    		});
        
        
        // Top Pane 
        message.setTextFill(Color.RED);
        message.setFont(Font.font("Cambria",FontWeight.BOLD, 25));
        message.setPadding(new Insets(0, 0, 0, 30));
        
        displayAllCards.setTooltip(new Tooltip("Zeige mir die Karten meiner Gegner"));
        
        top.add(message, 3, 0);
        top.add(displayAllCards, 2, 0);
        
        newGame.setStyle("-fx-font-weight: bold;");
        ranking.setStyle("-fx-font-weight: bold;");
        displayAllCards.setStyle("-fx-font-weight: bold;");
        top.add(newGame, 0 , 0);  
        top.add(ranking, 1, 0);  
        top.setPadding(new Insets(10,10,10,10));
        top.setHgap(10);

        //Bottom Pane
        for(int i=0; i<5; i++) {
        	circle[i] = new Circle(25);
        	circle[i].setFill(Color.LIGHTGRAY);
        	bottom.add(circle[i], i, 0);
        }
        
        for(int i=0; i<5; i++) {
        	chooseCards[i] = new Button(i + 1 + "");
        	chooseCards[i].setOnAction(choose);
        	chooseCards[i].setShape(new Circle(5.0));
	    	chooseCards[i].setStyle ("-fx-font-weight: bold; -fx-font-size: 14px;"); 
        	bottom.add(chooseCards[i], i, 0); 
        	bottom.setHalignment(chooseCards[i], HPos.CENTER); 
        }
        ok.setShape(new Circle(5.0));
        ok.setStyle ("-fx-font-weight: bold; -fx-font-size: 20px;"); 
        bottom.add(ok, 5, 0);
        bottom.setPadding(new Insets(5,0,0,38));
        bottom.setHgap(60);
        
        //ROOT BORDERPANE
        root.setPadding(new Insets(5));
        root.setTop(top);
        root.setCenter(browser);
        root.setBottom(bottom);
 	    root.setBackground(new Background(new BackgroundFill(Color.WHITE, null, null)));
	    
        Scene scene = new Scene(root); 
        stage.setTitle("POKER");
        stage.setScene(scene);
        stage.setX(400);
        stage.setY(20);
        stage.setWidth(paneWidth);
        stage.setHeight(paneHeight);
        stage.setResizable(false);
        stage.show();
    }
     
    public static void main(String[] args) {
        launch(args);
    }
 
}