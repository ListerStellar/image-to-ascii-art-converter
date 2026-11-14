package converters

import image.*
import org.scalatest.funsuite.AnyFunSuite

class GreyscaleToASCIIImageConverterTest extends AnyFunSuite:

  test(
    "GreyscaleToASCIIImageConverter converts rectangular 3x2 image preserving dimensions and characters"
  ) {
    val mockConverter: PixelConverter[GreyscalePixel, Char] =
      new PixelConverter[GreyscalePixel, Char]:
        override def convert(pixel: GreyscalePixel): Char =
          (pixel.scale + 65).toChar

    val imageConverter: GreyscaleToASCIIImageConverter =
      GreyscaleToASCIIImageConverter(mockConverter)

    val grid: Vector[Vector[GreyscalePixel]] = Vector(
      Vector(GreyscalePixel(0), GreyscalePixel(1)),
      Vector(GreyscalePixel(2), GreyscalePixel(3)),
      Vector(GreyscalePixel(4), GreyscalePixel(5))
    )

    val greyscaleImage: Image[GreyscalePixel] = Image(grid)
    val asciiImage: Image[Char] = imageConverter.convert(greyscaleImage)

    assert(asciiImage.getHeight == 3)
    assert(asciiImage.getWidth == 2)

    assert(asciiImage.getPixel(0, 0) == 'A')
    assert(asciiImage.getPixel(1, 0) == 'B')
    assert(asciiImage.getPixel(0, 1) == 'C')
    assert(asciiImage.getPixel(1, 1) == 'D')
    assert(asciiImage.getPixel(0, 2) == 'E')
    assert(asciiImage.getPixel(1, 2) == 'F')
  }

  test(
    "GreyscaleToASCIIImageConverter delegates every pixel to injected pixel converter"
  ) {
    val mockConverter: PixelConverter[GreyscalePixel, Char] =
      new PixelConverter[GreyscalePixel, Char]:
        override def convert(pixel: GreyscalePixel): Char = 'Z'

    val imageConverter: GreyscaleToASCIIImageConverter =
      GreyscaleToASCIIImageConverter(mockConverter)

    val grid: Vector[Vector[GreyscalePixel]] = Vector(
      Vector(GreyscalePixel(0), GreyscalePixel(100)),
      Vector(GreyscalePixel(200), GreyscalePixel(255))
    )
    val image: Image[GreyscalePixel] = Image(grid)
    val result: Image[Char] = imageConverter.convert(image)

    for
      y <- 0 until result.getHeight
      x <- 0 until result.getWidth
    do assert(result.getPixel(x, y) == 'Z')
  }
