package com.savanto.android.smsmorsify;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Locale;

/**
 * @author Anton
 *
 */
public class MorseCode
{
	private ArrayList< ArrayList<Morse> > ciphertext;
	private HashMap<Character, Morse> alphabet;
	private HashMap<String, Morse> dictionary;
	private int short_gap;
	private int medium_gap;

	/**
	 * Constructs full default ITU Morse code alphabet and prosign dictionary,
	 * with default gap lengths and empty Morse ciphertext.
	 */
	public MorseCode()
	{
		this("", MorseCode.DEFAULT_SHORT_GAP, MorseCode.DEFAULT_MEDIUM_GAP, true, true, true);
	}

	/**
	 * Creates Morse code from full default ITU Morse code alphabet and prosign dictionary.
	 * @param plaintext - the plaintext message to be encoded into Morse.
	 */
	public MorseCode(String plaintext)
	{
		this(plaintext, MorseCode.DEFAULT_SHORT_GAP, MorseCode.DEFAULT_MEDIUM_GAP, true, true, true);
	}

	/**
	 * Creates Morse code from default ITU Morse code alphabet and prosign dictionary, depending on options.
	 * @param plaintext - the plaintext message to be encoded into Morse.
	 * @param useNumbers - true if numbers are to be included.
	 * @param useSymbols - true if symbols are to be included.
	 * @param useProsigns - true if prosigns are to be included.
	 */
	public MorseCode(String plaintext, boolean useNumbers, boolean useSymbols, boolean useProsigns)
	{
		this(plaintext, MorseCode.DEFAULT_SHORT_GAP, MorseCode.DEFAULT_MEDIUM_GAP, useNumbers, useSymbols, useProsigns);
	}

	/**
	 * Creates Morse code from default ITU Morse code alphabet and prosign dictionary, with custom gap lengths.
	 * @param plaintext - the plaintext message to be encoded into Morse.
	 * @param short_gap - the gap length to set between Morse letters.
	 * @param medium_gap - the gap length to set between Morse words.
	 */
	public MorseCode(String plaintext, int short_gap, int medium_gap)
	{
		this(plaintext, short_gap, medium_gap, true, true, true);
	}

	/**
	 * Constructs default ITU Morse code alphabet and prosign dictionary with custom options.
	 * @param useNumbers - true if numbers are to be included.
	 * @param useSymbols - true if symbols are to be included.
	 * @param useProsigns - true if prosigns are to be included.
	 */
	public MorseCode(boolean useNumbers, boolean useSymbols, boolean useProsigns)
	{
		this("", MorseCode.DEFAULT_SHORT_GAP, MorseCode.DEFAULT_MEDIUM_GAP, useNumbers, useSymbols, useProsigns);
	}

	/**
	 * Constructs default ITU Morse code alphabet and prosign dictionary, taken from
	 * http://en.wikipedia.org/wiki/Morse_code
	 * with custom options for using numbers/symbols/prosigns, and custom gap lengths.
	 * @param plaintext - the plaintext message to be encoded into Morse.
	 * @param short_gap - the gap length to set between Morse letters.
	 * @param medium_gap - the gap length to set between Morse words.
	 * @param useNumbers - true if numbers are to be included.
	 * @param useSymbols - true if symbols are to be included.
	 * @param useProsigns - true if prosigns are to be included.
	 */
	public MorseCode(String plaintext, int short_gap, int medium_gap, boolean useNumbers, boolean useSymbols, boolean useProsigns)
	{
		// Calculate the alphabet size.
		final int DEFAULT_ALPHABET_SIZE = MorseCode.DEFAULT_LETTERS
				+ (useNumbers ? MorseCode.DEFAULT_NUMBERS : 0)
				+ (useSymbols ? MorseCode.DEFAULT_SYMBOLS : 0);
		// Calculate the dictionary size
		final int DEFAULT_DICTIONARY_SIZE = (useProsigns ? MorseCode.DEFAULT_PROSIGNS : 0);

		// Create default alphabet of Morse code characters.
		this.alphabet = new HashMap<Character, Morse>(DEFAULT_ALPHABET_SIZE);
		// Create default dictionary of Morse code words.
		this.dictionary = new HashMap<String, Morse>(DEFAULT_DICTIONARY_SIZE);

		// Create default gaps.
		this.short_gap = short_gap;
		this.medium_gap = medium_gap;

		// Add the default characters to the alphabet.
		// **** Letters **** //
		this.alphabet.put('A', new Morse(".-"));
		this.alphabet.put('B', new Morse("-..."));
		this.alphabet.put('C', new Morse("-.-."));
		this.alphabet.put('D', new Morse("-.."));
		this.alphabet.put('E', new Morse("."));
		this.alphabet.put('F', new Morse("..-."));
		this.alphabet.put('G', new Morse("--."));
		this.alphabet.put('H', new Morse("...."));
		this.alphabet.put('I', new Morse(".."));
		this.alphabet.put('J', new Morse(".---"));
		this.alphabet.put('K', new Morse("-.-"));
		this.alphabet.put('L', new Morse(".-.."));
		this.alphabet.put('M', new Morse("--"));
		this.alphabet.put('N', new Morse("-."));
		this.alphabet.put('O', new Morse("---"));
		this.alphabet.put('P', new Morse(".--."));
		this.alphabet.put('Q', new Morse("--.-"));
		this.alphabet.put('R', new Morse(".-."));
		this.alphabet.put('S', new Morse("..."));
		this.alphabet.put('T', new Morse("-"));
		this.alphabet.put('U', new Morse("..-"));
		this.alphabet.put('V', new Morse("...-"));
		this.alphabet.put('W', new Morse(".--"));
		this.alphabet.put('X', new Morse("-..-"));
		this.alphabet.put('Y', new Morse("-.--"));
		this.alphabet.put('Z', new Morse("--.."));

		// **** Numbers **** //
		if (useNumbers)
		{
			this.alphabet.put('0', new Morse("-----"));
			this.alphabet.put('1', new Morse(".----"));
			this.alphabet.put('2', new Morse("..---"));
			this.alphabet.put('3', new Morse("...--"));
			this.alphabet.put('4', new Morse("....-"));
			this.alphabet.put('5', new Morse("....."));
			this.alphabet.put('6', new Morse("-...."));
			this.alphabet.put('7', new Morse("--..."));
			this.alphabet.put('8', new Morse("---.."));
			this.alphabet.put('9', new Morse("----."));
		}

		// **** Symbols **** //
		if (useSymbols)
		{
			this.alphabet.put('.', new Morse(".-.-.-"));
			this.alphabet.put(',', new Morse("--..--"));
			this.alphabet.put('?', new Morse("..--.."));
			this.alphabet.put('\'', new Morse(".----."));
			this.alphabet.put('!', new Morse("-.-.--"));
			this.alphabet.put('/', new Morse("-..-."));
			this.alphabet.put('(', new Morse("-.--."));
			this.alphabet.put(')', new Morse("-.--.-"));
			this.alphabet.put('&', new Morse(".-..."));
			this.alphabet.put(':', new Morse("---..."));
			this.alphabet.put(';', new Morse("-.-.-."));
			this.alphabet.put('=', new Morse("-...-"));
			this.alphabet.put('+', new Morse(".-.-."));
			this.alphabet.put('-', new Morse("-....-"));
			this.alphabet.put('_', new Morse("..--.-"));
			this.alphabet.put('"', new Morse(".-..-."));
			this.alphabet.put('$', new Morse("...-..-"));
			this.alphabet.put('@', new Morse(".--.-."));
		}

		// Add the default words to the dictionary.
		// **** Prosigns **** //
		// These are actually single Morse characters, as
		// they are transmitted without inter-character separations.
		if (useProsigns)
		{
			this.dictionary.put(MorseCode.PROSIGN_AA, new Morse(".-.-"));
			this.dictionary.put(MorseCode.PROSIGN_AR, new Morse(".-.-."));
			this.dictionary.put(MorseCode.PROSIGN_AS, new Morse(".-..."));
			this.dictionary.put(MorseCode.PROSIGN_BK, new Morse("-...-.-"));
			this.dictionary.put(MorseCode.PROSIGN_BT, new Morse("-...-"));
			this.dictionary.put(MorseCode.PROSIGN_CL, new Morse("-.-..-.."));
			this.dictionary.put(MorseCode.PROSIGN_CT, new Morse("-.-.-"));
			this.dictionary.put(MorseCode.PROSIGN_DO, new Morse("-..---"));
			this.dictionary.put(MorseCode.PROSIGN_K, new Morse("-.-"));
			this.dictionary.put(MorseCode.PROSIGN_KN, new Morse("-.--."));
			this.dictionary.put(MorseCode.PROSIGN_SK, new Morse("...-.-"));
			this.dictionary.put(MorseCode.PROSIGN_SN, new Morse("...-."));
			this.dictionary.put(MorseCode.PROSIGN_SOS, new Morse("...---..."));
			this.dictionary.put(MorseCode.PROSIGN_EEEEEE, new Morse("......"));
		}

		// Set ciphertext to be empty.
		this.ciphertext = new ArrayList<ArrayList<Morse> >(0);

		// Parse the plaintext.
		this.parsePlaintext(plaintext);
	}

	private void parsePlaintext(String plaintext)
	{
		// Trim plaintext and convert to uppercase for lookup.
		plaintext = plaintext.trim();
		plaintext = plaintext.toUpperCase(Locale.getDefault());

		// If plaintext is empty, do nothing.
		if (plaintext.length() < 1)
			return;

		// Begin construction of ciphertext.
		// Words are constructed one by one from the Morse characters, then added to the ciphertext.
		ArrayList<Morse> word = new ArrayList<Morse>(0);

		// Perform prosign lookup of "Start transmission" prosign CT.
		// If prosign is found, add it to the ciphertext.
		if (this.dictionary.containsKey(MorseCode.PROSIGN_CT))
		{
			word.add(this.dictionary.get(MorseCode.PROSIGN_CT));
			this.ciphertext.add(word);
			word = new ArrayList<Morse>(0);
		}

		// Traverse plaintext.
		char c;
		for (int i = 0; i < plaintext.length(); i++)
		{
			c = plaintext.charAt(i);
			// Check for space between words.
			if (c == MorseCode.SPACE)
			{
				this.ciphertext.add(word);
				word = new ArrayList<Morse>(0);
				// Skip further lookup, continue to next character.
				continue;
			}
			// Perform character lookup in dictionary.
			// If character is found, add it to the ciphertext with short gap after.
			if (this.alphabet.containsKey(c))
				word.add(this.alphabet.get(c));
		}
		// Add last word to ciphertext
		this.ciphertext.add(word);

		// Perform prosign lookup of "End transmission" prosign SK.
		// If prosign is found, add it to the ciphertext.
		if (this.dictionary.containsKey(MorseCode.PROSIGN_SK))
		{
			word = new ArrayList<Morse>(0);
			word.add(this.dictionary.get(MorseCode.PROSIGN_SK));
			this.ciphertext.add(word);
		}
	}

	/**
	 * @return
	 */
	@Override
	public String toString()
	{
		String s = "";
		ArrayList<Morse> word;
		for (int i = 0; i < this.ciphertext.size(); i++)
		{
			word = this.ciphertext.get(i);
			for (int j = 0; j < word.size(); j++)
				s += word.get(j) + " ";
			s += '\n';
		}
		return s;
	}

	/**
	 * 
	 * @param wait
	 * @param dit
	 * @param dah
	 * @param gap
	 * @return
	 */
	public long[] toPattern(long wait, long dit, long dah, long gap)
	{
		ArrayList<Long> patternList = new ArrayList<Long>(0);
		// Add the wait.
		patternList.add(wait);
		// Traverse ciphertext
		ArrayList<Morse> word;
		char[] signal;
		for (int i = 0; i < this.ciphertext.size(); i++)
		{
			word = this.ciphertext.get(i);
			// Traverse letters in word.
			for (int j = 0; j < word.size(); j++)
			{
				signal = word.get(j).toSignal();
				// Traverse characters in signal.
				for (int k = 0; k < signal.length; k++)
				{
					switch (signal[k])
					{
						case Morse.DIT:
							patternList.add(dit);
							break;
						case Morse.DAH:
							patternList.add(dah);
							break;
						case Morse.GAP:
							patternList.add(gap);
							break;
					}
				}
				// Add short gap between letters.
				patternList.add(gap * this.short_gap);
			}
			// Remove previous short gap
			patternList.remove(patternList.size() - 1);
			// Add medium gap between words.
			patternList.add(gap * this.medium_gap);
		}
		// Remove previous medium gap.
		patternList.remove(patternList.size() - 1);

		// Convert patternList into pattern array.
		long[] pattern = new long[patternList.size()];
		for (int i = 0; i < pattern.length; i++)
			pattern[i] = patternList.get(i);

		return pattern;
	}

	// Default size/length values.
	private static final int DEFAULT_LETTERS	= 26;	// Alphabet A-Z
	private static final int DEFAULT_NUMBERS	= 10;	// Numbers 0-9
	private static final int DEFAULT_SYMBOLS	= 18;	// Misc symbols
	private static final int DEFAULT_PROSIGNS	= 14;	// Procedural signals
	public static final int DEFAULT_SHORT_GAP	= 3;	// Space between Morse letters.
	public static final int DEFAULT_MEDIUM_GAP	= 7;	// Space between Morse words.

	private static final char SPACE = ' ';
	public static final String PROSIGN_AA = "<AA>";
	public static final String PROSIGN_AR = "<AR>";
	public static final String PROSIGN_AS = "<AS>";
	public static final String PROSIGN_BK = "<BK>";
	public static final String PROSIGN_BT = "<BT>";
	public static final String PROSIGN_CL = "<CL>";
	public static final String PROSIGN_CT = "<CT>";
	public static final String PROSIGN_DO = "<DO>";
	public static final String PROSIGN_K = "<K>";
	public static final String PROSIGN_KN = "<KN>";
	public static final String PROSIGN_SK = "<SK>";
	public static final String PROSIGN_SN = "<SN>";
	public static final String PROSIGN_SOS = "<SOS>";
	public static final String PROSIGN_EEEEEE = "<EEEEEE>";

	// Default pattern values
	public static final long DEFAULT_WAIT		= 0;
	public static final long DEFAULT_DIT		= 100;
	public static final long DEFAULT_DAH		= 3 * MorseCode.DEFAULT_DIT;
	public static final long DEFAULT_GAP		= MorseCode.DEFAULT_DIT;
}
