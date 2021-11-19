package org.nmcpye.activitiesmanagement.domain.document;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonRootName;
import org.nmcpye.activitiesmanagement.domain.fileresource.FileResource;
import org.nmcpye.activitiesmanagement.extended.common.BaseIdentifiableObject;
import org.nmcpye.activitiesmanagement.extended.common.DxfNamespaces;
import org.nmcpye.activitiesmanagement.extended.common.MetadataObject;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity
@Table(name = "document")
@JsonRootName( value = "document", namespace = DxfNamespaces.DXF_2_0 )
public class Document
    extends BaseIdentifiableObject implements MetadataObject {
    /**
     * Can be either a valid URL, or the path (filename) of a file. If the
     * external property is true, this should be an URL. If the external
     * property is false, this should be the filename
     */
    @Column(name = "url")
    private String url;

    /**
     * A reference to the file associated with the Document. If document
     * represents an URL or a file uploaded before this property was added, this
     * will be null.
     */
    @ManyToOne
    private FileResource fileResource;

    /**
     * Determines if this document refers to a file (!external) or URL
     * (external).
     */
    @Column(name = "external")
    private boolean external;

    /**
     * The content type of the file referred to by the document, or null if
     * document refers to an URL
     */
    @Column(name = "content_type")
    private String contentType;

    /**
     * Flags whether the file should be displayed in-browser or downloaded. true
     * should trigger a download of the file when accessing the document data
     */
    @Column(name = "attachment")
    private Boolean attachment = false;

    public Document() {
    }

    public Document(String name, String url, boolean external, String contentType) {
        this.name = name;
        this.url = url;
        this.external = external;
        this.contentType = contentType;
    }

    @JsonProperty
    // @Property( PropertyType.URL )
    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    @JsonProperty
    public boolean isExternal() {
        return external;
    }

    public void setExternal(boolean external) {
        this.external = external;
    }

    @JsonProperty
    public String getContentType() {
        return contentType;
    }

    public void setContentType(String contentType) {
        this.contentType = contentType;
    }

    @JsonProperty
    public Boolean getAttachment() {
        return attachment;
    }

    public void setAttachment(Boolean attachment) {
        this.attachment = attachment;
    }

    // Should not be exposed in the api
    public FileResource getFileResource() {
        return fileResource;
    }

    public void setFileResource(FileResource fileResource) {
        this.fileResource = fileResource;
    }
}
