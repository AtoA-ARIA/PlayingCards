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
					//ディーラーのオープンカード
					int dealersOpenCard = BlackJackTable.culculateHandStrength(info.getDealersOpenCardsCopy());
					//自身の手札を保持。
					ArrayList<ArrayList<Card>> myHand = info.getAllPlayersHandsCopy().get(playerNumber);
					//手札に　A　があるかどうか判別。あればtrue。無ければfalse。
					boolean containsAce = false;
					//自身の手札の中に　A があればcontainsAceをtrueにする。
					for(int i = 0 ; i < myHand.get(0).size() ; i++) {
						if(myHand.get(0).get(i).getNumber() == 1) {
							containsAce = true;
						}
					}
					//手札の強さを保持。
					int playersHandsStrength = BlackJackTable.culculateHandStrength(myHand.get(0));
					//ヒットしたかどうかを判別。ヒットすればtrue。してなければfalse。
					boolean hitCount = false;
					//ヒットしていれば、hitCountをtrueにする。
					if(myHand.get(0).size() != 2)hitCount = true;
					//サレンダー、ダブルダウン、スプリットなどの処理。
					if(!hitCount && !containsAce && playersHandsStrength == 15 && dealersOpenCard == 10) {
						message.set(0, "SURRENDER");
					} else if(!hitCount && !containsAce && playersHandsStrength == 16 && dealersOpenCard == 9) {
						message.set(0, "SURRENDER");
					} else if(!hitCount && !containsAce && playersHandsStrength == 16 && dealersOpenCard == 10) {
						message.set(0, "SURRENDER");
					} else if(!hitCount && !containsAce && playersHandsStrength == 16 && dealersOpenCard == 1) {
						message.set(0, "SURRENDER");
//					} else if(BlackJackTable.canSplit(info.getAllPlayersHandsCopy().get(playerNumber).get(0)) && containsAce) {
//						message.set(0, "SPLIT");
//					} else if(BlackJackTable.canSplit(info.getAllPlayersHandsCopy().get(playerNumber).get(0)) && info.getAllPlayersHandsCopy().get(playerNumber).get(0).get(0).getNumber() == 8) {
//						message.set(0, "SPLIT");
					} else if(!hitCount && playersHandsStrength == 9 && 2 < dealersOpenCard && dealersOpenCard < 7) {
						message.set(0, "DOUBLEDOWN");
					} else if(!hitCount && playersHandsStrength == 10 && 1 < dealersOpenCard && dealersOpenCard < 10) {
						message.set(0, "DOUBLEDOWN");
					} else if(!hitCount && playersHandsStrength == 11 && 1 < dealersOpenCard && dealersOpenCard < 11) {
						message.set(0, "DOUBLEDOWN");
					} else if(!hitCount && containsAce && 12 < playersHandsStrength && playersHandsStrength < 19 && 3 < dealersOpenCard && dealersOpenCard < 7) {
						message.set(0, "DOUBLEDOWN");
					} else if(playersHandsStrength > 11 && dealersOpenCard < 7) {
						message.set(0, "STAND");
					} else if(playersHandsStrength > 16) {
						message.set(0, "STAND");
					} else{
						message.set(0, "HIT");
					}
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
