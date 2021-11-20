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
class PeriodServiceExtTest extends AMTest {

    @Autowired
    private PeriodServiceExt periodServiceExt;

    // -------------------------------------------------------------------------
    // Period
    // -------------------------------------------------------------------------

    @Test
    public void testAddPeriod()
    {
        List<PeriodType> periodTypes = periodServiceExt.getAllPeriodTypes();
        Iterator<PeriodType> it = periodTypes.iterator();
        PeriodType periodTypeA = it.next();
        PeriodType periodTypeB = it.next();

        Period periodA = new Period( periodTypeA, getDay( 1 ), getDay( 2 ) );
        Period periodB = new Period( periodTypeA, getDay( 2 ), getDay( 3 ) );
        Period periodC = new Period( periodTypeB, getDay( 2 ), getDay( 3 ) );
        long idA = periodServiceExt.addPeriod( periodA );
        long idB = periodServiceExt.addPeriod( periodB );
        long idC = periodServiceExt.addPeriod( periodC );

        periodA = periodServiceExt.getPeriod( idA );
        assertNotNull( periodA );
        assertEquals( idA, periodA.getId() );
        assertEquals( periodTypeA, periodA.getPeriodType() );
        assertEquals( getDay( 1 ), periodA.getStartDate() );
        assertEquals( getDay( 2 ), periodA.getEndDate() );

        periodB = periodServiceExt.getPeriod( idB );
        assertNotNull( periodB );
        assertEquals( idB, periodB.getId() );
        assertEquals( periodTypeA, periodB.getPeriodType() );
        assertEquals( getDay( 2 ), periodB.getStartDate() );
        assertEquals( getDay( 3 ), periodB.getEndDate() );

        periodC = periodServiceExt.getPeriod( idC );
        assertNotNull( periodC );
        assertEquals( idC, periodC.getId() );
        assertEquals( periodTypeB, periodC.getPeriodType() );
        assertEquals( getDay( 2 ), periodC.getStartDate() );
        assertEquals( getDay( 3 ), periodC.getEndDate() );
    }

    @Test
    public void testDeleteAndGetPeriod()
    {
        List<PeriodType> periodTypes = periodServiceExt.getAllPeriodTypes();
        Iterator<PeriodType> it = periodTypes.iterator();
        PeriodType periodTypeA = it.next();
        PeriodType periodTypeB = it.next();

        Period periodA = new Period( periodTypeA, getDay( 1 ), getDay( 2 ) );
        Period periodB = new Period( periodTypeA, getDay( 2 ), getDay( 3 ) );
        Period periodC = new Period( periodTypeB, getDay( 2 ), getDay( 3 ) );
        Period periodD = new Period( periodTypeB, getDay( 3 ), getDay( 4 ) );
        long idA = periodServiceExt.addPeriod( periodA );
        long idB = periodServiceExt.addPeriod( periodB );
        long idC = periodServiceExt.addPeriod( periodC );
        long idD = periodServiceExt.addPeriod( periodD );

        assertNotNull( periodServiceExt.getPeriod( idA ) );
        assertNotNull( periodServiceExt.getPeriod( idB ) );
        assertNotNull( periodServiceExt.getPeriod( idC ) );
        assertNotNull( periodServiceExt.getPeriod( idD ) );

        periodServiceExt.deletePeriod( periodA );
        assertNull( periodServiceExt.getPeriod( idA ) );
        assertNotNull( periodServiceExt.getPeriod( idB ) );
        assertNotNull( periodServiceExt.getPeriod( idC ) );
        assertNotNull( periodServiceExt.getPeriod( idD ) );

        periodServiceExt.deletePeriod( periodB );
        assertNull( periodServiceExt.getPeriod( idA ) );
        assertNull( periodServiceExt.getPeriod( idB ) );
        assertNotNull( periodServiceExt.getPeriod( idC ) );
        assertNotNull( periodServiceExt.getPeriod( idD ) );

        periodServiceExt.deletePeriod( periodC );
        assertNull( periodServiceExt.getPeriod( idA ) );
        assertNull( periodServiceExt.getPeriod( idB ) );
        assertNull( periodServiceExt.getPeriod( idC ) );
        assertNotNull( periodServiceExt.getPeriod( idD ) );

        periodServiceExt.deletePeriod( periodD );
        assertNull( periodServiceExt.getPeriod( idA ) );
        assertNull( periodServiceExt.getPeriod( idB ) );
        assertNull( periodServiceExt.getPeriod( idC ) );
        assertNull( periodServiceExt.getPeriod( idD ) );
    }

    @Test
    public void testGetPeriod()
    {
        List<PeriodType> periodTypes = periodServiceExt.getAllPeriodTypes();
        Iterator<PeriodType> it = periodTypes.iterator();
        PeriodType periodTypeA = it.next();
        PeriodType periodTypeB = it.next();

        Period periodA = new Period( periodTypeA, getDay( 1 ), getDay( 2 ) );
        Period periodB = new Period( periodTypeA, getDay( 2 ), getDay( 3 ) );
        Period periodC = new Period( periodTypeB, getDay( 2 ), getDay( 3 ) );
        Period periodD = new Period( periodTypeB, getDay( 3 ), getDay( 4 ) );
        Period periodE = new Period( periodTypeA, getDay( 3 ), getDay( 4 ) );
        long idA = periodServiceExt.addPeriod( periodA );
        long idB = periodServiceExt.addPeriod( periodB );
        long idC = periodServiceExt.addPeriod( periodC );
        long idD = periodServiceExt.addPeriod( periodD );
        long idE = periodServiceExt.addPeriod( periodE );

        periodA = periodServiceExt.getPeriod( getDay( 1 ), getDay( 2 ), periodTypeA );
        assertNotNull( periodA );
        assertEquals( idA, periodA.getId() );
        assertEquals( periodTypeA, periodA.getPeriodType() );
        assertEquals( getDay( 1 ), periodA.getStartDate() );
        assertEquals( getDay( 2 ), periodA.getEndDate() );

        periodB = periodServiceExt.getPeriod( getDay( 2 ), getDay( 3 ), periodTypeA );
        assertNotNull( periodB );
        assertEquals( idB, periodB.getId() );
        assertEquals( periodTypeA, periodB.getPeriodType() );
        assertEquals( getDay( 2 ), periodB.getStartDate() );
        assertEquals( getDay( 3 ), periodB.getEndDate() );

        periodC = periodServiceExt.getPeriod( getDay( 2 ), getDay( 3 ), periodTypeB );
        assertNotNull( periodC );
        assertEquals( idC, periodC.getId() );
        assertEquals( periodTypeB, periodC.getPeriodType() );
        assertEquals( getDay( 2 ), periodC.getStartDate() );
        assertEquals( getDay( 3 ), periodC.getEndDate() );

        periodD = periodServiceExt.getPeriod( getDay( 3 ), getDay( 4 ), periodTypeB );
        assertNotNull( periodD );
        assertEquals( idD, periodD.getId() );
        assertEquals( periodTypeB, periodD.getPeriodType() );
        assertEquals( getDay( 3 ), periodD.getStartDate() );
        assertEquals( getDay( 4 ), periodD.getEndDate() );

        periodE = periodServiceExt.getPeriod( getDay( 3 ), getDay( 4 ), periodTypeA );
        assertNotNull( periodE );
        assertEquals( idE, periodE.getId() );
        assertEquals( periodTypeA, periodE.getPeriodType() );
        assertEquals( getDay( 3 ), periodE.getStartDate() );
        assertEquals( getDay( 4 ), periodE.getEndDate() );

        assertNull( periodServiceExt.getPeriod( getDay( 1 ), getDay( 2 ), periodTypeB ) );
        assertNull( periodServiceExt.getPeriod( getDay( 4 ), getDay( 5 ), periodTypeA ) );
        assertNull( periodServiceExt.getPeriod( getDay( 1 ), getDay( 5 ), periodTypeB ) );
        assertNull( periodServiceExt.getPeriod( getDay( 4 ), getDay( 3 ), periodTypeB ) );
        assertNull( periodServiceExt.getPeriod( getDay( 5 ), getDay( 6 ), periodTypeA ) );
    }

    @Test
    public void testGetAllPeriods()
    {
        PeriodType periodType = periodServiceExt.getAllPeriodTypes().iterator().next();

        Period periodA = new Period( periodType, getDay( 1 ), getDay( 2 ) );
        Period periodB = new Period( periodType, getDay( 2 ), getDay( 3 ) );
        Period periodC = new Period( periodType, getDay( 3 ), getDay( 4 ) );

        periodServiceExt.addPeriod( periodA );
        periodServiceExt.addPeriod( periodB );
        periodServiceExt.addPeriod( periodC );

        List<Period> periods = periodServiceExt.getAllPeriods();

        assertNotNull( periods );
        assertEquals( 3, periods.size() );
        assertTrue( periods.contains( periodA ) );
        assertTrue( periods.contains( periodB ) );
        assertTrue( periods.contains( periodC ) );
    }

    @Test
    public void testGetPeriodsBetweenDates()
    {
        List<PeriodType> periodTypes = periodServiceExt.getAllPeriodTypes();
        Iterator<PeriodType> it = periodTypes.iterator();
        PeriodType periodTypeA = it.next();
        PeriodType periodTypeB = it.next();

        Period periodA = new Period( periodTypeA, getDay( 1 ), getDay( 2 ) );
        Period periodB = new Period( periodTypeA, getDay( 2 ), getDay( 3 ) );
        Period periodC = new Period( periodTypeB, getDay( 2 ), getDay( 3 ) );
        Period periodD = new Period( periodTypeB, getDay( 3 ), getDay( 4 ) );
        periodServiceExt.addPeriod( periodA );
        periodServiceExt.addPeriod( periodB );
        periodServiceExt.addPeriod( periodC );
        periodServiceExt.addPeriod( periodD );

        List<Period> periods = periodServiceExt.getPeriodsBetweenDates( getDay( 1 ), getDay( 1 ) );
        assertNotNull( periods );
        assertEquals( 0, periods.size() );

        periods = periodServiceExt.getPeriodsBetweenDates( getDay( 1 ), getDay( 2 ) );
        assertNotNull( periods );
        assertEquals( 1, periods.size() );
        assertEquals( periodA, periods.iterator().next() );

        periods = periodServiceExt.getPeriodsBetweenDates( getDay( 2 ), getDay( 4 ) );
        assertNotNull( periods );
        assertEquals( 3, periods.size() );
        assertTrue( periods.contains( periodB ) );
        assertTrue( periods.contains( periodC ) );
        assertTrue( periods.contains( periodD ) );

        periods = periodServiceExt.getPeriodsBetweenDates( getDay( 1 ), getDay( 5 ) );
        assertNotNull( periods );
        assertEquals( 4, periods.size() );
        assertTrue( periods.contains( periodA ) );
        assertTrue( periods.contains( periodB ) );
        assertTrue( periods.contains( periodC ) );
        assertTrue( periods.contains( periodD ) );
    }

    @Test
    public void testGetIntersectingPeriodsByPeriodType()
    {
        PeriodType ypt = PeriodType.getPeriodTypeByName( YearlyPeriodType.NAME );

        Date jan2006 = getDate( 2006, 1, 1 );
        Date dec2006 = getDate( 2006, 12, 31 );
        Date jan2007 = getDate( 2007, 1, 1 );
        Date dec2007 = getDate( 2007, 12, 31 );

        Period periodA = new Period( ypt, jan2006, dec2006 );
        Period periodB = new Period( ypt, jan2007, dec2007 );
        periodServiceExt.addPeriod( periodA );
        periodServiceExt.addPeriod( periodB );

        PeriodType mpt = PeriodType.getPeriodTypeByName( MonthlyPeriodType.NAME );

        Date janstart = getDate( 2006, 1, 1 );
        Date janend = getDate( 2006, 1, 31 );
        Date febstart = getDate( 2006, 2, 1 );
        Date febend = getDate( 2006, 2, 28 );
        Date marstart = getDate( 2006, 3, 1 );
        Date marend = getDate( 2006, 3, 31 );
        Date aprstart = getDate( 2006, 4, 1 );
        Date aprend = getDate( 2006, 4, 30 );
        Date maystart = getDate( 2006, 5, 1 );
        Date mayend = getDate( 2006, 5, 31 );
        Date junstart = getDate( 2006, 6, 1 );
        Date junend = getDate( 2006, 6, 30 );
        Date julstart = getDate( 2006, 7, 1 );
        Date julend = getDate( 2006, 7, 31 );
        Date augstart = getDate( 2006, 8, 1 );
        Date augend = getDate( 2006, 8, 31 );
        Date sepstart = getDate( 2006, 9, 1 );
        Date sepend = getDate( 2006, 9, 30 );
        Date octstart = getDate( 2006, 10, 1 );
        Date octend = getDate( 2006, 10, 31 );
        Date novstart = getDate( 2006, 11, 1 );
        Date novend = getDate( 2006, 11, 30 );
        Date decstart = getDate( 2006, 12, 1 );
        Date decend = getDate( 2006, 12, 31 );

        Period periodC = new Period( mpt, janstart, janend );
        Period periodD = new Period( mpt, febstart, febend );
        Period periodE = new Period( mpt, marstart, marend );
        Period periodF = new Period( mpt, aprstart, aprend );
        Period periodG = new Period( mpt, maystart, mayend );
        Period periodH = new Period( mpt, junstart, junend );
        Period periodI = new Period( mpt, julstart, julend );
        Period periodJ = new Period( mpt, augstart, augend );
        Period periodK = new Period( mpt, sepstart, sepend );
        Period periodL = new Period( mpt, octstart, octend );
        Period periodM = new Period( mpt, novstart, novend );
        Period periodN = new Period( mpt, decstart, decend );

        periodServiceExt.addPeriod( periodC );
        periodServiceExt.addPeriod( periodD );
        periodServiceExt.addPeriod( periodE );
        periodServiceExt.addPeriod( periodF );
        periodServiceExt.addPeriod( periodG );
        periodServiceExt.addPeriod( periodH );
        periodServiceExt.addPeriod( periodI );
        periodServiceExt.addPeriod( periodJ );
        periodServiceExt.addPeriod( periodK );
        periodServiceExt.addPeriod( periodL );
        periodServiceExt.addPeriod( periodM );
        periodServiceExt.addPeriod( periodN );

        List<Period> periodsA = periodServiceExt.getIntersectingPeriodsByPeriodType( ypt, getDate( 2006, 6, 1 ),
            getDate( 2006, 11, 30 ) );
        assertNotNull( periodsA );
        assertEquals( 1, periodsA.size() );

        List<Period> periodsB = periodServiceExt.getIntersectingPeriodsByPeriodType( mpt, getDate( 2006, 6, 1 ),
            getDate( 2006, 11, 30 ) );
        assertNotNull( periodsB );
        assertEquals( 6, periodsB.size() );
    }

    @Test
    public void testGetIntersectingPeriods()
    {
        PeriodType type = periodServiceExt.getAllPeriodTypes().iterator().next();

        Period periodA = new Period( type, getDay( 1 ), getDay( 2 ) );
        Period periodB = new Period( type, getDay( 2 ), getDay( 4 ) );
        Period periodC = new Period( type, getDay( 4 ), getDay( 6 ) );
        Period periodD = new Period( type, getDay( 6 ), getDay( 8 ) );
        Period periodE = new Period( type, getDay( 8 ), getDay( 10 ) );
        Period periodF = new Period( type, getDay( 10 ), getDay( 12 ) );
        Period periodG = new Period( type, getDay( 12 ), getDay( 14 ) );
        Period periodH = new Period( type, getDay( 2 ), getDay( 6 ) );
        Period periodI = new Period( type, getDay( 8 ), getDay( 12 ) );
        Period periodJ = new Period( type, getDay( 2 ), getDay( 12 ) );

        periodServiceExt.addPeriod( periodA );
        periodServiceExt.addPeriod( periodB );
        periodServiceExt.addPeriod( periodC );
        periodServiceExt.addPeriod( periodD );
        periodServiceExt.addPeriod( periodE );
        periodServiceExt.addPeriod( periodF );
        periodServiceExt.addPeriod( periodG );
        periodServiceExt.addPeriod( periodH );
        periodServiceExt.addPeriod( periodI );
        periodServiceExt.addPeriod( periodJ );

        List<Period> periods = periodServiceExt.getIntersectingPeriods( getDay( 4 ), getDay( 10 ) );

        assertEquals( periods.size(), 8 );

        assertTrue( periods.contains( periodB ) );
        assertTrue( periods.contains( periodC ) );
        assertTrue( periods.contains( periodD ) );
        assertTrue( periods.contains( periodE ) );
        assertTrue( periods.contains( periodF ) );
        assertTrue( periods.contains( periodH ) );
        assertTrue( periods.contains( periodI ) );
        assertTrue( periods.contains( periodJ ) );
    }

    @Test
    public void testGetPeriodsByPeriodType()
    {
        List<PeriodType> periodTypes = periodServiceExt.getAllPeriodTypes();
        Iterator<PeriodType> it = periodTypes.iterator();
        PeriodType periodTypeA = it.next();
        PeriodType periodTypeB = it.next();
        PeriodType periodTypeC = it.next();

        Period periodA = new Period( periodTypeA, getDay( 1 ), getDay( 2 ) );
        Period periodB = new Period( periodTypeA, getDay( 2 ), getDay( 3 ) );
        Period periodC = new Period( periodTypeA, getDay( 3 ), getDay( 4 ) );
        Period periodD = new Period( periodTypeB, getDay( 3 ), getDay( 4 ) );
        periodServiceExt.addPeriod( periodA );
        periodServiceExt.addPeriod( periodB );
        periodServiceExt.addPeriod( periodC );
        periodServiceExt.addPeriod( periodD );

        List<Period> periodsARef = new ArrayList<>();
        periodsARef.add( periodA );
        periodsARef.add( periodB );
        periodsARef.add( periodC );

        List<Period> periodsA = periodServiceExt.getPeriodsByPeriodType( periodTypeA );
        assertNotNull( periodsA );
        assertEquals( periodsARef.size(), periodsA.size() );
        assertTrue( periodsA.containsAll( periodsARef ) );

        List<Period> periodsB = periodServiceExt.getPeriodsByPeriodType( periodTypeB );
        assertNotNull( periodsB );
        assertEquals( 1, periodsB.size() );
        assertEquals( periodD, periodsB.iterator().next() );

        List<Period> periodsC = periodServiceExt.getPeriodsByPeriodType( periodTypeC );
        assertNotNull( periodsC );
        assertEquals( 0, periodsC.size() );
    }

    @Test
    public void testGetBoundaryPeriods()
    {
        PeriodType periodType = periodServiceExt.getAllPeriodTypes().iterator().next();

        Period periodA = new Period( periodType, getDay( 5 ), getDay( 8 ) );
        Period periodB = new Period( periodType, getDay( 8 ), getDay( 11 ) );
        Period periodC = new Period( periodType, getDay( 11 ), getDay( 14 ) );
        Period periodD = new Period( periodType, getDay( 14 ), getDay( 17 ) );
        Period periodE = new Period( periodType, getDay( 17 ), getDay( 20 ) );
        Period periodF = new Period( periodType, getDay( 5 ), getDay( 20 ) );

        List<Period> periods = new ArrayList<>();

        periods.add( periodA );
        periods.add( periodB );
        periods.add( periodC );
        periods.add( periodD );
        periods.add( periodE );
        periods.add( periodF );

        Period basePeriod = new Period( periodType, getDay( 9 ), getDay( 15 ) );

        List<Period> boundaryPeriods = periodServiceExt.getBoundaryPeriods( basePeriod, periods );

        assertTrue( boundaryPeriods.size() == 3 );
        assertTrue( boundaryPeriods.contains( periodB ) );
        assertTrue( boundaryPeriods.contains( periodD ) );
        assertTrue( boundaryPeriods.contains( periodF ) );

        basePeriod = new Period( periodType, getDay( 11 ), getDay( 14 ) );

        boundaryPeriods = periodServiceExt.getBoundaryPeriods( basePeriod, periods );

        assertTrue( boundaryPeriods.size() == 1 );
        assertTrue( boundaryPeriods.contains( periodF ) );

        basePeriod = new Period( periodType, getDay( 2 ), getDay( 5 ) );

        boundaryPeriods = periodServiceExt.getBoundaryPeriods( basePeriod, periods );

        assertTrue( boundaryPeriods.size() == 0 );
    }

    @Test
    public void testGetInclusivePeriods()
    {
        PeriodType periodType = periodServiceExt.getAllPeriodTypes().iterator().next();

        Period periodA = new Period( periodType, getDay( 5 ), getDay( 8 ) );
        Period periodB = new Period( periodType, getDay( 8 ), getDay( 11 ) );
        Period periodC = new Period( periodType, getDay( 11 ), getDay( 14 ) );
        Period periodD = new Period( periodType, getDay( 14 ), getDay( 17 ) );
        Period periodE = new Period( periodType, getDay( 17 ), getDay( 20 ) );
        Period periodF = new Period( periodType, getDay( 5 ), getDay( 20 ) );

        List<Period> periods = new ArrayList<>();

        periods.add( periodA );
        periods.add( periodB );
        periods.add( periodC );
        periods.add( periodD );
        periods.add( periodE );
        periods.add( periodF );

        Period basePeriod = new Period( periodType, getDay( 8 ), getDay( 20 ) );

        List<Period> inclusivePeriods = periodServiceExt.getInclusivePeriods( basePeriod, periods );

        assertTrue( inclusivePeriods.size() == 4 );
        assertTrue( inclusivePeriods.contains( periodB ) );
        assertTrue( inclusivePeriods.contains( periodC ) );
        assertTrue( inclusivePeriods.contains( periodD ) );
        assertTrue( inclusivePeriods.contains( periodE ) );

        basePeriod = new Period( periodType, getDay( 9 ), getDay( 18 ) );

        inclusivePeriods = periodServiceExt.getInclusivePeriods( basePeriod, periods );

        assertTrue( inclusivePeriods.size() == 2 );
        assertTrue( inclusivePeriods.contains( periodC ) );
        assertTrue( inclusivePeriods.contains( periodD ) );

        basePeriod = new Period( periodType, getDay( 2 ), getDay( 5 ) );

        inclusivePeriods = periodServiceExt.getInclusivePeriods( basePeriod, periods );

        assertTrue( inclusivePeriods.size() == 0 );
    }

    // -------------------------------------------------------------------------
    // PeriodType
    // -------------------------------------------------------------------------

    @Test
    public void testGetAndGetAllPeriodTypes()
    {
        List<PeriodType> periodTypes = periodServiceExt.getAllPeriodTypes();
        Iterator<PeriodType> it = periodTypes.iterator();
        PeriodType periodTypeA = it.next();
        PeriodType periodTypeB = it.next();
        PeriodType periodTypeC = it.next();
        PeriodType periodTypeD = it.next();

        assertNotNull( periodServiceExt.getPeriodTypeByName( periodTypeA.getName() ) );
        assertNotNull( periodServiceExt.getPeriodTypeByName( periodTypeB.getName() ) );
        assertNotNull( periodServiceExt.getPeriodTypeByName( periodTypeC.getName() ) );
        assertNotNull( periodServiceExt.getPeriodTypeByName( periodTypeD.getName() ) );
    }

    @Test
    public void testGetPeriodTypeByName()
    {
        List<PeriodType> periodTypes = periodServiceExt.getAllPeriodTypes();
        Iterator<PeriodType> it = periodTypes.iterator();
        PeriodType refA = it.next();
        PeriodType refB = it.next();

        PeriodType periodTypeA = periodServiceExt.getPeriodTypeByName( refA.getName() );
        assertNotNull( periodTypeA );
        assertEquals( refA.getName(), periodTypeA.getName() );

        PeriodType periodTypeB = periodServiceExt.getPeriodTypeByName( refB.getName() );
        assertNotNull( periodTypeB );
        assertEquals( refB.getName(), periodTypeB.getName() );
    }

    @Test
    public void testDeleteAndGetPeriodType()
    {
        List<PeriodType> periodTypes = periodServiceExt.getAllPeriodTypes();
        Iterator<PeriodType> it = periodTypes.iterator();
        PeriodType periodTypeA = it.next();
        PeriodType periodTypeB = it.next();
        PeriodType periodTypeC = it.next();
        PeriodType periodTypeD = it.next();

        int idA = periodTypeA.getId();
        int idB = periodTypeB.getId();
        int idC = periodTypeC.getId();
        int idD = periodTypeD.getId();

        assertNotNull( periodServiceExt.getPeriodType( idA ) );
        assertNotNull( periodServiceExt.getPeriodType( idB ) );
        assertNotNull( periodServiceExt.getPeriodType( idC ) );
        assertNotNull( periodServiceExt.getPeriodType( idD ) );

        assertNotNull( periodServiceExt.getPeriodType( periodTypeA.getId() ) );
        assertNotNull( periodServiceExt.getPeriodType( periodTypeB.getId() ) );
        assertNotNull( periodServiceExt.getPeriodType( periodTypeC.getId() ) );
        assertNotNull( periodServiceExt.getPeriodType( periodTypeD.getId() ) );
    }
}
