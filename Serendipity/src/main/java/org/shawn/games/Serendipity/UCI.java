package org.shawn.games.Serendipity;

import java.util.*;
import java.util.stream.Collectors;

import com.github.bhlangonijr.chesslib.Board;
import com.github.bhlangonijr.chesslib.Side;
import com.github.bhlangonijr.chesslib.move.Move;

public class UCI
{

	private static Board internalBoard = new Board();
	private static AlphaBeta engine = new AlphaBeta();

	public static void main(String args[])
	{
		UCIMainLoop();
	}

	public static void report(int depth, int nodes, int score, long ms, Move[] pv)
	{
		System.out.printf("info depth %d nodes %d score cp %d time %d pv %s\n", depth, nodes, score,
				ms, String.join(" ", Arrays.stream(pv).takeWhile(x -> x != null).map(Object::toString)
						.collect(Collectors.toList())));
	}

	public static void reportBestMove(Move bestMove)
	{
		System.out.println("bestmove " + bestMove);
	}

	public static void UCIMainLoop()
	{
		while (true)
		{
			Scanner input = new Scanner(System.in);
			while (true)
			{
				String command = input.nextLine();
				String[] fullCommand = command.split(" ");

				switch (command.split(" ")[0])
				{
					case "isready":
						System.out.println("readyok");
						break;
					case "uci":
						System.out.println("id name weakEngine");
						System.out.println("id author me");
						System.out.println("uciok");
						break;
					case "ucinewgame":
						internalBoard = new Board();
						break;
					case "quit":
						input.close();
						return;
					case "go":
						long wtime = 100;
						long btime = 100;
						long winc = 0;
						long binc = 0;
						int depth = 256;

						for (int i = 1; i < fullCommand.length; i++)
						{
							if (fullCommand[i].equals("infinite"))
							{
								wtime = btime = 157680000000L;
								break;
							}
							if (fullCommand[i].equals("wtime"))
							{
								wtime = Long.parseLong(fullCommand[i + 1]);
							}
							if (fullCommand[i].equals("btime"))
							{
								btime = Long.parseLong(fullCommand[i + 1]);
							}
							if (fullCommand[i].equals("winc"))
							{
								winc = Long.parseLong(fullCommand[i + 1]);
							}
							if (fullCommand[i].equals("winc"))
							{
								binc = Long.parseLong(fullCommand[i + 1]);
							}
							if (fullCommand[i].equals("depth"))
							{
								depth = Integer.parseInt(fullCommand[i + 1]);
								wtime = btime = 157680000000L;
								break;
							}
						}

						long timeGiven = internalBoard.getSideToMove() == Side.WHITE
								? wtime / 20 + winc / 2
								: btime / 20 + binc / 2;

						engine.nextMove(internalBoard.clone(), depth, timeGiven - 100);
						break;
					case "position":
						for (int i = 1; i < fullCommand.length; i++)
						{
							if (fullCommand[i].equals("startpos"))
							{
								internalBoard = new Board();
							}
							if (fullCommand[i].equals("fen"))
							{
								internalBoard = new Board();
								internalBoard.loadFromFen(fullCommand[i + 1] + " "
										+ fullCommand[i + 2] + " " + fullCommand[i + 3] + " "
										+ fullCommand[i + 4] + " " + fullCommand[i + 5] + " "
										+ fullCommand[i + 6]);
							}
							if (fullCommand[i].equals("moves"))
							{
								for (int j = i + 1; j < fullCommand.length; j++)
								{
									internalBoard.doMove(fullCommand[j]);
								}
								break;
							}
						}
						break;
				}
			}
		}
	}
}
