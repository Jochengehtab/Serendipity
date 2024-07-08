package org.shawn.games.Serendipity;

import com.github.bhlangonijr.chesslib.move.Move;

public class SearchStack
{
	public class SearchState
	{
		public boolean inCheck;
		public boolean ttHit;
		public int moveCount;
		public int staticEval = AlphaBeta.VALUE_NONE;
		public Move killer;
		public Move move;
		public Move excludedMove;
		public History continuationHistory;
	}

	private SearchState[] stack;

	public SearchStack(int n)
	{
		stack = new SearchState[n + 5];

		for (int i = 0; i < stack.length; i++)
		{
			stack[i] = new SearchState();
		}
	}

	public SearchState get(int index)
	{
		return stack[index + 5];
	}
}
