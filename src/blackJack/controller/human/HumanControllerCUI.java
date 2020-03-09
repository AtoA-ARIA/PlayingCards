package blackJack.controller.human;

import java.util.ArrayList;

import blackJack.BlackJackTable;
import blackJack.controller.BlackJackController;
import system.core.Card;
import system.front.ScannerForMultiThreadOnCUI;

public class HumanControllerCUI extends BlackJackController {

	private final ScannerForMultiThreadOnCUI scanCUI;

	public HumanControllerCUI(ScannerForMultiThreadOnCUI scanCUI, int playerNumber) {
		super(playerNumber);
		this.scanCUI = scanCUI;
	}

	public void run() {

	}

	@Override
	public String getMessage() {
		String ret = "";
		System.out.println(stage);
		while(true) {
			System.out.println("現在のゲーム状況を表示したい場合は \"INFO\" , ゲームのルールを確認したいときは \"RULE\" と入力してください");
			switch(stage) {
			case 1:
				System.out.println("この勝負の賭金を整数で入力してください。0を入力するとこの勝負をパスできます。");
				ret = scanCUI.scanString(this.getPlayerName() + "の賭金");
				break;
			case 3:
				System.out.println("インシュランスするならその額を賭金の半分までの整数で入力してください。0を入力するとインシュランスをしません。");
				ret = scanCUI.scanString(this.getPlayerName() + "のインシュランス");
				break;
			case 8:
				System.out.println("ゲームを終了する場合はENDを、次のゲームに行く場合はCONTINUEを入力してください。");
				ret = scanCUI.scanString(this.getPlayerName() + "の継続意思");
				break;
			default:
				ret = scanCUI.scanString(this.getPlayerName() + "のアクション");
				break;
			}
			if(ret.contentEquals("INFO")) {
				info.printStatus();
				System.out.println(this.stage);
			} else if(ret.contentEquals("RULE")) {
				System.out.println(BlackJackTable.rulesString);
			} else {
				break;
			}
		}
		return ret;
	}

	@Override
	public String getMessageSplited(int handNumber) {
		System.out.println("現在のゲーム状況を表示したい場合は \"INFO\" と入力してください");
		String ret = "";
		while(true) {
			System.out.println("アクションを選択してください。\n"
				+ "ヒットする場合はHIT、スタンドする場合はSTAND、サレンダーする場合はSURRENDER、ダブルダウンする場合はDOUBLEDOWN、スプリットする場合はSPLITを入力します。");
			if(info.getAllPlayersBetsCopy().get(playerNumber) > info.getPlayerChipsCopy().get(playerNumber)) {
				ArrayList<ArrayList<Card>> hand = info.getAllPlayersHandsCopy().get(playerNumber);
				if(hand.size() == 1 && hand.get(0).size() == 2) {
					System.out.println("現在ダブルダウンが可能です。");
					if(BlackJackTable.canSplit(hand.get(0))) {
						System.out.println("現在スプリットが可能です。");
					}
				}
			}
			if(info.getAllPlayersHandsCopy().get(playerNumber).size() > 1) {
				ret = scanCUI.scanString(this.getPlayerName() + "の第" + handNumber + "手札のアクション");
			} else {
				ret = scanCUI.scanString(this.getPlayerName() + "のアクション");
			}
			if(ret.contentEquals("INFO")) {
				info.printStatus();
			} else {
				break;
			}
		}
		return ret;
	}

}
