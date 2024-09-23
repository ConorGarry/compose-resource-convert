**Convert xml-based color resource library to a Jetpack Compose color object.**

Reduce the error-prone manual conversion of Android XML color resources to Jetpack Compose color objects.

- Maintains original color names, converts lower_snake to camelCase.
- Facilitates adding package and object names.
- Maintains original alpha values.

```
Usage: resourceconvert [<options>]

Options:
  -i, --input=<text>    The Android XML color resource file
  -o, --output=<text>   Output file for Compose colors
  -n, --name=<text>     Optional name for the object
  -p, --package=<text>  Package
  -h, --help            Show this message and exit
```

#### Usage:
Build with `./gradlew assemble`

```shell
./build/install/resourceconvert/bin/resourceconvert -i colors.xml -o Colors.kt
```
Converts:
```xml
<resources>
    <color name="my_primary_brand">#77AABB</color>
    <color name="my_alpha">#2277AABB</color>
</resources>
```
to:
```kotlin
package add.packagename
                                         
import androidx.compose.ui.graphics.Color
                                         
object Colors {
    val MyPrimaryBrand = Color(0xFF77AABB)
    val MyAlpha = Color(0x2277AABB)
}
```

#### TODO:
- [ ] Add support for other resource types.
- [ ] Use value class with companion object instead of `object`, similar to how Color is implemented in Compose source.
- [ ] Add support for xml values that reference other resources.