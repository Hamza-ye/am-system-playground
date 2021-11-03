package org.nmcpye.activitiesmanagement.extended.resourcetable;

public enum ResourceTableType {
    ORG_UNIT_STRUCTURE("_orgunitstructure"),
    //    DATA_SET_ORG_UNIT_CATEGORY( "_datasetorganisationunitcategory" ),
    //    CATEGORY_OPTION_COMBO_NAME( "_categoryoptioncomboname" ),
    //    DATA_ELEMENT_GROUP_SET_STRUCTURE( "_dataelementgroupsetstructure" ),
    //    INDICATOR_GROUP_SET_STRUCTURE( "_indicatorgroupsetstructure" ),
    ORG_UNIT_GROUP_SET_STRUCTURE("_organisationunitgroupsetstructure"),
    //    CATEGORY_STRUCTURE( "_categorystructure" ),
    //    DATA_ELEMENT_STRUCTURE( "_dataelementstructure" ),
    PERIOD_STRUCTURE("_periodstructure"),
    DATE_PERIOD_STRUCTURE("_dateperiodstructure"); //,

    //    DATA_ELEMENT_CATEGORY_OPTION_COMBO( "_dataelementcategoryoptioncombo" ),
    //    DATA_APPROVAL_REMAP_LEVEL( "_dataapprovalremaplevel" ),
    //    DATA_APPROVAL_MIN_LEVEL( "_dataapprovalminlevel" );

    private String tableName;

    ResourceTableType(String tableName) {
        this.tableName = tableName;
    }

    public String getTableName() {
        return tableName;
    }
}
