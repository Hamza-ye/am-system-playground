package org.nmcpye.activitiesmanagement.extended.system.tempfortest;

import com.google.common.collect.Sets;
import com.vividsolutions.jts.geom.Geometry;
import org.joda.time.DateTime;
import org.nmcpye.activitiesmanagement.domain.enumeration.OrganisationUnitType;
import org.nmcpye.activitiesmanagement.domain.organisationunit.OrganisationUnit;
import org.nmcpye.activitiesmanagement.domain.organisationunit.OrganisationUnitGroup;
import org.nmcpye.activitiesmanagement.extended.common.CodeGenerator;
import org.nmcpye.activitiesmanagement.extended.organisationunit.OrganisationUnitGroupService;
import org.nmcpye.activitiesmanagement.extended.organisationunit.OrganisationUnitGroupStore;
import org.nmcpye.activitiesmanagement.extended.organisationunit.OrganisationUnitService;
import org.nmcpye.activitiesmanagement.extended.organisationunit.OrganisationUnitStore;
import org.nmcpye.activitiesmanagement.extended.systemmodule.startup.TransactionContextStartupRoutine;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Date;

import static com.google.common.base.Preconditions.checkNotNull;

//@Component
public class OrgUnitPopulator extends TransactionContextStartupRoutine {

    // -------------------------------------------------------------------------
    // Dependencies
    // -------------------------------------------------------------------------

    private final Logger log = LoggerFactory.getLogger(OrgUnitPopulator.class);

    protected static final String BASE_UID = "abcdefghij";
    protected static final String BASE_OU_UID = "oustupefgh";

    private final OrganisationUnitStore orgUnitStore;

    private final OrganisationUnitService organisationUnitService;

    private final OrganisationUnitGroupService organisationUnitGroupService;

    private final OrganisationUnitGroupStore orgUnitGroupStore;

    private static Date date;

    static {
        DateTime dateTime = new DateTime(1970, 1, 1, 0, 0);
        date = dateTime.toDate();
    }

    public OrgUnitPopulator(
        OrganisationUnitStore orgUnitStore,
        OrganisationUnitService organisationUnitService,
        OrganisationUnitGroupService organisationUnitGroupService,
        OrganisationUnitGroupStore orgUnitGroupStore
    ) {
        this.organisationUnitService = organisationUnitService;
        this.organisationUnitGroupService = organisationUnitGroupService;
        //        this.em = em;
        checkNotNull(orgUnitStore);
        checkNotNull(orgUnitGroupStore);

        this.orgUnitGroupStore = orgUnitGroupStore;
        this.orgUnitStore = orgUnitStore;
        this.setRunlevel(4);
        this.setSkipInTests(true);
    }

    //    private Session getSession() {
    //        return em.unwrap(Session.class);
    //    }

    /**
     * @param uniqueCharacter A unique character to identify the object.
     */
    public static OrganisationUnit createOrganisationUnit(char uniqueCharacter) {
        OrganisationUnit unit = new OrganisationUnit();
        unit.setAutoFields();

        unit.setUid(BASE_OU_UID + uniqueCharacter);
        unit.setOrganisationUnitType(OrganisationUnitType.DISTRICT);
        unit.setName("OrganisationUnit" + uniqueCharacter);
        unit.setShortName("OrganisationUnitShort" + uniqueCharacter);
        unit.setCode("OrganisationUnitCode" + uniqueCharacter);
        unit.setOpeningDate(date);
        unit.setComment("Comment" + uniqueCharacter);

        return unit;
    }

    public static OrganisationUnit createOrganisationUnit(char uniqueCharacter, Geometry geometry) {
        OrganisationUnit unit = createOrganisationUnit(uniqueCharacter);

        unit.setGeometry(geometry);

        return unit;
    }

    /**
     * @param uniqueCharacter A unique character to identify the object.
     * @param parent          The parent.
     */
    public static OrganisationUnit createOrganisationUnit(char uniqueCharacter, OrganisationUnit parent) {
        OrganisationUnit unit = createOrganisationUnit(uniqueCharacter);
        unit.setParent(parent);
        parent.getChildren().add(unit);

        int level = unit.getHierarchyLevel();

        switch (level) {
            case 1:
                unit.setOrganisationUnitType(OrganisationUnitType.COUNTRY);
                break;
            case 2:
                unit.setOrganisationUnitType(OrganisationUnitType.GOV);
                break;
            case 3:
                unit.setOrganisationUnitType(OrganisationUnitType.DISTRICT);
                break;
            case 4:
                unit.setOrganisationUnitType(OrganisationUnitType.HEALTH_FACILITY);
                break;
            case 5:
                unit.setOrganisationUnitType(OrganisationUnitType.SUBVILLAGE);
                break;
                default:
                    unit.setOrganisationUnitType(OrganisationUnitType.SUBVILLAGE);
        }

        return unit;
    }

    /**
     * @param name The name, short name and code of the organisation unit.
     */
    public static OrganisationUnit createOrganisationUnit(String name) {
        OrganisationUnit unit = new OrganisationUnit();
        unit.setAutoFields();

        unit.setUid(CodeGenerator.generateUid());
        unit.setName(name);
        unit.setShortName(name);
        unit.setCode(name);
        unit.setOpeningDate(date);
        unit.setComment("Comment " + name);

        return unit;
    }

    /**
     * @param name   The name, short name and code of the organisation unit.
     * @param parent The parent.
     */
    public static OrganisationUnit createOrganisationUnit(String name, OrganisationUnit parent) {
        OrganisationUnit unit = createOrganisationUnit(name);

        unit.setParent(parent);
        parent.getChildren().add(unit);

        return unit;
    }

    /**
     * @param uniqueCharacter A unique character to identify the object.
     */
    public static OrganisationUnitGroup createOrganisationUnitGroup(char uniqueCharacter) {
        OrganisationUnitGroup group = new OrganisationUnitGroup();
        group.setAutoFields();

        group.setUid(BASE_UID + uniqueCharacter);
        group.setName("OrganisationUnitGroup" + uniqueCharacter);
        group.setShortName("OrganisationUnitGroupShort" + uniqueCharacter);
        group.setCode("OrganisationUnitGroupCode" + uniqueCharacter);

        return group;
    }

    // -------------------------------------------------------------------------
    // Execute
    // -------------------------------------------------------------------------

    @Override
    public void executeInTransaction() {
        OrganisationUnit ouA = createOrganisationUnit('A'); // 1
        OrganisationUnit ouB = createOrganisationUnit('B', ouA); // 2
        OrganisationUnit ouC = createOrganisationUnit('C', ouA); // 2
        OrganisationUnit ouD = createOrganisationUnit('D', ouB); // 3
        OrganisationUnit ouE = createOrganisationUnit('E', ouB); // 3
        OrganisationUnit ouF = createOrganisationUnit('F', ouC); // 3
        OrganisationUnit ouG = createOrganisationUnit('G', ouC); // 3

        OrganisationUnit ouH = createOrganisationUnit('H', ouE); // 3
        OrganisationUnit ouI = createOrganisationUnit('I', ouD); // 3

        OrganisationUnitGroup ougA = createOrganisationUnitGroup('A');
        OrganisationUnitGroup ougB = createOrganisationUnitGroup('B');
        OrganisationUnitGroup ougC = createOrganisationUnitGroup('C');

        ougA.getMembers().addAll(Sets.newHashSet(ouD, ouF));
        ougB.getMembers().addAll(Sets.newHashSet(ouE, ouG));
        ougC.getMembers().addAll(Sets.newHashSet(ouH, ouI));

        try {
            organisationUnitService.addOrganisationUnit(ouA);
            organisationUnitService.addOrganisationUnit(ouB);
            organisationUnitService.addOrganisationUnit(ouC);
            organisationUnitService.addOrganisationUnit(ouD);
            organisationUnitService.addOrganisationUnit(ouE);
            organisationUnitService.addOrganisationUnit(ouF);
            organisationUnitService.addOrganisationUnit(ouG);
            organisationUnitService.addOrganisationUnit(ouH);
            organisationUnitService.addOrganisationUnit(ouI);

            organisationUnitGroupService.addOrganisationUnitGroup(ougA);
            organisationUnitGroupService.addOrganisationUnitGroup(ougB);
            organisationUnitGroupService.addOrganisationUnitGroup(ougC);

        } catch (Exception exception) {
            exception.printStackTrace();
        }
    }
}
