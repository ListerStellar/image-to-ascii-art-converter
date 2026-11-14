package converters

import image.*
import org.scalatest.funsuite.AnyFunSuite

class DefaultLinearASCIIPixelConverterTest extends AnyFunSuite:

  private val defaultTable: String = " .:-=+*#%@"

  test(
    "DefaultLinearASCIIPixelConverter correctly maps all default symbols"
  ) {
    val converter: DefaultLinearASCIIPixelConverter =
      DefaultLinearASCIIPixelConverter()

    assert(converter.convert(GreyscalePixel(0)) == ' ')
    assert(converter.convert(GreyscalePixel(25)) == ' ')
    assert(converter.convert(GreyscalePixel(26)) == '.')
    assert(converter.convert(GreyscalePixel(51)) == '.')
    assert(converter.convert(GreyscalePixel(52)) == ':')
    assert(converter.convert(GreyscalePixel(76)) == ':')
    assert(converter.convert(GreyscalePixel(77)) == '-')
    assert(converter.convert(GreyscalePixel(102)) == '-')
    assert(converter.convert(GreyscalePixel(103)) == '=')
    assert(converter.convert(GreyscalePixel(127)) == '=')
    assert(converter.convert(GreyscalePixel(128)) == '+')
    assert(converter.convert(GreyscalePixel(153)) == '+')
    assert(converter.convert(GreyscalePixel(154)) == '*')
    assert(converter.convert(GreyscalePixel(179)) == '*')
    assert(converter.convert(GreyscalePixel(180)) == '#')
    assert(converter.convert(GreyscalePixel(204)) == '#')
    assert(converter.convert(GreyscalePixel(205)) == '%')
    assert(converter.convert(GreyscalePixel(230)) == '%')
    assert(converter.convert(GreyscalePixel(231)) == '@')
    assert(converter.convert(GreyscalePixel(255)) == '@')
  }

  test(
    "DefaultLinearASCIIPixelConverter outputs exclusively characters from the default table"
  ) {
    val converter: DefaultLinearASCIIPixelConverter =
      DefaultLinearASCIIPixelConverter()

    for scale <- 0 to 255 do
      val char: Char = converter.convert(GreyscalePixel(scale))
      assert(defaultTable.contains(char))
  }

  test(
    "DefaultLinearASCIIPixelConverter maintains monotonic ordering across all scales"
  ) {
    val converter: DefaultLinearASCIIPixelConverter =
      DefaultLinearASCIIPixelConverter()

    for scale <- 0 until 255 do
      val currentIdx: Int =
        defaultTable.indexOf(converter.convert(GreyscalePixel(scale)))
      val nextIdx: Int =
        defaultTable.indexOf(
          converter.convert(GreyscalePixel(scale + 1))
        )
      assert(currentIdx <= nextIdx)
  }
