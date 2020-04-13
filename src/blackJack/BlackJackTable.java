package blackJack;

import java.util.ArrayList;

import system.core.Card;
import system.core.CardDeck;
import system.core.GameTable;
import system.front.ScannerForMultiThreadOnCUI;

/**
 * ブラックジャックのゲームを司るクラス。
 * ゲームの情報すべてを保持し、プレイヤーの行動の実際の処理の部分も行う。
 * @author Takashi Sakakihara
 *
 */
public class BlackJackTable extends GameTable {
	/**
	 * 入出力のモード。現状コンソールの標準入出力のみなので、特に使用せず。
	 */
	private int ioMode;
	/**
	 * ゲーム番号。賭けが終わって次のゲームが始まるタイミングで1増える。
	 */
	private int gameNumber;
	/**
	 * デッキ数。トランプのデッキをいくつ使ってゲームをするかを表す。
	 */
	private int numberOfDecks;
	/**
	 * 初期チップ。最初にプレイヤーがどれだけのチップを持っているかを表す。
	 */
	private int initialPlayerChips;
	/**
	 * 最小賭金。すべてのプレイヤーはこの値以上の賭金で勝負しなければならない。
	 */
	private int minimumBet;
	/**
	 * ゲームの進行度。ゲームの処理がどの段階かを表す。
	 * 1 : プレイヤーの賭金の決定処理。
	 * 2 : 全員にカードを配る処理。
	 * 3 : インシュランスの決定処理。ディーラーのオープンカードがAではないときはスキップされる。
	 * 4 : ディーラーのブラックジャックの確認処理。ディーラーのオープンカードがAまたは10点のカードではないときスキップされる。
	 * 5 : プレイヤーの行動処理。
	 * 6 : ディーラーの行動処理。
	 * 7 : 勝敗処理。
	 * 8 : 次のゲームの確認処理。
	 */
	private int stage;
	/**
	 * 次のゲームがあるかどうか。
	 */
	private boolean hasNextGame;
	/**
	 * プレイヤーに共有するゲームの基本情報オブジェクト。
	 */
	private CommonInformation commonInformation;

	/**
	 * プレイヤーの実体。
	 */
	private ArrayList<BlackJackPlayer> players;
	/**
	 * プレイヤーの名前。
	 */
	private ArrayList<String> playerNames;
	/**
	 * 全プレイヤーの現在のチップ。
	 */
	private ArrayList<Integer> allPlayersChips;
	/**
	 * ディーラーの公開カード。ディーラーが引く
	 */
	private ArrayList<Card> dealersOpenCards;
	/**
	 * ディーラーの伏せカード。
	 */
	private Card dealersHiddenCard;
	/**
	 * 全プレイヤーの全手札。
	 * 一番内側の ArrayList は手札1つを表す。ディーラーの手と勝負する単位。
	 * 手札1つの ArrayList はあるプレイヤーの持つそれぞれの手札を表す。スプリットをしたときのみ2つ以上の要素を持つ。
	 * つまりこれはすべてのプレイヤーそれぞれの、全ての手札となる。
	 */
	private ArrayList<ArrayList<ArrayList<Card>>> allPlayersHands;
	/**
	 * 全てのプレイヤーの現在のゲームでの賭金。
	 */
	private ArrayList<Integer> allPlayersBets;
	/**
	 * 全てのプレイヤーの現在のゲームでのインシュランス。
	 */
	private ArrayList<Integer> allPlayersInsurance;
	/**
	 * プレイヤーの手札の状態。
	 * スプリットで手札が増えた場合、それぞれの手札がステータスを持つ。
	 */
	private ArrayList<ArrayList<String>> allPlayersStatus;

	/**
	 * 行動決定の待ち時間。（ミリ秒）
	 * プレイヤーから間違ったメッセージを受け取ったときなどに次のメッセージ受け取りまでシステムが待つ時間。
	 */
	public static final int waitingTime = 100;
	/**
	 * 間違ったメッセージを受け付ける回数。
	 * この回数間違ったメッセージをプレイヤーから受け取ると、そのプレイヤーは強制的にパスになる。
	 */
	public static final int numberOfTimesToAcceptWrongMessage = 5;

	/**
	 * 最大ゲーム数。
	 * この回数ゲームが行われると強制的に終了する。
	 */
	private int maxGameNumber = Integer.MAX_VALUE;
	/**
	 * コンストラクタ。マルチスレッド用スキャナーを受け取る。
	 * @param scanCUI スキャナー
	 */
	public BlackJackTable(ScannerForMultiThreadOnCUI scanCUI) {
		super(scanCUI);
		ioMode = 0;
		if(scanCUI == null) {
			ioMode = 1;
		}
		this.gameNumber = 0;
		super.minPlayers = 1;
		super.maxPlayers = 7;
		this.hasNextGame = true;
		this.players = new ArrayList<BlackJackPlayer>();
		this.playerNames = new ArrayList<String>();
		this.allPlayersHands = new ArrayList<ArrayList<ArrayList<Card>>>();
		this.allPlayersChips = new ArrayList<Integer>();
		this.allPlayersBets = new ArrayList<Integer>();
		this.allPlayersInsurance = new ArrayList<Integer>();
		this.allPlayersStatus = new ArrayList<ArrayList<String>>();
		this.dealersOpenCards = new ArrayList<Card>();
	}

	@Override
	public void printStatus() {
		// TODO 自動生成されたメソッド・スタブ
		System.out.println("Game " + this.gameNumber);

	}

	@Override
	public void printName() {
		// TODO 自動生成されたメソッド・スタブ
		System.out.println("ブラックジャック");
	}

	@Override
	public void printClass() {
		// TODO 自動生成されたメソッド・スタブ
		System.out.println("BlackJackTable");
	}

	@Override
	public void settingTable() {
		// データのロード機能をあとでつける
		if(this.ioMode == 0) {
			setGameRulesOnCUI();
			this.gameDeck = new CardDeck(this.numberOfDecks);
			setPlayersOnCUI();
		}
	}

	/**
	 * ゲームの基本設定をCUIで行う。
	 * ここではゲームで使用するトランプデッキ数、プレイヤーの初期チップ量、ゲームの最低賭金
	 */
	private void setGameRulesOnCUI() {
		System.out.println("ブラックジャックに使うトランプのデッキ数を1～8で入力してください。");
		this.numberOfDecks = scanCUI.scanInt("デッキ数", 1, 8);
		System.out.println("プレイヤーの初期チップの量を100～10000で入力してください。");
		this.initialPlayerChips = scanCUI.scanInt("初期チップ", 100, 10000);
		System.out.println("このテーブルの最低賭金を1～100で入力してください。");
		this.minimumBet = scanCUI.scanInt("最低賭金", 1, 100);
		System.out.println("何回までゲームを行うかを" + 1 + "～" + Integer.MAX_VALUE + "で入力してください。");
		this.maxGameNumber = scanCUI.scanInt("最大ゲーム数", 1, Integer.MAX_VALUE);
	}

	/**
	 * プレイヤーの初期設定をCUIで行う。
	 * まず参加人数を決定し、その後参加人数分のプレイヤー初期設定をする。
	 */
	private void setPlayersOnCUI() {
		System.out.println("ブラックジャックの参加人数は何人ですか？1～7人で入力してください。");
		this.numberOfPlayers = scanCUI.scanInt("参加人数", 1, 7);
		for(int i = 0; i < this.numberOfPlayers; i++) {
			System.out.println("プレイヤー" + i + "を追加します。");
			addPlayerOnCUI(i);
			allPlayersChips.add(initialPlayerChips);
			allPlayersBets.add(0);
			allPlayersStatus.add(new ArrayList<String>());
			allPlayersHands.add(new ArrayList<ArrayList<Card>>());
		}
	}

	/**
	 * プレイヤーのオブジェクトをゲームに追加する。
	 * ここの入力で決定したPlayerTypeによって、生成されるプレイヤーの意思決定処理がCUIの入力になったり、AIになったりする。
	 * @param playerNumber プレイヤーの通し番号
	 */
	private void addPlayerOnCUI(int playerNumber) {
		String playerName = "";
		while(playerName != null && playerName.contentEquals("")) {
			playerName = scanCUI.scanString("プレイヤー" + playerNumber + "の名前");
		}
		// !!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
		// 新しいAIなどのコントローラーを追加した場合、ここから下を変更してください。
		// !!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
		System.out.println("このプレイヤーの種類を入力してください。\n"
				+ "0 : 人間、1 : AI、2 : IRAI");
		int playerTypeInt = scanCUI.scanInt("プレイヤーの種類", 0, 2);
		String playerType = "";
		switch(playerTypeInt) {
		case 0:
			playerType = "ManualPlayer";
			break;
		case 1:
			playerType = "BasicAIPlayer";
			break;
		case 2:
			playerType = "IrAi";
			break;
			// !!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
			// 新しいAI追加時、ここに上記のような記述を追加する。
			// playerTypeにはBlackJackPlayerの方で設定した文字列を入れる。
			// !!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!

		default:
			break;
		}
		playerNames.add(playerName);
		BlackJackPlayer player = new BlackJackPlayer(playerName, playerNumber, playerType, scanCUI);
		this.players.add(player);
		System.out.println();
	}

	/**
	 * ゲームの初期化。毎ゲーム呼び出す。
	 */
	private void gameInitialize() {
		gameNumber++;
		stage = 0;
		dealersOpenCards.clear();
		for(ArrayList<ArrayList<Card>> playersHands : allPlayersHands) {
			playersHands.clear();
			playersHands.add(new ArrayList<Card>());
		}
		if(gameDeck.getRemainingRate() < 0.4) {
			gameDeck = new CardDeck(numberOfDecks);
		}
		for(BlackJackPlayer player : players) {
			allPlayersBets.set(player.getPlayerNumber(), 0);
		}
		allPlayersInsurance.clear();
		for(int playerNumber = 0; playerNumber < numberOfPlayers; playerNumber++) {
			allPlayersInsurance.add(0);
		}
		for(ArrayList<String> playersStatus : allPlayersStatus) {
			playersStatus.clear();
			playersStatus.add("INITIALIZED");
		}

		this.commonInformation = new CommonInformation(numberOfPlayers,
				gameNumber,
				minimumBet,
				gameDeck,
				playerNames,
				allPlayersChips,
				allPlayersHands,
				allPlayersBets,
				allPlayersInsurance,
				allPlayersStatus,
				dealersOpenCards);
		for(BlackJackPlayer player : players) {
			player.prepare(commonInformation);
		}
	}

	@Override
	public void saveGame() {
		// TODO 自動生成されたメソッド・スタブ
		// 未実装
	}

	@Override
	public void loadGame(String fileName) {
		// TODO 自動生成されたメソッド・スタブ
		// 未実装
	}

	@Override
	public synchronized void printRulesOnCUI() {
		// TODO 自動生成されたメソッド・スタブ
		System.out.println(rulesString);
	}

	/**
	 * Thread クラスの run のオーバーライド。
	 * 次のゲームがある限りゲームの処理を続ける。
	 */
	@Override
	public void run() {
		// TODO 自動生成されたメソッド・スタブ
		while(hasNextGame) {
			gameInitialize();
			// ゲームの進行処理
			while(stage < 8) {
				advanceStage();
				callGameProgressMethod();
			}
		}
		for(BlackJackPlayer player : players) {
			player.stop();
		}
	}

	/**
	 * ゲームの進行度を進める。
	 * また、全てのプレイヤーに進行度を通知する。
	 */
	private void advanceStage() {
		this.stage++;
		if(isValidStage()) {
			for(BlackJackPlayer player : this.players) {
				player.setStage(this.stage);
			}
			try {
				Thread.sleep(waitingTime);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}

	/**
	 * 現在の進行度の処理が必要かどうかを判別する。
	 * @return stage の処理が必要ならtrue、必要ないならfalse
	 */
	private boolean isValidStage() {
		if(stage < 9) {
			if(stage == 2) {
				boolean isSomeoneBet = false;
				for(ArrayList<String> playerStatus : allPlayersStatus) {
					if(playerStatus.get(0).equals("BET")) {
						isSomeoneBet = true;
					}
				}
				if(!isSomeoneBet) {
					stage = 8;
					return false;
				}
			} else if(stage == 3) {
				int dealersNumber = dealersOpenCards.get(0).getNumber();
				if(dealersNumber != 1) {
					return false;
				}
			} else if(stage == 4) {
				int dealersNumber = dealersOpenCards.get(0).getNumber();
				if(1 < dealersNumber && dealersNumber < 10) {
					return false;
				}
			}
			return true;
		}
		return false;
	}

	/**
	 * 進行度に応じたゲームの処理のメソッドを呼び出す。
	 */
	private void callGameProgressMethod() {
		// System.out.println("STAGE" + stage);
		switch(stage) {
		case 1:
			makePlayersToBet();
			break;
		case 2:
			dealFirstHandCards();
			break;
		case 3:
			if(isValidStage()) {
				makePlayersToInsure();
			}
			break;
		case 4:
			if(isValidStage()) {
				checkDealersBlackJack();
			}
			break;
		case 5:
			processPlayersAction();
			break;
		case 6:
			processDealersAction();
			break;
		case 7:
			processWinningAndLosing();
			break;
		case 8:
			checkNextGame();
			break;
		}
		System.out.println();
	}

	/**
	 * プレイヤーの賭金の設定処理。
	 * まず、それぞれのプレイヤーから賭金をメッセージとして受け取る。
	 * 受け取ったメッセージをもとに賭金の情報を設定し、その分チップから引く。
	 */
	private void makePlayersToBet() {
		for(BlackJackPlayer player : players) {
			String currentPlayersInformation = player.getName() + "(" + player.getPlayerNumber() + ")";
			for(int i = 0; i < numberOfTimesToAcceptWrongMessage; i++) {
				if(i > 0) {
					System.out.print("もう一度");
				}
				System.out.println(currentPlayersInformation + "の賭金を確認します。");
				String betString = player.getMessage();
				int bet = -1;
				try {
					bet = Integer.parseInt(betString);
				} catch(Exception e) {
					System.out.println("賭金が読み取れませんでした。");
					bet = -1;
					try {
						Thread.sleep(waitingTime);
					} catch (InterruptedException e1) {}
				}
				if(bet < 0) {
					System.out.println("0または最低賭金以上の数ではありません。");
				} else if(bet == 0) {
					allPlayersBets.set(player.getPlayerNumber(),bet);
					allPlayersStatus.get(player.getPlayerNumber()).set(0, "PASS");
					System.out.println(currentPlayersInformation + "はこの勝負をパスします。");
					break;
				} else if(bet < minimumBet) {
					System.out.println("最低賭金(" + minimumBet + ")を下回っています。");
				} else if(bet > this.allPlayersChips.get(player.getPlayerNumber())) {
					System.out.println("賭金が自分の出せるチップの量(" + allPlayersChips.get(player.getPlayerNumber()) + ")を上回っています");
				} else {
					allPlayersBets.set(player.getPlayerNumber(),bet);
					int playersChip = allPlayersChips.get(player.getPlayerNumber()) - bet;
					allPlayersChips.set(player.getPlayerNumber(), playersChip);
					allPlayersStatus.get(player.getPlayerNumber()).set(0, "BET");
					System.out.println(currentPlayersInformation + "の賭金は" + bet + "になりました。");
					break;
				}
				try {
					Thread.sleep(waitingTime);
				} catch (InterruptedException e) {
					// TODO 自動生成された catch ブロック
					e.printStackTrace();
				}
			}
			if(allPlayersStatus.get(player.getPlayerNumber()).get(0).contentEquals("INITIALIZE")) {
				allPlayersBets.set(player.getPlayerNumber(), 0);
				allPlayersStatus.get(player.getPlayerNumber()).set(0, "PASS");
				System.out.println(currentPlayersInformation + "はこの勝負をパスします。");
			}
			System.out.println();
		}
	}

	/**
	 * プレイヤーの初期手札を配る処理。
	 * プレイヤーの状態がBET（つまり賭金をかけた）ならば配る。そうでないならば配らない。
	 * その後、ディーラーのオープンカード及び伏せカードも配る。
	 */
	private void dealFirstHandCards() {
		for(BlackJackPlayer player : players) {
			if(allPlayersStatus.get(player.getPlayerNumber()).get(0).contentEquals("BET")) {
				for(int i = 0; i < 2; i++) {
					allPlayersHands.get(player.getPlayerNumber()).get(0).add(gameDeck.drawCard());
				}
				allPlayersStatus.get(player.getPlayerNumber()).set(0, "DEALED");
			}
		}
		dealersHiddenCard = gameDeck.drawCard();
		dealersOpenCards.add(gameDeck.drawCard());
		System.out.println("ディーラーのオープンカードは " + dealersOpenCards.get(0).cardInfo() + " です。");
		System.out.println();
	}

	/**
	 * インシュランスの処理。
	 * それぞれのプレイヤーからメッセージを受け取り、適正な値であればインシュランスを設定し、その分チップから引く。
	 */
	private void makePlayersToInsure() {
		for(BlackJackPlayer player : players) {
			if(allPlayersStatus.get(player.getPlayerNumber()).get(0).contentEquals("DEALED")) {
				String currentPlayersInformation = player.getName() + "(" + player.getPlayerNumber() + ")";
				for(int i = 0; i < numberOfTimesToAcceptWrongMessage; i++) {
					if(i > 0) {
						System.out.print("もう一度");
					}
					System.out.println(currentPlayersInformation + "のインシュランスを確認します。");
					String betString = player.getMessage();
					int insurance = -1;
					try {
						insurance = Integer.parseInt(betString);
					} catch(Exception e) {
						System.out.println("インシュランスが読み取れませんでした。");
						insurance = -1;
						try {
							Thread.sleep(waitingTime);
						} catch (InterruptedException e1) {}
					}
					if(insurance < 0) {
					} else if(insurance == 0) {
						allPlayersInsurance.set(player.getPlayerNumber(), insurance);
						allPlayersStatus.get(player.getPlayerNumber()).set(0, "NO INSURED");
						System.out.println(player.getName() + "(" + player.getPlayerNumber() + ")はインシュランスをしません。");
						break;
					} else if(insurance > this.allPlayersChips.get(player.getPlayerNumber())) {
						System.out.println("インシュランスが自分のチップ(" + allPlayersChips.get(player.getPlayerNumber()) + ")を上回っています。");
					} else if(insurance > allPlayersBets.get(player.getPlayerNumber()) / 2) {
						System.out.println("インシュランスは賭金の半分(" + (allPlayersBets.get(player.getPlayerNumber()) / 2) + ")までです。");
					} else {
						int chip = allPlayersChips.get(player.getPlayerNumber()) - insurance;
						allPlayersChips.set(player.getPlayerNumber(), chip);
						allPlayersInsurance.set(player.getPlayerNumber(), insurance);
						allPlayersStatus.get(player.getPlayerNumber()).set(0, "INSURED");
						System.out.println(player.getName() + "(" + player.getPlayerNumber() + ")のインシュランスは" + insurance + "になりました。");
						break;
					}
					try {
						Thread.sleep(waitingTime);
					} catch (InterruptedException e) {
						// TODO 自動生成された catch ブロック
						e.printStackTrace();
					}
				}
			}
			System.out.println();
		}
	}

	/**
	 * ディーラーのブラックジャックの確認処理。
	 * ディーラーがブラックジャックだった場合、インシュランスの払い戻しの処理を行う。
	 * その後 stage が 6 になり、プレイヤーの行動処理はスキップされる。
	 */
	private void checkDealersBlackJack() {
		System.out.println("ディーラーのブラックジャックを確認します。");
		ArrayList<Card> dealersHand = new ArrayList<Card>(dealersOpenCards);
		dealersHand.add(dealersHiddenCard);
		if(culculateHandStrength(dealersHand) == 21) {
			dealersOpenCards.add(dealersHiddenCard);
			System.out.println("ディーラーのブラックジャックです。");
			for(BlackJackPlayer player : players) {
				if(allPlayersStatus.get(player.getPlayerNumber()).get(0).contentEquals("INSURED")) {
					String currentPlayersInformation = player.getName() + "(" + player.getPlayerNumber() + ")";
					int chip = allPlayersChips.get(player.getPlayerNumber());
					int insuranceRefund = allPlayersInsurance.get(player.getPlayerNumber()) * 3;
					chip += insuranceRefund;
					allPlayersChips.set(player.getPlayerNumber(), chip);
					System.out.println(currentPlayersInformation + "はインシュランスの払い戻しで" + insuranceRefund + "を受け取りました。");
				}
			}
			stage = 6;
			System.out.println();
		} else {
			System.out.println("ディーラーはブラックジャックではありませんでした。");
		}
	}

	/**
	 * プレイヤーの行動処理。
	 * プレイヤーのそれぞれの手札に対してそれぞれメッセージを受け取る。
	 * その後、メッセージの内容に応じて処理するメソッドを呼び出す。
	 * これらのメソッドは実行可能かどうかを返すので、実行できなかった場合はまたメッセージを受け取る。(numberOfTimesToAcceptWrongMessage)
	 */
	public void processPlayersAction() {
		for(BlackJackPlayer player : players) {
			if(!allPlayersStatus.get(player.getPlayerNumber()).get(0).contentEquals("PASS")) {
				String currentPlayersInformation = player.getName() + "(" + player.getPlayerNumber() + ")";
				int handNumber = 0;
				while(handNumber < allPlayersHands.get(player.getPlayerNumber()).size()) {
					ArrayList<Card> playersHand = allPlayersHands.get(player.getPlayerNumber()).get(handNumber);
					for(int i = 0; i < numberOfTimesToAcceptWrongMessage; i++) {
						if(i > 0) {
							System.out.println("もう一度アクションを確認します。");
						}
						System.out.println(currentPlayersInformation + "のカードは");
						for(Card card : playersHand) {
							card.printStatus();
						}
						System.out.println("です。");
						String playersAction = player.getMessageSplited(handNumber);
						boolean isCorrectInput = true;
						switch(playersAction) {
						case "HIT":
							isCorrectInput = processPlayersHit(player, handNumber);
							break;
						case "STAND":
							isCorrectInput = processPlayersStand(player, handNumber);
							break;
						case "DOUBLEDOWN":
							isCorrectInput = processPlayersDoubleDown(player, handNumber);
							break;
						case "SPLIT":
							isCorrectInput = processPlayersSplit(player, handNumber);
							break;
						case "SURRENDER":
							isCorrectInput = processPlayersSurrender(player, handNumber);
							break;
						default:
							isCorrectInput = false;
							break;
						}
						if(isCorrectInput) break;
						try {
							Thread.sleep(waitingTime);
						} catch (InterruptedException e) {
							// TODO 自動生成された catch ブロック
							e.printStackTrace();
						}
					}
					try {
						Thread.sleep(waitingTime);
					} catch (InterruptedException e) {
						// TODO 自動生成された catch ブロック
						e.printStackTrace();
					}
					if(		allPlayersStatus.get(player.getPlayerNumber()).get(handNumber).contentEquals("STAND") ||
							allPlayersStatus.get(player.getPlayerNumber()).get(handNumber).contentEquals("SURRENDER") ||
							allPlayersStatus.get(player.getPlayerNumber()).get(handNumber).contentEquals("BURST")) {
						handNumber++;
					}
				}
			}
			System.out.println();
		}
	}

	/**
	 * プレイヤーのヒット処理。
	 * プレイヤーの handNumber 番目の手札（スプリットした場合のみ増える）に一枚追加する。
	 * 正常に行われた場合、playerStatus が変化する。
	 * バーストした場合は BURST に、そうでない場合は HIT になる。
	 * エースのスプリットをしたあとのヒットをした場合のみ playerStatus は STAND になる。
	 * @param player 処理を行うプレイヤー
	 * @param handNumber 手札の番号
	 * @return 正常に動作が行える入力だったかどうか
	 */
	private boolean processPlayersHit(BlackJackPlayer player, int handNumber) {
		String currentPlayersInformation = player.getName() + "(" + player.getPlayerNumber() + ")";
		if(allPlayersHands.get(player.getPlayerNumber()).size() > 1) {
			currentPlayersInformation += "の手札 " + (handNumber + 1) + " ";
		}
		System.out.println(currentPlayersInformation + "はヒットしました。");
		Card drawCard = gameDeck.drawCard();
		System.out.println("引いたカードは" + drawCard.cardInfo() + "です。");
		ArrayList<Card> playersHand = allPlayersHands.get(player.getPlayerNumber()).get(handNumber);
		playersHand.add(drawCard);
		int handStrength = culculateHandStrength(playersHand);
		if(handStrength < 0) {
			System.out.println(currentPlayersInformation + "はバーストしました。");
			allPlayersStatus.get(player.getPlayerNumber()).set(handNumber, "BURST");
		} else {
			System.out.println(currentPlayersInformation + "の合計は" + handStrength + "になりました。");
			if(allPlayersStatus.get(player.getPlayerNumber()).get(handNumber) == "SPLIT" && playersHand.get(0).getNumber() == 1) {
				System.out.println("エースのスプリットのあとは1回しかヒットできません。");
				allPlayersStatus.get(player.getPlayerNumber()).set(handNumber, "STAND");
			} else {
				allPlayersStatus.get(player.getPlayerNumber()).set(handNumber, "HIT");
			}
		}
		return true;
	}

	/**
	 * プレイヤーのスタンド処理。
	 * 正常に行われた場合、playerStatus が STAND になる。
	 * @param player 処理を行うプレイヤー
	 * @param handNumber 手札の番号
	 * @return 正常に動作が行える入力だったかどうか
	 */
	private boolean processPlayersStand(BlackJackPlayer player, int handNumber) {
		String currentPlayersInformation = player.getName() + "(" + player.getPlayerNumber() + ")";
		if(allPlayersHands.get(player.getPlayerNumber()).size() > 1) {
			currentPlayersInformation += "の手札 " + (handNumber + 1) + " ";
		}
		System.out.println(currentPlayersInformation + "はスタンドしました。");
		allPlayersStatus.get(player.getPlayerNumber()).set(handNumber, "STAND");
		return true;
	}

	/**
	 * プレイヤーのダブルダウン処理。
	 * プレイヤーのチップが賭金以上のときのみ動作。
	 * 賭金を倍にし、増加分だけチップを減らす。
	 * さらに、バーストした場合は playerStatus が BURST になり、そうでない場合は STAND になる。
	 * @param player 処理を行うプレイヤー
	 * @param handNumber 手札の番号
	 * @return 正常に動作が行える入力だったかどうか
	 */
	private boolean processPlayersDoubleDown(BlackJackPlayer player, int handNumber) {
		String currentPlayersInformation = player.getName() + "(" + player.getPlayerNumber() + ")";
		if(handNumber == 0 && allPlayersHands.get(player.getPlayerNumber()).get(0).size() == 2) {
			int bet = allPlayersBets.get(player.getPlayerNumber()) * 2;
			int chip = allPlayersChips.get(player.getPlayerNumber()) - (bet / 2);
			if(chip >= 0) {
				System.out.println(currentPlayersInformation + "はダブルダウンしました。");
				allPlayersBets.set(player.getPlayerNumber(), bet);
				Card drawCard = gameDeck.drawCard();
				System.out.println("引いたカードは" + drawCard.cardInfo() + "です。");
				allPlayersHands.get(player.getPlayerNumber()).get(handNumber).add(drawCard);
				int handStrength = culculateHandStrength(allPlayersHands.get(player.getPlayerNumber()).get(handNumber));
				if(handStrength < 0) {
					System.out.println(currentPlayersInformation + "はバーストしました。");
					allPlayersStatus.get(player.getPlayerNumber()).set(handNumber, "BURST");
				} else {
					System.out.println(currentPlayersInformation + "の合計は" + handStrength + "になりました。");

					allPlayersStatus.get(player.getPlayerNumber()).set(handNumber, "DOUBLEDOWN");
				}
			} else {
				System.out.println("ダブルダウンするためのチップがありません！");
				return false;
			}
		} else {
			System.out.println("最初にしかダブルダウンはできません！");
			return false;
		}
		return true;
	}

	/**
	 * プレイヤーのスプリット処理。
	 * プレイヤーの最初のアクションで、チップが賭金以上であり、かつ手札が同じ点数のカードの場合のみ動作。
	 * 手札を一つ追加し、現在の手札から一枚をそちらに移動させる。
	 * さらにチップからプレイヤーの賭金分を引く。
	 * その後、playerStatus を SPLIT にする。
	 * @param player 処理を行うプレイヤー
	 * @param handNumber 手札の番号
	 * @return 正常に動作が行える入力だったかどうか
	 */
	private boolean processPlayersSplit(BlackJackPlayer player, int handNumber) {
		String currentPlayersInformation = player.getName() + "(" + player.getPlayerNumber() + ")";
		ArrayList<Card> playersHand1 = allPlayersHands.get(player.getPlayerNumber()).get(handNumber);
		if(handNumber == 0 && playersHand1.size() == 2) {
			if(canSplit(playersHand1)) {
				int bet = allPlayersBets.get(player.getPlayerNumber());
				int chip = allPlayersChips.get(player.getPlayerNumber()) - bet;
				if(chip >= 0) {
					System.out.println(currentPlayersInformation + "はスプリットしました。");
					allPlayersChips.set(player.getPlayerNumber(), chip);
						ArrayList<Card> playersHand2 = new ArrayList<Card>();
					playersHand2.add(playersHand1.remove(1));
					allPlayersHands.get(player.getPlayerNumber()).add(playersHand2);
					allPlayersStatus.get(player.getPlayerNumber()).add("SPLIT");
					allPlayersStatus.get(player.getPlayerNumber()).set(handNumber, "SPLIT");
				} else {
					System.out.println("スプリットするためのチップがありません！");
					return false;
				}
			} else {
				System.out.println("2枚の数字が同じでなければスプリットできません！");
				return false;
			}
		} else {
			System.out.println("最初にしかスプリットはできません！");
			return false;
		}
		return true;
	}

	/**
	 * プレイヤーのサレンダー処理。
	 * プレイヤーの最初のアクションの場合のみ動作する。
	 * 賭金の半分をプレイヤーのチップに返却し、playerStatus を SURRENDER にする。
	 * @param player 処理を行うプレイヤー
	 * @param handNumber 手札の番号
	 * @return 正常に動作が行える入力だったかどうか
	 */
	private boolean processPlayersSurrender(BlackJackPlayer player, int handNumber) {
		String currentPlayersInformation = player.getName() + "(" + player.getPlayerNumber() + ")";
		if(handNumber == 0 && allPlayersHands.get(player.getPlayerNumber()).get(0).size() == 2) {
			int back = allPlayersBets.get(player.getPlayerNumber()) / 2;
			int chip = allPlayersChips.get(player.getPlayerNumber()) + back;
			allPlayersChips.set(player.getPlayerNumber(), chip);
			System.out.println(currentPlayersInformation + "はサレンダーしました。\n"
					+ currentPlayersInformation + "に" + back + "が返却されました。");
			allPlayersStatus.get(player.getPlayerNumber()).set(handNumber, "SURRENDER");
		} else {
			System.out.println("最初にしかサレンダーはできません！");
			return false;
		}
		return true;
	}

	/**
	 * ディーラーのアクションの処理。
	 * ディーラーの伏せカードを公開カードに追加し、その後合計が17以上になるかバーストするまでカードをデッキから引く。
	 */
	private void processDealersAction() {
		dealersOpenCards.add(dealersHiddenCard);
		System.out.println("ディーラーの手は");
		for(Card card : dealersOpenCards) {
			card.printStatus();
		}
		System.out.println("です。");
		Card drawCard;
		int dealersHandStrength = culculateHandStrength(dealersOpenCards);
		while(0 < dealersHandStrength && dealersHandStrength < 17) {
			drawCard = gameDeck.drawCard();
			System.out.println("ディーラーは" + drawCard.cardInfo() + "を引きました。");
			dealersOpenCards.add(drawCard);
			dealersHandStrength = culculateHandStrength(dealersOpenCards);
		}
		if(culculateHandStrength(dealersOpenCards) < 0) {
			System.out.println("ディーラーはバーストしました。");
		} else {
			System.out.println("ディーラーの合計は" + dealersHandStrength + "です。");
		}
		System.out.println();
	}

	/**
	 * 勝敗処理。
	 * パスまたはサレンダーではないプレイヤーの手札に対してそれぞれディーラーの手の強さと比較する。
	 * プレイヤーの方が低い場合は、プレイヤーの敗北となる。この場合は特になにもしない。
	 * プレイヤーとディーラーが同じ場合は引き分けとなり、賭金が返却される。
	 * プレイヤーの方が高い場合は、さらにプレイヤーがブラックジャックかどうかで分かれる。
	 * プレイヤーがブラックジャックの場合は賭金の2.5倍がチップに払い戻される。
	 * プレイヤーがブラックジャックではない場合は賭金の2倍がチップに払い戻される。
	 */
	private void processWinningAndLosing() {
		int dealersHandStrength = culculateHandStrength(dealersOpenCards);
		for(BlackJackPlayer player : players) {
			if(!(allPlayersStatus.get(player.getPlayerNumber()).get(0).contentEquals("PASS") || allPlayersStatus.get(player.getPlayerNumber()).get(0).contentEquals("SURRENDER"))) {
				int numberOfPlayersHands = allPlayersHands.get(player.getPlayerNumber()).size();
				for(int handNumber = 0; handNumber < numberOfPlayersHands ; handNumber++){
					String currentPlayersInformation = player.getName() + "(" + player.getPlayerNumber() + ")";
					if(allPlayersHands.get(player.getPlayerNumber()).size() > 1) {
						currentPlayersInformation += "の手札 " + (handNumber + 1) + " ";
					}
					if(allPlayersStatus.get(player.getPlayerNumber()).get(handNumber).contentEquals("BURST")) {
						System.out.println(currentPlayersInformation + "はバーストしたので負けました。");
						System.out.println(currentPlayersInformation + "のチップは " + allPlayersChips.get(player.getPlayerNumber()) + "になりました。");
					} else {
						int playersChip = allPlayersChips.get(player.getPlayerNumber());
						int playersBet = allPlayersBets.get(player.getPlayerNumber());
						if(dealersHandStrength < 0) {
							System.out.println("ディーラーがバーストしたので" + currentPlayersInformation + "の勝利です。");
							playersChip = playersChip + playersBet * 2;
							allPlayersChips.set(player.getPlayerNumber(), playersChip);
							System.out.println(currentPlayersInformation + "のチップは " + playersChip + "になりました。");
						} else {
							ArrayList<Card> playersHand = allPlayersHands.get(player.getPlayerNumber()).get(handNumber);
							int playersHandStrength = culculateHandStrength(playersHand);
							System.out.println("ディーラーの合計は " + dealersHandStrength + " で、" + currentPlayersInformation +
									"の合計は " + playersHandStrength + " です。");
							if(dealersHandStrength > playersHandStrength) {
								System.out.println(currentPlayersInformation + "は負けました。");
								System.out.println(currentPlayersInformation + "のチップは " + playersChip + "になりました。");
							} else if(dealersHandStrength == playersHandStrength) {
								System.out.println(currentPlayersInformation + "は引き分けです。");
								playersChip = playersChip + playersBet;
								allPlayersChips.set(player.getPlayerNumber(), playersChip);
								System.out.println(currentPlayersInformation + "のチップは " + playersChip + "になりました。");
							} else if(playersHandStrength == 21 && allPlayersHands.get(player.getPlayerNumber()).size() == 1 && playersHand.size() == 2){
								System.out.println(currentPlayersInformation + "はブラックジャックで勝利しました。");
								playersChip = playersChip + playersBet * 5 / 2;
								allPlayersChips.set(player.getPlayerNumber(), playersChip);
								System.out.println(currentPlayersInformation + "のチップは " + playersChip + "になりました。");
							} else {
								System.out.println(currentPlayersInformation + "は勝利しました。");
								playersChip = playersChip + playersBet * 2;
								allPlayersChips.set(player.getPlayerNumber(), playersChip);
								System.out.println(currentPlayersInformation + "のチップは " + playersChip + "になりました。");
							}
						}
					}
				}
			}
			System.out.println();
		}
		for(BlackJackPlayer player : players) {
			String currentPlayersInformation = player.getName() + "(" + player.getPlayerNumber() + ")";
			System.out.println(currentPlayersInformation + " : " + allPlayersChips.get(player.getPlayerNumber()));
		}
		System.out.println();
	}

	/**
	 * 次のゲームの確認処理。
	 * プレイヤーそれぞれからメッセージを受け取り、ENDを受け取った場合は hasNextGame を false にする。
	 * または1ゲームの間応答がないプレイヤーが居る場合、プレイヤーの状態は INITIALIZED のままなので、その場合も hasNextGame を false にする。
	 */
	private void checkNextGame() {
		boolean allPlayersPassed = true;
		for(BlackJackPlayer player : players) {
			try {
				Thread.sleep(waitingTime);
			} catch (InterruptedException e) {
				// TODO 自動生成された catch ブロック
				e.printStackTrace();
			}
			String currentPlayersInformation = player.getName() + "(" + player.getPlayerNumber() + ")";
			System.out.println(currentPlayersInformation + "のゲーム終了を確認します。");
			if(allPlayersChips.get(player.getPlayerNumber()) < minimumBet) {
				hasNextGame = false;
			} else {
				String playersAction = player.getMessage();
				if(playersAction.contentEquals("END")) {
					hasNextGame = false;
				}
				if(!allPlayersStatus.get(player.getPlayerNumber()).get(0).contentEquals("PASS")) allPlayersPassed = false;
			}

			if(allPlayersStatus.get(player.getPlayerNumber()).get(0).contentEquals("INITIALIZED")) hasNextGame = false;

			System.out.println();
		}
		if(allPlayersPassed) hasNextGame = false;
		if(this.gameNumber >= maxGameNumber) hasNextGame = false;

		if(hasNextGame) {
			System.out.println("次のゲームが開始できます。");
		} else {
			System.out.println("次のゲームはありません。");
			System.out.println("\nー最終結果ー");
			for(BlackJackPlayer player : players) {
				String currentPlayersInformation = player.getName() + "(" + player.getPlayerNumber() + ")";
				System.out.println(currentPlayersInformation + " : " + allPlayersChips.get(player.getPlayerNumber()));
			}
		}
	}

	/**
	 * 手札の強さの点数を返す静的メソッド。
	 * @param hand 手札
	 * @return 強さの値
	 */
	public static int culculateHandStrength(ArrayList<Card> hand) {
		int strength = 0;
		boolean containsAce = false;
		for(Card card : hand) {
			int cardNumber = card.getNumber();
			if(cardNumber > 10) cardNumber = 10;
			strength += cardNumber;
			if(cardNumber == 1) containsAce = true;
		}
		if(strength < 12 && containsAce) {
			strength += 10;
		}
		if(strength > 21) strength = -1;
		return strength;
	}

	/**
	 * スプリットできる手札かどうかを判別する静的メソッド。
	 * @param hand 手札
	 * @return スプリットできるなら true できないなら false
	 */
	public static boolean canSplit(ArrayList<Card> hand) {
		int hand1 = hand.get(0).getNumber();
		int hand2 = hand.get(1).getNumber();
		if(hand1 > 10) hand1 = 10;
		if(hand2 > 10) hand2 = 10;
		return hand1 == hand2;
	}


	// コードに長い文字列をベタ書きするのはよくないので、後でファイルから読み取るように書き直す。
	public static final String rulesString = "ブラックジャックはディーラー対各プレイヤーのゲームです。\n"
			+ "他のプレイヤーとのやり取りはありません。\n"
			+ "絵札を10、エースを1または11と数えて、配られたカードの合計が21に近いほうが勝利となります。\n"
			+ "ただし、合計が22以上になってしまうとバーストとなり、即座に敗北となります。\n"
			+ "また、Aと10の2枚で21になることをナチュラルブラックジャックといい、3枚以上で21になるよりも強いです。\n"
			+ "各プレイヤーはディーラーに勝つと賭金と同額の配当を受け、自分の賭金が返還されます。\n"
			+ "引き分けのプレイヤーには配当はありませんが、自分の賭金は戻ってきます。\n"
			+ "敗北したプレイヤーの賭金は没収されます。\n"
			+ "また、特別にナチュラルブラックジャックで勝利したときのみ配当額が1.5倍（端数切捨て）になります。\n"
			+ "誰かのチップが場の最低賭金を下回るか、やめることを選択するまでゲームを繰り返します。\n"
			+ "ゲーム終了時には所持チップの量で優劣をつけます。\n"
			+ "ゲームの流れは以下の通りです。\n"
			+ "1.各プレイヤーはまず賭金（ベット金額）を決めます。\n"
			+ "このとき賭金を0に設定すると1ゲームパスできます。\n"
			+ "2.プレイヤーが全員ベットし終えるとディーラーが各プレイヤーと自身にカードを2枚配ります。\n"
			+ "各プレイヤーのカードはすべて表向きに配られます。ディーラーのカードは2枚のうち1枚だけ裏向きに配られます。\n"
			+ "（次の3、4の手順は特定の条件下の場合のみ存在し、通常はありません。）\n"
			+ "3.ディーラーの表向きのカードがエースだった場合、各プレイヤーは賭金の半分以下を払ってインシュランスをすることができます。\n"
			+ "このインシュランスはディーラーがナチュラルブラックジャックであることに賭ける追加のチップです。\n"
			+ "4.ディーラーにナチュラルブラックジャックの可能性がある場合は、\n"
			+ "（ディーラーのオープンカードがエースのときは全プレイヤーのインシュランスを確認した後）、\n"
			+ "ディーラーは自分のがナチュラルブラックジャックかを確認します。\n"
			+ "もしディーラーがナチュラルブラックジャックだった場合、即座に勝敗が決定します。\n"
			+ "このとき、ナチュラルブラックジャックのプレイヤーは引き分けに、そうでないプレイヤーは敗北になります。\n"
			+ "また、このときインシュランスを賭けていたプレイヤーにはインシュランス分の2倍の配当があり、インシュランスの賭金も戻ってきます。\n"
			+ "5.ディーラーの左隣のプレイヤーから順に次のうちどれかを選択します。\n"
			+ "   ・ヒット：ディーラーに更に1枚のカードを配ってもらいます。バーストするまで繰り返し選ぶことができます。\n"
			+ "   ・スタンド：手を現状で確定し、ディーラーと勝負します。\n"
			+ "   ・サレンダー：自分のカードが最初の2枚のときのみ可能です。賭金の半分を諦め、勝負から降ります。\n"
			+ "   ・ダブルダウン：自分のカードが最初の2枚のときのみ可能です。賭金を倍に上乗せし、次に配られる1枚を含めた3枚で勝負をします。\n"
			+ "   ・スプリット：最初の自分のカード2枚が同じ数字だったときのみ可能です。自分の賭金の同額を追加し、自分の手札を2つに分けてそれぞれ独立に勝負を行います。\n"
			+ "6.すべてのプレイヤーがスタンドもしくはサレンダーをしたら、ディーラーは裏向きにしていたカードを表にします。\n"
			+ "そして、ディーラーは自分の手が17以上になるまでカードを引きます。\n"
			+ "ディーラーの手が17以上になったらその時のディーラーの手と各プレイヤーで勝負を行います。\n"
			+ "7.賭金の精算を行い、次のゲームを始めます。";
}
