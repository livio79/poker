package poker;

import java.util.ArrayList;

public class Score extends Card{
	private int numberOfPlayer; 
	private String [] names = {"Du","Simon","Alex","Andrew","Livio"}; 
	private int [] winners ;
	private ArrayList<ArrayList<Card>> startHand = new ArrayList<>();
	private ArrayList<ArrayList<Card>> secondHand = new ArrayList<>(); 
	 
	public ArrayList<ArrayList<Card>>  firstHand() {			 
		ArrayList<Card> deck = createDeck(true); 
			for(int p=0; p<numberOfPlayer; p++	) {
				startHand.add(sortDeck(giveCards(deck)));   
			}
		return startHand;
	}
	
	public ArrayList<ArrayList<Card>> seconHand() { 
		 for(int i=0; i<numberOfPlayer; i++) {
			 if(i==0) { 
				  
				 secondHand.add(sortDeck( changeCards(  changeMyCards( startHand.get(i)) )));
				 }
			 else 
				 secondHand.add(sortDeck( changeCards( valuableCards(startHand.get(i)))));
		 } 
		return secondHand;
	}
	
	 
	public String winners(ArrayList<ArrayList<Card>> secondHand) {
		ArrayList<ArrayList<Card>> hand = new ArrayList<>();
		winners = new int[numberOfPlayer];
		for(int i=0; i<numberOfPlayer; i++) {
			hand.add( valuableCards(secondHand.get(i)));				 
		} 
			for(int i=0; i<numberOfPlayer; i++) {
				winners[i] =  calculateScore(hand.get(i));
				}
		 
		boolean [] win = whoWin(winners);
		
		String printWinners = "";
		int count =0;
		for(int i=0; i<win.length; i++) { 
			if(win[i]) {
				printWinners += getName(i) +" ";
				++count;
			}
		}
		
		if(count==win.length)
			printWinners = "Kein Gewinner";
		else if(count>1)
			printWinners = "Die Gewinner: " + printWinners;
		else
			printWinners = "Der Gewinner: " + printWinners;
													 
		 return printWinners;
	}
	 

	/**estimate which cards are relevant for the final score*/             
	public  ArrayList<Card> valuableCards(ArrayList<Card> sortedDeck ) {
		
		ArrayList<Card> cardToEvaluate = new ArrayList<>();

			for (int i = 0; i < sortedDeck.size()-1; i++) {
				int pip1 = sortedDeck.get(i).getPip();
				int pip2 = sortedDeck.get(i+1).getPip();
						if(pip1 ==  pip2) {
							if(!cardToEvaluate.contains(sortedDeck.get(i)))
								cardToEvaluate.add(sortedDeck.get(i));
						
							cardToEvaluate.add(sortedDeck.get(i+1));
						}		
			}
			
			//it could be a straight or a flush  
			if(cardToEvaluate.size()==0) { 
				if(  isStraight(sortedDeck) ||  isFlush(sortedDeck) ) {					 
					for(Card t : sortedDeck) {
						cardToEvaluate.add(t);
					} 
				}
			 }  
		 return cardToEvaluate;
	}
	 
	/**Create an hexadecimal value on the basis of the valuable cards.
	 * The figures have decreasing importance for the valuation of the score
	 * The first figure determine the score ( Pair, Double Pair etc. ) */
	public int calculateScore(ArrayList<Card> cardToEvaluate) {
		int size = cardToEvaluate.size();  
		String hexString = "";
		
			if(size<2) {
				hexString  +=  Integer.toHexString(0);
			}
			if(size==2) {												//Pair
				hexString  +=  Integer.toHexString(1);
				
				int pip =  cardToEvaluate.get(0).getPip();
				hexString  +=  Integer.toHexString(pip);
			}
			
			if(size==3) {												//Tris
				hexString  +=  Integer.toHexString(3);
				int pip =  cardToEvaluate.get(0).getPip();
				hexString  +=  Integer.toHexString(pip);
			}
			
			if(size==4) {
				int pip1 = cardToEvaluate.get(0).getPip();  
				int pip2 = cardToEvaluate.get(3).getPip(); 
					if(pip1==pip2) {
						hexString  +=  Integer.toHexString(7);
						hexString  +=  Integer.toHexString(pip1);		//Poker
						 							
					} else {				
						hexString  +=  Integer.toHexString(2);
						hexString  +=  Integer.toHexString(pip2);
						hexString  +=  Integer.toHexString(pip1);		//Double Pair
						
					}
			}//End of size 4
			
			if(size==5) {
				boolean flush = isFlush(cardToEvaluate);
				boolean straight = isStraight(cardToEvaluate);
				int greaterPip = cardToEvaluate.get(4).getPip();
				
					if(straight && flush) { 							//Royal Straight
						hexString  +=  Integer.toHexString(8);
						hexString  +=  Integer.toHexString(greaterPip);
						
						
					} else if(!straight && flush) {						//Flush
						hexString  +=  Integer.toHexString(5);
						hexString  +=  Integer.toHexString( cardToEvaluate.get(0).getSuit()  );
						 
					} else if(straight && !flush) {						//Straight
						hexString  +=  Integer.toHexString(4);
						hexString  +=  Integer.toHexString(greaterPip);
					
					} else if(!straight && !flush){						//Full  
						hexString  +=  Integer.toHexString(6);
						hexString  +=  Integer.toHexString(cardToEvaluate.get(2).getPip());
						
							if(cardToEvaluate.get(2).getPip()==cardToEvaluate.get(0).getPip())
								hexString  +=  Integer.toHexString( cardToEvaluate.get(4).getPip()  );
								 
							else
								hexString  +=  Integer.toHexString(cardToEvaluate.get(0).getPip()   ); 	 
					}
			}
			 
			while(hexString.length()<4) {
				hexString += 0;
			} 
		return	Integer.parseInt(hexString,16);
	} 
  
	
	/**Compare the scores and add the winner/s in an ArrayList<Integer> */
	public boolean[] whoWin(int [] scores) {
		boolean [] winners = new boolean [numberOfPlayer];
		int max = 0;
		for (int i = 0; i < scores.length; i++) {
			if(max < scores[i])
				max = scores[i];
		}
		
		for (int i = 0; i < scores.length; i++) {
			if(scores[i]== max)
				winners[i]= true;
		}
		return winners;
	}
	 
	
	/**Determine whether the score is a straight */
	public boolean isStraight(ArrayList<Card> sortedDeck) {
		boolean straight = true;
			for (int i = 0; i < sortedDeck.size()-1; i++) {
				int pip1  =	sortedDeck.get(i).getPip() ;
				int pip2  = sortedDeck.get(i+1).getPip() ;
					if(pip1 != pip2 -1) {
						straight= false;			
					}
			}
			return straight;
	}

	/**Determine whether the score is a flush */
	public boolean isFlush(ArrayList<Card> sortedDeck) {
		boolean flush = true; 
			for (int i = 0; i < sortedDeck.size()-1; i++) {
				int suit1 = sortedDeck.get(i).getSuit() ;
				int suit2 = sortedDeck.get(i+1).getSuit() ;
					if(suit1 != suit2) {
						flush = false;
					}
			}
			return flush;
	}
 
	
	public ArrayList<ArrayList<Card>> getHand(){
		return startHand;
	}
	
	public void setNumberPlayer(int n) {
		this.numberOfPlayer = n;
	}
	
	public int getNumberPlayer() {
		return numberOfPlayer;
	}
	
	public String getName(int position) {
		return names[position];
	}
}
