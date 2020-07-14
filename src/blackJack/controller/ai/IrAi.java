package blackJack.controller.ai;

import java.util.ArrayList;

import blackJack.BlackJackTable;
import blackJack.CommonInformation;
import blackJack.controller.BlackJackController;
import system.core.Card;

public class IrAi extends BlackJackController {
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
	public IrAi(int playerNumber) {
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
				Integer bet = info.minimumBet;
				message.set(0, bet.toString());
				lastProcessedStage = 1;
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
					int insurance = info.getAllPlayersBetsCopy().get(playerNumber) / 2;
					message.set(0, Integer.toString(insurance));
					lastProcessedStage = 3;
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
			case 5: // プレイヤーの行動処理。
				if(lastProcessedStage == stage) {
					try {
						Thread.sleep(waitingTime);
					} catch (InterruptedException e) {
						// TODO 自動生成された catch ブロック
						e.printStackTrace();
					}
				} else {
					playHand();
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
					message.set(0, "CONTINUE");
					lastProcessedStage = 8;
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

	private void playHand() {
		//ディーラーのオープンカード
		int dealersOpenCard = BlackJackTable.culculateHandStrength(info.getDealersOpenCardsCopy());
		//自身の手札を保持。
		ArrayList<ArrayList<Card>> myHand = info.getAllPlayersHandsCopy().get(playerNumber);
		//スプリットしたかどうかを判別。スプリットしていればtrue。してなければfalse。
		boolean checkSplit = false;
		//スプリットしていれば、checkSplitをtrueにする。
		if(myHand.size() == 2) checkSplit = true;

		//サレンダー、ダブルダウン、スプリットなどの処理。
		for(int i = 0 ; i < myHand.size() ; i++) {
			//手札に　A　があるかどうか判別。あればtrue。無ければfalse。
			boolean containsAce = false;
			//ヒットしたかどうかを判別。ヒットすればtrue。してなければfalse。
			boolean hitCount = false;
			//カードの数字を保持。
			int myCardNum = myHand.get(i).get(0).getNumber();
			//手札の強さを保持。
			int playersHandsStrength = BlackJackTable.culculateHandStrength(myHand.get(i));
			//ヒットしていれば、hitCountをtrueにする。
			if(myHand.get(i).size() != 2)hitCount = true;
			//自身の手札の中に　A があればcontainsAceをtrueにする。
			for(int j = 0 ; j < myHand.get(i).size() ; j++) {
				if(myHand.get(i).get(j).getNumber() == 1) {
					containsAce = true;
				}
			}
			// サレンダー、ダブルダウン、スプリットができない場合
			if(hitCount || checkSplit) {
				if(playersHandsStrength > 11 && dealersOpenCard < 7) {
					message.set(i, "STAND");
				} else if(playersHandsStrength > 16) {
					message.set(i, "STAND");
				} else{
					message.set(i, "HIT");
				}
			}else {
				//サレンダー、ダブルダウン、スプリットの処理

				//サレンダー、ダブルダウン、スプリットしたかどうかの情報を保持
				//いずれかをプレイすればfalseにする
				boolean playCount = true;
				//エースがあるときの処理
				if(containsAce) {
					//エースがある場合のスプリットの処理
					if(BlackJackTable.canSplit(myHand.get(i))) {
						message.set(i, "SPLIT");
						if(myHand.size() < 2)
							message.add("wait");
						playCount = false;
					}
					//エースがある場合のダブルダウンの処理
					if(playersHandsStrength > 12 && playersHandsStrength < 19
						&& dealersOpenCard > 3 && dealersOpenCard < 7) {
						message.set(i, "DOUBLEDOWN");
						playCount = false;
					}
				} else {
					//エースがない場合のサレンダーの処理
					if(
						(playersHandsStrength == 15 && dealersOpenCard == 10)
						|| (playersHandsStrength == 16 && dealersOpenCard == 9)
						|| (playersHandsStrength == 16 && dealersOpenCard == 10)
						|| (playersHandsStrength == 16 && dealersOpenCard == 1)
						) {
						message.set(i, "SURRENDER");
						playCount = false;
					}
					//エースがない場合のスプリットの処理
					if(BlackJackTable.canSplit(myHand.get(i))) {
						if((myCardNum < 10)||(myCardNum != 6)||(myCardNum != 5)||(myCardNum != 4)){
							message.set(i, "SPLIT");
							if(myHand.size() < 2)
								message.add("wait");
							playCount = false;
						}
					}
					//エースがない場合のダブルダウンの処理
					if(
						(playersHandsStrength == 9 && 2 < dealersOpenCard && dealersOpenCard < 7)
						||(playersHandsStrength == 10 && 1 < dealersOpenCard && dealersOpenCard < 10)
						||(playersHandsStrength == 11 && 1 < dealersOpenCard && dealersOpenCard < 11)
						) {
						message.set(i, "DOUBLEDOWN");
						playCount = false;
					}
				}
				//上記のどれもプレイできない場合の処理
				if(playCount) {
					if(playersHandsStrength > 11 && dealersOpenCard < 7) {
						message.set(i, "STAND");
					} else if(playersHandsStrength > 16) {
						message.set(i, "STAND");
					} else{
						message.set(i, "HIT");
					}
				}
			}
		}

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
		message.set(0,"WAIT");
		return ret;
	}

	@Override
	public String getMessageSplited(int handNumber) {
		String ret = message.get(handNumber);
		message.set(handNumber, "WAIT");
		return ret;
	}
}
