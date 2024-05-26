package pl.kuponik.architecture;

import com.tngtech.archunit.base.DescribedPredicate;
import com.tngtech.archunit.core.domain.JavaClass;
import com.tngtech.archunit.core.importer.ClassFileImporter;
import org.junit.jupiter.api.Test;
import pl.kuponik.loyalty.LoyaltyFacade;

import static com.tngtech.archunit.lang.syntax.ArchRuleDefinition.noClasses;

class ArchitectureTest {

    public static final String BASE_PACKAGE = "pl.kuponik";

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
    void packagesShouldNotExist() {
        noClasses()
                .should().resideInAnyPackage(
                        "pl.kuponik.domain",
                        "pl.kuponik.infrastructure",
                        "pl.kuponik.application")
                .because("After modularization, these packages may exist but only within specific modules")
                .check(new ClassFileImporter().importPackages(BASE_PACKAGE));
    }

    @Test
    void loyaltyModuleShouldNotDependOnCouponModule() {
        noClasses()
                .that()
                .resideInAPackage("..pl.kuponik.loyalty..")
                .should()
                .dependOnClassesThat()
                .resideInAPackage("..pl.kuponik.coupon..")
                .check(new ClassFileImporter().importPackages(BASE_PACKAGE));
    }

    @Test
    void couponModuleShouldNotDependOnLoyaltyModuleExcludeFacade() {
        noClasses()
                .that()
                .resideInAPackage("..pl.kuponik.coupon..")
                .should()
                .dependOnClassesThat(originatesFromLoyaltyModuleAndIsNotFacade())
                .check(new ClassFileImporter().importPackages(BASE_PACKAGE));
    }

    private static DescribedPredicate<JavaClass> originatesFromLoyaltyModuleAndIsNotFacade() {
        return new DescribedPredicate<>("originates from loyalty module and is not a facade") {
            @Override
            public boolean test(JavaClass input) {
                return input.getPackageName().startsWith("pl.kuponik.loyalty")
                        && !input.isAssignableFrom(LoyaltyFacade.class);
            }
        };
    }
}