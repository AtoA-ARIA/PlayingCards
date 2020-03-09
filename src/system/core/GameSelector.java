package system.core;

import java.util.InputMismatchException;

import blackJack.BlackJackTable;
import system.front.ScannerForMultiThreadOnCUI;

/**
 * ゲームを選ぶクラス。
 * 新しいゲームを追加するたびにこれを書き換えることで実行可能にする。
 * @author Takashi Sakakihara
 *
 */
public class GameSelector {
	/**
	 * ゲーム選択の選択肢をCUI上で提示するメソッド。
	 * 新しいゲームを追加する際は下に println 文を新しい番号で追加する。
	 */
	public static void selectMessageOnCUI() {
		System.out.println("-------------------------");
		System.out.println("|   ゲームを選択します  |");
		System.out.println("-------------------------");
		// ============================================================
		// 新しいゲーム追加時はここから下に書き足す。
		// ============================================================
		System.out.println("1 : ブラックジャック");
		// ============================================================
		// 書き足す部分ここまで。
		// ============================================================
		System.out.println("0 : 終了");

	}


	/**
	 * CUI上でゲームの種類を選んで選んだテーブルオブジェクトのインスタンスを返す。
	 * ゲームを追加するときは switch 文中の case を追加し、そこでそのゲームの GameTable のサブクラスのインスタンスを return する。
	 * @param scan main メソッドで作った System.in で初期化した Scanner のインスタンス。
	 * @return 選ばれたゲームの GameTable のインスタンス。
	 */
	public static GameTable selectTableOnCUI(ScannerForMultiThreadOnCUI scan) {
		int gameNumber = 0;
		while(true) {
			selectMessageOnCUI();
			System.out.print("ゲーム番号 :");
			try {
				gameNumber = scan.nextInt();
				if(isCorrectTableNumber(gameNumber)) {
					break;
				} else {
					System.out.println("メニューから選んでください。");
				}
			} catch(InputMismatchException e) {
				System.out.println("半角数字で入力してください。");
				scan.next();
			}
			System.out.println();
		}

		switch(gameNumber) {
		// ============================================================
		// ゲームの追加時はここに書き足す。数字が飛ばないように注意！
		// ============================================================
		case 1:
			return new BlackJackTable(scan);
		// ============================================================
		// 書き足す部分ここまで
		// ============================================================
		default:
			return null;
		}
	}


	/**
	 * ゲームの種類が存在しているか判別する。
	 * ゲームを追加するときは if 文中の条件式を必ず書き換える。
	 * @param number ゲーム番号
	 * @return 存在するなら true しないなら false を返す。
	 */
	public static boolean isCorrectTableNumber(int number) {
		// ============================================================
		// ゲームの追加時は下のif文の条件式を書き換える。
		// ============================================================
		if(0 <= number && number <= 1) {
			return true;
		}
		return false;
	}
}
