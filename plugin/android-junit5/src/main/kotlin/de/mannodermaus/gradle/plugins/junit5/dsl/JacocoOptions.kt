package de.mannodermaus.gradle.plugins.junit5.dsl

import org.gradle.api.Project
import org.gradle.api.tasks.Input
import java.io.File
import javax.inject.Inject

/**
 * Options for controlling Jacoco reporting
 */
public abstract class JacocoOptions
@Inject constructor(project: Project) {

    public operator fun invoke(config: JacocoOptions.() -> Unit) {
        this.config()
    }

    /**
     * Whether to enable Jacoco task integration
     */
    @get:Input
    public var taskGenerationEnabled: Boolean = true

    public fun taskGenerationEnabled(state: Boolean) {
        this.taskGenerationEnabled = state
    }

    private val _onlyGenerateTasksForVariants = mutableSetOf<String>()

    @get:Input
    public val onlyGenerateTasksForVariants: Set<String>
        get() = _onlyGenerateTasksForVariants

    /**
     * Filter the generated Jacoco tasks,
     * so that only the given build variants are provided with a companion task.
     * Make sure to add the full product flavor name if necessary
     * (i.e. "paidDebug" if you use a "paid" product flavor and the "debug" build type)
     */
    public fun onlyGenerateTasksForVariants(vararg variants: String) {
        _onlyGenerateTasksForVariants.addAll(variants)
    }

    /**
     * Options for controlling the HTML Report generated by Jacoco
     */
    public val html: Report = project.objects.newInstance(Report::class.java)

    /**
     * Options for controlling the CSV Report generated by Jacoco
     */
    public val csv: Report = project.objects.newInstance(Report::class.java)

    /**
     * Options for controlling the XML Report generated by Jacoco
     */
    public val xml: Report = project.objects.newInstance(Report::class.java)

    /**
     * List of class name patterns that should be excluded from being processed by Jacoco.
     * By default, this will exclude R.class & BuildConfig.class
     */
    public var excludedClasses: MutableList<String> =
            mutableListOf("**/R.class", "**/R$*.class", "**/BuildConfig.*")

    public fun excludedClasses(vararg classes: String) {
        excludedClasses.addAll(classes)
    }

    public abstract class Report {

        public operator fun invoke(config: Report.() -> Unit) {
            this.config()
        }

        /**
         * Whether or not this report should be generated
         */
        public var enabled: Boolean = true

        public fun enabled(state: Boolean) {
            this.enabled = state
        }

        /**
         * Name of the file to be generated; note that
         * due to the variant-aware nature of the plugin,
         * each variant will be assigned a distinct folder if necessary
         */
        public var destination: File? = null

        public fun destination(file: File?) {
            this.destination = file
        }
    }
}
