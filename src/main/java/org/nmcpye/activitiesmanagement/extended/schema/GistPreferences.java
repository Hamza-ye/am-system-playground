package org.nmcpye.activitiesmanagement.extended.schema;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.nmcpye.activitiesmanagement.extended.schema.annotation.Gist;
import org.nmcpye.activitiesmanagement.extended.schema.annotation.Gist.Include;

/**
 * Information as extracted from {@link org.nmcpye.activitiesmanagement.extended.schema.annotation.Gist}
 * annotation to be included in {@link Property} metadata.
 *
 */
public final class GistPreferences
{
    public static final GistPreferences DEFAULT = new GistPreferences( Include.AUTO, Gist.Transform.AUTO );

    /**
     * @see Gist#included()
     */
    @JsonProperty
    private final Include included;

    /**
     * @see Gist#transformation()
     */
    @JsonProperty
    private final Gist.Transform transformation;

    public GistPreferences(Include included, Gist.Transform transformation) {
        this.included = included;
        this.transformation = transformation;
    }

    public static GistPreferences getDEFAULT() {
        return DEFAULT;
    }

    public Include getIncluded() {
        return included;
    }

    public Gist.Transform getTransformation() {
        return transformation;
    }
}
