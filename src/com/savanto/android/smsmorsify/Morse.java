package com.savanto.android.smsmorsify;

/**
 * Abstracts the storage of a sequence of Morse code elements.
 * Implementation can be changed to store the Morse code as Strings, Integers, etc,
 * while still providing the same functionality to programs that rely on this class.
 * 
 * @author Anton
 */
public class Morse
{
	/**
	 * Defines the dots and dashes used by Morse for storage.
	 */
	public static final char 	DIT 		= '.';
	public static final char 	DAH 		= '-';
	public static final char 	GAP 		= ' ';

	/**
	 * Storage container for the Morse.
	 * As of now, uses a char[] of dots and dashs.
	 */
	private char[] morse;

	/**
	 * Default constructor. Creates an empty Morse.
	 */
	public Morse()
	{
		this.morse = new char[0];
	}

	/**
	 * Special constructor: creates an empty Morse consisting of gaps only.
	 * @param gaps - specifies the number of gaps in the empty Morse.
	 */
/*	public Morse(int gaps)
	{
		this.morse = new char[gaps];
		for (int i = 0; i < this.morse.length; i++)
			this.morse[i] = Morse.GAP;
	}
*/
	/**
	 * Creates Morse from String with given chars for the dots and dashes.
	 * @param s - String contains only given chars for dots and dashes.
	 * @param dit - char representing a dot.
	 * @param dah - char representing a dash.
	 */
	public Morse(String s, char dit, char dah)
	{
		if (dit != Morse.DIT)
			s = s.replace(dit, Morse.DIT);
		if (dah != Morse.DAH)
			s = s.replace(dah, Morse.DAH);

		// **** Implementation specific **** //
		this.morse = new char[2 * s.length() - 1];	// Morse char[] is spaced
		for (int i = 0; i < s.length(); i++)
			this.morse[i*2] = s.charAt(i);
		for (int i = 1; i < this.morse.length; i += 2)
			this.morse[i] = Morse.GAP;
	}

	/**
	 * Creates Morse from String.
	 * @param s - String containing only dots (.) and dashes (-).
	 */
	public Morse(String s)
	{
		this(s, Morse.DIT, Morse.DAH);
	}

	/**
	 * Creates Morse from String with given char for dots and String for dashes. 
	 * @param s - String contains only given char for dots and Strings for dashes.
	 * @param dit - char representing a dot.
	 * @param dah - String representing a dash.
	 */
	public Morse(String s, char dit, String dah)
	{
		this(s.replace(dah, String.valueOf(Morse.DAH)), dit, Morse.DAH);
	}

	/**
	 * Creates Morse from String with given Strings for dots and dashes.
	 * @param s - String contains only given Strings for dots and dashes.
	 * @param dit - String representing a dot.
	 * @param dah - String representing a dash.
	 */
	public Morse(String s, String dit, String dah)
	{
		this(s.replace(dit, String.valueOf(Morse.DIT)).replace(dah, String.valueOf(Morse.DAH)), Morse.DIT, Morse.DAH);
	}

	/**
	 * 
	 * @param rhs
	 * @return
	 */
	public boolean equals(Morse rhs)
	{
		if (this.morse.length != rhs.morse.length)
			return false;
		for (int i = 0; i < this.morse.length; i++)
			if (this.morse[i] != rhs.morse[i])
				return false;
		return true;
	}

	/**
	 * @return
	 */
	@Override
	public String toString()
	{
		String s = "";
		for (int i = 0; i < this.morse.length; i += 2)
			s += this.morse[i];
		return s;
	}

	public char[] toSignal()
	{
		return this.morse;
	}
}
