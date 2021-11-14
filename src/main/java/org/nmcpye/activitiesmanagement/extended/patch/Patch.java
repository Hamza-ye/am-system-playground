package org.nmcpye.activitiesmanagement.extended.patch;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.common.base.MoreObjects;

import java.util.ArrayList;
import java.util.List;

public class Patch {
    private List<Mutation> mutations = new ArrayList<>();

    public Patch() {
    }

    public Patch(List<Mutation> mutations) {
        this.mutations = mutations;
    }

    @JsonProperty
    public List<Mutation> getMutations() {
        return mutations;
    }

    public void setMutations(List<Mutation> mutations) {
        this.mutations = mutations;
    }

    public Patch addMutation(Mutation mutation) {
        if (mutation != null) {
            mutations.add(mutation);
        }

        return this;
    }

    @Override
    public String toString() {
        return MoreObjects.toStringHelper(this)
            .add("mutations", mutations)
            .toString();
    }
}
