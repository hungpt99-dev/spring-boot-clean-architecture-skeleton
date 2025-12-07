package com.yourorg.yourapp.architecture;

import static com.tngtech.archunit.lang.syntax.ArchRuleDefinition.classes;
import static com.tngtech.archunit.lang.syntax.ArchRuleDefinition.noClasses;

import com.tngtech.archunit.core.importer.ImportOption;
import com.tngtech.archunit.junit.AnalyzeClasses;
import com.tngtech.archunit.junit.ArchTest;
import com.tngtech.archunit.lang.ArchRule;
import com.tngtech.archunit.library.dependencies.SlicesRuleDefinition;
import com.yourorg.yourapp.config.Application;

/**
 * Architectural guardrails to keep Clean Architecture boundaries in place.
 * These run in CI so any violation blocks merge.
 */
@AnalyzeClasses(
    packagesOf = Application.class,
    importOptions = {
        ImportOption.DoNotIncludeTests.class,
        ImportOption.DoNotIncludeJars.class
    }
)
public class CleanArchitectureTest {

    private static final String BASE_PACKAGE = deriveBasePackage();
    private static final String DOMAIN_PACKAGE = BASE_PACKAGE + ".domain..";
    private static final String USECASE_PACKAGE = BASE_PACKAGE + ".usecase..";
    private static final String ADAPTER_PACKAGE = BASE_PACKAGE + ".adapter..";
    private static final String APPLICATION_PACKAGE = BASE_PACKAGE + ".config..";

    private static String deriveBasePackage() {
        String appPackage = Application.class.getPackageName();
        return appPackage.endsWith(".config")
            ? appPackage.substring(0, appPackage.length() - ".config".length())
            : appPackage;
    }

    @ArchTest
    static final ArchRule domain_should_be_isolated =
        noClasses()
            .that().resideInAPackage(DOMAIN_PACKAGE)
            .should().dependOnClassesThat()
            .resideInAnyPackage(
                USECASE_PACKAGE,
                ADAPTER_PACKAGE,
                APPLICATION_PACKAGE,
                "org.springframework..",
                "jakarta..",
                "javax.."
            );

    @ArchTest
    static final ArchRule usecase_should_not_depend_on_frameworks_or_outer_layers =
        noClasses()
            .that().resideInAPackage(USECASE_PACKAGE)
            .should().dependOnClassesThat()
            .resideInAnyPackage(
                ADAPTER_PACKAGE,
                APPLICATION_PACKAGE,
                "org.springframework..",
                "jakarta..",
                "javax.."
            );

    @ArchTest
    static final ArchRule adapter_should_not_depend_on_application_layer =
        noClasses()
            .that().resideInAPackage(ADAPTER_PACKAGE)
            .should().dependOnClassesThat()
            .resideInAPackage(APPLICATION_PACKAGE);

    @ArchTest
    static final ArchRule layers_should_be_acyclic =
        SlicesRuleDefinition.slices()
            .matching(BASE_PACKAGE + ".(*)..")
            .should().beFreeOfCycles();
}


