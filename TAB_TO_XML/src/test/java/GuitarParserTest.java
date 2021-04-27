import static org.junit.jupiter.api.Assertions.*;

import java.util.ArrayList;

import org.junit.jupiter.api.Test;

import TAB_TO_XML.GuitarParser;

class GuitarParserTest {
	
	
	@Test
	public void test_durationCount_01() {
		ArrayList<String> a = new ArrayList<>();
		a.add("-----------0-----");
		a.add("---------0---0---");
		a.add("-------1-------1-");
		a.add("-----2-----------");
		a.add("---2-------------");
		a.add("-0---------------");
		int b = GuitarParser.durationCount(a, 2, 15);
		int c = 1;
		assertEquals(b, c);
		
	}
	@Test
	public void test_durationCount_02() {
		ArrayList<String> a = new ArrayList<>();
		a.add("-----------0-----");
		a.add("---------0---0---");
		a.add("-------1-------1-");
		a.add("-----2-----------");
		a.add("---2-------------");
		a.add("-0---------------");
		int b = GuitarParser.durationCount(a, 5, 1);
		int c = 2;
		assertEquals(b, c);
		
	}
	@Test
	public void test_durationCount_03() {
		ArrayList<String> a = new ArrayList<>();
		a.add("-0---------------");
		a.add("-0---------------");
		a.add("-1---------------");
		a.add("-2---------------");
		a.add("-2---------------");
		a.add("-0---------------");
		int b = GuitarParser.durationCount(a, 2, 1);
		int c = 8;
		assertEquals(b, c);
		
	}
	
	@Test
	public void test_durationCount_04() {
		ArrayList<String> a = new ArrayList<>();
		a.add("-0---------------");
		a.add("-0---------------");
		a.add("-1---------------");
		a.add("-2---------------");
		a.add("-2---------------");
		a.add("-0---------------");
		int b = GuitarParser.durationCount(a, 4, 1);
		int c = 8;
		assertEquals(b, c);
		
	}
	
/*******************************************************************/
	
	@Test
	public void test_divisionCount_01() {
		// Suppose the time signature is 4/4
		ArrayList<String> a = new ArrayList<>();
		a.add("-0---------------");
		a.add("-0---------------");
		a.add("-1---------------");
		a.add("-2---------------");
		a.add("-2---------------");
		a.add("-0---------------");
		
		String temp = a.get(0);
		int b = GuitarParser.divisionCount(temp,4);
		int c = 2;
		assertEquals(b, c);
		
	}
	
	@Test
	public void test_divisionCount_02() {
		// Suppose the time signature is 4/4
		ArrayList<String> a = new ArrayList<>();
		a.add("-----------0-----");
		a.add("---------0---0---");
		a.add("-------1-------1-");
		a.add("-----2-----------");
		a.add("---2-------------");
		a.add("-0---------------");
		String temp = a.get(0);
	
		int b = GuitarParser.divisionCount(temp,4);
		int c = 2;
		assertEquals(b, c);
		
	}
	
/*******************************************************************/
	
	@Test
	public void test_stepCount_01() {
		// Suppose the time signature is 4/4 like the one given in the course website
		ArrayList<String> a = new ArrayList<>();
		a.add("-----------0-----");
		a.add("---------0---0---");
		a.add("-------1-------1-");
		a.add("-----2-----------");
		a.add("---2-------------");
		a.add("-0---------------");
		/*
		 * Suppose we choose the 0 note in the beginning
		 */
		String b = GuitarParser.stepCount(0,1,a);
		String c = "F";
		assertEquals(b, c);
	}
	
	@Test
	public void test_stepCount_02() {
		ArrayList<String> a = new ArrayList<>();
		a.add("-----------0-----");
		a.add("---------0---0---");
		a.add("-------1-------1-");
		a.add("-----2-----------");
		a.add("---2-------------");
		a.add("-0---------------");
		String b = GuitarParser.stepCount(4,8,a);
		String c = "F";
		assertEquals(b, c);
	}
	
/*******************************************************************/
	
	@Test
	public void test_octaveCount_01() {
		ArrayList<String> a = new ArrayList<>();
		a.add("-----------0-----");
		a.add("---------0---0---");
		a.add("-------1-------1-");
		a.add("-----2-----------");
		a.add("---2-------------");
		a.add("-0---------------");
		
		String b = GuitarParser.octaveCount(0, Character.getNumericValue(a.get(0).charAt(11)));
		String c = "4";
		assertEquals(b, c);
	}
	
	@Test
	public void test_octaveCount_02() {
		ArrayList<String> a = new ArrayList<>();
		a.add("-----------0-----");
		a.add("---------0---0---");
		a.add("-------1-------1-");
		a.add("-----2-----------");
		a.add("---2-------------");
		a.add("-0---------------");
		String b = GuitarParser.octaveCount(3, Character.getNumericValue(a.get(3).charAt(5)));
		String c = "3";
		assertEquals(b, c);
	}
	
/*******************************************************************/
	
	@Test
	public void test_parseAlter_01() {
		ArrayList<String> a = new ArrayList<>();
		a.add("-----------0-----");
		a.add("---------0---0---");
		a.add("-------1-------1-");
		a.add("-----2-----------");
		a.add("---2-------------");
		a.add("-0---------------");
		String b = GuitarParser.parseAlter(GuitarParser.stepCount(4,8,a));
		String c = "0";
		assertEquals(b, c);
	}
	
	@Test
	public void test_parseAlter_02() {
		ArrayList<String> a = new ArrayList<>();
		a.add("-----------0-----");
		a.add("---------0---0---");
		a.add("-------1-------1-");
		a.add("-----2-----------");
		a.add("---2-------------");
		a.add("-0---------------");
		String b = GuitarParser.parseAlter(GuitarParser.stepCount(4,12,a));
		String c = "0";
		assertEquals(b, c);
	}
	
/*******************************************************************/
	
	@Test
	public void test_tabToCollection_01() {
		ArrayList<String> a = new ArrayList<>();
		ArrayList<String> b = new ArrayList<>();
		ArrayList<ArrayList<String>> c = new ArrayList<>();
		ArrayList<ArrayList<String>> d = new ArrayList<>();
		a.add("|-----------0-----|-0---------------|");
		a.add("|---------0---0---|-0---------------|");
		a.add("|-------1-------1-|-1---------------|");
		a.add("|-----2-----------|-2---------------|");
		a.add("|---2-------------|-2---------------|");
		a.add("|-0---------------|-0---------------|");
		for (int i = 0; i < a.size(); i++) {
			b.add(a.get(i));
		}
		b.add(" ");
		c.add(b);
		d = GuitarParser.tabToCollection(a);
		
		assertEquals(c.get(0).get(0), d.get(0).get(0));
		
	}
	
	@Test
	public void test_collectionToMeasure_01() {
		ArrayList<String> a = new ArrayList<>();
		ArrayList<String> b = new ArrayList<>();
		ArrayList<ArrayList<String>> c = new ArrayList<>();
		ArrayList<ArrayList<String>> d = new ArrayList<>();
		a.add("|-----------0-----|-0---------------|");
		a.add("|---------0---0---|-0---------------|");
		a.add("|-------1-------1-|-1---------------|");
		a.add("|-----2-----------|-2---------------|");
		a.add("|---2-------------|-2---------------|");
		a.add("|-0---------------|-0---------------|");
		b.add("-----------0-----");
		b.add("---------0---0---");
		b.add("-------1-------1-");
		b.add("-----2-----------");
		b.add("---2-------------");
		b.add("-0---------------");
		c.add(b);
		b = new ArrayList<String>();
		b.add("-0---------------");
		b.add("-0---------------");
		b.add("-1---------------");
		b.add("-2---------------");
		b.add("-2---------------");
		b.add("-0---------------");
		c.add(b);
		d = GuitarParser.collectionToMeasure(a);
		
		for (int i = 0; i < c.size(); i++) {
			for (int j = 0; j < 6; j++) {
				assertEquals(c.get(i).get(j), d.get(i).get(j));
			}
			
		}
		assertTrue(c.equals(d));
	}
	
	@Test
	public void test_collectionToMeasure_02() {
		ArrayList<String> a = new ArrayList<>();
		ArrayList<String> b = new ArrayList<>();
		ArrayList<ArrayList<String>> c = new ArrayList<>();
		ArrayList<ArrayList<String>> d = new ArrayList<>();
		a.add("|-----------0-----||----------0--------4|");
		a.add("|---------0---0---||----------0--------||");
		a.add("|-------1-------1-||*---------1-------*||");
		a.add("|-----2-----------||*---------2-------*||");
		a.add("|---2-------------||------2---2--------||");
		a.add("|-0---------------||--0-------0--------||");
		b.add("-----------0-----");
		b.add("---------0---0---");
		b.add("-------1-------1-");
		b.add("-----2-----------");
		b.add("---2-------------");
		b.add("-0---------------");
		c.add(b);
		b = new ArrayList<String>();
		b.add("|----------0--------4");
		b.add("|----------0--------|");
		b.add("|*---------1-------*|");
		b.add("|*---------2-------*|");
		b.add("|------2---2--------|");
		b.add("|--0-------0--------|");
		c.add(b);
		d = GuitarParser.collectionToMeasure(a);
		
		for (int i = 0; i < c.size(); i++) {
			for (int j = 0; j < 6; j++) {
				assertEquals(c.get(i).get(j), d.get(i).get(j));
			}
			
		}
		assertTrue(c.equals(d));
	}
	
	@Test
	public void test_collectionToMeasure_03() {
		ArrayList<String> a = new ArrayList<>();
		ArrayList<String> b = new ArrayList<>();
		ArrayList<ArrayList<String>> c = new ArrayList<>();
		ArrayList<ArrayList<String>> d = new ArrayList<>();
		a.add("|-----------0-----||----------0--------||");
		a.add("|---------0---0---||----------0--------||");
		a.add("|-------1-------1-||*---------1-------*||");
		a.add("|-----2-----------||*---------2-------*||");
		a.add("|---2-------------||------2---2--------||");
		a.add("|-0---------------||--0-------0--------||");
		b.add("-----------0-----");
		b.add("---------0---0---");
		b.add("-------1-------1-");
		b.add("-----2-----------");
		b.add("---2-------------");
		b.add("-0---------------");
		c.add(b);
		b = new ArrayList<String>();
		b.add("|----------0--------|");
		b.add("|----------0--------|");
		b.add("|*---------1-------*|");
		b.add("|*---------2-------*|");
		b.add("|------2---2--------|");
		b.add("|--0-------0--------|");
		c.add(b);
		d = GuitarParser.collectionToMeasure(a);
		
		for (int i = 0; i < c.size(); i++) {
			for (int j = 0; j < 6; j++) {
				assertEquals(c.get(i).get(j), d.get(i).get(j));
			}
			
		}
		assertTrue(c.equals(d));
	}
/*******************************************************************/

	
}
