package deps.app

import org.gradle.api.Plugin
import org.gradle.api.Project

/**
 * DepPlugin
 *
 * @author fionera
 * @date 2022/10/18 in hisense-android.
 */
class DepPlugin : Plugin<Project> {

  private val depDelegate = Dependencies

  override fun apply(target: Project) {
    println("Configuring by composite build")
  }

  companion object {

  }
}
