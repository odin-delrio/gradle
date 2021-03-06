/*
 * Copyright 2014 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.gradle.api.reporting.components.internal;

import org.apache.commons.lang.StringUtils;
import org.gradle.api.tasks.diagnostics.internal.text.TextReportBuilder;
import org.gradle.internal.text.TreeFormatter;
import org.gradle.language.base.LanguageSourceSet;
import org.gradle.logging.StyledTextOutput;
import org.gradle.model.ModelMap;
import org.gradle.platform.base.BinarySpec;
import org.gradle.platform.base.internal.BinaryBuildAbility;
import org.gradle.platform.base.internal.BinarySpecInternal;
import org.gradle.reporting.ReportRenderer;

// TODO - bust up this hierarchy and compose using interfaces instead
public abstract class AbstractBinaryRenderer<T extends BinarySpec> extends ReportRenderer<BinarySpec, TextReportBuilder> {
    public void render(BinarySpec binary, TextReportBuilder builder) {
        StyledTextOutput textOutput = builder.getOutput();

        textOutput.append(StringUtils.capitalize(binary.getDisplayName()));
        if (!binary.isBuildable()) {
            textOutput.append(" (not buildable)");
        }
        textOutput.println();

        builder.item("build using task", binary.getBuildTask().getPath());

        T specialized = getTargetType().cast(binary);

        renderTasks(specialized, builder);

        renderDetails(specialized, builder);

        renderOutputs(specialized, builder);

        renderBuildAbility(specialized, builder);

        renderOwnedSourceSets(specialized, builder);
    }

    public abstract Class<T> getTargetType();

    protected void renderOutputs(T binary, TextReportBuilder builder) {
    }

    protected void renderDetails(T binary, TextReportBuilder builder) {
    }

    protected void renderTasks(T binary, TextReportBuilder builder) {
    }

    private void renderBuildAbility(BinarySpec binary, TextReportBuilder builder) {
        BinaryBuildAbility buildAbility = ((BinarySpecInternal) binary).getBuildAbility();
        if (!buildAbility.isBuildable()) {
            TreeFormatter formatter = new TreeFormatter();
            buildAbility.explain(formatter);
            builder.item(formatter.toString());
        }
    }

    protected void renderOwnedSourceSets(T binary, TextReportBuilder builder) {
        if (((BinarySpecInternal) binary).isLegacyBinary()) {
            return;
        }
        ModelMap<LanguageSourceSet> sources = binary.getSources();
        if (!sources.isEmpty()) {
            SourceSetRenderer sourceSetRenderer = new SourceSetRenderer();
            builder.itemCollection("source sets", sources.values(), sourceSetRenderer, "owned source sets");
        }
    }
}
