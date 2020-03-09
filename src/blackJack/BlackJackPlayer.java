package blackJack;

import blackJack.controller.BlackJackController;
import blackJack.controller.ai.BasicAI;
import blackJack.controller.human.HumanControllerCUI;
import system.core.VisualizerOnCUI;
import system.front.ScannerForMultiThreadOnCUI;

public class BlackJackPlayer implements VisualizerOnCUI {
	private final String name;
	private final int playerNumber;
	private CommonInformation commonInformation;
	private int stage = 0;

	private BlackJackController controller;

	public BlackJackPlayer(String name, int playerNumber, String playerType, ScannerForMultiThreadOnCUI scanCUI) {
		this.name = name;
		this.playerNumber = playerNumber;
		switch(playerType) {
		case "ManualPlayer":
			this.controller = new HumanControllerCUI(scanCUI, playerNumber);
			break;
		case "BasicAIPlayer":
			this.controller = new BasicAI(playerNumber);
			break;
		default:
			break;
		}
		this.controller.start();
	}

	public void setInformation(CommonInformation commonInformation) {
		this.commonInformation = commonInformation;
		controller.setInfo(this.commonInformation);
	}

	public String getName() {
		return name;
	}

	public int getPlayerNumber() {
		return playerNumber;
	}

	public void prepare(CommonInformation commonInformation) {
		this.stage = -1;
		setInformation(commonInformation);
		controller.prepare(commonInformation);
	}

	public String getMessage() {
		if(controller != null) {
			return controller.getMessage();
		}
		return "No Controller!";
	}

	public String getMessageSplited(int handNumber) {
		if(controller != null) {
			return controller.getMessageSplited(handNumber);
		}
		return "No Controller!";
	}

	public void setStage(int stage) {
		this.stage = stage;
		this.controller.setStage(stage);
	}

	public void stop() {
		controller.stopController();
	}

	@Override
	public void printStatus() {
		// TODO 自動生成されたメソッド・スタブ
		System.out.println(playerNumber + ":" + name);
		System.out.println("stage : " + stage);
	}

	@Override
	public void printName() {
		// TODO 自動生成されたメソッド・スタブ
		System.out.println(name);
	}

	@Override
	public void printClass() {
		// TODO 自動生成されたメソッド・スタブ
		System.out.println("BlackJackPlayer");
	}


}
