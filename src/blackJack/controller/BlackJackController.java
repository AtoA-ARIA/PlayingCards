package blackJack.controller;

import blackJack.CommonInformation;

/**
 * ブラックジャックのプレイヤーの行動選択処理を行う抽象クラス。
 * スレッドを継承しているため、これを継承したクラスは基本的にrun()をオーバーライドする。
 * @author ディリアス
 *
 */
public abstract class BlackJackController extends Thread {
	/**
	 * ゲームの進行度。
	 */
	protected Integer stage;
	/**
	 * ゲームの公開情報。
	 */
	protected CommonInformation info;
	/**
	 * このコントローラーが生きているかどうか。
	 * これがfalseになったときrun()が終了するように作る。
	 */
	protected boolean isAlive = true;
	/**
	 * プレイヤー番号。
	 */
	protected final int playerNumber;

	/**
	 * コンストラクタ。
	 * @param playerNumber プレイヤー番号。
	 */
	public BlackJackController(int playerNumber) {
		this.playerNumber = playerNumber;
		stage = -1;
	}

	/**
	 * ゲーム進行度の更新。
	 * @param stage ゲーム進行度。
	 */
	public void setStage(int stage) {
		this.stage = stage;
	}
	/**
	 * ゲームの公開情報の更新。
	 * @param info ゲームの公開情報。
	 */
	public void setInfo(CommonInformation info) {
		this.info = info;
	}
	/**
	 * ゲーム開始時の準備処理。
	 * @param info 公開情報。
	 */
	public synchronized void prepare(CommonInformation info) {
		setStage(0);
		setInfo(info);
	}
	/**
	 * コントローラーの処理の終了。
	 * isAliveをfalseにするだけなので、必ずrun()はisAliveがfalseのときに終了するようにする。
	 */
	public final void stopController() {
		System.out.println(this.getPlayerName() + "はいなくなりました。");
		isAlive = false;
	}

	/**
	 * プレイヤー名の取得。
	 * @return プレイヤー名。
	 */
	public String getPlayerName() {
		return info.getPlayerNamesCopy().get(playerNumber);
	}

	/**
	 * メッセージ取得を行う抽象メソッド。
	 * stageに応じてBlackJackTableで処理できるメッセージを返す。
	 * @return メッセージ。
	 */
	public abstract String getMessage();
	/**
	 * 複数手札があり得る場合のメッセージ取得を行う抽象メソッド。
	 * stageに応じてBlackJackTableで処理できるメッセージを返す。
	 * @param handNumber 手札の番号
	 * @return メッセージ。
	 */
	public abstract String getMessageSplited(int handNumber);
}
