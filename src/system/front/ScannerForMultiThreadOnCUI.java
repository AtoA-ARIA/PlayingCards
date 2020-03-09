package system.front;

import java.util.InputMismatchException;
import java.util.Scanner;

/**
 * マルチスレッドで一つの標準入力を使い回すためのクラス
 * @author Takashi Sakakihara
 *
 */
public class ScannerForMultiThreadOnCUI {
	/**
	 * 標準入力のスキャナー
	 */
	Scanner scan;

	/**
	 * コンストラクタ。スキャナを標準入力で初期化。
	 */
	public ScannerForMultiThreadOnCUI() {
		this.scan = new Scanner(System.in);
	}

	/**
	 * synchronized な Scanner の next()
	 * @return 標準入力の文字列
	 */
	public synchronized String next() {
		return scan.next();
	}
	/**
	 * synchronized な Scanner の next()
	 * @return 標準入力の文字列
	 */
	public synchronized String nextLine() {
		return scan.nextLine();
	}
	/**
	 * synchronized な Scanner の nextInt()
	 * @return
	 * @throws InputMismatchException
	 */
	public synchronized int nextInt() throws InputMismatchException {
		return scan.nextInt();
	}
	/**
	 * synchronized な Scanner の next()
	 * @return 標準入力の文字列
	 */
	public synchronized double nextDouble() throws InputMismatchException {
		return scan.nextDouble();
	}

	/**
	 * 項目名 inputValueName を minValue から maxValue までで標準入力する
	 * @param inputValueName 入力する項目名
	 * @param minValue 受け取る最小値
	 * @param maxValue 受け取る最大値
	 * @return 標準入力された値
	 */
	public synchronized int scanInt(String inputValueName, int minValue, int maxValue) {
		int inputValue;
		while(true) {
			System.out.print(inputValueName + " :");
			try {
				inputValue = nextInt();
				if(minValue <= inputValue && inputValue <= maxValue) {
					break;
				} else {
					System.out.println(minValue + "～" + maxValue + "で入力してください。");
				}
			} catch(InputMismatchException e) {
				System.out.println("半角数字で入力してください。");
				scan.next();
			}
			System.out.println();
		}
		return inputValue;
	}

	public synchronized String scanString(String inputValueName) {
		String input;
		System.out.print(inputValueName + " :");
		input = next();
		System.out.println();
		return input;
	}

}
