package converters

import image.*
import org.scalatest.funsuite.AnyFunSuite

class ImageConvertersIntegrationTest extends AnyFunSuite:

  test(
    "End-to-end pipeline converts RGB image to ASCII image with default linear converter"
  ) {
    val rgbToGreyImage: RGBToGreyscaleImageConverter =
      RGBToGreyscaleImageConverter(RGBToGreyscalePixelConverter())
    val greyToAsciiImage: GreyscaleToASCIIImageConverter =
      GreyscaleToASCIIImageConverter(DefaultLinearASCIIPixelConverter())

    val grid: Vector[Vector[RGBPixel]] = Vector(
      Vector(RGBPixel(0, 0, 0), RGBPixel(255, 255, 255)),
      Vector(RGBPixel(255, 0, 0), RGBPixel(0, 255, 0))
    )
    val rgbImage: Image[RGBPixel] = Image(grid)

    val greyImage: Image[GreyscalePixel] = rgbToGreyImage.convert(rgbImage)
    assert(greyImage.getHeight == 2)
    assert(greyImage.getWidth == 2)
    assert(greyImage.getPixel(0, 0).scale == 0)
    assert(greyImage.getPixel(1, 0).scale == 255)
    assert(greyImage.getPixel(0, 1).scale == 77)
    assert(greyImage.getPixel(1, 1).scale == 150)

    val asciiImage: Image[Char] = greyToAsciiImage.convert(greyImage)
    assert(asciiImage.getHeight == 2)
    assert(asciiImage.getWidth == 2)
    assert(asciiImage.getPixel(0, 0) == ' ')
    assert(asciiImage.getPixel(1, 0) == '@')
    assert(asciiImage.getPixel(0, 1) == '-')
    assert(asciiImage.getPixel(1, 1) == '+')
  }

  test(
    "End-to-end pipeline operates correctly with non-linear gamma converter"
  ) {
    val rgbToGreyImage: RGBToGreyscaleImageConverter =
      RGBToGreyscaleImageConverter(RGBToGreyscalePixelConverter())
    val nonLinearAsciiImage: GreyscaleToASCIIImageConverter =
      GreyscaleToASCIIImageConverter(NonLinearASCIIPixelConverter())

    val grid: Vector[Vector[RGBPixel]] = Vector(
      Vector(RGBPixel(0, 0, 0), RGBPixel(128, 128, 128)),
      Vector(RGBPixel(255, 255, 255), RGBPixel(50, 50, 50))
    )
    val rgbImage: Image[RGBPixel] = Image(grid)

    val asciiImage: Image[Char] =
      nonLinearAsciiImage.convert(rgbToGreyImage.convert(rgbImage))

    assert(asciiImage.getHeight == 2)
    assert(asciiImage.getWidth == 2)
    assert(asciiImage.getPixel(0, 0) == ' ')
    assert(asciiImage.getPixel(1, 0) == ':')
    assert(asciiImage.getPixel(0, 1) == '@')
  }

  test("End-to-end pipeline operates correctly with custom symbols converter") {
    val rgbToGreyImage: RGBToGreyscaleImageConverter =
      RGBToGreyscaleImageConverter(RGBToGreyscalePixelConverter())
    val customAsciiImage: GreyscaleToASCIIImageConverter =
      GreyscaleToASCIIImageConverter(
        CustomLinearASCIIPixelConverter(" .:")
      )

    val grid: Vector[Vector[RGBPixel]] = Vector(
      Vector(RGBPixel(0, 0, 0), RGBPixel(255, 255, 255))
    )
    val rgbImage: Image[RGBPixel] = Image(grid)

    val asciiImage: Image[Char] =
      customAsciiImage.convert(rgbToGreyImage.convert(rgbImage))

    assert(asciiImage.getHeight == 1)
    assert(asciiImage.getWidth == 2)
    assert(asciiImage.getPixel(0, 0) == ' ')
    assert(asciiImage.getPixel(1, 0) == ':')
  }
