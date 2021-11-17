package org.nmcpye.activitiesmanagement.extended.chv;

import org.nmcpye.activitiesmanagement.domain.chv.CHV;
import org.nmcpye.activitiesmanagement.extended.common.IdentifiableObjectStore;

/**
 * Created by Hamza on 16/11/2021.
 */
public interface ChvStore extends IdentifiableObjectStore<CHV> {
    String ID = ChvStore.class.getName();
}
