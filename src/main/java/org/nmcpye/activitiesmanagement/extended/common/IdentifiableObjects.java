package org.nmcpye.activitiesmanagement.extended.common;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.common.base.MoreObjects;

import java.util.ArrayList;
import java.util.List;

public class IdentifiableObjects {
    private List<BaseIdentifiableObject> additions = new ArrayList<>();

    private List<BaseIdentifiableObject> deletions = new ArrayList<>();

    public IdentifiableObjects() {
    }

    @JsonProperty
    public List<BaseIdentifiableObject> getAdditions() {
        return additions;
    }

    public void setAdditions(List<BaseIdentifiableObject> additions) {
        this.additions = additions;
    }

    @JsonProperty
    public List<BaseIdentifiableObject> getDeletions() {
        return deletions;
    }

    public void setDeletions(List<BaseIdentifiableObject> deletions) {
        this.deletions = deletions;
    }

    @JsonProperty
    public List<BaseIdentifiableObject> getIdentifiableObjects() {
        return additions;
    }

    public void setIdentifiableObjects(List<BaseIdentifiableObject> identifiableObjects) {
        this.additions = identifiableObjects;
    }

    @Override
    public String toString() {
        return MoreObjects.toStringHelper(this)
            .add("additions", additions)
            .add("deletions", deletions)
            .toString();
    }
}
