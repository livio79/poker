package poker;

import java.util.*;

public class Card  {  	 
	private int suit;
	private int pip;
	private static final int NUMBER_OF_CARDS = 5;
	private static final int SUITS = 4;
	private static final int PIPS = 13;
	private  ArrayList<Card> deck = new ArrayList<>();	 
	private boolean [] changeCards = new boolean[NUMBER_OF_CARDS];
	 		
	  
	 /** create a deck. if shuffle is false the deck is still sorted
	 * @param shuffle */
	public ArrayList<Card> createDeck (boolean shuffle ){
		for(int i=0; i<SUITS; i++) {
			for(int j=0; j<PIPS; j++) {					
				Card card = new Card();
				card.suit = i;
				card.pip  = j;
				deck.add(card); 
			}
		}
		if(shuffle)
			Collections.shuffle(deck);
		return deck;
	}

	
	/**give a NUMBER OF CARD to a player*/
	public ArrayList<Card> giveCards( ArrayList<Card> deck) {   
		ArrayList<Card> cardsHand = new ArrayList<>();
		 
				for(int i = 0; i<NUMBER_OF_CARDS; i++) {
					cardsHand.add(deck.get(0));
					deck.remove(0);
				}
		return cardsHand;	
	}
 												

	/**sort the cards on the basis of the pips*/   
	public  ArrayList<Card> sortDeck(ArrayList<Card> cards) {
		ArrayList<Card> sorted = new ArrayList<>();
		int size = cards.size();
		int pip [] = new int [size];
		int suit[] = new int [size];
			
			for(int i=0; i<size; i++) {
				pip[i]= cards.get(i).getPip();
				suit[i]= cards.get(i).getSuit();
			}
			
			for (int i = 0; i < size-1; i++)  {     
			       for (int j = 0; j < size-i-1; j++) {
				           if (pip[j] > pip[j+1]) {
				        	   	int tmp= pip[j];
				   				pip[j]= pip[j+1];
				   				pip[j+1]= tmp;
				   				
				   				int tmp2= suit[j];
				   				suit[j]= suit[j+1];
				   				suit[j+1]= tmp2;
				           }
			       }
			   }
			Card [] sortedCards = new Card[size];
			for (int i = 0; i < suit.length; i++) {
				sortedCards[i] = new Card();
				sortedCards[i].setPip(pip[i]);
				sortedCards[i].setSuit(suit[i]);
				sorted.add(sortedCards[i]);
			} 
		 	return sorted;
	}
 
	/**the real player decides which cards to keep*/
	public ArrayList<Card> changeMyCards ( ArrayList<Card> startHand) {
		for(int i = startHand.size()-1; i>=0; i--) {
				 if(changeCards[i])
				   startHand.remove(i);
		}
		return startHand;
	}


	/**give the cards to change the discarded cards*/
	public ArrayList<Card> changeCards( ArrayList<Card> cardToEvaluate) {            
		int cardsTochange = NUMBER_OF_CARDS - cardToEvaluate.size();		
				for(int i = 0; i<cardsTochange; i++) {
					cardToEvaluate.add(deck.get(0));
					deck.remove(0);
				}
		return cardToEvaluate;	
	}
	
 
	public ArrayList<Card> getDeck(){
		return deck;
	}
	
	public int getSuit() {
		return suit;
	}

	public void setSuit(int suit) {
		this.suit=suit;
	}

	public int getPip() {
		return pip;
	}

	public void setPip(int pip) {
		this.pip = pip;
	}
	
 
	public void setChange(boolean []cambia) {
		this.changeCards = cambia;
	}
	
	public boolean[] getChange() {
		return changeCards;
	}
 }
