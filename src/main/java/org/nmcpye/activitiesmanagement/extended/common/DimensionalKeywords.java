package org.nmcpye.activitiesmanagement.extended.common;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class DimensionalKeywords {

    public class Keyword {

        private String key;

        private String uid;

        private String name;

        private String code;

        Keyword(String key, String uid, String name, String code) {
            this.key = key;
            this.uid = uid;
            this.name = name;
            this.code = code;
        }

        public String getKey() {
            return key;
        }

        public String getUid() {
            return uid;
        }

        public String getName() {
            return name;
        }

        public String getCode() {
            return code;
        }
    }

    private List<Keyword> groupBy;

    public DimensionalKeywords(List<IdentifiableObject> groupBy) {
        this.groupBy = new ArrayList<>();

        this.groupBy.addAll(groupBy.stream().map(this::toKeyword).collect(Collectors.toList()));
    }

    public DimensionalKeywords() {
        this.groupBy = new ArrayList<>();
    }

    public void addGroupBy(IdentifiableObject groupByItem) {
        this.groupBy.add(toKeyword(groupByItem));
    }

    public void addGroupBy(List<? extends IdentifiableObject> groupByItems) {
        for (IdentifiableObject item : groupByItems) {
            this.addGroupBy(item);
        }
    }

    public void addGroupBy(String key, String name) {
        this.groupBy.add(new Keyword(key, null, name, null));
    }

    public List<Keyword> getGroupBy() {
        return groupBy;
    }

    private Keyword toKeyword(IdentifiableObject object) {
        return new Keyword(object.getUid(), object.getUid(), object.getName(), object.getCode());
    }

    public boolean isEmpty() {
        return groupBy.isEmpty();
    }
}
