import com.android.build.api.dsl.LibraryExtension
import com.youhajun.convention.configureAndroidCompose
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.configure
import org.gradle.kotlin.dsl.getByType

class AndroidLibraryComposeConventionPlugin : Plugin<Project> {

    override fun apply(target: Project) {
        with(target) {
            with(pluginManager) {
                apply("com.android.library")
            }

            extensions.configure<LibraryExtension> {
                val extension = extensions.getByType<LibraryExtension>()
                configureAndroidCompose(extension)
            }
        }
    }
}
