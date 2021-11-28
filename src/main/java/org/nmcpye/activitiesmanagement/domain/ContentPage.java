package org.nmcpye.activitiesmanagement.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.nmcpye.activitiesmanagement.extended.common.BaseIdentifiableObject;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

/**
 * A ContentPage.
 */
@Entity
@Table(name = "content_page")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class ContentPage extends BaseIdentifiableObject {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    private Long id;

    @NotNull
    @Size(max = 11)
    @Column(name = "uid", length = 11, nullable = false, unique = true)
    private String uid;

    @Column(name = "code", unique = true)
    private String code;

    @Column(name = "name")
    private String name;

    @Column(name = "created")
    private Date created;

    @Column(name = "last_updated")
    private Date lastUpdated;

    /**
     * Owner of this object.
     */
    @ManyToOne
    @JoinColumn(name = "created_by")
    protected User createdBy;

    /**
     * Last user updated this object.
     */
    @ManyToOne
    @JoinColumn(name = "last_updated_by")
    protected User lastUpdatedBy;

    @Column(name = "title")
    private String title;

    @Column(name = "subtitle")
    private String subtitle;

    @Size(max = 3000)
    @Column(name = "content", length = 3000)
    private String content;

    @Column(name = "active")
    private Boolean active;

    @Column(name = "visited_count")
    private Integer visitedCount;

    @JsonIgnoreProperties(value = {"images"}, allowSetters = true)
    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(unique = true)
    private ImageAlbum imageAlbum;

    @ManyToMany
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JoinTable(
        name = "content_page_related_link",
        joinColumns = @JoinColumn(name = "content_page_id"),
        inverseJoinColumns = @JoinColumn(name = "related_link_id")
    )
    @JsonIgnoreProperties(value = { "contentPages" }, allowSetters = true)
    private Set<RelatedLink> relatedLinks = new HashSet<>();

    @Override
    public Long getId() {
        return id;
    }

    @Override
    public void setId(Long id) {
        this.id = id;
    }

    public ContentPage id(Long id) {
        this.id = id;
        return this;
    }

    @Override
    public String getUid() {
        return this.uid;
    }

    public ContentPage uid(String uid) {
        this.uid = uid;
        return this;
    }

    @Override
    public void setUid(String uid) {
        this.uid = uid;
    }

    @Override
    public String getCode() {
        return this.code;
    }

    public ContentPage code(String code) {
        this.code = code;
        return this;
    }

    @Override
    public void setCode(String code) {
        this.code = code;
    }

    @Override
    public String getName() {
        return this.name;
    }

    public ContentPage name(String name) {
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

    public ContentPage created(Date created) {
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

    public ContentPage lastUpdated(Date lastUpdated) {
        this.lastUpdated = lastUpdated;
        return this;
    }

    @Override
    public void setLastUpdated(Date lastUpdated) {
        this.lastUpdated = lastUpdated;
    }

    @JsonProperty
    public String getTitle() {
        return this.title;
    }

    public ContentPage title(String title) {
        this.title = title;
        return this;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    @JsonProperty
    public String getSubtitle() {
        return this.subtitle;
    }

    public ContentPage subtitle(String subtitle) {
        this.subtitle = subtitle;
        return this;
    }

    public void setSubtitle(String subtitle) {
        this.subtitle = subtitle;
    }

    @JsonProperty
    public String getContent() {
        return this.content;
    }

    public ContentPage content(String content) {
        this.content = content;
        return this;
    }

    public void setContent(String content) {
        this.content = content;
    }

    @JsonProperty
    public Boolean getActive() {
        return this.active;
    }

    public ContentPage active(Boolean active) {
        this.active = active;
        return this;
    }

    public void setActive(Boolean active) {
        this.active = active;
    }

    @JsonProperty
    public Integer getVisitedCount() {
        return this.visitedCount;
    }

    public ContentPage visitedCount(Integer visitedCount) {
        this.visitedCount = visitedCount;
        return this;
    }

    public void setVisitedCount(Integer visitedCount) {
        this.visitedCount = visitedCount;
    }

    @JsonProperty
    public ImageAlbum getImageAlbum() {
        return this.imageAlbum;
    }

    public ContentPage imagesAlbum(ImageAlbum imageAlbum) {
        this.setImageAlbum(imageAlbum);
        return this;
    }

    public void setImageAlbum(ImageAlbum imageAlbum) {
        this.imageAlbum = imageAlbum;
    }

    @Override
    public User getCreatedBy() {
        return createdBy;
    }

    @Override
    public void setCreatedBy(User createdBy) {
        this.createdBy = createdBy;
    }

    @Override
    public User getLastUpdatedBy() {
        return lastUpdatedBy;
    }

    @Override
    public void setLastUpdatedBy(User lastUpdatedBy) {
        this.lastUpdatedBy = lastUpdatedBy;
    }

    @JsonProperty
    @JsonSerialize(contentAs = BaseIdentifiableObject.class)
    public Set<RelatedLink> getRelatedLinks() {
        return this.relatedLinks;
    }

    public ContentPage relatedLinks(Set<RelatedLink> relatedLinks) {
        this.setRelatedLinks(relatedLinks);
        return this;
    }

    public ContentPage addRelatedLink(RelatedLink relatedLink) {
        this.relatedLinks.add(relatedLink);
        relatedLink.getContentPages().add(this);
        return this;
    }

    public ContentPage removeRelatedLink(RelatedLink relatedLink) {
        this.relatedLinks.remove(relatedLink);
        relatedLink.getContentPages().remove(this);
        return this;
    }

    public void setRelatedLinks(Set<RelatedLink> relatedLinks) {
        this.relatedLinks = relatedLinks;
    }

    @JsonProperty
    public Boolean hasRelatedLinks() {
        return relatedLinks != null || !relatedLinks.isEmpty();
    }
}
