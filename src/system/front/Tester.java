package system.front;

import system.core.GameSelector;

public class Tester {

	public static void main(String[] args) {
		GameSelector.selectTableOnCUI(new ScannerForMultiThreadOnCUI());
	}

}
