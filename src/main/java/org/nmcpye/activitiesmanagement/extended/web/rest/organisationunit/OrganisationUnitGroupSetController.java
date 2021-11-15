package org.nmcpye.activitiesmanagement.extended.web.rest.organisationunit;

import org.nmcpye.activitiesmanagement.domain.organisationunit.OrganisationUnitGroupSet;
import org.nmcpye.activitiesmanagement.extended.schemamodule.descriptors.OrganisationUnitGroupSetSchemaDescriptor;
import org.nmcpye.activitiesmanagement.extended.web.constants.ApiEndPoint;
import org.nmcpye.activitiesmanagement.extended.web.rest.AbstractCrudController;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping(value = ApiEndPoint.API_END_POINT + OrganisationUnitGroupSetSchemaDescriptor.API_ENDPOINT)
public class OrganisationUnitGroupSetController
    extends AbstractCrudController<OrganisationUnitGroupSet> {
}
