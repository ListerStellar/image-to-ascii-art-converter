package converters

import image.*
import org.scalatest.funsuite.AnyFunSuite

class RGBToGreyscalePixelConverterTest extends AnyFunSuite:

  test(
    "RGBToGreyscalePixelConverter converts extreme boundary values correctly"
  ) {
    val converter: RGBToGreyscalePixelConverter = RGBToGreyscalePixelConverter()

    val blackPixel: RGBPixel = RGBPixel(0, 0, 0)
    val convertedBlack: GreyscalePixel = converter.convert(blackPixel)
    assert(convertedBlack.scale == 0)

    val whitePixel: RGBPixel = RGBPixel(255, 255, 255)
    val convertedWhite: GreyscalePixel = converter.convert(whitePixel)
    assert(convertedWhite.scale == 255)
  }

  test(
    "RGBToGreyscalePixelConverter converts pure primary color channels correctly"
  ) {
    val converter: RGBToGreyscalePixelConverter = RGBToGreyscalePixelConverter()

    val pureRed: RGBPixel = RGBPixel(255, 0, 0)
    assert(converter.convert(pureRed).scale == 77)

    val pureGreen: RGBPixel = RGBPixel(0, 255, 0)
    assert(converter.convert(pureGreen).scale == 150)

    val pureBlue: RGBPixel = RGBPixel(0, 0, 255)
    assert(converter.convert(pureBlue).scale == 28)
  }

  test(
    "RGBToGreyscalePixelConverter converts intermediate RGB combinations correctly"
  ) {
    val converter: RGBToGreyscalePixelConverter = RGBToGreyscalePixelConverter()

    val mixedPixel: RGBPixel = RGBPixel(100, 150, 200)
    assert(converter.convert(mixedPixel).scale == 141)

    val grey50Pixel: RGBPixel = RGBPixel(50, 50, 50)
    assert(converter.convert(grey50Pixel).scale == 50)

    val grey128Pixel: RGBPixel = RGBPixel(128, 128, 128)
    assert(converter.convert(grey128Pixel).scale == 128)

    val minimalPixel: RGBPixel = RGBPixel(1, 1, 1)
    assert(converter.convert(minimalPixel).scale == 1)
  }

  test(
    "RGBToGreyscalePixelConverter output is always clamped within valid 0 to 255 range"
  ) {
    val converter: RGBToGreyscalePixelConverter = RGBToGreyscalePixelConverter()
    val testPixels: Seq[RGBPixel] = Seq(
      RGBPixel(0, 0, 0),
      RGBPixel(255, 255, 255),
      RGBPixel(255, 0, 0),
      RGBPixel(0, 255, 0),
      RGBPixel(0, 0, 255),
      RGBPixel(127, 127, 127)
    )
    for pixel <- testPixels do
      val result: GreyscalePixel = converter.convert(pixel)
      assert(result.scale >= 0 && result.scale <= 255)
  }
