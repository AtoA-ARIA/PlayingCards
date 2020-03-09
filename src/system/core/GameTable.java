package system.core;

import system.front.ScannerForMultiThreadOnCUI;

/**
 * ゲームを管理する抽象クラス。すべてのゲームはこのクラスを継承する。
 * @author Takashi Sakakihara
 *
 */
public abstract class GameTable extends Thread implements VisualizerOnCUI {
	/**
	 * CUIでゲームをするときに使用する標準入力のスキャナ。
	 * マルチスレッド用に作ったクラス。
	 */
	protected ScannerForMultiThreadOnCUI scanCUI;
	/**
	 * ゲームで使用する山札。
	 */
	protected CardDeck gameDeck;
	/**
	 * ゲームの参加人数。
	 */
	protected int numberOfPlayers;
	/**
	 * ゲームの最大プレイヤー人数。
	 */
	protected int maxPlayers;
	/**
	 * ゲームの最小プレイヤー人数。
	 */
	protected int minPlayers;

	/**
	 * CUIでゲームをするときのコンストラクタ。
	 * 外からScannerを共有しておく。
	 * @param scan 標準入力のスキャナ。
	 */
	public GameTable(ScannerForMultiThreadOnCUI scanCUI) {
		this.scanCUI = scanCUI;
	}

	/**
	 * ゲームの初期設定やプレイヤーの配置などを行う。
	 */
	public abstract void settingTable();
	/**
	 * ファイルにゲームをセーブする。
	 */
	public abstract void saveGame();
	/**
	 * ゲームのデータをロードする。
	 * @param scan
	 */
	public abstract void loadGame(String fileName);

	/**
	 * ゲームのルールをCUIに表示する。
	 */
	public abstract void printRulesOnCUI();

}
