package blackJack;

import java.util.ArrayList;

import system.core.Card;
import system.core.CardDeck;
import system.core.VisualizerOnCUI;

public final class CommonInformation implements VisualizerOnCUI {
	public final int numberOfPlayers;
	public final int gameNumber;
	public final int minimumBet;
	private CardDeck gameDeck;
	private ArrayList<String> playerNames;
	private ArrayList<Integer> allPlayersChips;
	private ArrayList<ArrayList<ArrayList<Card>>> allPlayersHands;
	private ArrayList<Integer> allPlayersBets;
	private ArrayList<ArrayList<String>> allPlayersStatus;
	private ArrayList<Integer> allPlayersInsurance;
	private ArrayList<Card> dealersOpenCards;

	public CommonInformation(int numberOfPlayers,
			int gameNumber,
			int minimumBet,
			CardDeck gameDeck,
			ArrayList<String> playerNames,
			ArrayList<Integer> allPlayersChips,
			ArrayList<ArrayList<ArrayList<Card>>> allPlayersHands,
			ArrayList<Integer> allPlayersBets,
			ArrayList<Integer> allPlayersInsurance,
			ArrayList<ArrayList<String>> allPlayersStatus,
			ArrayList<Card> dealersOpenCards) {
		this.numberOfPlayers = numberOfPlayers;
		this.gameNumber = gameNumber;
		this.minimumBet = minimumBet;
		this.gameDeck = gameDeck;
		this.playerNames = playerNames;
		this.allPlayersChips = allPlayersChips;
		this.allPlayersHands = allPlayersHands;
		this.allPlayersBets = allPlayersBets;
		this.allPlayersInsurance = allPlayersInsurance;
		this.allPlayersStatus = allPlayersStatus;
		this.dealersOpenCards = dealersOpenCards;
	}

	public int getCurrentDeckSize() {
		return gameDeck.getCurrentDeckSize();
	}

	public int getInitialDeckSize() {
		return gameDeck.getInitialDeckSize();
	}

	public ArrayList<String> getPlayerNamesCopy() {
		return new ArrayList<String>(playerNames);
	}

	public ArrayList<Integer> getPlayerChipsCopy(){
		return new ArrayList<Integer>(this.allPlayersChips);
	}

	public ArrayList<ArrayList<ArrayList<Card>>> getAllPlayersHandsCopy() {
		ArrayList<ArrayList<ArrayList<Card>>> copyAllPlayerHands = new ArrayList<ArrayList<ArrayList<Card>>>();
		for(ArrayList<ArrayList<Card>> playerHand : this.allPlayersHands) {
			ArrayList<ArrayList<Card>> copyPlayerHand = new ArrayList<ArrayList<Card>>();
			copyAllPlayerHands.add(copyPlayerHand);
			for(ArrayList<Card> singleHand : playerHand) {
				copyPlayerHand.add(new ArrayList<Card>(singleHand));
			}
		}
		return copyAllPlayerHands;
	}

	public ArrayList<Integer> getAllPlayersBetsCopy() {
		ArrayList<Integer> copyAllPlayersBets = new ArrayList<Integer>(allPlayersBets);
		return copyAllPlayersBets;
	}

	public ArrayList<Integer> getAllPlayersInsuranceCopy() {
		return new ArrayList<Integer>(allPlayersInsurance);
	}

	public ArrayList<ArrayList<String>> getAllPlayersStatusCopy() {
		ArrayList<ArrayList<String>> copyAllPlayersStatus = new ArrayList<ArrayList<String>>();
		for(ArrayList<String> playersStatus : this.allPlayersStatus) {
			copyAllPlayersStatus.add(new ArrayList<String>(playersStatus));
		}
		return copyAllPlayersStatus;
	}

	public ArrayList<Card> getDealersOpenCardsCopy(){
		return new ArrayList<Card>(this.dealersOpenCards);
	}

	@Override
	public void printStatus() {
		for(int i = 0; i < numberOfPlayers; i++) {
			System.out.println(playerNames.get(i) + "のチップ : " + allPlayersChips.get(i));
		}
		if(!allPlayersStatus.isEmpty()) {
			if(!allPlayersStatus.get(0).get(0).contentEquals("INITIALIZED")) {
				System.out.println("ディーラーのオープンカード");
				for(Card card : dealersOpenCards) {
					System.out.println(card.cardInfo());
				}
			}
			for(int i = 0; i < numberOfPlayers; i++) {
				ArrayList<String> playersStatus = allPlayersStatus.get(i);
				if(playersStatus.size() > 1) {
					for(int j = 0; j < playersStatus.size(); j++) {
						System.out.println(playerNames.get(i) + "の手札 " + j + " の状態 : " + playersStatus.get(j));
						System.out.println(playerNames.get(i) + "の賭金 : " + allPlayersBets.get(i));
						System.out.println(playerNames.get(i) + "の手札 " + j);
						for(Card card : allPlayersHands.get(i).get(j)) {
							card.printStatus();
						}
						System.out.println();
					}
				} else {
					System.out.println(playerNames.get(i) + "の状態 : " + playersStatus.get(0));
					if(
							!(
								playersStatus.get(0).contentEquals("PASS") ||
								playersStatus.get(0).contentEquals("BET") ||
								playersStatus.get(0).contentEquals("INITIALIZED")
							)
							) {
						System.out.println(playerNames.get(i) + "の賭金 : " + allPlayersBets.get(i));
						System.out.println(playerNames.get(i) + "の手札");
						for(Card card : allPlayersHands.get(i).get(0)) {
							card.printStatus();
						}
						System.out.println();
					}
				}
			}

		}
	}

	@Override
	public void printName() {
		System.out.println("game" + gameNumber + "の情報");
	}

	@Override
	public void printClass() {
		System.out.println("CommonInformation");
	}

}
