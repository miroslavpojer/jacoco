/*******************************************************************************
 * Copyright (c) 2009, 2023 Mountainminds GmbH & Co. KG and Contributors
 * This program and the accompanying materials are made available under
 * the terms of the Eclipse Public License 2.0 which is available at
 * http://www.eclipse.org/legal/epl-2.0
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 * Contributors:
 *    Evgeny Mandrikov - initial API and implementation
 *
 *******************************************************************************/
package org.jacoco.maven;

import java.io.File;
import java.io.IOException;
import java.util.Locale;

import org.apache.maven.plugins.annotations.LifecyclePhase;
import org.apache.maven.plugins.annotations.Mojo;
import org.apache.maven.plugins.annotations.Parameter;
import org.jacoco.report.IReportGroupVisitor;

/**
 * Creates a code coverage report for tests of a single project in multiple
 * formats (HTML, XML, and CSV).
 *
 * @since 0.5.3
 */
@Mojo(name = "report", defaultPhase = LifecyclePhase.VERIFY, threadSafe = true)
public class ReportMojo extends AbstractReportMojo {

	/**
	 * Output directory for the reports. Note that this parameter is only
	 * relevant if the goal is run from the command line or from the default
	 * build lifecycle. If the goal is run indirectly as part of a site
	 * generation, the output directory configured in the Maven Site Plugin is
	 * used instead.
	 */
	@Parameter(defaultValue = "${project.reporting.outputDirectory}/jacoco")
	private File outputDirectory;

	/**
	 * File with execution data.
	 */
	@Parameter(property = "jacoco.dataFile", defaultValue = "${project.build.directory}/jacoco.exec")
	private File dataFile;

	/**
	 * If true method filtering logic will be performed.
	 */
	@Parameter(property = "do.method.filtration", defaultValue = "false")
	private Boolean doMethodFiltration;

	/**
	 * If defined used as root path for analysis of class sources, otherwise
	 * method filtering logic will be skipped.
	 */
	@Parameter(property = "src.root.dir", defaultValue = "${project.build.directory}/../src/main/scala")
	private File srcRootDir;

	/**
	 * Activate method filtering logic for scala source files.
	 */
	@Parameter(property = "do.method.filtration.scala", defaultValue = "false")
	private Boolean doScalaMethodFiltration;

	@Parameter(property = "do.manual.method.filtration.scala", defaultValue = "false")
	private Boolean doManualScalaMethodFiltration;

	@Override
	boolean canGenerateReportRegardingDataFiles() {
		return dataFile.exists();
	}

	@Override
	boolean canDoMethodFiltration() {
		return doMethodFiltration;
	}

	@Override
	boolean isScalaMethodFiltrationRequired() {
		return doScalaMethodFiltration;
	}

	boolean isManualScalaMethodFiltrationRequired() {
		return doManualScalaMethodFiltration;
	}

	@Override
	boolean canGenerateReportRegardingClassesDirectory() {
		return new File(project.getBuild().getOutputDirectory()).exists();
	}

	@Override
	void loadExecutionData(final ReportSupport support) throws IOException {
		support.loadExecutionData(dataFile);
	}

	@Override
	File getOutputDirectory() {
		return outputDirectory;
	}

	@Override
	File getSourceRootDir() {
		return srcRootDir;
	}

	@Override
	void createReport(final IReportGroupVisitor visitor,
			final ReportSupport support) throws IOException {
		support.processProject(visitor, title, project, getIncludes(),
				getExcludes(), getMethodExcludes(), sourceEncoding);
	}

	public File getReportOutputDirectory() {
		return outputDirectory;
	}

	public void setReportOutputDirectory(final File reportOutputDirectory) {
		if (reportOutputDirectory != null && !reportOutputDirectory
				.getAbsolutePath().endsWith("jacoco")) {
			outputDirectory = new File(reportOutputDirectory, "jacoco");
		} else {
			outputDirectory = reportOutputDirectory;
		}
	}

	public String getOutputName() {
		return "jacoco/index";
	}

	public String getName(final Locale locale) {
		return "JaCoCo";
	}
}
