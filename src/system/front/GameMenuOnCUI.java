package system.front;

import system.core.GameSelector;
import system.core.GameTable;

/**
 * CUI上でのゲームメニューの実装。
 * @author Takashi Sakakihara
 *
 */
public class GameMenuOnCUI {

	public static void main(String[] args) {
		ScannerForMultiThreadOnCUI scan = new ScannerForMultiThreadOnCUI();
		System.out.println("---------------------------------");
		System.out.println("トランプゲームしようぜ！");
		System.out.println("---------------------------------");

		while(true) {
			GameTable table = GameSelector.selectTableOnCUI(scan);
			if(table == null) break;
			table.settingTable();
			table.start();
			try {
				table.join();
			} catch (InterruptedException e) {
				// TODO 自動生成された catch ブロック
				e.printStackTrace();
			}
		}

		System.out.println("---------------------------------");
		System.out.println("終了します。");
		System.out.println("---------------------------------");
	}

}
