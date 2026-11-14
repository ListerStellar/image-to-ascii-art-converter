package converters

import image.*
import org.scalatest.funsuite.AnyFunSuite

class CustomLinearASCIIPixelConverterTest extends AnyFunSuite:

  test("CustomLinearASCIIPixelConverter distributes symbols linearly") {
    val converter: CustomLinearASCIIPixelConverter =
      CustomLinearASCIIPixelConverter("ABCD")

    assert(converter.convert(GreyscalePixel(0)) == 'A')
    assert(converter.convert(GreyscalePixel(63)) == 'A')
    assert(converter.convert(GreyscalePixel(64)) == 'B')
    assert(converter.convert(GreyscalePixel(127)) == 'B')
    assert(converter.convert(GreyscalePixel(128)) == 'C')
    assert(converter.convert(GreyscalePixel(191)) == 'C')
    assert(converter.convert(GreyscalePixel(192)) == 'D')
    assert(converter.convert(GreyscalePixel(255)) == 'D')
  }

  test(
    "CustomLinearASCIIPixelConverter produces monotonic mapping across all scales"
  ) {
    val symbols: String = "0123456789"
    val converter: CustomLinearASCIIPixelConverter =
      CustomLinearASCIIPixelConverter(symbols)

    for scale <- 0 until 255 do
      val currentIdx: Int =
        symbols.indexOf(converter.convert(GreyscalePixel(scale)))
      val nextIdx: Int =
        symbols.indexOf(converter.convert(GreyscalePixel(scale + 1)))
      assert(currentIdx <= nextIdx)
  }

  test(
    "CustomLinearASCIIPixelConverter maps all scales to single character when string length is 1"
  ) {
    val converter: CustomLinearASCIIPixelConverter =
      CustomLinearASCIIPixelConverter("@")

    for scale <- 0 until 255 do
      assert(converter.convert(GreyscalePixel(scale)) == '@')
  }

  test(
    "CustomLinearASCIIPixelConverter throws IllegalArgumentException when symbol string is empty"
  ) {
    assertThrows[IllegalArgumentException](CustomLinearASCIIPixelConverter(""))
  }
