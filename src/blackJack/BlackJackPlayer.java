package blackJack;

import blackJack.controller.BlackJackController;
import blackJack.controller.ai.BasicAI;
import blackJack.controller.human.HumanControllerCUI;
import system.core.VisualizerOnCUI;
import system.front.ScannerForMultiThreadOnCUI;

/**
 * ブラックジャックのプレイヤーの実体オブジェクト。
 * プレイヤーの行動選択の処理はBlackJackControllerクラスを継承したサブクラスのコントローラーで行う。
 * コントローラーに何を使うかはこのオブジェクトを生成する際に決定する。
 * @author Takashi Sakakihara
 *
 */
public class BlackJackPlayer implements VisualizerOnCUI {
	/**
	 * プレイヤー名。
	 */
	private final String name;
	/**
	 * プレイヤーの番号。
	 */
	private final int playerNumber;
	/**
	 * ゲームの公開情報。
	 */
	private CommonInformation commonInformation;
	/**
	 * ゲームの進行度。
	 */
	private int stage = 0;

	/**
	 * このプレイヤーのコントローラー。
	 */
	private BlackJackController controller;

	/**
	 * コンストラクタ。
	 * PlayerTypeの内容に応じてコントローラーを設定する。
	 * @param name プレイヤーの名前。
	 * @param playerNumber プレイヤー番号。
	 * @param playerType プレイヤーの（コントローラーの）種類。
	 * @param scanCUI CUIを使うコントローラー用のスキャナー。
	 */
	public BlackJackPlayer(String name, int playerNumber, String playerType, ScannerForMultiThreadOnCUI scanCUI) {
		this.name = name;
		this.playerNumber = playerNumber;
		// playerType によるコントローラーの場合分け。新しくAIなどを作った場合は下に追加する。
		switch(playerType) {
		case "ManualPlayer":
			this.controller = new HumanControllerCUI(scanCUI, playerNumber);
			break;
		case "BasicAIPlayer":
			this.controller = new BasicAI(playerNumber);
			break;
		/*
		 * ここにこのように記述して追加する。
		 *
		 * case "新しいAIの名前":
		 * 		this.controller = new 新しいAIのコンストラクタ();
		 * 		break;
		 */
		default:
			break;
		}
		this.controller.start();
	}

	/**
	 * 公開情報の更新。
	 * @param commonInformation 公開情報
	 */
	public void setInformation(CommonInformation commonInformation) {
		this.commonInformation = commonInformation;
		controller.setInfo(this.commonInformation);
	}

	/**
	 * 名前の取得。
	 * @return プレイヤー名。
	 */
	public String getName() {
		return name;
	}

	/**
	 * プレイヤー番号の取得。
	 * @return プレイヤー番号。
	 */
	public int getPlayerNumber() {
		return playerNumber;
	}

	/**
	 * ゲーム開始時の準備処理。
	 * @param commonInformation 公開情報オブジェクト。
	 */
	public void prepare(CommonInformation commonInformation) {
		this.stage = -1;
		setInformation(commonInformation);
		controller.prepare(commonInformation);
	}

	/**
	 * プレイヤーの行動のメッセージを取得する。
	 * BlackJackTableが呼び出す。
	 * 実際の処理はコントローラーが行う。
	 * @return メッセージ。
	 */
	public String getMessage() {
		if(controller != null) {
			return controller.getMessage();
		}
		return "No Controller!";
	}

	/**
	 * プレイヤーの行動のメッセージを取得する。
	 * 手札が複数存在しうるときはこちらを呼び出す。
	 * 実際の処理はコントローラーが行う。
	 * @param handNumber 手札の番号。
	 * @return メッセージ。
	 */
	public String getMessageSplited(int handNumber) {
		if(controller != null) {
			return controller.getMessageSplited(handNumber);
		}
		return "No Controller!";
	}

	/**
	 * ゲームの進行度の更新。
	 * @param stage ゲームの進行度。
	 */
	public void setStage(int stage) {
		this.stage = stage;
		this.controller.setStage(stage);
	}

	/**
	 * コントローラーのスレッドを終了する。
	 */
	public void stop() {
		controller.stopController();
	}

	/**
	 * このプレイヤーの番号及び名前の表示。
	 * VisualizerOnCUIインターフェースの実装。
	 */
	@Override
	public void printStatus() {
		// TODO 自動生成されたメソッド・スタブ
		System.out.println(playerNumber + ":" + name);
		System.out.println("stage : " + stage);
	}

	/**
	 * このプレイヤーの名前の表示。
	 * VisualizerOnCUIインターフェースの実装。
	 */
	@Override
	public void printName() {
		// TODO 自動生成されたメソッド・スタブ
		System.out.println(name);
	}

	/**
	 * このオブジェクトのクラス名の表示。
	 * VisualizerOnCUIインターフェースの実装。
	 */
	@Override
	public void printClass() {
		// TODO 自動生成されたメソッド・スタブ
		System.out.println("BlackJackPlayer");
	}


}
