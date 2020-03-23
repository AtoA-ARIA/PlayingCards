package system.core;

/**
 * トランプのカード一枚一枚を実装するクラス。
 * このクラスのインスタンスはコンストラクタで定めた値から変わらない不変オブジェクトとなる。
 * @author Takashi Sakakihara
 *
 */
public class Card implements VisualizerOnCUI {
	/**
	 * トランプのカードの数字（1から15まで）
	 */
	private int number;
	/**
	 * トランプのカードのマーク（4がジョーカー、3がスペード、2がハート、1がクラブ、0がダイヤ）
	 */
	private int symbol;

	/**
	 * コンストラクタ。不正な値が入ってきた場合は例外InvalidCardExceptionを投げる。
	 * @param number カード番号
	 * @param symbol カードのマーク番号
	 * @throws InvalidCardException 存在し得ないカードである例外
	 */
	public Card(int number, int symbol) throws InvalidCardException {
		if(0 < number && number < 16) {
			this.number = number;
		} else {
			throw new InvalidCardException("set number " + number);
		}

		if( (0 <= symbol && symbol <= 3) || (13 < number && symbol == 4) ) {
			this.symbol = symbol;
		} else {
			throw new InvalidCardException("set symbol " + symbol);
		}
	}

	/**
	 * カードの数字を返す。ジョーカーは14と15
	 * @return カードの数字
	 */
	public int getNumber() {
		return this.number;
	}

	/**
	 * カードのマークの番号（4がジョーカー、3がスペード、2がハート、1がクラブ、0がダイヤ）を返す。
	 * @return カードのマークの数字
	 */
	public int getSymbol() {
		return this.symbol;
	}

	/**
	 * カードのマークの文字列（ジョーカー、スペード、ハート、クラブ、ダイヤ）を返す。
	 * @return カードのマークの文字列
	 */
	public String getSymbolString() {
		return symbolNumberToString(this.symbol);
	}

	/**
	 * カードの数字の文字列を返す。
	 * 文字列はそれぞれ（エース、2～10、ジャック、クイーン、キング、（ジョーカーの）強い方、弱い方）
	 * @return カードの数字の文字列
	 */
	public String getNumberString() {
		return numberToString(this.number);
	}

	/**
	 * カードの情報（マークと数字）の文字列を返す。
	 * @return カードの情報
	 */
	public String cardInfo() {
		return this.getSymbolString() + "の" + this.getNumberString();
	}

	/**
	 * マークの数字（0～4）を文字列（ダイヤ～スペード、ジョーカー）を返す静的メソッド。
	 * @param symbolNum マークの番号
	 * @return マークの文字列
	 */
	public static String symbolNumberToString(int symbolNum) {
		switch(symbolNum) {
		case 4:
			return "ジョーカー";
		case 3:
			return "スペード";
		case 2:
			return "ハート";
		case 1:
			return "クラブ";
		case 0:
			return "ダイヤ";
		}
		System.out.println("対応するマークはありません");
		return "";
	}

	/**
	 * カードの数字（1～15）から文字列を返す静的メソッド。
	 * @param number カードの数字
	 * @return カードの数字の文字列
	 */
	public static String numberToString(int number) {
		String ret;
		switch(number) {
		case 15:
			ret = "強い方";
			break;
		case 14:
			ret = "弱い方";
			break;
		case 13:
			ret = "キング";
			break;
		case 12:
			ret = "クイーン";
			break;
		case 11:
			ret = "ジャック";
			break;
		case 1:
			ret = "エース";
			break;
		default:
			ret = Integer.toString(number);
			break;
		}
		return ret;
	}

	/**
	 * マークの文字列（ダイヤ～スペード）からマークの数字（0～3）を返す静的メソッド。
	 * @param symbolStr マークの文字列
	 * @return マークの番号
	 */
	public static int symbolStringToNum(String symbolStr) {
		switch(symbolStr) {
		case "ジョーカー":
			return 4;
		case "スペード":
			return 3;
		case "ハート":
			return 2;
		case "クラブ":
			return 1;
		case "ダイヤ":
			return 0;
		}
		System.out.println("対応する数字はありません");
		return -1;
	}

	/**
	 * カードの強さの値を返す。
	 * 15のジョーカー＞14のジョーカー＞A＞13～2
	 * @return カードパワーの数値
	 */
	public int cardPower() {
		int num = this.number - 1;
		if(num == 0) num = 13;
		return num * 4 + this.symbol;
	}

	/**
	 * 通常のトランプの強さで2枚のカードを比較する。
	 * カードの強さの差を返す。
	 * @param card1 1枚目のカード
	 * @param card2 2枚目のカード
	 * @return 1枚目のカードの強さから2枚目のカードの強さを引いた値
	 */
	public static int compareCards(Card card1, Card card2) {
		return card1.cardPower() - card2.cardPower();
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + number;
		result = prime * result + symbol;
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Card other = (Card) obj;
		if (number != other.number)
			return false;
		if (symbol != other.symbol)
			return false;
		return true;
	}

	@Override
	public void printStatus() {
		System.out.println(cardInfo());
	}

	@Override
	public void printName() {
		System.out.println("トランプのカード");
	}

	@Override
	public void printClass() {
		System.out.println("Card");
	}
}
