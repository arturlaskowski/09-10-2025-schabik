package pl.schabik.architecture;

import com.tngtech.archunit.core.domain.JavaClasses;
import com.tngtech.archunit.core.importer.ClassFileImporter;
import org.junit.jupiter.api.Test;

import static com.tngtech.archunit.library.Architectures.layeredArchitecture;

class ArchitectureTest {

    private static final String BASE_PACKAGE = "pl.schabik";
    private final JavaClasses classes = new ClassFileImporter().importPackages(BASE_PACKAGE);

    @Test
    void checkModuleDependencies() {
        layeredArchitecture().consideringOnlyDependenciesInLayers()
                .layer("customer").definedBy("pl.schabik.customer..")
                .layer("order").definedBy("pl.schabik.order..")
                .whereLayer("customer").mayNotAccessAnyLayer()
                .whereLayer("order").mayNotAccessAnyLayer()
                .check(classes);
    }

    @Test
    void checkOrderDependencies() {
        layeredArchitecture().consideringOnlyDependenciesInLayers()
                .layer("domain").definedBy("pl.schabik.order.domain..")
                .layer("command").definedBy("pl.schabik.order.command..")
                .layer("query").definedBy("pl.schabik.order.query..")
                .layer("replication").definedBy("pl.schabik.order.replication..")
                .layer("web").definedBy("pl.schabik.order.web..")
                .whereLayer("domain").mayNotAccessAnyLayer()
                .whereLayer("command").mayOnlyAccessLayers("domain", "replication")
                .whereLayer("query").mayOnlyAccessLayers("domain", "replication", "web")
                .check(classes);
    }
}


