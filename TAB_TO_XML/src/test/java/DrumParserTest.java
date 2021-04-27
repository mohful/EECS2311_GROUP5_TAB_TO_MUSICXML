import static org.junit.jupiter.api.Assertions.*;

import java.util.ArrayList;

import org.junit.jupiter.api.Test;

import TAB_TO_XML.DParser;

class DrumParserTest {
	
	/*
	 * This method is executed before each test method is executed.
	 */
	
	@Test
	public void test_durationCount_01() {
		ArrayList<String> a = new ArrayList<>();
		a.add("x---------------");
		a.add("--x-x-x-x-x-x-x-");
		a.add("----o-------o---");
		a.add("----------------");
		a.add("----------------");
		a.add("o-------o-------");
		int b = DParser.durationCount(a, 0);
		int c = 2;
		assertEquals(b, c);
		
	}
	
	@Test
	public void test_durationCount_02() {
		ArrayList<String> a = new ArrayList<>();
		a.add("--------x-------");
		a.add("----------------");
		a.add("oooo------------");
		a.add("----oo----------");
		a.add("------oo--------");
		a.add("o-------o-------");
		int b = DParser.durationCount(a, 2);
		int c = 1;
		assertEquals(b, c);
		
	}
	
	/*******************************************************************/
	
	@Test
	public void test_durationCountLastLine_01() {
		ArrayList<String> a = new ArrayList<>();
		a.add("x---------------");
		a.add("--x-x-x-x-x-x-x-");
		a.add("----o-------o---");
		a.add("----------------");
		a.add("----------------");
		a.add("o-------o-------");
		int b = DParser.durationCountLastLine(a, 8);
		int c = 8;
		assertEquals(b, c);
		
	}
	
	@Test
	public void test_durationCountLastLine_02() {
		ArrayList<String> a = new ArrayList<>();
		a.add("--------x-------");
		a.add("----------------");
		a.add("oooo------------");
		a.add("----oo----------");
		a.add("------oo--------");
		a.add("o-------o-------");
		int b = DParser.durationCountLastLine(a, 0);
		int c = 8;
		assertEquals(b, c);
		
	}
	
	/*******************************************************************/
	
	@Test
	public void test_divisionCount_01() {
		ArrayList<String> a = new ArrayList<>();
		a.add("x---------------");
		a.add("--x-x-x-x-x-x-x-");
		a.add("----o-------o---");
		a.add("----------------");
		a.add("----------------");
		a.add("o-------o-------");
		int b = DParser.divisionCount(a.get(2), 4);
		int c = 4;
		assertEquals(b, c);
		
	}
	
	@Test
	public void test_divisionCount_02() {
		ArrayList<String> a = new ArrayList<>();
		a.add("--------x-------");
		a.add("----------------");
		a.add("oooo------------");
		a.add("----oo----------");
		a.add("------oo--------");
		a.add("o-------o-------");
		int b = DParser.divisionCount(a.get(2), 2);
		int c = 8;
		assertEquals(b, c);
		
	}
	
	/*******************************************************************/
	
	@Test
	public void test_collectionToMeasureExtractID_01() {
		ArrayList<String> a = new ArrayList<>();
		ArrayList<ArrayList<String>> b = new ArrayList<>();
		a.add("C |x--------x--------|---------x--x-----|x--------x--------|---------x--x-----|");
		a.add("R |------x-----x--x--|x--x-xx-----------|---x-xx-----x-xx--|x--x-xx-----------|");
		a.add("SD|---------o--------|---------o--o-----|---------o--------|---------o--o-----|");
		a.add("FT|------------------|--------------o-o-|------------------|--------------oo--|");
		a.add("B |o----------------o|o----------o------|o-----------------|o----------oo-----|");
		b = DParser.collectionToMeasureExtractID(a);
		String d = b.get(0).get(1); // R 
		String c = "R ";
		assertEquals(d, c);
	}
	
	@Test
	public void test_collectionToMeasureExtractID_02() {
		ArrayList<String> a = new ArrayList<>();
		ArrayList<ArrayList<String>> b = new ArrayList<>();
		a.add("C |x--------x--------|---------x--x-----|x--------x--------|---------x--x-----|");
		a.add("R |------x-----x--x--|x--x-xx-----------|---x-xx-----x-xx--|x--x-xx-----------|");
		a.add("T |-----------oo-----|------------oo----|------------------|------------------|");
		a.add("SD|---------o--------|---------o--o-----|---------o--------|---------o--o-----|");
		a.add("FT|------------------|--------------o-o-|------------------|--------------oo--|");
		a.add("B |o----------------o|o----------o------|o-----------------|o----------oo-----|");
		b = DParser.collectionToMeasureExtractID(a);
		String d = b.get(0).get(2); // T 
		String c = "T ";
		assertEquals(d, c);
	}
	
	/*******************************************************************/
	
	@Test
	public void test_tabtoCollection_01() {
		ArrayList<String> a = new ArrayList<>();
		ArrayList<String> c = new ArrayList<>();
		ArrayList<ArrayList<String>> b = new ArrayList<>();
		a.add("C |x--------x--------|---------x--x-----|x--------x--------|---------x--x-----|");
		a.add("R |------x-----x--x--|x--x-xx-----------|---x-xx-----x-xx--|x--x-xx-----------|");
		a.add("SD|---------o--------|---------o--o-----|---------o--------|---------o--o-----|");
		a.add("FT|------------------|--------------o-o-|------------------|--------------oo--|");
		a.add("B |o----------------o|o----------o------|o-----------------|o----------oo-----|");
		a.add("");
		a.add("C |x--------x--------|---------x--x-----|x--------x--------|---------x--x-----|");
		a.add("R |------x-----x--x--|x--x-xx-----------|---x-xx-----x-xx--|x--x-xx-----------|");
		a.add("T |-----------oo-----|------------oo----|------------------|------------------|");
		a.add("SD|---------o--------|---------o--o-----|---------o--------|---------o--o-----|");
		a.add("FT|------------------|--------------o-o-|------------------|--------------oo--|");
		a.add("B |o----------------o|o----------o------|o-----------------|o----------oo-----|");
		b = DParser.tabToCollection(a);
		c.add("C |x--------x--------|---------x--x-----|x--------x--------|---------x--x-----|");
		c.add("R |------x-----x--x--|x--x-xx-----------|---x-xx-----x-xx--|x--x-xx-----------|");
		c.add("SD|---------o--------|---------o--o-----|---------o--------|---------o--o-----|");
		c.add("FT|------------------|--------------o-o-|------------------|--------------oo--|");
		c.add("B |o----------------o|o----------o------|o-----------------|o----------oo-----|");
		assertEquals(b.get(0).get(4), c.get(4));
	}
	
	@Test
	public void test_tabtoCollection_02() {
		ArrayList<String> a = new ArrayList<>();
		ArrayList<String> c = new ArrayList<>();
		ArrayList<ArrayList<String>> b = new ArrayList<>();
		a.add("C |x--------x--------|---------x--x-----|x--------x--------|---------x--x-----|");
		a.add("R |------x-----x--x--|x--x-xx-----------|---x-xx-----x-xx--|x--x-xx-----------|");
		a.add("T |-----------oo-----|------------oo----|------------------|------------------|");
		a.add("SD|---------o--------|---------o--o-----|---------o--------|---------o--o-----|");
		a.add("FT|------------------|--------------o-o-|------------------|--------------oo--|");
		a.add("B |o----------------o|o----------o------|o-----------------|o----------oo-----|");
		a.add("");
		a.add("C |x--------x--------|---------x--x-----|x--------x--------|---------x--x-----|");
		a.add("R |------x-----x--x--|x--x-xx-----------|---x-xx-----x-xx--|x--x-xx-----------|");
		a.add("T |-----------oo-----|------------oo----|------------------|------------------|");
		a.add("SD|---------o--------|---------o--o-----|---------o--------|---------o--o-----|");
		a.add("FT|------------------|--------------o-o-|------------------|--------------oo--|");
		a.add("B |o----------------o|o----------o------|o-----------------|o----------oo-----|");
		a.add("");
		a.add("C |xx--------------|----------------|");
		a.add("HH|----x-x-x-x-x-x-|x-x-x-x-xox-x-x-|");
		a.add("T |----------------|----------------|");
		a.add("SD|----------------|----------------|");
		a.add("B |oo--------------|----------------|");
		b = DParser.tabToCollection(a);
		c.add("C |xx--------------|----------------|");
		c.add("HH|----x-x-x-x-x-x-|x-x-x-x-xox-x-x-|");
		c.add("T |----------------|----------------|");
		c.add("SD|----------------|----------------|");
		c.add("B |oo--------------|----------------|");
		assertEquals(b.get(2).get(1), c.get(1));
	}
	
	/*******************************************************************/
	
	@Test
	public void test_typeDeclare_01() {
		ArrayList<String> a = new ArrayList<>();
		a.add("--------x-------");
		a.add("----------------");
		a.add("oooo------------");
		a.add("----oo----------");
		a.add("------oo--------");
		a.add("o-------o-------");
		int b = DParser.durationCount(a, 0);
		String d = DParser.typeDeclare(b, a.get(0).length());
		String c = "16th";
		assertEquals(d, c);
	}
	
	@Test
	public void test_typeDeclare_02() {
		ArrayList<String> a = new ArrayList<>();
		a.add("----------------");
		a.add("x-x-x-x-xox-x-x-");
		a.add("----------------");
		a.add("----------------");
		a.add("----------------");
		int b = DParser.durationCount(a, 4);
		String d = DParser.typeDeclare(b, a.get(0).length());
		String c = "eighth";
		assertEquals(d, c);
	}
	
	/*******************************************************************/
	
	@Test
	public void test_typeDeclareGrace_01() {
		ArrayList<String> a = new ArrayList<>();
		a.add("--------x-------");
		a.add("----------------");
		a.add("oooo------------");
		a.add("----oo----------");
		a.add("------oo--------");
		a.add("o-------o-------");
		int b = DParser.durationCount(a, 8);
		String d = DParser.typeDeclare(b, a.get(0).length());
		String e = DParser.typeDeclareGrace(d);
		String c = "quarter";
		assertEquals(e, c);
	}
	
	@Test
	public void test_typeDeclareGrace_02() {
		ArrayList<String> a = new ArrayList<>();
		a.add("----------------");
		a.add("x-x-x-x-xox-x-x-");
		a.add("----------------");
		a.add("----------------");
		a.add("----------------");
		int b = DParser.durationCount(a, 4);
		String d = DParser.typeDeclare(b, a.get(0).length());
		String e = DParser.typeDeclareGrace(d);
		String c = "16th";
		assertEquals(e, c);
	}
	
	/*******************************************************************/
	
	@Test
	public void test_typeDeclareTwoGrace_01() {
		ArrayList<String> a = new ArrayList<>();
		a.add("--------x-------");
		a.add("----------------");
		a.add("oooo------------");
		a.add("----oo----------");
		a.add("------oo--------");
		a.add("o-------o-------");
		int b = DParser.durationCount(a, 8);
		String d = DParser.typeDeclare(b, a.get(0).length());
		String e = DParser.typeDeclareTwoGrace(d);
		String c = "eighth";
		assertEquals(e, c);
	}
	
	@Test
	public void test_typeDeclareTwoGrace_02() {
		ArrayList<String> a = new ArrayList<>();
		a.add("----------------");
		a.add("x-x-x-x-x-----x-");
		a.add("----------------");
		a.add("----------------");
		a.add("----------------");
		int b = DParser.durationCount(a, 8);
		String d = DParser.typeDeclare(b, a.get(0).length());
		System.out.println(d);
		String e = DParser.typeDeclareTwoGrace(d);
		String c = "16th";
		assertEquals(e, c);
	}
	
	/*******************************************************************/
	
	@Test
	public void test_octaveCount_01() {
		ArrayList<String> a = new ArrayList<>();
		ArrayList<ArrayList<String>> b = new ArrayList<>();
		a.add("C |x--------x--------|---------x--x-----|x--------x--------|---------x--x-----|");
		a.add("R |------x-----x--x--|x--x-xx-----------|---x-xx-----x-xx--|x--x-xx-----------|");
		a.add("SD|---------o--------|---------o--o-----|---------o--------|---------o--o-----|");
		a.add("FT|------------------|--------------o-o-|------------------|--------------oo--|");
		a.add("B |o----------------o|o----------o------|o-----------------|o----------oo-----|");
		b = DParser.collectionToMeasureExtractID(a);
		String d = b.get(0).get(2); //SD
		d = DParser.octaveCount(d);
		String c = "5";
		assertEquals(d, c);
	}
	
	@Test
	public void test_octaveCount_02() {
		ArrayList<String> a = new ArrayList<>();
		ArrayList<ArrayList<String>> b = new ArrayList<>();
		a.add("C |x--------x--------|---------x--x-----|x--------x--------|---------x--x-----|");
		a.add("R |------x-----x--x--|x--x-xx-----------|---x-xx-----x-xx--|x--x-xx-----------|");
		a.add("T |-----------oo-----|------------oo----|------------------|------------------|");
		a.add("SD|---------o--------|---------o--o-----|---------o--------|---------o--o-----|");
		a.add("FT|------------------|--------------o-o-|------------------|--------------oo--|");
		a.add("B |o----------------o|o----------o------|o-----------------|o----------oo-----|");
		b = DParser.collectionToMeasureExtractID(a);
		String d = b.get(0).get(4);
		d = DParser.octaveCount(d);
		String c = "4";
		assertEquals(d, c);
	}
	
	/*******************************************************************/
	
	@Test
	public void test_stepCount_01() {
		ArrayList<String> a = new ArrayList<>();
		ArrayList<ArrayList<String>> b = new ArrayList<>();
		a.add("C |x--------x--------|---------x--x-----|x--------x--------|---------x--x-----|");
		a.add("R |------x-----x--x--|x--x-xx-----------|---x-xx-----x-xx--|x--x-xx-----------|");
		a.add("SD|---------o--------|---------o--o-----|---------o--------|---------o--o-----|");
		a.add("FT|------------------|--------------o-o-|------------------|--------------oo--|");
		a.add("B |o----------------o|o----------o------|o-----------------|o----------oo-----|");
		b = DParser.collectionToMeasureExtractID(a);
		String d = b.get(0).get(1); // R
		d = DParser.stepCount(d);
		String c = "F";
		assertEquals(d, c);
	}
	
	@Test
	public void test_stepCount_02() {
		ArrayList<String> a = new ArrayList<>();
		ArrayList<ArrayList<String>> b = new ArrayList<>();
		a.add("C |x--------x--------|---------x--x-----|x--------x--------|---------x--x-----|");
		a.add("R |------x-----x--x--|x--x-xx-----------|---x-xx-----x-xx--|x--x-xx-----------|");
		a.add("T |-----------oo-----|------------oo----|------------------|------------------|");
		a.add("SD|---------o--------|---------o--o-----|---------o--------|---------o--o-----|");
		a.add("FT|------------------|--------------o-o-|------------------|--------------oo--|");
		a.add("B |o----------------o|o----------o------|o-----------------|o----------oo-----|");
		b = DParser.collectionToMeasureExtractID(a);
		String d = b.get(0).get(2); // T 
		d = DParser.stepCount(d);
		String c = "E";
		assertEquals(d, c);
	}
	
	/*******************************************************************/
	
	@Test
	public void test_beamState_01() {
		ArrayList<String> a = new ArrayList<>();
		a.add("--------x-------");
		a.add("----------------");
		a.add("oooo------------");
		a.add("----oo----------");
		a.add("------oo--------");
		a.add("o-------o-------");
		int b = DParser.durationCount(a, 0);
		String d = DParser.typeDeclare(b, a.get(0).length());
		String e = DParser.beamState(a, d, 2, 0, 0);
		String c = "begin";
		assertEquals(e, c);
	}
	
	@Test
	public void test_beamState_02() {
		ArrayList<String> a = new ArrayList<>();
		a.add("----------------");
		a.add("x-x-x-x-x-----x-");
		a.add("----------------");
		a.add("----------------");
		a.add("----------------");
		int b = DParser.durationCount(a, 2);
		String d = DParser.typeDeclare(b, a.get(0).length());
		String e = DParser.beamState(a, d, 1, 2, 1);
		String c = "continue";
		assertEquals(e, c);
	}
	
	/*******************************************************************/
	
	@Test
	public void test_countBeam_01() {
		int a = 2;
		a = DParser.countBeam("begin", a);
		int b = 1;
		assertEquals(a, b);
	}
	
	@Test
	public void test_countBeam_02() {
		int a = 2;
		a = DParser.countBeam("end", a);
		int b = 4;
		assertEquals(a, b);
	}
	
	@Test
	public void test_countBeam_03() {
		int a = 2;
		a = DParser.countBeam("continue", a);
		int b = 3;
		assertEquals(a, b);
	}
	
	@Test
	public void test_countBeam_04() {
		int a = 3;
		a = DParser.countBeam("continue", a);
		int b = 4;
		assertEquals(a, b);
	}
	
	/*******************************************************************/
	
	@Test
	public void test_beamNumber_01() {
		ArrayList<String> a = new ArrayList<>();
		a.add("--------x-------");
		a.add("----------------");
		a.add("oooo------------");
		a.add("----oo----------");
		a.add("------oo--------");
		a.add("o-------o-------");
		int b = DParser.durationCount(a, 0);
		String d = DParser.typeDeclare(b, a.get(0).length());
		b = DParser.beamNumber(d);
		int c = 2;
		assertEquals(b, c);
	}
	
	@Test
	public void test_beamNumber_02() {
		ArrayList<String> a = new ArrayList<>();
		a.add("----------------");
		a.add("x-x-x-x-xox-x-x-");
		a.add("----------------");
		a.add("----------------");
		a.add("----------------");
		int b = DParser.durationCount(a, 4);
		String d = DParser.typeDeclare(b, a.get(0).length());
		b = DParser.beamNumber(d);
		int c = 1;
		assertEquals(b, c);
	}
	
	/*******************************************************************/
	
	@Test
	public void test_collectionToMeasure_01() {
		ArrayList<String> a = new ArrayList<>();
		ArrayList<ArrayList<String>> b = new ArrayList<>();
		ArrayList<String> c = new ArrayList<>();
		a.add("C |x--------x--------|---------x--x-----|x--------x--------|---------x--x-----|");
		a.add("R |------x-----x--x--|x--x-xx-----------|---x-xx-----x-xx--|x--x-xx-----------|");
		a.add("SD|---------o--------|---------o--o-----|---------o--------|---------o--o-----|");
		a.add("FT|------------------|--------------o-o-|------------------|--------------oo--|");
		a.add("B |o----------------o|o----------o------|o-----------------|o----------oo-----|");
		b = DParser.collectionToMeasure(a);
		c.add("---------x--x-----");
		c.add("x--x-xx-----------");
		c.add("---------o--o-----");
		c.add("--------------oo--");
		c.add("o----------oo-----");
		assertEquals(b.get(3).get(3), c.get(3));
	}
	
	@Test
	public void test_collectionToMeasure_02() {
		ArrayList<String> a = new ArrayList<>();
		ArrayList<ArrayList<String>> b = new ArrayList<>();
		ArrayList<String> c = new ArrayList<>();
		a.add("C |x--------x--------|---------x--x-----|x--------x--------|---------x--x-----|");
		a.add("R |------x-----x--x--|x--x-xx-----------|---x-xx-----x-xx--|x--x-xx-----------|");
		a.add("T |-----------oo-----|------------oo----|------------------|------------------|");
		a.add("SD|---------o--------|---------o--o-----|---------o--------|---------o--o-----|");
		a.add("FT|------------------|--------------o-o-|------------------|--------------oo--|");
		a.add("B |o----------------o|o----------o------|o-----------------|o----------oo-----|");
		b = DParser.collectionToMeasure(a);
		c.add("---------x--x-----");
		c.add("x--x-xx-----------");
		c.add("------------oo----");
		c.add("---------o--o-----");
		c.add("--------------o-o-");
		c.add("o----------o------");
		assertEquals(b.get(1).get(3), c.get(3));
	}
	
	/*******************************************************************/
	
	@Test
	public void test_collectionToMeasureRepeatedMeasure_01() {
		ArrayList<String> a = new ArrayList<>();
		ArrayList<Integer> c = new ArrayList<>();
		a.add("  |----REPEAT-7X---|");
		a.add("C |x---------------|");
		a.add("HH|--x-x-x-x-x-x-x-|");
		a.add("SD|----o----o--o---|");
		a.add("B |o--o----o--o--o-|");
		c = DParser.collectionToMeasureRepeatedMeasure(a);
		int b = 1;
		assertEquals(b, c.get(0));
	}
	
	@Test
	public void test_collectionToMeasureRepeatedMeasure_02() {
		ArrayList<String> a = new ArrayList<>();
		ArrayList<Integer> c = new ArrayList<>();
		a.add("  |------------REPEAT-7X------------|");
		a.add("C |xx--------------|----------------|xx--------------|----------------|");
		a.add("HH|----x-x-x-x-x-x-|x-x-x-x-xox-x-x-|----x-x-x-x-x-x-|----------x-x-x-|");
		a.add("T |----------------|----------------|----------------|--o-------------|");
		a.add("SD|----------------|----------------|----------------|o----o--f-------|");
		a.add("B |oo--------------|----------------|oo--------------|-------o-o-oo-o-|");
		c = DParser.collectionToMeasureRepeatedMeasure(a);
		int b = 1;
		assertEquals(b, c.get(0));
	}
	
	/*******************************************************************/
	
	@Test
	public void test_collectionToMeasureRepeatedAmount_01() {
		ArrayList<String> a = new ArrayList<>();
		ArrayList<Integer> c = new ArrayList<>();
		a.add("  |----REPEAT-7X---|");
		a.add("C |x---------------|");
		a.add("HH|--x-x-x-x-x-x-x-|");
		a.add("SD|----o----o--o---|");
		a.add("B |o--o----o--o--o-|");
		c = DParser.collectionToMeasureRepeatedAmount(a);
		int b = 7;
		assertEquals(b, c.get(0));
	}
	
	@Test
	public void test_collectionToMeasureRepeatedAmount_02() {
		ArrayList<String> a = new ArrayList<>();
		ArrayList<Integer> c = new ArrayList<>();
		a.add("  |------------REPEAT-7X------------|");
		a.add("C |xx--------------|----------------|xx--------------|----------------|");
		a.add("HH|----x-x-x-x-x-x-|x-x-x-x-xox-x-x-|----x-x-x-x-x-x-|----------x-x-x-|");
		a.add("T |----------------|----------------|----------------|--o-------------|");
		a.add("SD|----------------|----------------|----------------|o----o--f-------|");
		a.add("B |oo--------------|----------------|oo--------------|-------o-o-oo-o-|");
		c = DParser.collectionToMeasureRepeatedAmount(a);
		int b = 7;
		assertEquals(b, c.get(0));
	}
	
	/*******************************************************************/
	
	@Test
	public void test_measureSize_01() {
		ArrayList<String> a = new ArrayList<>();
		int c;
		a.add("C |x--------x--------|---------x--x-----|x--------x--------|---------x--x-----|");
		a.add("R |------x-----x--x--|x--x-xx-----------|---x-xx-----x-xx--|x--x-xx-----------|");
		a.add("SD|---------o--------|---------o--o-----|---------o--------|---------o--o-----|");
		a.add("FT|------------------|--------------o-o-|------------------|--------------oo--|");
		a.add("B |o----------------o|o----------o------|o-----------------|o----------oo-----|");
		c = DParser.measureSize(a.get(0), 3);
		int b = 19;
		assertEquals(b, c);
	}
	
	@Test
	public void test_measureSize_02() {
		ArrayList<String> a = new ArrayList<>();
		int c;
		a.add("  |------------REPEAT-7X------------|");
		a.add("C |xx--------------|----------------|xx--------------|----------------|");
		a.add("HH|----x-x-x-x-x-x-|x-x-x-x-xox-x-x-|----x-x-x-x-x-x-|----------x-x-x-|");
		a.add("T |----------------|----------------|----------------|--o-------------|");
		a.add("SD|----------------|----------------|----------------|o----o--f-------|");
		a.add("B |oo--------------|----------------|oo--------------|-------o-o-oo-o-|");
		c = DParser.measureSize(a.get(1), 20);
		int b = 17;
		assertEquals(b, c);
	}
	
	/*******************************************************************/
	
	@Test
	public void test_augInput_01() {
		ArrayList<String> a = new ArrayList<>();
		ArrayList<String> c = new ArrayList<>();
		a.add("C |x--------x--------|---------x--x-----|x--------x--------|---------x--x-----|");
		a.add("R |------x-----x--x--|x--x-xx-----------|---x-xx-----x-xx--|x--x-xx-----------|");
		a.add("SD|---------o--------|---------o--o-----|---------o--------|---------o--o-----|");
		a.add("FT|------------------|--------------o-o-|------------------|--------------oo--|");
		a.add("B |o----------------o|o----------o------|o-----------------|o----------oo-----|");
		c.add("|x--------x--------|---------x--x-----|x--------x--------|---------x--x-----|");
		c.add("|------x-----x--x--|x--x-xx-----------|---x-xx-----x-xx--|x--x-xx-----------|");
		c.add("|---------o--------|---------o--o-----|---------o--------|---------o--o-----|");
		c.add("|------------------|--------------o-o-|------------------|--------------oo--|");
		c.add("|o----------------o|o----------o------|o-----------------|o----------oo-----|");
		a = DParser.augInput(a);
		assertEquals(a.get(0), c.get(0));
	}
	
	@Test
	public void test_augInput_02() {
		ArrayList<String> a = new ArrayList<>();
		ArrayList<String> c = new ArrayList<>();
		a.add("  |------------REPEAT-7X------------|");
		a.add("C |xx--------------|----------------|xx--------------|----------------|");
		a.add("HH|----x-x-x-x-x-x-|x-x-x-x-xox-x-x-|----x-x-x-x-x-x-|----------x-x-x-|");
		a.add("T |----------------|----------------|----------------|--o-------------|");
		a.add("SD|----------------|----------------|----------------|o----o--f-------|");
		a.add("B |oo--------------|----------------|oo--------------|-------o-o-oo-o-|");
		c.add("|xx--------------|----------------|xx--------------|----------------|");
		c.add("|----x-x-x-x-x-x-|x-x-x-x-xox-x-x-|----x-x-x-x-x-x-|----------x-x-x-|");
		c.add("|----------------|----------------|----------------|--o-------------|");
		c.add("|----------------|----------------|----------------|o----o--f-------|");
		c.add("|oo--------------|----------------|oo--------------|-------o-o-oo-o-|");
		a = DParser.augInput(a);
		assertEquals(a.get(0), c.get(0));
	}
	
	/*******************************************************************/

}
