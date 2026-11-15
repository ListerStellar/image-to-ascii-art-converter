package filters

import image.*
import org.scalatest.funsuite.AnyFunSuite

class FlipYImageFilterTest extends AnyFunSuite:

  test("FlipYImageFilter horizontally flips columns of a 2x2 image") {
    val filter: FlipYImageFilter[GreyscalePixel] =
      FlipYImageFilter[GreyscalePixel]()
    val image: Image[GreyscalePixel] = Image(
      Vector(
        Vector(GreyscalePixel(10), GreyscalePixel(20)),
        Vector(GreyscalePixel(30), GreyscalePixel(40))
      )
    )
    val result: Image[GreyscalePixel] = filter.apply(image)

    assert(result.getHeight == 2)
    assert(result.getWidth == 2)
    assert(result.getPixel(0, 0).scale == 20)
    assert(result.getPixel(1, 0).scale == 10)
    assert(result.getPixel(0, 1).scale == 40)
    assert(result.getPixel(1, 1).scale == 30)
  }

  test(
    "FlipYImageFilter horizontally flips non-square 2x3 image preserving dimensions"
  ) {
    val filter: FlipYImageFilter[GreyscalePixel] =
      FlipYImageFilter[GreyscalePixel]()
    val image: Image[GreyscalePixel] = Image(
      Vector(
        Vector(GreyscalePixel(1), GreyscalePixel(2), GreyscalePixel(3)),
        Vector(GreyscalePixel(4), GreyscalePixel(5), GreyscalePixel(6))
      )
    )
    val result: Image[GreyscalePixel] = filter.apply(image)

    assert(result.getHeight == 2)
    assert(result.getWidth == 3)
    assert(result.getPixel(0, 0).scale == 3)
    assert(result.getPixel(1, 0).scale == 2)
    assert(result.getPixel(2, 0).scale == 1)
    assert(result.getPixel(0, 1).scale == 6)
    assert(result.getPixel(1, 1).scale == 5)
    assert(result.getPixel(2, 1).scale == 4)
  }

  test("FlipYImageFilter applied twice restores original image") {
    val filter: FlipYImageFilter[GreyscalePixel] =
      FlipYImageFilter[GreyscalePixel]()
    val image: Image[GreyscalePixel] = Image(
      Vector(
        Vector(GreyscalePixel(1), GreyscalePixel(2), GreyscalePixel(3)),
        Vector(GreyscalePixel(4), GreyscalePixel(5), GreyscalePixel(6))
      )
    )
    val doubleFlipped: Image[GreyscalePixel] = filter.apply(filter.apply(image))

    assert(doubleFlipped == image)
  }

  test("FlipYImageFilter leaves single-column image unchanged") {
    val filter: FlipYImageFilter[GreyscalePixel] =
      FlipYImageFilter[GreyscalePixel]()
    val image: Image[GreyscalePixel] = Image(
      Vector(
        Vector(GreyscalePixel(10)),
        Vector(GreyscalePixel(20)),
        Vector(GreyscalePixel(30))
      )
    )
    val result: Image[GreyscalePixel] = filter.apply(image)

    assert(result == image)
  }
