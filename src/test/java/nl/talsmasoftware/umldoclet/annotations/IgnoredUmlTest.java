/*
 * Copyright 2016-2018 Talsma ICT
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *        http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package nl.talsmasoftware.umldoclet.annotations;

import nl.talsmasoftware.umldoclet.UMLDoclet;
import nl.talsmasoftware.umldoclet.util.Testing;
import org.junit.BeforeClass;
import org.junit.Test;

import java.io.File;
import java.util.spi.ToolProvider;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.not;

public class IgnoredUmlTest {
    private static final File outputdir = new File("target/test-annotations");
    private static final File packageUmlFile = new File(outputdir,
            IgnoredUmlTest.class.getPackageName().replace('.', '/') + "/package.puml");

    private static String packageUml;

    @BeforeClass
    public static void createPackageDocumentation() {
        assertThat("Javadoc result", ToolProvider.findFirst("javadoc").get().run(
                System.out, System.err,
                "-d", outputdir.getPath(),
                "-doclet", UMLDoclet.class.getName(),
                "-quiet",
                "-createPumlFiles",
                "-sourcepath", "src/test/java",
                IgnoredUmlTest.class.getPackageName()
        ), is(0));
    }

    @Test
    public void testCreateJavaDoc() {
        if (packageUml == null) packageUml = Testing.read(packageUmlFile);
        assertThat(packageUml, not(containsString("IgnoredInnerClass")));
    }

    @Test
    @UML(exclude = true)
    public void testExcludeMethod() {
        if (packageUml == null) packageUml = Testing.read(packageUmlFile);
        assertThat(packageUml, containsString("testCreateJavaDoc()"));
        assertThat(packageUml, not(containsString("testExcludeMethod()")));
    }

    @UML(exclude = true)
    public static class IgnoredInnerClass {

    }
}
