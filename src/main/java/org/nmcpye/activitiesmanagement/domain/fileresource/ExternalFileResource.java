package org.nmcpye.activitiesmanagement.domain.fileresource;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import org.nmcpye.activitiesmanagement.extended.common.BaseIdentifiableObject;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import java.util.Date;

@Entity
@Table(name = "external_file_resource")
public class ExternalFileResource
    extends BaseIdentifiableObject {
    /**
     * FileResource containing the file we are exposing
     */
    @OneToOne
    private FileResource fileResource;

    /**
     * The accessToken required to identify ExternalFileResources trough the api
     */
    @Column(name = "access_token")
    private String accessToken;

    /**
     * Date when the resource expires. Null means it never expires
     */
    @Column(name = "expires")
    private Date expires;

    @JsonProperty
    public Date getExpires() {
        return expires;
    }

    public void setExpires(Date expires) {
        this.expires = expires;
    }

    @JsonProperty
    public String getAccessToken() {
        return accessToken;
    }

    public void setAccessToken(String accessToken) {
        this.accessToken = accessToken;
    }

    @JsonProperty
    @JsonSerialize(as = BaseIdentifiableObject.class)
    public FileResource getFileResource() {
        return fileResource;
    }

    public void setFileResource(FileResource fileResource) {
        this.fileResource = fileResource;
    }
}
