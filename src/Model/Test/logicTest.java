package Model.Test;

import Model.Logic.Board;
import Model.Logic.Tile;
import Model.Logic.Word;
import Model.Logic.Tile.Bag;

public class logicTest {

	public static void testBag() {
		Bag b = Tile.Bag.getBag();
		Bag b1 = Tile.Bag.getBag();
		if (b1 != b)
			System.out.println("your Bag in not a Singleton (-5)");

		int[] q0 = b.getQuantities();
		q0[0] += 1;
		int[] q1 = b.getQuantities();
		if (q0[0] != q1[0] + 1)
			System.out.println("getQuantities did not return a clone (-5)");

		for (int k = 0; k < 9; k++) {
			int[] qs = b.getQuantities();
			Tile t = b.getRand();
			int i = t.letter - 'A';
			int[] qs1 = b.getQuantities();
			if (qs1[i] != qs[i] - 1)
				System.out.println("problem with getRand (-1)");

			b.put(t);
			b.put(t);
			b.put(t);

			if (b.getQuantities()[i] != qs[i])
				System.out.println("problem with put (-1)");
		}

		if (b.getTile('a') != null || b.getTile('$') != null || b.getTile('A') == null)
			System.out.println("your getTile is wrong (-2)");

	}

	private static Tile[] get(String s) {
		Tile[] ts = new Tile[s.length()];
		int i = 0;
		for (char c : s.toCharArray()) {
			ts[i] = Bag.getBag().getTile(c);
			i++;
		}
		return ts;
	}

	public static void testBoard() {
		Board b = Board.getBoard();
		if (b != Board.getBoard())
			System.out.println("board should be a Singleton (-5)");

		Bag bag = Bag.getBag();
		Tile[] ts = new Tile[10];
		for (int i = 0; i < ts.length; i++)
			ts[i] = bag.getRand();

		Word w0 = new Word(ts, 0, 6, true);
		Word w1 = new Word(ts, 7, 6, false);
		Word w2 = new Word(ts, 6, 7, true);
		Word w3 = new Word(ts, -1, 7, true);
		Word w4 = new Word(ts, 7, -1, false);
		Word w5 = new Word(ts, 0, 7, true);
		Word w6 = new Word(ts, 7, 0, false);

		if (b.boardLegal(w0) || b.boardLegal(w1) || b.boardLegal(w2) || b.boardLegal(w3) || b.boardLegal(w4)
				|| !b.boardLegal(w5) || !b.boardLegal(w6))
			System.out.println("your boardLegal function is wrong (-10)");

		for (Tile t : ts)
			bag.put(t);

		Word horn = new Word(get("HORN"), 7, 5, false);
		if (b.tryPlaceWord(horn) != 14)
			System.out.println("problem in placeWord for 1st word (-10)");

		Word farm = new Word(get("FA_M"), 5, 7, true);
		if (b.tryPlaceWord(farm) != 9)
			System.out.println("problem in placeWord for 2ed word (-10)");

		Word paste = new Word(get("PASTE"), 9, 5, false);
		b.getWords(paste);
		if (b.tryPlaceWord(paste) != 25)
			System.out.println("problem in placeWord for 3ed word (-10)");

		Word mob = new Word(get("_OB"), 8, 7, false);
		if (b.tryPlaceWord(mob) != 18)
			System.out.println("problem in placeWord for 4th word (-10)");

		Word bit = new Word(get("BIT"), 10, 4, false);
		if (b.tryPlaceWord(bit) != 22)
			System.out.println("problem in placeWord for 5th word (-15)");

		/******************* TEST 1 ******************/

		Word bit2 = new Word(get("S_TA"), 9, 4, true);
		if (b.tryPlaceWord(bit2) != 28)
			System.out.println("SBTA should be 28 (-15)");

		Word bit3 = new Word(get("A_ONE"), 11, 3, false);
		if (b.tryPlaceWord(bit3) != 26)
			System.out.println("ATONE should be 26 (-15)");

		Word bit4 = new Word(get("XX"), 10, 7, false);
		if (b.tryPlaceWord(bit4) != 0)
			System.out.println("XE should be 0 (-15)");

		/******************************** */

		// Word bit4 = new Word(get("AKA"), 0, 12, false);
		// if (b.tryPlaceWord(bit4) != 0)
		// System.out.println("ABA should be 0 (-15)");

		// Word b2 = new Word(get("ADEL"), 3, 11, false);
		// if (b.tryPlaceWord(b2) != 0)
		// System.out.println("ADEL should be 0 (-15)");

		// Word b3 = new Word(get("ADE"), 12, 5, true);
		// if (b.tryPlaceWord(b3) != 21)
		// System.out.println("ADE should be 21 (-15)");

		// Word bit4 = new Word(get("XX"), 10, 7, false);
		// if (b.tryPlaceWord(bit4) != 0)
		// System.out.println("XE should be 0 (-15)");

		b.printBoard();
		System.out.println();
		b.printPlacedWords();

		/******************* TEST-2 ******************/

		// Word bit4 = new Word(get("__TTER"), 8, 9, true);
		// if (b.tryPlaceWord(bit4) != 12)
		// System.out.println("BETTER should be 12 (-15)");

		// Word bit5 = new Word(get("HO___"), 8, 5, false);
		// if (b.tryPlaceWord(bit5) != 39)
		// System.out.println("HOMOB should be 39 (-15)");

		// Word bit6 = new Word(get("_ANTAS"), 5, 7, false);
		// if (b.tryPlaceWord(bit6) != 11)
		// System.out.println("FANTAST should be 11 (-15)");

		// b.printBoard();

		/******************* TEST-3 ******************/

		// Word ahi = new Word(get("A_I"), 6, 5, true);
		// if (b.tryPlaceWord(ahi) != 16)
		// System.out.println("problem in placeWord for AHI word (-15)");

		// Word al = new Word(get("AL"), 6, 9, true);
		// if (b.tryPlaceWord(al) != 16)
		// System.out.println("problem in placeWord for AL word (-15)");

		// b.printBoard();

		// Word ran = new Word(get("R_N"), 6, 6, false);
		// if (b.tryPlaceWord(ran) != 16)
		// System.out.println("problem in placeWord for RAN word (-15)");

		// b.printBoard();

		/******************* TEST-4 ******************/

		// Word farm = new Word(get("FARM"), 5, 7, true);
		// int r = b.tryPlaceWord(farm);
		// b.printBoard();
		// System.out.println("FARM score: " + r);

		// Word guy = new Word(get("GUY"),4,0,true);
		// int res = b.tryPlaceWord(guy);
		// System.out.println("GUY score: " + res);

		// Word dude = new Word(get("DUDE"),12,9,false);
		// int res2 = b.tryPlaceWord(dude);
		// System.out.println("DUDE score: " + res2);

		// Word mother = new Word(get("_OTHER"),8,7,false);
		// int res3 = b.tryPlaceWord(mother);
		// System.out.println(res3);
		// b.printBoard();

	}

	public static void main(String[] args) {
		testBag(); // 30 points
		testBoard(); // 70 points

		System.out.println("done");
	}

}