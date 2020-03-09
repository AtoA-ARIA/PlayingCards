package blackJack.controller;

import blackJack.CommonInformation;

public abstract class BlackJackController extends Thread {
	protected Integer stage;
	protected CommonInformation info;
	protected boolean isAlive = true;
	protected final int playerNumber;

	public BlackJackController(int playerNumber) {
		this.playerNumber = playerNumber;
		stage = -1;
	}

	public void setStage(int stage) {
		this.stage = stage;
	}
	public void setInfo(CommonInformation info) {
		this.info = info;
	}
	public synchronized void prepare(CommonInformation info) {
		setStage(0);
		setInfo(info);
	}
	public void stopController() {
		System.out.println(this.getPlayerName() + "はいなくなりました。");
		isAlive = false;
	}

	public String getPlayerName() {
		return info.getPlayerNamesCopy().get(playerNumber);
	}

	public abstract String getMessage();
	public abstract String getMessageSplited(int handNumber);
}
