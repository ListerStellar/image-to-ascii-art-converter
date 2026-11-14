package converters

import image.*
import org.scalatest.funsuite.AnyFunSuite

class RGBToGreyscaleImageConverterTest extends AnyFunSuite:

  test(
    "RGBToGreyscaleImageConverter converts rectangular 3x4 image preserving dimensions and coordinates"
  ) {
    val mockConverter: PixelConverter[RGBPixel, GreyscalePixel] =
      new PixelConverter[RGBPixel, GreyscalePixel]:
        override def convert(pixel: RGBPixel): GreyscalePixel =
          GreyscalePixel(pixel.red)

    val imageConverter: RGBToGreyscaleImageConverter =
      RGBToGreyscaleImageConverter(mockConverter)

    val grid: Vector[Vector[RGBPixel]] = Vector(
      Vector(
        RGBPixel(0, 0, 0),
        RGBPixel(10, 0, 0),
        RGBPixel(20, 0, 0),
        RGBPixel(30, 0, 0)
      ),
      Vector(
        RGBPixel(40, 0, 0),
        RGBPixel(50, 0, 0),
        RGBPixel(60, 0, 0),
        RGBPixel(70, 0, 0)
      ),
      Vector(
        RGBPixel(80, 0, 0),
        RGBPixel(90, 0, 0),
        RGBPixel(100, 0, 0),
        RGBPixel(110, 0, 0)
      )
    )

    val rgbImage: Image[RGBPixel] = Image(grid)
    val convertedImage: Image[GreyscalePixel] =
      imageConverter.convert(rgbImage)

    assert(convertedImage.getHeight == 3)
    assert(convertedImage.getWidth == 4)

    assert(convertedImage.getPixel(0, 0).scale == 0)
    assert(convertedImage.getPixel(1, 0).scale == 10)
    assert(convertedImage.getPixel(2, 0).scale == 20)
    assert(convertedImage.getPixel(3, 0).scale == 30)

    assert(convertedImage.getPixel(0, 1).scale == 40)
    assert(convertedImage.getPixel(1, 1).scale == 50)
    assert(convertedImage.getPixel(2, 1).scale == 60)
    assert(convertedImage.getPixel(3, 1).scale == 70)

    assert(convertedImage.getPixel(0, 2).scale == 80)
    assert(convertedImage.getPixel(1, 2).scale == 90)
    assert(convertedImage.getPixel(2, 2).scale == 100)
    assert(convertedImage.getPixel(3, 2).scale == 110)
  }

  test(
    "RGBToGreyscaleImageConverter delegates every pixel conversion to injected pixel converter"
  ) {
    val mockConverter: PixelConverter[RGBPixel, GreyscalePixel] =
      new PixelConverter[RGBPixel, GreyscalePixel]:
        override def convert(pixel: RGBPixel): GreyscalePixel =
          GreyscalePixel(pixel.green)

    val imageConverter: RGBToGreyscaleImageConverter =
      RGBToGreyscaleImageConverter(mockConverter)

    val grid: Vector[Vector[RGBPixel]] = Vector(
      Vector(RGBPixel(10, 20, 30), RGBPixel(40, 50, 60)),
      Vector(RGBPixel(70, 80, 90), RGBPixel(100, 110, 120))
    )
    val image: Image[RGBPixel] = Image(grid)
    val result: Image[GreyscalePixel] = imageConverter.convert(image)

    assert(result.getPixel(0, 0).scale == 20)
    assert(result.getPixel(1, 0).scale == 50)
    assert(result.getPixel(0, 1).scale == 80)
    assert(result.getPixel(1, 1).scale == 110)
  }
