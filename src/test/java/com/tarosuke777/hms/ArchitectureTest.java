package com.tarosuke777.hms;

import static com.tngtech.archunit.library.Architectures.*;
import com.tngtech.archunit.core.importer.ImportOption;
import com.tngtech.archunit.core.importer.Location;
import com.tngtech.archunit.junit.AnalyzeClasses;
import com.tngtech.archunit.junit.ArchTest;
import com.tngtech.archunit.lang.ArchRule;

@AnalyzeClasses(packages = "com.tarosuke777.hms", importOptions = {
        ImportOption.DoNotIncludeTests.class, ArchitectureTest.ExcludeConfigAndMain.class})
class ArchitectureTest {

    // 内部クラスとして除外条件を定義
    static class ExcludeConfigAndMain implements ImportOption {
        @Override
        public boolean includes(Location location) {
            return !location.contains("HmsApplication");
        }
    }

    // @formatter:off
    @ArchTest
    static final ArchRule layer_dependencies_are_respected = layeredArchitecture()
            .consideringAllDependencies()
            .layer("Controller").definedBy("..controller..")
            .layer("Service")   .definedBy("..domain..", "..service..")
            .layer("Repository").definedBy("..repository..")

            .whereLayer("Controller").mayNotBeAccessedByAnyLayer()
            .whereLayer("Service")   .mayOnlyBeAccessedByLayers("Controller")
            .whereLayer("Repository").mayOnlyBeAccessedByLayers("Service");
    // @formatter:on
}
