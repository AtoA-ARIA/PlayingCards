package system.core;

/**
 * カードに不正な情報が入ったときの例外を扱うクラス。
 * @author Takashi Sakakihara
 *
 */
public class InvalidCardException extends Exception {
	/**
	 * エラーログを入れる。
	 */
	String errorLog = "InvalidCardException";

	/**
	 * エラーログに種類（数字あるいはマークに存在しないものが入った）情報を追加するコンストラクタ
	 * @param errorLog エラーの種類（set number x / set symbol x）
	 */
	public InvalidCardException(String errorLog) {
		this.errorLog += (" : " + errorLog);
	}

	/**
	 * toStringのオーバーライド。エラーログをそのまま出す。
	 */
	public String toString() {
		return errorLog;
	}
}
