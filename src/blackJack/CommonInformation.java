package blackJack;

import java.util.ArrayList;

import system.core.Card;
import system.core.CardDeck;
import system.core.VisualizerOnCUI;

/**
 * ゲームの公開情報のクラス。
 * ゲームシステムとプレイヤーの情報共有のために使用する。
 * ほとんどの情報はシャローコピーのため、元のデータが変更されると自動的にこちらも変更される。
 * @author Takashi Sakakihara
 */
public final class CommonInformation implements VisualizerOnCUI {
	/**
	 * プレイヤーの人数。
	 */
	public final int numberOfPlayers;
	/**
	 * ゲーム番号。
	 */
	public final int gameNumber;
	/**
	 * 最小賭金。
	 */
	public final int minimumBet;
	/**
	 * ゲームで使用する山札。シャローコピー。
	 */
	private CardDeck gameDeck;
	/**
	 * 全プレイヤーの名前。シャローコピー。
	 */
	private ArrayList<String> playerNames;
	/**
	 * 全プレイヤーのチップ。シャローコピー。
	 */
	private ArrayList<Integer> allPlayersChips;
	/**
	 * 全プレイヤーの全ての手札（スプリットして増えたものを含む）。シャローコピー。
	 */
	private ArrayList<ArrayList<ArrayList<Card>>> allPlayersHands;
	/**
	 * 全てのプレイヤーの賭金。シャローコピー。
	 */
	private ArrayList<Integer> allPlayersBets;
	/**
	 * 全てのプレイヤーの全ての手札（スプリットした場合のみ増える）における状態。シャローコピー。
	 */
	private ArrayList<ArrayList<String>> allPlayersStatus;
	/**
	 * 全てのプレイヤーのインシュランス。シャローコピー。
	 */
	private ArrayList<Integer> allPlayersInsurance;
	/**
	 * ディーラーのオープンカード。シャローコピー。
	 */
	private ArrayList<Card> dealersOpenCards;

	/**
	 * コンストラクタ。
	 * @param numberOfPlayers プレイヤー数
	 * @param gameNumber ゲーム番号
	 * @param minimumBet 最小賭金
	 * @param gameDeck ゲームの山札
	 * @param playerNames 全プレイヤーの名前
	 * @param allPlayersChips 全プレイヤーのチップ
	 * @param allPlayersHands 全プレイヤーの全手札
	 * @param allPlayersBets 全プレイヤーの賭金
	 * @param allPlayersInsurance 全プレイヤーのインシュランス
	 * @param allPlayersStatus 全プレイヤーのステータス
	 * @param dealersOpenCards ディーラーの公開カード
	 */
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

	/**
	 * ゲームの山札の枚数を取得する。
	 * @return ゲームの山札の枚数。
	 */
	public int getCurrentDeckSize() {
		return gameDeck.getCurrentDeckSize();
	}

	/**
	 * ゲームの山札の初期枚数を取得する。
	 * @return ゲームの山札の初期枚数。
	 */
	public int getInitialDeckSize() {
		return gameDeck.getInitialDeckSize();
	}

	/**
	 * 全プレイヤーの名前を取得する。
	 * 元のデータの改変を防ぐためディープコピーを返す。
	 * @return 全プレイヤー名のディープコピー。
	 */
	public ArrayList<String> getPlayerNamesCopy() {
		return new ArrayList<String>(playerNames);
	}

	/**
	 * 全プレイヤーのチップを取得する。
	 * 元のデータの改変を防ぐためディープコピーを返す。
	 * @return 全プレイヤーのチップのディープコピー。
	 */
	public ArrayList<Integer> getPlayerChipsCopy(){
		return new ArrayList<Integer>(this.allPlayersChips);
	}

	/**
	 * 全プレイヤーの全ての手札を取得する。
	 * 元のデータの改変を防ぐためディープコピーを返す。
	 * @return 全プレイヤーの全手札のディープコピー。
	 */
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

	/**
	 * 全プレイヤーの賭金を取得する。
	 * 元のデータの改変を防ぐためディープコピーを返す。
	 * @return 全プレイヤーの賭金のディープコピー。
	 */
	public ArrayList<Integer> getAllPlayersBetsCopy() {
		ArrayList<Integer> copyAllPlayersBets = new ArrayList<Integer>(allPlayersBets);
		return copyAllPlayersBets;
	}

	/**
	 * 全プレイヤーのインシュランスを取得する。
	 * 元のデータの改変を防ぐためディープコピーを返す。
	 * @return 全プレイヤーのインシュランスのディープコピー。
	 */
	public ArrayList<Integer> getAllPlayersInsuranceCopy() {
		return new ArrayList<Integer>(allPlayersInsurance);
	}

	/**
	 * 全プレイヤーの全手札の状態を取得する。
	 * 元のデータの改変を防ぐためディープコピーを返す。
	 * @return 全プレイヤーの全手札の状態のディープコピー。
	 */
	public ArrayList<ArrayList<String>> getAllPlayersStatusCopy() {
		ArrayList<ArrayList<String>> copyAllPlayersStatus = new ArrayList<ArrayList<String>>();
		for(ArrayList<String> playersStatus : this.allPlayersStatus) {
			copyAllPlayersStatus.add(new ArrayList<String>(playersStatus));
		}
		return copyAllPlayersStatus;
	}

	/**
	 * ディーラーのオープンカードを取得する。
	 * 元のデータの改変を防ぐためディープコピーを返す。
	 * @return ディーラーのオープンカードのディープコピー。
	 */
	public ArrayList<Card> getDealersOpenCardsCopy(){
		return new ArrayList<Card>(this.dealersOpenCards);
	}

	/**
	 * 現在の場の全ての状態の表示。
	 * VisualizerOnCUIインターフェースの実装。
	 */
	@Override
	public void printStatus() {
		System.out.println("-------------------------------------------");
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
		System.out.println("-------------------------------------------");
		System.out.println();
	}

	/**
	 * このオブジェクトの名前の表示。
	 * VisualizerOnCUIインターフェースの実装。
	 */
	@Override
	public void printName() {
		System.out.println("game" + gameNumber + "の情報");
	}

	/**
	 * このオブジェクトのクラス名の表示。
	 * VisualizerOnCUIインターフェースの実装。
	 */
	@Override
	public void printClass() {
		System.out.println("CommonInformation");
	}

}
