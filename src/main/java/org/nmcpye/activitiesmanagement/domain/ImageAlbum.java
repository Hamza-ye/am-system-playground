package org.nmcpye.activitiesmanagement.domain;

import com.fasterxml.jackson.annotation.JsonProperty;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;
import org.nmcpye.activitiesmanagement.domain.fileresource.FileResource;
import org.nmcpye.activitiesmanagement.extended.common.BaseIdentifiableObject;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

/**
 * A ImageAlbum.
 */
@Entity
@Table(name = "image_album")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class ImageAlbum extends BaseIdentifiableObject {

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

    @Column(name = "cover_image_uid")
    private String coverImageUid;

    @Column(name = "subtitle")
    private String subtitle;

    @OneToMany(cascade = CascadeType.ALL)//(mappedBy = "imagesAlbum")
    @JoinTable(name = "image_album_images", joinColumns = @JoinColumn(name = "image_album_id"),
        inverseJoinColumns = @JoinColumn(name = "image_id"))
    @Fetch(FetchMode.JOIN)
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    private Set<FileResource> images = new HashSet<>();

    @Override
    public Long getId() {
        return id;
    }

    @Override
    public void setId(Long id) {
        this.id = id;
    }

    public ImageAlbum id(Long id) {
        this.id = id;
        return this;
    }

    @Override
    public String getUid() {
        return this.uid;
    }

    public ImageAlbum uid(String uid) {
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

    public ImageAlbum code(String code) {
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

    public ImageAlbum name(String name) {
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

    public ImageAlbum created(Date created) {
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

    public ImageAlbum lastUpdated(Date lastUpdated) {
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

    public ImageAlbum title(String title) {
        this.title = title;
        return this;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    @JsonProperty
    public String getCoverImageUid() {
        return this.coverImageUid;
    }

    public ImageAlbum coverImageUid(String coverImageUid) {
        this.coverImageUid = coverImageUid;
        return this;
    }

    public void setCoverImageUid(String coverImageUid) {
        this.coverImageUid = coverImageUid;
    }

    @JsonProperty
    public String getSubtitle() {
        return this.subtitle;
    }

    public ImageAlbum subtitle(String subtitle) {
        this.subtitle = subtitle;
        return this;
    }

    public void setSubtitle(String subtitle) {
        this.subtitle = subtitle;
    }

    @JsonProperty
    public Set<FileResource> getImages() {
        return this.images;
    }

    public ImageAlbum images(Set<FileResource> fileResources) {
        this.setImages(fileResources);
        return this;
    }

    public ImageAlbum addImage(FileResource fileResource) {
        this.images.add(fileResource);
//        fileResource.setImageAlbum(this);
        return this;
    }

    public ImageAlbum removeImage(FileResource fileResource) {
        this.images.remove(fileResource);
//        fileResource.setImageAlbum(null);
        return this;
    }

    public void setImages(Set<FileResource> fileResources) {
//        if (this.images != null) {
//            this.images.forEach(i -> i.setImageAlbum(null));
//        }
//        if (fileResources != null) {
//            fileResources.forEach(i -> i.setImageAlbum(this));
//        }
        this.images = fileResources;
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
}
