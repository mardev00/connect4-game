import java.util.Random;

/**
 * class Connect4 is main class with main method
 * @author martinarmenta
 *
 */
public class Connect4 {
    static final int rows = 5;
    static final int columns = 7;
    static char[][] board = new char[rows][columns];
	
	
	public static void main(String args[]) {
		
		try {
			Player anna = new Player("Anna");
			Player max = new Player("Max");
			anna.token = 'A';
			max.token = 'X';
			simulateGame(anna, max);
		} catch (InterruptedException e) {
			e.printStackTrace();
			System.exit(-1);
		}
		
	}

	public static void simulateGame(Player p1, Player p2) throws InterruptedException {
		
			for(int i=0; i<rows; i++) {
				for (int j=0; j<columns; j++) {
					board[i][j] = '*';
				}
			}


		System.out.println("Starting game...");
		System.out.println("Player 1: " + p1.name);
		System.out.println("Player 2: " + p2.name);
        Thread.sleep(1000);
        printBoard();

		p1.start();
		p2.start();
		
		while(true) {
			sync(p1, board);
			sync(p2, board);
			Player p = findWinner(board,  p1,  p2);
			if(null!=p) {
				System.out.println("==============================================");
				System.out.println("WE HAVE A WINNER!!!");
				System.out.println("WINNER IS: " + p.name);
				System.out.println("==============================================");
				printBoard();
				break;
			}
			
		}
		System.exit(0);
		
			
	}
	
	public static void printBoard() {
		for(int i=0; i< rows; i++) {
			for (int j=0; j< columns; j++) {
				System.out.print(board[i][j] + " ");
			}
			System.out.println("\n");
		}
	}

/**
 * Player class
 * @param board
 * @param p1
 * @param p2
 * @return
 */
	
	public static Player findWinner(char[][] board, Player p1, Player p2) {

		for(int i=0; i<rows; i++) {
			for (int j=0; j<columns; j++) {
				
				//check horizontally
				if(j<=3 && board[i][j]!= '*' && board[i][j] == board[i][j+1] && board[i][j] == board[i][j+2] && board[i][j] == board[i][j+3]) {
					if(board[i][j]==p1.token) {
						p1.numWins++;
						return p1;
					}
					else {
						p2.numWins++;
						return p2;
					}
				} 
				
				else if (j==3 && board[i][j]!= '*' && board[i][j] == board[i][j-1] && board[i][j] == board[i][j-2] && board[i][j] == board[i][j-3]) {
					if(board[i][j]==p1.token) {
						p1.numWins++;
						return p1;
					}
					else {
						p2.numWins++;
						return p2;
					}
				}

				
				//Check vertically
				if(i<=1 && board[i][j]!= '*' && board[i][j] == board[i+1][j] && board[i][j] == board[i+2][j] && board[i][j] == board[i+3][j]) {
					if(board[i][j]==p1.token) {
						p1.numWins++;
						return p1;
					}
					else {
						p2.numWins++;
						return p2;
					}
				} 
				else if (i>1 && i<4 && board[i][j]!= '*' && board[i][j] == board[i+1][j] && board[i][j] == board[i-1][j] && board[i][j] == board[i-2][j]) {
					if(board[i][j]==p1.token) {
						p1.numWins++;
						return p1;
					}
					else {
						p2.numWins++;
						return p2;
					}
				}
				else if (i==4 && board[i][j]!= '*' && board[i][j] == board[i-1][j] && board[i][j] == board[i-2][j] && board[i][j] == board[i-3][j]) {
					if(board[i][j]==p1.token) {
						p1.numWins++;
						return p1;
					}
					else {
						p2.numWins++;
						return p2;
					}
				}
				
				//Check diagonally downwards left to right
				if(i<=1 && j<=3 && board[i][j]!= '*' && board[i][j] == board[i+1][j+1] && board[i][j] == board[i+2][j+2] && board[i][j] == board[i+3][j+3]) {
					if(board[i][j]==p1.token)
						return p1;
					else 
						return p2;
				} 
				
				//Check diagonally upwards right to left
				if(i>=3 && j>=3 && board[i][j]!= '*' && board[i][j] == board[i-1][j-1] && board[i][j] == board[i-2][j-2] && board[i][j] == board[i-3][j-3]) {
					if(board[i][j]==p1.token) {
						p1.numWins++;
						return p1;
					}
					else  {
						p2.numWins++;
						return p2;
					}
				} 
			}
		}
		
		return null;
	}
	
	/**
         * sync() method is used to place a piece into the game by a player.
         * The method synchronizes game moves one at a time by each player.
         * It also prints the board to show the game status after each move
         * @param Player p
	 * @param char[][] board 
	 * @throws InterruptedException
	 */
	public static void sync(Player p, char[][] board) throws InterruptedException {
		Thread.currentThread().sleep(1);
		p.lastMove = p.move();
		int countForStatus = 0;
		synchronized(p) {
			while(!(board[p.lastMove.row][p.lastMove.col]=='*')) {
				countForStatus++;
				p.lastMove = p.move();
				// waiting too long so check if game is draw
				if(countForStatus>30) {
					checkStatus(board);
				}
			}
			board[p.lastMove.row][p.lastMove.col] = p.token;
		System.out.println("==============================================");				
		System.out.println("Printing game board...");
		System.out.println("Current Player: " + p.name + "- Token-> " + p.token);
		System.out.println("==============================================");
		printBoard();		
		p.notifyAll();
		Thread.currentThread().sleep(100);

		}

	}

	private static void checkStatus(char[][] board) {
		//check if no space is left
		for(int i=0; i< rows; i++) {
			for (int j=0; j< columns; j++) {
				if(board[i][j] == '*')
					return;
			}
		}
		
		// No more space available Game is draw
		System.out.println("Game is draw");
		System.exit(0);
		
	}


}

/**
 * 
 * @author martinarmenta
 *
 */
class Move {
	int row;
	int col;
}

/**
 * 
 * @author martinarmenta
 *
 */
class Player extends Thread{
	int numWins;
	
	Player(String name) {
		this.name = name;
	}
	String name;
	char token;
	int points;
	Move lastMove;
	
	public void run() {
		 move();
	}
	
	/**
	 * move() method represents a randomly chosen move by the player.
	 * The move must be within the constraints of the game board
	 * @return
	 */

	public Move move(){
		Random rand = new Random();
		int randRow = rand.nextInt(5);
		int randCol = rand.nextInt(7);
		Move m = new Move();
		m.row = randRow;
		m.col = randCol;
		return m;
	}
	
}

