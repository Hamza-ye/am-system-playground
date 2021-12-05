package org.nmcpye.activitiesmanagement.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonRootName;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.nmcpye.activitiesmanagement.extended.common.BaseIdentifiableObject;
import org.nmcpye.activitiesmanagement.extended.common.DxfNamespaces;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.io.Serializable;
import java.time.Instant;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

/**
 * A RelatedLink.
 */
@Entity
@Table(name = "related_link")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@JsonRootName(value = "relatedLink", namespace = DxfNamespaces.DXF_2_0)
public class RelatedLink extends BaseIdentifiableObject {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    private Long id;

    @NotNull
    @Size(max = 11)
    @Column(name = "uid", length = 11, nullable = false, unique = true)
    private String uid;

    @Column(name = "name")
    private String name;

    @Column(name = "created")
    private Date created;

    @Column(name = "last_updated")
    private Date lastUpdated;

    @Column(name = "url")
    private String url;

    @ManyToMany(mappedBy = "relatedLinks")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "imageAlbum", "createdBy", "lastUpdatedBy", "relatedLinks" }, allowSetters = true)
    private Set<ContentPage> contentPages = new HashSet<>();

    @Override
    public Long getId() {
        return id;
    }

    @Override
    public void setId(Long id) {
        this.id = id;
    }

    public RelatedLink id(Long id) {
        this.id = id;
        return this;
    }

    @Override
    public String getUid() {
        return this.uid;
    }

    public RelatedLink uid(String uid) {
        this.uid = uid;
        return this;
    }

    @Override
    public void setUid(String uid) {
        this.uid = uid;
    }

    @Override
    public String getName() {
        return this.name;
    }

    public RelatedLink name(String name) {
        this.name = name;
        return this;
    }

    @Override
    public void setName(String name) {
        this.name = name;
    }

    @Override
    public Date getCreated() {
        return this.created;
    }

    public RelatedLink created(Date created) {
        this.created = created;
        return this;
    }

    @Override
    public void setCreated(Date created) {
        this.created = created;
    }

    @Override
    public Date getLastUpdated() {
        return this.lastUpdated;
    }

    public RelatedLink lastUpdated(Date lastUpdated) {
        this.lastUpdated = lastUpdated;
        return this;
    }

    @Override
    public void setLastUpdated(Date lastUpdated) {
        this.lastUpdated = lastUpdated;
    }

    @JsonProperty
    public String getUrl() {
        return this.url;
    }

    public RelatedLink url(String url) {
        this.url = url;
        return this;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    @JsonProperty
    @JsonSerialize(contentAs = BaseIdentifiableObject.class)
    public Set<ContentPage> getContentPages() {
        return this.contentPages;
    }

    public RelatedLink contentPages(Set<ContentPage> contentPages) {
        this.setContentPages(contentPages);
        return this;
    }

    public RelatedLink addContentPage(ContentPage contentPage) {
        this.contentPages.add(contentPage);
        contentPage.getRelatedLinks().add(this);
        return this;
    }

    public RelatedLink removeContentPage(ContentPage contentPage) {
        this.contentPages.remove(contentPage);
        contentPage.getRelatedLinks().remove(this);
        return this;
    }

    public void setContentPages(Set<ContentPage> contentPages) {
        if (this.contentPages != null) {
            this.contentPages.forEach(i -> i.removeRelatedLink(this));
        }
        if (contentPages != null) {
            contentPages.forEach(i -> i.addRelatedLink(this));
        }
        this.contentPages = contentPages;
    }
}
