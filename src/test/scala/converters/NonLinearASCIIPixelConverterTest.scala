package converters

import image.*
import org.scalatest.funsuite.AnyFunSuite

class NonLinearASCIIPixelConverterTest extends AnyFunSuite:

  private val defaultTable: String = " .:-=+*#%@"

  test(
    "NonLinearASCIIPixelConverter maps extreme scales to first and last symbols"
  ) {
    val converter: NonLinearASCIIPixelConverter =
      NonLinearASCIIPixelConverter()

    assert(converter.convert(GreyscalePixel(0)) == ' ')
    assert(converter.convert(GreyscalePixel(255)) == '@')
  }

  test(
    "NonLinearASCIIPixelConverter with gamma 2.0 compresses midtones into darker symbols compared to linear"
  ) {
    val nonLinearConverter: NonLinearASCIIPixelConverter =
      NonLinearASCIIPixelConverter()
    val linearConverter: NonLinearASCIIPixelConverter =
      NonLinearASCIIPixelConverter(defaultTable, 1.0)

    val midPixel: GreyscalePixel = GreyscalePixel(128)
    val nonLinearResult: Char = nonLinearConverter.convert(midPixel)
    val linearResult: Char = linearConverter.convert(midPixel)

    assert(nonLinearResult == ':')
    assert(linearResult == '+')
    assert(
      defaultTable.indexOf(nonLinearResult) <
        defaultTable.indexOf(linearResult)
    )
  }

  test(
    "NonLinearASCIIPixelConverter with gamma 0.5 expands midtones into brighter symbols compared to linear"
  ) {
    val subLinearConverter: NonLinearASCIIPixelConverter =
      NonLinearASCIIPixelConverter(defaultTable, 0.5)
    val linearConverter: NonLinearASCIIPixelConverter =
      NonLinearASCIIPixelConverter(defaultTable, 1.0)

    val midPixel: GreyscalePixel = GreyscalePixel(128)
    val subLinearResult: Char = subLinearConverter.convert(midPixel)
    val linearResult: Char = linearConverter.convert(midPixel)

    assert(subLinearResult == '#')
    assert(linearResult == '+')
    assert(
      defaultTable.indexOf(subLinearResult) >
        defaultTable.indexOf(linearResult)
    )
  }

  test(
    "NonLinearASCIIPixelConverter supports default, custom symbols, and custom gamma constructors"
  ) {
    val defaultConverter: NonLinearASCIIPixelConverter =
      NonLinearASCIIPixelConverter()
    val customSymbolsConverter: NonLinearASCIIPixelConverter =
      NonLinearASCIIPixelConverter("ABCDE")
    val fullyParameterizedConverter: NonLinearASCIIPixelConverter =
      NonLinearASCIIPixelConverter("ABCDE", 3.0)

    assert(defaultConverter.convert(GreyscalePixel(0)) == ' ')
    assert(defaultConverter.convert(GreyscalePixel(255)) == '@')
    assert(customSymbolsConverter.convert(GreyscalePixel(0)) == 'A')
    assert(customSymbolsConverter.convert(GreyscalePixel(255)) == 'E')
    assert(fullyParameterizedConverter.convert(GreyscalePixel(0)) == 'A')
    assert(fullyParameterizedConverter.convert(GreyscalePixel(255)) == 'E')
  }

  test(
    "NonLinearASCIIPixelConverter produces monotonic mapping across all scales"
  ) {
    val converter: NonLinearASCIIPixelConverter =
      NonLinearASCIIPixelConverter()

    for scale <- 0 until 255 do
      val currentIdx: Int =
        defaultTable.indexOf(converter.convert(GreyscalePixel(scale)))
      val nextIdx: Int =
        defaultTable.indexOf(
          converter.convert(GreyscalePixel(scale + 1))
        )
      assert(currentIdx <= nextIdx)
  }

  test(
    "NonLinearASCIIPixelConverter throws IllegalArgumentException when gamma is zero or negative"
  ) {
    assertThrows[IllegalArgumentException](
      NonLinearASCIIPixelConverter(".*", 0.0)
    )
    assertThrows[IllegalArgumentException](
      NonLinearASCIIPixelConverter(".*", -0.5)
    )
    assertThrows[IllegalArgumentException](
      NonLinearASCIIPixelConverter(".*", -2.0)
    )
  }

  test(
    "NonLinearASCIIPixelConverter throws IllegalArgumentException when gamma is NaN or infinite"
  ) {
    assertThrows[IllegalArgumentException](
      NonLinearASCIIPixelConverter(".*", Double.NaN)
    )
    assertThrows[IllegalArgumentException](
      NonLinearASCIIPixelConverter(".*", Double.PositiveInfinity)
    )
    assertThrows[IllegalArgumentException](
      NonLinearASCIIPixelConverter(".*", Double.NegativeInfinity)
    )
  }

  test(
    "NonLinearASCIIPixelConverter throws IllegalArgumentException when symbols string is empty"
  ) {
    assertThrows[IllegalArgumentException](
      NonLinearASCIIPixelConverter("", 2.0)
    )
    assertThrows[IllegalArgumentException](
      NonLinearASCIIPixelConverter("")
    )
  }
