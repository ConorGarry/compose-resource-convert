package dev.conorgarry

import com.github.ajalt.clikt.core.CliktCommand
import com.github.ajalt.clikt.core.main
import com.github.ajalt.clikt.parameters.options.default
import com.github.ajalt.clikt.parameters.options.option
import com.github.ajalt.clikt.parameters.options.required
import java.io.File

fun main(args: Array<String>) = Hello().main(args)

class Hello : CliktCommand() {
    private val inputFile by option("-i", "--input", help = "The Android XML color resource file").required()
    private val outputFile by option("-o", "--output", help = "Output file for Compose colors").required()
    private val name by option("-n", "--name", help = "Optional name for the object").default("Colors")
    private val `package` by option("-p", "--package", help = "Package").default("add.packagename")

    override fun run() {
        val xmlContent = File(inputFile).readText()
        val composeColors = convertXmlToCompose(xmlContent)
        File(outputFile).writeText(composeColors)
        println("Successfully converted $inputFile to Compose colors and saved to $outputFile")
    }

    // Function to convert XML color definitions to Jetpack Compose colors
    private fun convertXmlToCompose(xml: String): String {
        val colorMap = mutableMapOf<String, String>()
        val refMap = mutableMapOf<String, String>()
        // Regex to find both color values and references to other colors
        val regex = """<color name="(\w+)">(#([0-9a-fA-F]{6,8})|@\w+/(\w+))</color>""".toRegex()
        val matches = regex.findAll(xml)
        matches.forEach { match ->
            val colorName = match.groupValues[1]
            val colorValue = match.groupValues[2]

            // Store color value or reference
            if (colorValue.startsWith("#")) {
                colorMap[colorName] = colorValue
            } else {
                refMap[colorName] = match.groupValues[4]
            }
        }

        fun String.colorValue() = with(removePrefix("#")) { (if (length == 6) "0xFF" else "0x") + this }

        // Convert the map into Jetpack Compose colors
        val composeColors =
            colorMap
                .map { (name, value) ->
                    val formattedName =
                        name
                            .replace('_', ' ')
                            .split(" ")
                            .joinToString("") { it.capitalize() }
                    "\tval $formattedName = Color(${value.colorValue()})"
                }.joinToString("\n")
                .trim()

        return buildString {
            appendLine("package $`package`").appendLine()
            appendLine("import androidx.compose.ui.graphics.Color").appendLine()
            appendLine("object $name {")
            appendLine("\t$composeColors")
            appendLine("}")
        }
    }
}
