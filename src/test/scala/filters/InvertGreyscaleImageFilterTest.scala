package filters

import image.*
import org.scalatest.funsuite.AnyFunSuite

class InvertGreyscaleImageFilterTest extends AnyFunSuite:

  test(
    "InvertGreyscaleImageFilter inverts greyscale values"
  ) {
    val filter: InvertGreyscaleImageFilter = InvertGreyscaleImageFilter()
    val image: Image[GreyscalePixel] = Image(
      Vector(
        Vector(GreyscalePixel(255), GreyscalePixel(55)),
        Vector(GreyscalePixel(200), GreyscalePixel(0))
      )
    )
    val result: Image[GreyscalePixel] = filter.apply(image)

    assert(result.getPixel(0, 0).scale == 0)
    assert(result.getPixel(1, 0).scale == 200)
    assert(result.getPixel(0, 1).scale == 55)
    assert(result.getPixel(1, 1).scale == 255)
  }

  test(
    "InvertGreyscaleImageFilter preserves non-square dimensions and coordinates"
  ) {
    val filter: InvertGreyscaleImageFilter = InvertGreyscaleImageFilter()
    val image: Image[GreyscalePixel] = Image(
      Vector(
        Vector(GreyscalePixel(10), GreyscalePixel(20), GreyscalePixel(30)),
        Vector(GreyscalePixel(40), GreyscalePixel(50), GreyscalePixel(60))
      )
    )
    val result: Image[GreyscalePixel] = filter.apply(image)

    assert(result.getHeight == 2)
    assert(result.getWidth == 3)
    assert(result.getPixel(0, 0).scale == 245)
    assert(result.getPixel(1, 0).scale == 235)
    assert(result.getPixel(2, 0).scale == 225)
    assert(result.getPixel(0, 1).scale == 215)
    assert(result.getPixel(1, 1).scale == 205)
    assert(result.getPixel(2, 1).scale == 195)
  }

  test("InvertGreyscaleImageFilter applied twice restores original image") {
    val filter: InvertGreyscaleImageFilter = InvertGreyscaleImageFilter()
    val image: Image[GreyscalePixel] = Image(
      Vector(
        Vector(GreyscalePixel(0), GreyscalePixel(128)),
        Vector(GreyscalePixel(255), GreyscalePixel(42))
      )
    )
    val doubleInverted: Image[GreyscalePixel] =
      filter.apply(filter.apply(image))

    assert(doubleInverted == image)
  }
