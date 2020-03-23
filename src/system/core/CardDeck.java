package system.core;

import java.util.ArrayList;
import java.util.Collections;

/**
 * カードの山札を扱うクラス。
 * @author Takashi Sakakihara
 *
 */
public class CardDeck implements VisualizerOnCUI {
	/**
	 * カードの山札。
	 */
	private ArrayList<Card> cards;
	/**
	 * この山札がゲーム開始時もともと何枚だったかを保持する。
	 */
	private int initialDeckSize;

	/**
	 * 初期山札（カードのArrayList）を受け取って初期化するコンストラクタ。
	 * @param cards カードのArrayList
	 */
	public CardDeck(ArrayList<Card> cards) {
		this.cards = cards;
		this.initialDeckSize = cards.size();
		Collections.shuffle(cards);
	}

	/**
	 * ジョーカーを除いたトランプのカード52枚のデッキを引数個混ぜて初期化するコンストラクタ。
	 * @param numberOfCardDeck トランプのデッキ数
	 */
	public CardDeck(int numberOfCardDeck) {
		cards = new ArrayList<Card>();
		for(int i = 0; i < numberOfCardDeck; i++) {
			for(int j = 1; j < 14; j++) {
				for(int k = 0; k < 4; k++) {
					try {
						cards.add(new Card(j, k));
					} catch(Exception e) {
						System.out.println(e);
					}
				}
			}
		}

		this.initialDeckSize = numberOfCardDeck * 52;
		Collections.shuffle(cards);
	}

	/**
	 * デフォルトのコンストラクタ。ジョーカーを除いた52枚で初期化する。
	 */
	public CardDeck() {
		this(1);
	}

	/**
	 * 山札にジョーカーを numberOfJokers 枚追加する。
	 * ジョーカーは強いものと弱いものをそれぞれ15と14に交互に割り当てる。
	 * 初期化直後のカードが減っていないときにのみ動作する。
	 * @param numberOfJokers 山札に入れるジョーカーの枚数
	 */
	public void addJokers(int numberOfJokers) {
		if(numberOfJokers < 1) {
			System.out.println("正の整数ではない枚数のジョーカーを追加することはできません。");
			return;
		} else if(cards.size() != this.initialDeckSize) {
			System.out.println("すでに使用が始まっているデッキにジョーカーを追加することはできません。");
		}
		int jokerNumber = 15;
		for(int i = 0; i < numberOfJokers; i++) {
			try {
				cards.add(new Card(jokerNumber, 4));
				jokerNumber = (jokerNumber == 15 ? 14 : 15);
			} catch(InvalidCardException e) {
				System.out.println(e);
			}
		}
		this.initialDeckSize = cards.size();
		Collections.shuffle(cards);
	}


	/**
	 * デッキからカードを drawNumber 枚引いて、引いたカードを返す。
	 * @param drawNumber 引く枚数
	 * @return 引いたカードのArrayList
	 */
	public ArrayList<Card> drawCards(int drawNumber) {
		ArrayList<Card> drawCards = new ArrayList<Card>();
		for(int i = 0; i < drawNumber; i++) {
			drawCards.add(cards.remove(0));
		}
		return drawCards;
	}

	public Card drawCard() {
		return cards.remove(0);
	}

	public int getInitialDeckSize() {
		return this.initialDeckSize;
	}

	public int getCurrentDeckSize() {
		return this.cards.size();
	}

	public double getRemainingRate() {
		return (((double) cards.size() / (double) initialDeckSize));
	}

	@Override
	public void printStatus() {
		System.out.println("山札の初期枚数：" + this.initialDeckSize);
		System.out.println("カードの残り枚数：" + this.cards.size());
	}

	@Override
	public void printName() {
		System.out.println("トランプの山札");
	}

	@Override
	public void printClass() {
		System.out.println("CardDeck");
	}
}
