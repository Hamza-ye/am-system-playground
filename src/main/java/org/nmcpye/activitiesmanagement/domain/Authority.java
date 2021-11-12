package org.nmcpye.activitiesmanagement.domain;

import java.io.Serializable;
import java.util.Objects;
import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.nmcpye.activitiesmanagement.domain.person.PersonAuthorityGroup;
import org.nmcpye.activitiesmanagement.extended.security.AuthorityType;

/**
 * An authority (a security role) used by Spring Security.
 */
@Entity
@Table(name = "app_authority")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class Authority implements Serializable {

    private static final long serialVersionUID = 1L;

    @NotNull
    @Size(max = 50)
    @Id
    @Column(length = 50)
    private String name;

//    AuthorityType type;
//    @ManyToOne
//    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
//    @JsonIgnoreProperties(value = { "authorities", "members" }, allowSetters = true)
//    private PersonAuthorityGroup personAuthorityGroup;

    public Authority() {}

    public Authority(@NotNull @Size(max = 50) String name) {
        this.name = name;
    }

//    public Authority(@NotNull @Size(max = 50) String name, AuthorityType type) {
//        this.name = name;
//        this.type = type;
//    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

//    public PersonAuthorityGroup getPersonAuthorityGroup() {
//        return personAuthorityGroup;
//    }
//
//    public void setPersonAuthorityGroup(PersonAuthorityGroup personAuthorityGroup) {
//        this.personAuthorityGroup = personAuthorityGroup;
//    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Authority)) {
            return false;
        }
        return Objects.equals(name, ((Authority) o).name);
    }

//    public AuthorityType getType() {
//        return type;
//    }
//
//    public void setType(AuthorityType type) {
//        this.type = type;
//    }

    @Override
    public int hashCode() {
        return Objects.hashCode(name);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Authority{" +
            "name='" + name + '\'' +
            "}";
    }
}
