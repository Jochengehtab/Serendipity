package org.shawn.games.Serendipity;

import java.util.Arrays;

import com.github.bhlangonijr.chesslib.Square;
import com.github.bhlangonijr.chesslib.move.*;

public class TranspositionTable
{
	public enum NodeType
	{
		EXACT, LOWERBOUND, UPPERBOUND
	}

	public class Entry
	{

		// depth: (0-255) 8 bits
		// NodeType: 2 bits
		// evaluation: 16 bits
		// staticEval: 16 bits
		// Square: 64 bits
		//

		private long signature;
		private short depthAndType;
		private short evaluation;
		private short staticEval;
		private short move;

		public Entry(NodeType type, short depth, int evaluation, long signature, Move move, int staticEval)
		{
			this.signature = signature;
			this.depthAndType = (short) ((depth << 2) + typeToByte(type));
			this.move = (move == null) ? 0 : (short) ((move.getFrom().ordinal() << 6) + move.getTo().ordinal());
			this.evaluation = (short) evaluation;
			this.staticEval = (short) staticEval;
		}

		private byte typeToByte(NodeType type)
		{
			return switch (type)
			{
				case EXACT -> 0;
				case LOWERBOUND -> 1;
				case UPPERBOUND -> 2;
			};
		}

		public long getSignature()
		{
			return signature;
		}

		public NodeType getType()
		{
			return switch (this.depthAndType & 0b11)
			{
				case 0 -> NodeType.EXACT;
				case 1 -> NodeType.LOWERBOUND;
				case 2 -> NodeType.UPPERBOUND;
				default -> throw new IllegalArgumentException("Unexpected value: " + (this.depthAndType & 0b11));
			};
		}

		public short getDepth()
		{
			return (short) (depthAndType >> 2);
		}

		public int getEvaluation()
		{
			return evaluation;
		}

		public int getStaticEval()
		{
			return staticEval;
		}

		public Move getMove()
		{
			//System.out.print(this.originalMove + " " + this.originalMove != null ? 0 : this.originalMove.getTo().ordinal() + " ");
			//System.out.println(Integer.toBinaryString(this.move));
			return move == 0 ? null : new Move(Square.squareAt(move >> 6), Square.squareAt(move & 0b111111));
		}
	}

	public final int size;
	private final int mask;
	private Entry[] entries;

	public TranspositionTable(int size)
	{
		this.size = Integer.highestOneBit(size);
		this.mask = this.size - 1;
		this.entries = new Entry[this.size];
	}

	public Entry probe(long hash)
	{
		return entries[(int) (hash & mask)];
	}

	public void write(long hash, NodeType type, int depth, int evaluation, Move move, int staticEval)
	{
		entries[(int) (hash & mask)] = new Entry(type, (short) depth, evaluation, hash, move, staticEval);
	}

	public void clear()
	{
		Arrays.fill(entries, null);
	}
}