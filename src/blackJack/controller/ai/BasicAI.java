package blackJack.controller.ai;

import java.util.ArrayList;

import blackJack.BlackJackTable;
import blackJack.CommonInformation;
import blackJack.controller.BlackJackController;

/**
 * 最も単純な思考のAIによるコントローラー。
 * @author Takashi Sakakihara
 *
 */
public class BasicAI extends BlackJackController {

	/**
	 * メッセージのリスト。
	 * 実際にBlackJackTableに送られるメッセージはこれ。
	 */
	private ArrayList<String> message;
	/**
	 * 最後に処理したゲームの進行度。
	 */
	private int lastProcessedStage;

	private final int waitingTime = 50;

	/**
	 * コンストラクタ。
	 * @param playerNumber プレイヤー番号。
	 */
	public BasicAI(int playerNumber) {
		super(playerNumber);
		message = new ArrayList<String>();
		message.add("INITIALIZED");
		lastProcessedStage = 0;
	}

	/**
	 * このAIの思考処理。
	 * Thread の run() のオーバーライド。
	 */
	public void run() {
		// isAlive が True のときは無限ループ。
		while(isAlive) {
			// ゲーム進行度による場合分け。
			switch(stage) {
			case 1: // 賭金のメッセージの設定。常時最低賭金。
				bet();
				break;
			case 2: // とくになにもしない。待つ。
				if(lastProcessedStage == stage) {
					try {
						Thread.sleep(waitingTime);
					} catch (InterruptedException e) {
						// TODO 自動生成された catch ブロック
						e.printStackTrace();
					}
				} else {
					message.set(0, "WAIT");
					lastProcessedStage = 2;
				}
				break;
			case 3: // インシュランスのメッセージの設定。常に賭金の半分のインシュランス。
				if(lastProcessedStage == stage) {
					try {
						Thread.sleep(waitingTime);
					} catch (InterruptedException e) {
						// TODO 自動生成された catch ブロック
						e.printStackTrace();
					}
				} else {
					insure();
				}
				break;
			case 4: // とくになにもしない。待つ。
				if(lastProcessedStage == stage) {
					try {
						Thread.sleep(waitingTime);
					} catch (InterruptedException e) {
						// TODO 自動生成された catch ブロック
						e.printStackTrace();
					}
				} else {
					message.set(0, "WAIT");

					lastProcessedStage = 4;
				}
				break;
			case 5: // プレイヤーの行動処理。ディーラーと同じく16を超えるまでヒットし、超えたらスタンドするだけ。
				if(lastProcessedStage == stage) {
					try {
						Thread.sleep(waitingTime);
					} catch (InterruptedException e) {
						// TODO 自動生成された catch ブロック
						e.printStackTrace();
					}
				} else {
					takePlayerAction();
				}

				break;
			case 6: // 6,7もなにもしない。待つ。
			case 7:
				if(lastProcessedStage == stage) {
					try {
						Thread.sleep(waitingTime);
					} catch (InterruptedException e) {
						// TODO 自動生成された catch ブロック
						e.printStackTrace();
					}
				} else {
					message.set(0, "WAIT");
					lastProcessedStage = 7;
				}
				break;
			case 8: // 次のゲームの参加意思表示のメッセージの設定。常に続行。
				if(lastProcessedStage == stage) {
					try {
						Thread.sleep(waitingTime);
					} catch (InterruptedException e) {
						// TODO 自動生成された catch ブロック
						e.printStackTrace();
					}
				} else {
					chooseToContinue();
				}
				break;
			default:
			}
			try {
				Thread.sleep(waitingTime);
			} catch (InterruptedException e) {
				// TODO 自動生成された catch ブロック
				e.printStackTrace();
			}
		}
	}

	/**
	 * 賭金を決める。
	 */
	private void bet() {
		Integer bet = info.minimumBet;
		message.set(0, bet.toString());
		lastProcessedStage = 1;
	}

	/**
	 * インシュランスを決める。
	 */
	private void insure() {
		int insurance = info.getAllPlayersBetsCopy().get(playerNumber) / 2;
		message.set(0, Integer.toString(insurance));
		lastProcessedStage = 3;
	}

	/**
	 * プレイヤーのアクションを決める。
	 */
	private void takePlayerAction() {
		if(BlackJackTable.culculateHandStrength(info.getAllPlayersHandsCopy().get(playerNumber).get(0)) > 16) {
			message.set(0, "STAND");
		} else {
			message.set(0, "HIT");
		}
	}

	/**
	 * 次のゲームをするかどうかを決める。
	 */
	private void chooseToContinue() {
		message.set(0, "CONTINUE");
		lastProcessedStage = 8;
	}


	/**
	 * ゲームの開始の準備処理。
	 * BlackJackControllerのオーバーライドメソッド。
	 * 前のゲームのメッセージの削除および初期化済みメッセージの設定を行う。
	 */
	@Override
	public void prepare(CommonInformation info) {
		super.prepare(info);
		message.clear();
		message.add("INITIALIZED");
		lastProcessedStage = 0;
	}

	@Override
	public String getMessage() {
		String ret = message.get(0);
		message.set(0, "WAIT");
		return ret;
	}

	@Override
	public String getMessageSplited(int handNumber) {
		String ret = message.get(handNumber);
		message.set(handNumber, "WAIT");
		return ret;
	}

}
