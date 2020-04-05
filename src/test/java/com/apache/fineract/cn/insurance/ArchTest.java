package com.apache.fineract.cn.insurance;

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
            .importPackages("com.apache.fineract.cn.insurance");

        noClasses()
            .that()
            .resideInAnyPackage("com.apache.fineract.cn.insurance.service..")
            .or()
            .resideInAnyPackage("com.apache.fineract.cn.insurance.repository..")
            .should()
            .dependOnClassesThat()
            .resideInAnyPackage("..com.apache.fineract.cn.insurance.web..")
            .because("Services and repositories should not depend on web layer")
            .check(importedClasses);
    }
}
