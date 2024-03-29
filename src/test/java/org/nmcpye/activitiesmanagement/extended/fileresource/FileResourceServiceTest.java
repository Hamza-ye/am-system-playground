package org.nmcpye.activitiesmanagement.extended.fileresource;

import com.google.common.collect.ImmutableMap;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.junit.Rule;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnit;
import org.mockito.junit.MockitoRule;
import org.nmcpye.activitiesmanagement.domain.fileresource.FileResource;
import org.nmcpye.activitiesmanagement.extended.common.IllegalQueryException;
import org.nmcpye.activitiesmanagement.extended.fileresource.events.FileDeletedEvent;
import org.nmcpye.activitiesmanagement.extended.fileresource.events.FileSavedEvent;
import org.nmcpye.activitiesmanagement.extended.fileresource.events.ImageFileSavedEvent;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.util.MimeTypeUtils;

import java.io.File;
import java.util.Map;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.hasKey;
import static org.hamcrest.Matchers.is;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

public class FileResourceServiceTest {
    @Mock
    private FileResourceStore fileResourceStore;

    @Mock
    private SessionFactory sessionFactory;

    @Mock
    private FileResourceContentStore fileResourceContentStore;

    @Mock
    private ImageProcessingService imageProcessingService;

    @Mock
    private ApplicationEventPublisher fileEventPublisher;

    @Mock
    private Session session;

    @Captor
    private ArgumentCaptor<FileSavedEvent> fileSavedEventCaptor;

    @Captor
    private ArgumentCaptor<ImageFileSavedEvent> imageFileSavedEventCaptor;

    @Captor
    private ArgumentCaptor<FileDeletedEvent> fileDeletedEventCaptor;

    @Rule
    public MockitoRule rule = MockitoJUnit.rule();

    private FileResourceServiceExt subject;

    @BeforeEach
    public void setUp() {
        subject = new DefaultFileResourceService(fileResourceStore, fileResourceContentStore,
            imageProcessingService, fileEventPublisher);
    }

    @Test
    public void verifySaveFile() {
        FileResource fileResource = new FileResource("mycat.pdf", "application/pdf", 1000, "md5",
            FileResourceDomain.PUSH_ANALYSIS);

        fileResource.setUid("fileRes1");

        File file = new File("");

        when(sessionFactory.getCurrentSession()).thenReturn(session);

        subject.saveFileResource(fileResource, file);

        verify(fileResourceStore).saveObject(fileResource);
        verify(session).flush();

        verify(fileEventPublisher, times(1)).publishEvent(fileSavedEventCaptor.capture());

        FileSavedEvent event = fileSavedEventCaptor.getValue();

        assertThat(event.getFileResource(), is("fileRes1"));
        assertThat(event.getFile(), is(file));
    }

    @Test//( expected = IllegalQueryException.class )
    public void verifySaveIllegalFileTypeResourceA() {
        FileResource fileResource = new FileResource("very_evil_script.html", "text/html", 1024, "md5",
            FileResourceDomain.USER_AVATAR);

        File file = new File("very_evil_script.html");

        Assertions.assertThrows(IllegalQueryException.class, () -> {
            subject.saveFileResource(fileResource, file);
        });
    }

    @Test//( expected = IllegalQueryException.class )
    public void verifySaveIllegalFileTypeResourceB() {
        FileResource fileResource = new FileResource("suspicious_program.rpm", "application/x-rpm", 2048, "md5",
            FileResourceDomain.MESSAGE_ATTACHMENT);

        File file = new File("suspicious_program.rpm");


        Assertions.assertThrows(IllegalQueryException.class, () -> {
            subject.saveFileResource(fileResource, file);
        });
    }

    @Test
    public void verifySaveImageFile() {
        FileResource fileResource = new FileResource("test.jpeg", MimeTypeUtils.IMAGE_JPEG.toString(), 1000, "md5",
            FileResourceDomain.DATA_VALUE);

        File file = new File("");

        Map<ImageFileDimension, File> imageFiles = ImmutableMap.of(ImageFileDimension.LARGE, file);

        when(imageProcessingService.createImages(fileResource, file)).thenReturn(imageFiles);

        when(sessionFactory.getCurrentSession()).thenReturn(session);

        fileResource.setUid("imageUid1");

        subject.saveFileResource(fileResource, file);

        verify(fileResourceStore).saveObject(fileResource);
        verify(session).flush();

        verify(fileEventPublisher, times(1)).publishEvent(imageFileSavedEventCaptor.capture());

        ImageFileSavedEvent event = imageFileSavedEventCaptor.getValue();

        assertThat(event.getFileResource(), is("imageUid1"));
        assertFalse(event.getImageFiles().isEmpty());
        assertThat(event.getImageFiles().size(), is(1));
        assertThat(event.getImageFiles(), hasKey(ImageFileDimension.LARGE));
    }

    @Test
    public void verifyDeleteFile() {
        FileResource fileResource = new FileResource("test.pdf", "application/pdf", 1000, "md5",
            FileResourceDomain.DOCUMENT);

        fileResource.setUid("fileUid1");

        when(fileResourceStore.get(anyLong())).thenReturn(fileResource);

        subject.deleteFileResource(fileResource);

        verify(fileResourceStore).delete(fileResource);

        verify(fileEventPublisher, times(1)).publishEvent(fileDeletedEventCaptor.capture());

        FileDeletedEvent event = fileDeletedEventCaptor.getValue();

        assertThat(event.getContentType(), is("application/pdf"));
        assertThat(event.getDomain(), is(FileResourceDomain.DOCUMENT));
    }

    @Test
    public void verifySaveOrgUnitImageFile() {
        FileResource fileResource = new FileResource("test.jpeg", MimeTypeUtils.IMAGE_JPEG.toString(), 1000, "md5",
            FileResourceDomain.ORG_UNIT);

        File file = new File("");

        Map<ImageFileDimension, File> imageFiles = ImmutableMap.of(ImageFileDimension.LARGE, file);

        when(imageProcessingService.createImages(fileResource, file)).thenReturn(imageFiles);

        when(sessionFactory.getCurrentSession()).thenReturn(session);

        fileResource.setUid("imageUid1");

        subject.saveFileResource(fileResource, file);

        verify(fileResourceStore).saveObject(fileResource);
        verify(session).flush();

        verify(fileEventPublisher, times(1)).publishEvent(imageFileSavedEventCaptor.capture());

        ImageFileSavedEvent event = imageFileSavedEventCaptor.getValue();

        assertThat(event.getFileResource(), is("imageUid1"));
        assertFalse(event.getImageFiles().isEmpty());
        assertThat(event.getImageFiles().size(), is(1));
        assertThat(event.getImageFiles(), hasKey(ImageFileDimension.LARGE));
    }
}
