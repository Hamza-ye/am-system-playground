package org.nmcpye.activitiesmanagement.extended.period;

import org.junit.jupiter.api.Test;
import org.nmcpye.activitiesmanagement.AMTest;
import org.nmcpye.activitiesmanagement.IntegrationTest;
import org.nmcpye.activitiesmanagement.domain.period.MonthlyPeriodType;
import org.nmcpye.activitiesmanagement.domain.period.Period;
import org.nmcpye.activitiesmanagement.domain.period.PeriodType;
import org.nmcpye.activitiesmanagement.domain.period.YearlyPeriodType;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@IntegrationTest
class PeriodStoreTest extends AMTest {

    @Autowired
    private PeriodStore periodStore;

    // -------------------------------------------------------------------------
    // Period
    // -------------------------------------------------------------------------

    @Test
    void testAddPeriod() {
        List<PeriodType> periodTypes = periodStore.getAllPeriodTypes();
        Iterator<PeriodType> it = periodTypes.iterator();
        PeriodType periodTypeA = it.next();
        PeriodType periodTypeB = it.next();

        Period periodA = new Period(periodTypeA, getDay(1), getDay(2));
        Period periodB = new Period(periodTypeA, getDay(2), getDay(3));
        Period periodC = new Period(periodTypeB, getDay(2), getDay(3));
        periodStore.saveObject(periodA);
        Long idA = periodA.getId();
        periodStore.saveObject(periodB);
        Long idB = periodB.getId();
        periodStore.saveObject(periodC);
        Long idC = periodC.getId();

        periodA = periodStore.get(idA);
        assertNotNull(periodA);
        assertEquals(idA, periodA.getId());
        assertEquals(periodTypeA, periodA.getPeriodType());
        assertEquals(getDay(1), periodA.getStartDate());
        assertEquals(getDay(2), periodA.getEndDate());

        periodB = periodStore.get(idB);
        assertNotNull(periodB);
        assertEquals(idB, periodB.getId());
        assertEquals(periodTypeA, periodB.getPeriodType());
        assertEquals(getDay(2), periodB.getStartDate());
        assertEquals(getDay(3), periodB.getEndDate());

        periodC = periodStore.get(idC);
        assertNotNull(periodC);
        assertEquals(idC, periodC.getId());
        assertEquals(periodTypeB, periodC.getPeriodType());
        assertEquals(getDay(2), periodC.getStartDate());
        assertEquals(getDay(3), periodC.getEndDate());
    }

    @Test
    void testDeleteAndGetPeriod() {
        List<PeriodType> periodTypes = periodStore.getAllPeriodTypes();
        Iterator<PeriodType> it = periodTypes.iterator();
        PeriodType periodTypeA = it.next();
        PeriodType periodTypeB = it.next();

        Period periodA = new Period(periodTypeA, getDay(1), getDay(2));
        Period periodB = new Period(periodTypeA, getDay(2), getDay(3));
        Period periodC = new Period(periodTypeB, getDay(2), getDay(3));
        Period periodD = new Period(periodTypeB, getDay(3), getDay(4));
        periodStore.saveObject(periodA);
        Long idA = periodA.getId();
        periodStore.saveObject(periodB);
        Long idB = periodB.getId();
        periodStore.saveObject(periodC);
        Long idC = periodC.getId();
        periodStore.saveObject(periodD);
        Long idD = periodD.getId();

        assertNotNull(periodStore.get(idA));
        assertNotNull(periodStore.get(idB));
        assertNotNull(periodStore.get(idC));
        assertNotNull(periodStore.get(idD));

        periodStore.delete(periodA);
        assertNull(periodStore.get(idA));
        assertNotNull(periodStore.get(idB));
        assertNotNull(periodStore.get(idC));
        assertNotNull(periodStore.get(idD));

        periodStore.delete(periodB);
        assertNull(periodStore.get(idA));
        assertNull(periodStore.get(idB));
        assertNotNull(periodStore.get(idC));
        assertNotNull(periodStore.get(idD));

        periodStore.delete(periodC);
        assertNull(periodStore.get(idA));
        assertNull(periodStore.get(idB));
        assertNull(periodStore.get(idC));
        assertNotNull(periodStore.get(idD));

        periodStore.delete(periodD);
        assertNull(periodStore.get(idA));
        assertNull(periodStore.get(idB));
        assertNull(periodStore.get(idC));
        assertNull(periodStore.get(idD));
    }

    @Test
    void testGetPeriod() {
        List<PeriodType> periodTypes = periodStore.getAllPeriodTypes();
        Iterator<PeriodType> it = periodTypes.iterator();
        PeriodType periodTypeA = it.next();
        PeriodType periodTypeB = it.next();

        Period periodA = new Period(periodTypeA, getDay(1), getDay(2));
        Period periodB = new Period(periodTypeA, getDay(2), getDay(3));
        Period periodC = new Period(periodTypeB, getDay(2), getDay(3));
        Period periodD = new Period(periodTypeB, getDay(3), getDay(4));
        Period periodE = new Period(periodTypeA, getDay(3), getDay(4));
        periodStore.saveObject(periodA);
        Long idA = periodA.getId();
        periodStore.saveObject(periodB);
        Long idB = periodB.getId();
        periodStore.saveObject(periodC);
        Long idC = periodC.getId();
        periodStore.saveObject(periodD);
        Long idD = periodD.getId();
        periodStore.saveObject(periodE);
        Long idE = periodE.getId();

        periodA = periodStore.getPeriod(getDay(1), getDay(2), periodTypeA);
        assertNotNull(periodA);
        assertEquals(idA, periodA.getId());
        assertEquals(periodTypeA, periodA.getPeriodType());
        assertEquals(getDay(1), periodA.getStartDate());
        assertEquals(getDay(2), periodA.getEndDate());

        periodB = periodStore.getPeriod(getDay(2), getDay(3), periodTypeA);
        assertNotNull(periodB);
        assertEquals(idB, periodB.getId());
        assertEquals(periodTypeA, periodB.getPeriodType());
        assertEquals(getDay(2), periodB.getStartDate());
        assertEquals(getDay(3), periodB.getEndDate());

        periodC = periodStore.getPeriod(getDay(2), getDay(3), periodTypeB);
        assertNotNull(periodC);
        assertEquals(idC, periodC.getId());
        assertEquals(periodTypeB, periodC.getPeriodType());
        assertEquals(getDay(2), periodC.getStartDate());
        assertEquals(getDay(3), periodC.getEndDate());

        periodD = periodStore.getPeriod(getDay(3), getDay(4), periodTypeB);
        assertNotNull(periodD);
        assertEquals(idD, periodD.getId());
        assertEquals(periodTypeB, periodD.getPeriodType());
        assertEquals(getDay(3), periodD.getStartDate());
        assertEquals(getDay(4), periodD.getEndDate());

        periodE = periodStore.getPeriod(getDay(3), getDay(4), periodTypeA);
        assertNotNull(periodE);
        assertEquals(idE, periodE.getId());
        assertEquals(periodTypeA, periodE.getPeriodType());
        assertEquals(getDay(3), periodE.getStartDate());
        assertEquals(getDay(4), periodE.getEndDate());

        assertNull(periodStore.getPeriod(getDay(1), getDay(2), periodTypeB));
        assertNull(periodStore.getPeriod(getDay(1), getDay(3), periodTypeA));
        assertNull(periodStore.getPeriod(getDay(1), getDay(5), periodTypeB));
        assertNull(periodStore.getPeriod(getDay(4), getDay(3), periodTypeB));
        assertNull(periodStore.getPeriod(getDay(5), getDay(6), periodTypeA));
    }

    //    @Disabled("Disabled until knowing why the null pointer!")
    @Test
    void testGetAllPeriods() {
        PeriodType periodType = periodStore.getAllPeriodTypes().iterator().next();

        Period periodA = new Period(periodType, getDay(1), getDay(1));
        Period periodB = new Period(periodType, getDay(1), getDay(2));
        Period periodC = new Period(periodType, getDay(2), getDay(3));

        periodStore.saveObject(periodA);
        periodStore.saveObject(periodB);
        periodStore.saveObject(periodC);

        List<Period> periods = periodStore.getAll();

        assertNotNull(periods);
        assertEquals(3, periods.size());
        assertTrue(periods.contains(periodA));
        assertTrue(periods.contains(periodB));
        assertTrue(periods.contains(periodC));
    }

    @Test
    void testGetPeriodsBetweenDates() {
        List<PeriodType> periodTypes = periodStore.getAllPeriodTypes();
        Iterator<PeriodType> it = periodTypes.iterator();
        PeriodType periodTypeA = it.next();
        PeriodType periodTypeB = it.next();

        Period periodA = new Period(periodTypeA, getDay(1), getDay(2));
        Period periodB = new Period(periodTypeA, getDay(2), getDay(3));
        Period periodC = new Period(periodTypeB, getDay(2), getDay(3));
        Period periodD = new Period(periodTypeB, getDay(3), getDay(4));
        periodStore.saveObject(periodA);
        periodStore.saveObject(periodB);
        periodStore.saveObject(periodC);
        periodStore.saveObject(periodD);

        List<Period> periods = periodStore.getPeriodsBetweenDates(getDay(1), getDay(1));
        assertNotNull(periods);
        assertEquals(0, periods.size());

        periods = periodStore.getPeriodsBetweenDates(getDay(1), getDay(2));
        assertNotNull(periods);
        assertEquals(1, periods.size());
        assertEquals(periodA, periods.iterator().next());

        periods = periodStore.getPeriodsBetweenDates(getDay(2), getDay(4));
        assertNotNull(periods);
        assertEquals(3, periods.size());
        assertTrue(periods.contains(periodB));
        assertTrue(periods.contains(periodC));
        assertTrue(periods.contains(periodD));

        periods = periodStore.getPeriodsBetweenDates(getDay(1), getDay(5));
        assertNotNull(periods);
        assertEquals(4, periods.size());
        assertTrue(periods.contains(periodA));
        assertTrue(periods.contains(periodB));
        assertTrue(periods.contains(periodC));
        assertTrue(periods.contains(periodD));
    }

    @Test
    void testGetPeriodsBetweenOrSpanningDates() {
        List<PeriodType> periodTypes = periodStore.getAllPeriodTypes();
        Iterator<PeriodType> it = periodTypes.iterator();
        PeriodType periodTypeA = it.next();
        PeriodType periodTypeB = it.next();

        Period periodA = new Period(periodTypeA, getDay(1), getDay(2));
        Period periodB = new Period(periodTypeA, getDay(2), getDay(3));
        Period periodC = new Period(periodTypeB, getDay(2), getDay(3));
        Period periodD = new Period(periodTypeB, getDay(3), getDay(4));
        Period periodE = new Period(periodTypeB, getDay(1), getDay(4));
        periodStore.saveObject(periodA);
        periodStore.saveObject(periodB);
        periodStore.saveObject(periodC);
        periodStore.saveObject(periodD);
        periodStore.saveObject(periodE);

        List<Period> periods = periodStore.getPeriodsBetweenOrSpanningDates(getDay(1), getDay(1));
        assertNotNull(periods);
        assertEquals(2, periods.size());
        assertTrue(periods.contains(periodA));
        assertTrue(periods.contains(periodE));

        periods = periodStore.getPeriodsBetweenOrSpanningDates(getDay(1), getDay(2));
        assertNotNull(periods);
        assertEquals(2, periods.size());
        assertTrue(periods.contains(periodA));
        assertTrue(periods.contains(periodE));

        periods = periodStore.getPeriodsBetweenOrSpanningDates(getDay(2), getDay(3));
        assertNotNull(periods);
        assertEquals(3, periods.size());
        assertTrue(periods.contains(periodB));
        assertTrue(periods.contains(periodC));
        assertTrue(periods.contains(periodE));

        periods = periodStore.getPeriodsBetweenOrSpanningDates(getDay(2), getDay(4));
        assertNotNull(periods);
        assertEquals(4, periods.size());
        assertTrue(periods.contains(periodB));
        assertTrue(periods.contains(periodC));
        assertTrue(periods.contains(periodD));
        assertTrue(periods.contains(periodE));
    }

    @Test
    void testGetIntersectingPeriodsByPeriodType() {
        PeriodType ypt = PeriodType.getPeriodTypeByName(YearlyPeriodType.NAME);

        Date jan2006 = getDate(2006, 1, 1);
        Date dec2006 = getDate(2006, 12, 31);
        Date jan2007 = getDate(2007, 1, 1);
        Date dec2007 = getDate(2007, 12, 31);

        Period periodA = new Period(ypt, jan2006, dec2006);
        Period periodB = new Period(ypt, jan2007, dec2007);
        periodStore.addPeriod(periodA);
        periodStore.addPeriod(periodB);

        PeriodType mpt = PeriodType.getPeriodTypeByName(MonthlyPeriodType.NAME);

        Date janstart = getDate(2006, 1, 1);
        Date janend = getDate(2006, 1, 31);
        Date febstart = getDate(2006, 2, 1);
        Date febend = getDate(2006, 2, 28);
        Date marstart = getDate(2006, 3, 1);
        Date marend = getDate(2006, 3, 31);
        Date aprstart = getDate(2006, 4, 1);
        Date aprend = getDate(2006, 4, 30);
        Date maystart = getDate(2006, 5, 1);
        Date mayend = getDate(2006, 5, 31);
        Date junstart = getDate(2006, 6, 1);
        Date junend = getDate(2006, 6, 30);
        Date julstart = getDate(2006, 7, 1);
        Date julend = getDate(2006, 7, 31);
        Date augstart = getDate(2006, 8, 1);
        Date augend = getDate(2006, 8, 31);
        Date sepstart = getDate(2006, 9, 1);
        Date sepend = getDate(2006, 9, 30);
        Date octstart = getDate(2006, 10, 1);
        Date octend = getDate(2006, 10, 31);
        Date novstart = getDate(2006, 11, 1);
        Date novend = getDate(2006, 11, 30);
        Date decstart = getDate(2006, 12, 1);
        Date decend = getDate(2006, 12, 31);

        Period periodC = new Period(mpt, janstart, janend);
        Period periodD = new Period(mpt, febstart, febend);
        Period periodE = new Period(mpt, marstart, marend);
        Period periodF = new Period(mpt, aprstart, aprend);
        Period periodG = new Period(mpt, maystart, mayend);
        Period periodH = new Period(mpt, junstart, junend);
        Period periodI = new Period(mpt, julstart, julend);
        Period periodJ = new Period(mpt, augstart, augend);
        Period periodK = new Period(mpt, sepstart, sepend);
        Period periodL = new Period(mpt, octstart, octend);
        Period periodM = new Period(mpt, novstart, novend);
        Period periodN = new Period(mpt, decstart, decend);

        periodStore.addPeriod(periodC);
        periodStore.addPeriod(periodD);
        periodStore.addPeriod(periodE);
        periodStore.addPeriod(periodF);
        periodStore.addPeriod(periodG);
        periodStore.addPeriod(periodH);
        periodStore.addPeriod(periodI);
        periodStore.addPeriod(periodJ);
        periodStore.addPeriod(periodK);
        periodStore.addPeriod(periodL);
        periodStore.addPeriod(periodM);
        periodStore.addPeriod(periodN);

        List<Period> periodsA = periodStore.getIntersectingPeriodsByPeriodType(ypt, getDate(2006, 6, 1),
            getDate(2006, 11, 30));
        assertNotNull(periodsA);
        assertEquals(1, periodsA.size());

        List<Period> periodsB = periodStore.getIntersectingPeriodsByPeriodType(mpt, getDate(2006, 6, 1),
            getDate(2006, 11, 30));
        assertNotNull(periodsB);
        assertEquals(6, periodsB.size());
    }

    @Test
    void testGetIntersectingPeriods() {
        PeriodType type = periodStore.getAllPeriodTypes().iterator().next();

        Period periodA = new Period(type, getDay(1), getDay(2));
        Period periodB = new Period(type, getDay(2), getDay(4));
        Period periodC = new Period(type, getDay(4), getDay(6));
        Period periodD = new Period(type, getDay(6), getDay(8));
        Period periodE = new Period(type, getDay(8), getDay(10));
        Period periodF = new Period(type, getDay(10), getDay(12));
        Period periodG = new Period(type, getDay(12), getDay(14));
        Period periodH = new Period(type, getDay(2), getDay(6));
        Period periodI = new Period(type, getDay(8), getDay(12));
        Period periodJ = new Period(type, getDay(2), getDay(12));

        periodStore.saveObject(periodA);
        periodStore.saveObject(periodB);
        periodStore.saveObject(periodC);
        periodStore.saveObject(periodD);
        periodStore.saveObject(periodE);
        periodStore.saveObject(periodF);
        periodStore.saveObject(periodG);
        periodStore.saveObject(periodH);
        periodStore.saveObject(periodI);
        periodStore.saveObject(periodJ);

        List<Period> periods = periodStore.getIntersectingPeriods(getDay(4), getDay(10));

        assertEquals(periods.size(), 8);

        assertTrue(periods.contains(periodB));
        assertTrue(periods.contains(periodC));
        assertTrue(periods.contains(periodD));
        assertTrue(periods.contains(periodE));
        assertTrue(periods.contains(periodF));
        assertTrue(periods.contains(periodH));
        assertTrue(periods.contains(periodI));
        assertTrue(periods.contains(periodJ));
    }

    @Test
    void testGetPeriodsByPeriodType() {
        List<PeriodType> periodTypes = periodStore.getAllPeriodTypes();
        Iterator<PeriodType> it = periodTypes.iterator();
        PeriodType periodTypeA = it.next();
        PeriodType periodTypeB = it.next();
        PeriodType periodTypeC = it.next();

        Period periodA = new Period(periodTypeA, getDay(1), getDay(2));
        Period periodB = new Period(periodTypeA, getDay(2), getDay(3));
        Period periodC = new Period(periodTypeA, getDay(3), getDay(4));
        Period periodD = new Period(periodTypeB, getDay(3), getDay(4));
        periodStore.saveObject(periodA);
        periodStore.saveObject(periodB);
        periodStore.saveObject(periodC);
        periodStore.saveObject(periodD);

        List<Period> periodsARef = new ArrayList<>();
        periodsARef.add(periodA);
        periodsARef.add(periodB);
        periodsARef.add(periodC);

        List<Period> periodsA = periodStore.getPeriodsByPeriodType(periodTypeA);
        assertNotNull(periodsA);
        assertEquals(periodsARef.size(), periodsA.size());
        assertTrue(periodsA.containsAll(periodsARef));

        List<Period> periodsB = periodStore.getPeriodsByPeriodType(periodTypeB);
        assertNotNull(periodsB);
        assertEquals(1, periodsB.size());
        assertEquals(periodD, periodsB.iterator().next());

        List<Period> periodsC = periodStore.getPeriodsByPeriodType(periodTypeC);
        assertNotNull(periodsC);
        assertEquals(0, periodsC.size());
    }
}
