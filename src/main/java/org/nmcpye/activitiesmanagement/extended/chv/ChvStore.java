package org.nmcpye.activitiesmanagement.extended.chv;

import org.nmcpye.activitiesmanagement.domain.chv.Chv;
import org.nmcpye.activitiesmanagement.extended.common.IdentifiableObjectStore;

/**
 * Created by Hamza on 17-11-2021.
 */
public interface ChvStore extends IdentifiableObjectStore<Chv> {
    String ID = ChvStore.class.getName();
}
