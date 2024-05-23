package pl.schabik.architecture;

import com.tngtech.archunit.core.importer.ClassFileImporter;
import org.junit.jupiter.api.Test;

import static com.tngtech.archunit.lang.syntax.ArchRuleDefinition.noClasses;

class ArchitectureTest {

    public static final String BASE_PACKAGE = "pl.schabik";

    @Test
    void domainShouldNotDependOnSpring() {
        noClasses()
                .that()
                .resideInAPackage("..domain..")
                .should()
                .dependOnClassesThat()
                .resideInAPackage("org.springframework..")
                .check(new ClassFileImporter().importPackages(BASE_PACKAGE));
    }

    @Test
    void domainShouldNotDependOnInfrastructure() {
        noClasses()
                .that()
                .resideInAPackage("..domain..")
                .should()
                .dependOnClassesThat()
                .resideInAPackage("..infrastructure..")
                .check(new ClassFileImporter().importPackages(BASE_PACKAGE));
    }

    @Test
    void domainShouldNotDependOnApplication() {
        noClasses()
                .that()
                .resideInAPackage("..domain..")
                .should()
                .dependOnClassesThat()
                .resideInAPackage("..application..")
                .check(new ClassFileImporter().importPackages(BASE_PACKAGE));
    }

    @Test
    void applicationShouldNotDependOnInfrastructure() {
        noClasses()
                .that()
                .resideInAPackage("..application..")
                .should()
                .dependOnClassesThat()
                .resideInAPackage("..infrastructure..")
                .check(new ClassFileImporter().importPackages(BASE_PACKAGE));
    }

    @Test
    void customerModuleShouldNotDependOnOrderModule() {
        noClasses()
                .that()
                .resideInAPackage("..pl.schabik.customer..")
                .should()
                .dependOnClassesThat()
                .resideInAPackage("..pl.schabik.order..")
                .check(new ClassFileImporter().importPackages(BASE_PACKAGE));
    }

    @Test
    void orderModuleShouldNotDependOnCustomerModule() {
        noClasses()
                .that()
                .resideInAPackage("..pl.schabik.order..")
                .should()
                .dependOnClassesThat()
                .resideInAPackage("..pl.schabik.customer..")
                .check(new ClassFileImporter().importPackages(BASE_PACKAGE));
    }
}


