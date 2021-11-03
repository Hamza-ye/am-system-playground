package org.nmcpye.activitiesmanagement.extended.scheduling.parameters.jackson;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import org.nmcpye.activitiesmanagement.extended.scheduling.parameters.MetadataSyncJobParameters;

public class MetadataSyncJobParametersDeserializer extends AbstractJobParametersDeserializer<MetadataSyncJobParameters> {

    public MetadataSyncJobParametersDeserializer() {
        super(MetadataSyncJobParameters.class, CustomJobParameters.class);
    }

    @JsonDeserialize
    private static class CustomJobParameters extends MetadataSyncJobParameters {}
}
