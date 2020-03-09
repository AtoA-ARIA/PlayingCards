package system.core;

/**
 * CUI（コンソール）環境での情報の出力をさせるインターフェース
 * @author Takashi Sakakihara
 *
 */
public interface VisualizerOnCUI {
	/**
	 * インスタンスの情報を出力する。
	 */
	public void printStatus();
	/**
	 * インスタンスの名前を出力する。
	 */
	public void printName();
	/**
	 * インスタンスのクラス名をString型で出力する。
	 */
	public void printClass();
}
