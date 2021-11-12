package org.nmcpye.activitiesmanagement.extended.web.webdomain;

import com.fasterxml.jackson.annotation.JsonProperty;
import org.nmcpye.activitiesmanagement.extended.common.Pager;
import org.nmcpye.activitiesmanagement.extended.dxf2module.metadata.Metadata;

public class WebMetadata
    extends Metadata {
    private Pager pager;

    @JsonProperty
    public Pager getPager() {
        return pager;
    }

    public void setPager(Pager pager) {
        this.pager = pager;
    }
}
