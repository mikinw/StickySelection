plugins {
    id("org.jetbrains.intellij") version "1.3.0"
    java
}

group = "com.mnw.stickyselection"
version = "3.1-SNAPSHOT"

repositories {
    mavenCentral()
}

dependencies {
    testImplementation("org.junit.jupiter:junit-jupiter-api:5.6.0")
    testRuntimeOnly("org.junit.jupiter:junit-jupiter-engine")
}

// See https://github.com/JetBrains/gradle-intellij-plugin/
intellij {
    version.set("2021.2")
}
tasks {
    patchPluginXml {
        changeNotes.set("""
                  v1.0 this is the first release<br>
      v1.2 <br>
      v2.0 redesign and lots of new features (navigation, convert, arbitrary number of groups, visual hints, undo)<br>
      v2.1 persisting selections when IDE gets closed<br>
      v2.2 fixed: exception during first start, fixed: incorrectly restored Paint after reopening the IDE (when the document has changed) <br>
      v2.3 fixed: caret can jump to deleted highlight, fixed: preferences page does not respond to Paint Group changes, fixed: exception after "paint, convert, paint, undo" <br>
      v2.4 fixed: exception when changing color, fixed: lingering highlights when changing color <br>
      v3.0 refactored components that were using deprecated api, increased min version, migrated to gradle, fixed: layer is not saved, fixed: highlights
      are not updated when changing settings, fixed: exception when deleting shortcut or layer field in settings, alpha channel is saved with the color, fixed: exception when using next/prev  <br>        
      v3.1 option to paint only the selected text, but not all the similar; option to clear highlight at caret only, but not the whole group; fixed: memory leak, fixed: saved highlights are not updated when document changes <br>
      """.trimIndent())

    }
}
tasks.getByName<Test>("test") {
    useJUnitPlatform()
}
