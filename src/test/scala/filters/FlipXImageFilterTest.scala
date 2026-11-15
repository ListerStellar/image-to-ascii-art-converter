package filters

import image.*
import org.scalatest.funsuite.AnyFunSuite

class FlipXImageFilterTest extends AnyFunSuite:

  test("FlipXImageFilter vertically flips rows of a 2x2 image") {
    val filter: FlipXImageFilter[GreyscalePixel] =
      FlipXImageFilter[GreyscalePixel]()
    val image: Image[GreyscalePixel] = Image(
      Vector(
        Vector(GreyscalePixel(10), GreyscalePixel(20)),
        Vector(GreyscalePixel(30), GreyscalePixel(40))
      )
    )
    val result: Image[GreyscalePixel] = filter.apply(image)

    assert(result.getHeight == 2)
    assert(result.getWidth == 2)
    assert(result.getPixel(0, 0).scale == 30)
    assert(result.getPixel(1, 0).scale == 40)
    assert(result.getPixel(0, 1).scale == 10)
    assert(result.getPixel(1, 1).scale == 20)
  }

  test(
    "FlipXImageFilter vertically flips non-square 3x2 image preserving dimensions"
  ) {
    val filter: FlipXImageFilter[GreyscalePixel] =
      FlipXImageFilter[GreyscalePixel]()
    val image: Image[GreyscalePixel] = Image(
      Vector(
        Vector(GreyscalePixel(1), GreyscalePixel(2)),
        Vector(GreyscalePixel(3), GreyscalePixel(4)),
        Vector(GreyscalePixel(5), GreyscalePixel(6))
      )
    )
    val result: Image[GreyscalePixel] = filter.apply(image)

    assert(result.getHeight == 3)
    assert(result.getWidth == 2)
    assert(result.getPixel(0, 0).scale == 5)
    assert(result.getPixel(1, 0).scale == 6)
    assert(result.getPixel(0, 1).scale == 3)
    assert(result.getPixel(1, 1).scale == 4)
    assert(result.getPixel(0, 2).scale == 1)
    assert(result.getPixel(1, 2).scale == 2)
  }

  test("FlipXImageFilter applied twice restores original image") {
    val filter: FlipXImageFilter[GreyscalePixel] =
      FlipXImageFilter[GreyscalePixel]()
    val image: Image[GreyscalePixel] = Image(
      Vector(
        Vector(GreyscalePixel(1), GreyscalePixel(2)),
        Vector(GreyscalePixel(3), GreyscalePixel(4)),
        Vector(GreyscalePixel(5), GreyscalePixel(6))
      )
    )
    val doubleFlipped: Image[GreyscalePixel] = filter.apply(filter.apply(image))

    assert(doubleFlipped == image)
  }

  test("FlipXImageFilter leaves single-row image unchanged") {
    val filter: FlipXImageFilter[GreyscalePixel] =
      FlipXImageFilter[GreyscalePixel]()
    val image: Image[GreyscalePixel] = Image(
      Vector(
        Vector(GreyscalePixel(10), GreyscalePixel(20), GreyscalePixel(30))
      )
    )
    val result: Image[GreyscalePixel] = filter.apply(image)

    assert(result == image)
  }
