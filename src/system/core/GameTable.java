package system.core;

import java.util.Scanner;

/**
 * ゲームを管理する抽象クラス。すべてのゲームはこのクラスを継承する。
 * @author Takashi Sakakihara
 *
 */
public abstract class GameTable implements VisualizerOnCUI, Runnable {
	/**
	 * CUIでゲームをするときに使用する標準入力のスキャナ。
	 */
	protected Scanner scanCUI;
	/**
	 * ゲームで使用する山札。
	 */
	protected CardDeck deck;
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
	public GameTable(Scanner scan) {
		this.scanCUI = scan;
	}

	/**
	 * ゲームの初期設定やプレイヤーの配置などを行う。
	 */
	public abstract void settingTable();
	/**
	 * ゲームを開始する。
	 */
	public abstract void start();
	/**
	 * ファイルにゲームをセーブする。
	 */
	public abstract void saveGame();
	/**
	 * スキャナからゲームのデータをロードする。
	 * このスキャナはファイルからの入力で初期化されていることを想定している。
	 * @param scan
	 */
	public abstract void loadGame(Scanner scan);

	/**
	 * ゲームのルールをCUIに表示する。
	 */
	public abstract void printRulesOnCUI();

}
