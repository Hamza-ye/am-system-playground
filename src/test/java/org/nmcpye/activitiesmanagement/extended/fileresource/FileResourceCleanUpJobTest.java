package org.nmcpye.activitiesmanagement.extended.fileresource;

import org.joda.time.DateTime;
import org.joda.time.Days;
import org.junit.Rule;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnit;
import org.mockito.junit.MockitoRule;
import org.nmcpye.activitiesmanagement.AMTest;
import org.nmcpye.activitiesmanagement.IntegrationTest;
import org.nmcpye.activitiesmanagement.domain.User;
import org.nmcpye.activitiesmanagement.domain.fileresource.ExternalFileResource;
import org.nmcpye.activitiesmanagement.domain.fileresource.FileResource;
import org.nmcpye.activitiesmanagement.domain.period.Period;
import org.nmcpye.activitiesmanagement.domain.period.PeriodType;
import org.nmcpye.activitiesmanagement.extended.organisationunit.OrganisationUnitServiceExt;
import org.nmcpye.activitiesmanagement.extended.period.PeriodServiceExt;
import org.nmcpye.activitiesmanagement.extended.user.UserServiceExt;
import org.springframework.beans.factory.annotation.Autowired;

import java.nio.charset.StandardCharsets;
import java.util.Date;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@IntegrationTest
public class FileResourceCleanUpJobTest
    extends AMTest {

    private FileResourceCleanUpJob cleanUpJob;

    @Autowired
    private FileResourceServiceExt fileResourceService;

    @Autowired
    private OrganisationUnitServiceExt organisationUnitService;

    @Autowired
    private PeriodServiceExt periodService;

    @Autowired
    private ExternalFileResourceServiceExt externalFileResourceService;

    @Autowired
    private UserServiceExt _userService;

    @Mock
    private FileResourceContentStore fileResourceContentStore;

    @Rule
    public MockitoRule mockitoRule = MockitoJUnit.rule();

    private byte[] content;

    private Period period;

    @BeforeEach
    public void init() {
        userServiceExt = _userService;

        cleanUpJob = new FileResourceCleanUpJob(fileResourceService, fileResourceContentStore);

        period = createPeriod(PeriodType.getPeriodTypeByName("Monthly"), new Date(), new Date());
        periodService.addPeriod(period);
    }

//    @Test
//    public void testNoRetention()
//    {
//        when( fileResourceContentStore.fileResourceContentExists( any( String.class ) ) ).thenReturn( true );
//
//        systemSettingManager.saveSystemSetting( SettingKey.FILE_RESOURCE_RETENTION_STRATEGY,
//            FileResourceRetentionStrategy.NONE );
//
//        content = "filecontentA".getBytes();
//        dataValueA = createFileResourceDataValue( 'A', content );
//        assertNotNull( fileResourceService.getFileResource( dataValueA.getValue() ) );
//
//        dataValueService.deleteDataValue( dataValueA );
//
//        cleanUpJob.execute( null );
//
//        assertNull( fileResourceService.getFileResource( dataValueA.getValue() ) );
//    }
//
//    @Test
//    public void testRetention()
//    {
//        when( fileResourceContentStore.fileResourceContentExists( any( String.class ) ) ).thenReturn( true );
//
//        systemSettingManager.saveSystemSetting( SettingKey.FILE_RESOURCE_RETENTION_STRATEGY,
//            FileResourceRetentionStrategy.THREE_MONTHS );
//
//        content = "filecontentA".getBytes( StandardCharsets.UTF_8 );
//        dataValueA = createFileResourceDataValue( 'A', content );
//        assertNotNull( fileResourceService.getFileResource( dataValueA.getValue() ) );
//
//        content = "filecontentB".getBytes( StandardCharsets.UTF_8 );
//        dataValueB = createFileResourceDataValue( 'B', content );
//        assertNotNull( fileResourceService.getFileResource( dataValueB.getValue() ) );
//
//        content = "fileResourceC".getBytes( StandardCharsets.UTF_8 );
//        FileResource fileResource = createFileResource( 'C', content );
//        dataValueB.setValue( fileResource.getUid() );
//        dataValueService.updateDataValue( dataValueB );
//        fileResource.setAssigned( true );
//
//        DataValueAudit audit = dataValueAuditService.getDataValueAudits( dataValueB ).get( 0 );
//        audit.setCreated( getDate( 2000, 1, 1 ) );
//        dataValueAuditStore.updateDataValueAudit( audit );
//
//        cleanUpJob.execute( null );
//
//        assertNotNull( fileResourceService.getFileResource( dataValueA.getValue() ) );
//        assertTrue( fileResourceService.getFileResource( dataValueA.getValue() ).isAssigned() );
//        assertNull( dataValueService.getDataValue( dataValueA.getDataElement(), dataValueA.getPeriod(),
//            dataValueA.getSource(), null ) );
//        assertNull( fileResourceService.getFileResource( dataValueB.getValue() ) );
//    }

    @Test
    public void testOrphan() {
        when(fileResourceContentStore.fileResourceContentExists(any(String.class))).thenReturn(false);

//        systemSettingManager.saveSystemSetting( SettingKey.FILE_RESOURCE_RETENTION_STRATEGY,
//            FileResourceRetentionStrategy.NONE );

        content = "filecontentA".getBytes(StandardCharsets.UTF_8);
        FileResource fileResourceA = createFileResource('A', content);
        fileResourceA.setCreated(DateTime.now().minus(Days.ONE).toDate());
        String uidA = fileResourceService.saveFileResource(fileResourceA, content);

        content = "filecontentB".getBytes(StandardCharsets.UTF_8);
        FileResource fileResourceB = createFileResource('A', content);
        fileResourceB.setCreated(DateTime.now().minus(Days.ONE).toDate());
        String uidB = fileResourceService.saveFileResource(fileResourceB, content);

        User userB = createUser('B');
        userB.setAvatar(fileResourceB);
        userB.setPerson(null);
        userServiceExt.addUser(userB);

        assertNotNull(fileResourceService.getFileResource(uidA));
        assertNotNull(fileResourceService.getFileResource(uidB));

        cleanUpJob.execute(null);

        assertNull(fileResourceService.getFileResource(uidA));
        assertNotNull(fileResourceService.getFileResource(uidB));

        // The following is needed because HibernateDbmsManager.emptyDatabase
        // empties fileresource before userinfo (which it must because
        // fileresource references userinfo).

        userB.setAvatar(null);
        userServiceExt.updateUser(userB);
    }

    @Test
    @Disabled
    public void testFalsePositive() {
//        systemSettingManager.saveSystemSetting( SettingKey.FILE_RESOURCE_RETENTION_STRATEGY,
//            FileResourceRetentionStrategy.THREE_MONTHS );

        content = "externalA".getBytes();
        ExternalFileResource ex = createExternal('A', content);

        String uid = ex.getFileResource().getUid();
        ex.getFileResource().setAssigned(false);
        fileResourceService.updateFileResource(ex.getFileResource());

        cleanUpJob.execute(null);

        assertNotNull(externalFileResourceService.getExternalFileResourceByAccessToken(ex.getAccessToken()));
        assertNotNull(fileResourceService.getFileResource(uid));
        assertTrue(fileResourceService.getFileResource(uid).isAssigned());
    }

    @Test
    @Disabled
    public void testFailedUpload() {
//        systemSettingManager.saveSystemSetting( SettingKey.FILE_RESOURCE_RETENTION_STRATEGY,
//            FileResourceRetentionStrategy.THREE_MONTHS );

        content = "externalA".getBytes();
        ExternalFileResource ex = createExternal('A', content);

        String uid = ex.getFileResource().getUid();
        ex.getFileResource().setStorageStatus(FileResourceStorageStatus.PENDING);
        fileResourceService.updateFileResource(ex.getFileResource());

        cleanUpJob.execute(null);

        assertNull(externalFileResourceService.getExternalFileResourceByAccessToken(ex.getAccessToken()));
        assertNull(fileResourceService.getFileResource(uid));
    }

//    private DataValue createFileResourceDataValue( char uniqueChar, byte[] content )
//    {
//        DataElement fileElement = createDataElement( uniqueChar, ValueType.FILE_RESOURCE, AggregationType.NONE );
//        OrganisationUnit orgUnit = createOrganisationUnit( uniqueChar );
//
//        dataElementService.addDataElement( fileElement );
//        organisationUnitService.addOrganisationUnit( orgUnit );
//
//        FileResource fileResource = createFileResource( uniqueChar, content );
//        String uid = fileResourceService.saveFileResource( fileResource, content );
//
//        DataValue dataValue = createDataValue( fileElement, period, orgUnit, uid, null );
//        fileResource.setAssigned( true );
//        fileResource.setCreated( DateTime.now().minus( Days.ONE ).toDate() );
//        fileResource.setStorageStatus( FileResourceStorageStatus.STORED );
//
//        fileResourceService.updateFileResource( fileResource );
//        dataValueService.addDataValue( dataValue );
//
//        return dataValue;
//    }

    private ExternalFileResource createExternal(char uniqueChar, byte[] content) {
        ExternalFileResource externalFileResource = createExternalFileResource(uniqueChar, content);

        fileResourceService.saveFileResource(externalFileResource.getFileResource(), content);
        externalFileResourceService.saveExternalFileResource(externalFileResource);

        FileResource fileResource = externalFileResource.getFileResource();
        fileResource.setCreated(DateTime.now().minus(Days.ONE).toDate());
        fileResource.setStorageStatus(FileResourceStorageStatus.STORED);

        fileResourceService.updateFileResource(fileResource);
        return externalFileResource;
    }
}
