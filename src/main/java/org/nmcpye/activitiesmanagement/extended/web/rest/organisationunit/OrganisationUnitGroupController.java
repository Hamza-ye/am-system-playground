package org.nmcpye.activitiesmanagement.extended.web.rest.organisationunit;

import org.nmcpye.activitiesmanagement.domain.organisationunit.OrganisationUnitGroup;
import org.nmcpye.activitiesmanagement.extended.schemamodule.descriptors.OrganisationUnitGroupSchemaDescriptor;
import org.nmcpye.activitiesmanagement.extended.web.constants.ApiEndPoint;
import org.nmcpye.activitiesmanagement.extended.web.rest.AbstractCrudController;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping(value = ApiEndPoint.API_END_POINT + OrganisationUnitGroupSchemaDescriptor.API_ENDPOINT)
public class OrganisationUnitGroupController
    extends AbstractCrudController<OrganisationUnitGroup> {
}
