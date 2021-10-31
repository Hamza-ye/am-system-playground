package org.nmcpye.activitiesmanagement;

import static com.tngtech.archunit.lang.syntax.ArchRuleDefinition.noClasses;

import com.tngtech.archunit.core.domain.JavaClasses;
import com.tngtech.archunit.core.importer.ClassFileImporter;
import com.tngtech.archunit.core.importer.ImportOption;
import org.junit.jupiter.api.Test;

class ArchTest {

    @Test
    void servicesAndRepositoriesShouldNotDependOnWebLayer() {
        JavaClasses importedClasses = new ClassFileImporter()
            .withImportOption(ImportOption.Predefined.DO_NOT_INCLUDE_TESTS)
            .importPackages("org.nmcpye.activitiesmanagement");

        noClasses()
            .that()
            .resideInAnyPackage("org.nmcpye.activitiesmanagement.service..")
            .or()
            .resideInAnyPackage("org.nmcpye.activitiesmanagement.repository..")
            .should()
            .dependOnClassesThat()
            .resideInAnyPackage("..org.nmcpye.activitiesmanagement.web..")
            .because("Services and repositories should not depend on web layer")
            .check(importedClasses);
    }
}
