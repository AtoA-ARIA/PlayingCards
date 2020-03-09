package blackJack.controller.ai;

import java.util.ArrayList;

import blackJack.BlackJackTable;
import blackJack.CommonInformation;
import blackJack.controller.BlackJackController;

public class BasicAI extends BlackJackController {

	private ArrayList<String> message;
	private int lastProcessedStage;

	public BasicAI(int playerNumber) {
		super(playerNumber);
		message = new ArrayList<String>();
		message.add("INITIALIZED");
		lastProcessedStage = 0;
	}

	public void run() {
		while(isAlive) {
			switch(stage) {
			case 1:
				Integer bet = info.minimumBet;
				message.set(0, bet.toString());
				lastProcessedStage = 1;
				break;
			case 2:
				if(lastProcessedStage == stage) {
					try {
						Thread.sleep(250);
					} catch (InterruptedException e) {
						// TODO 自動生成された catch ブロック
						e.printStackTrace();
					}
				} else {
					message.set(0, "WAIT");
					lastProcessedStage = 2;
				}
				break;
			case 3:
				if(lastProcessedStage == stage) {
					try {
						Thread.sleep(250);
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
			case 4:
				if(lastProcessedStage == stage) {
					try {
						Thread.sleep(250);
					} catch (InterruptedException e) {
						// TODO 自動生成された catch ブロック
						e.printStackTrace();
					}
				} else {
					message.set(0, "WAIT");

					lastProcessedStage = 4;
				}
				break;
			case 5:
				if(lastProcessedStage == stage) {
					try {
						Thread.sleep(250);
					} catch (InterruptedException e) {
						// TODO 自動生成された catch ブロック
						e.printStackTrace();
					}
				} else {
					if(BlackJackTable.culculateHandStrength(info.getAllPlayersHandsCopy().get(playerNumber).get(0)) > 16) {
						message.set(0, "STAND");
					} else {
						message.set(0, "HIT");
					}
				}

				break;
			case 6:
			case 7:
				if(lastProcessedStage == stage) {
					try {
						Thread.sleep(250);
					} catch (InterruptedException e) {
						// TODO 自動生成された catch ブロック
						e.printStackTrace();
					}
				} else {
					message.set(0, "WAIT");
					lastProcessedStage = 7;
				}
				break;
			case 8:
				if(lastProcessedStage == stage) {
					try {
						Thread.sleep(250);
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
				Thread.sleep(250);
			} catch (InterruptedException e) {
				// TODO 自動生成された catch ブロック
				e.printStackTrace();
			}
		}
	}

	public void prepare(CommonInformation info) {
		super.prepare(info);
		message.clear();
		message.add("INITIALIZED");
		lastProcessedStage = 0;
	}


	@Override
	public String getMessage() {
		return message.get(0);
	}

	@Override
	public String getMessageSplited(int handNumber) {
		return message.get(handNumber);
	}

}
