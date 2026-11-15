package filters

import image.*
import org.scalatest.funsuite.AnyFunSuite

class BrightnessGreyscaleImageFilterTest extends AnyFunSuite:

  test(
    "BrightnessGreyscaleImageFilter increases brightness by positive delta"
  ) {
    val filter: BrightnessGreyscaleImageFilter =
      BrightnessGreyscaleImageFilter(50)
    val image: Image[GreyscalePixel] =
      Image(Vector(Vector(GreyscalePixel(100))))
    val result: Image[GreyscalePixel] = filter.apply(image)

    assert(result.getPixel(0, 0).scale == 150)
  }

  test(
    "BrightnessGreyscaleImageFilter decreases brightness by negative delta"
  ) {
    val filter: BrightnessGreyscaleImageFilter =
      BrightnessGreyscaleImageFilter(-30)
    val image: Image[GreyscalePixel] =
      Image(Vector(Vector(GreyscalePixel(100))))
    val result: Image[GreyscalePixel] = filter.apply(image)

    assert(result.getPixel(0, 0).scale == 70)
  }

  test(
    "BrightnessGreyscaleImageFilter clamps brightness to 255 on overflow"
  ) {
    val filter: BrightnessGreyscaleImageFilter =
      BrightnessGreyscaleImageFilter(100)
    val image: Image[GreyscalePixel] =
      Image(Vector(Vector(GreyscalePixel(200))))
    val result: Image[GreyscalePixel] = filter.apply(image)

    assert(result.getPixel(0, 0).scale == 255)
  }

  test(
    "BrightnessGreyscaleImageFilter clamps brightness to 0 on underflow"
  ) {
    val filter: BrightnessGreyscaleImageFilter =
      BrightnessGreyscaleImageFilter(-100)
    val image: Image[GreyscalePixel] = Image(Vector(Vector(GreyscalePixel(50))))
    val result: Image[GreyscalePixel] = filter.apply(image)

    assert(result.getPixel(0, 0).scale == 0)
  }

  test(
    "BrightnessGreyscaleImageFilter leaves pixels unchanged with zero delta"
  ) {
    val filter: BrightnessGreyscaleImageFilter =
      BrightnessGreyscaleImageFilter(0)
    val image: Image[GreyscalePixel] = Image(
      Vector(
        Vector(GreyscalePixel(0), GreyscalePixel(128)),
        Vector(GreyscalePixel(255), GreyscalePixel(42))
      )
    )
    val result: Image[GreyscalePixel] = filter.apply(image)

    assert(result == image)
  }

  test("BrightnessGreyscaleImageFilter preserves dimensions and coordinates") {
    val filter: BrightnessGreyscaleImageFilter =
      BrightnessGreyscaleImageFilter(20)
    val image: Image[GreyscalePixel] = Image(
      Vector(
        Vector(GreyscalePixel(10), GreyscalePixel(20), GreyscalePixel(30)),
        Vector(GreyscalePixel(40), GreyscalePixel(50), GreyscalePixel(60))
      )
    )
    val result: Image[GreyscalePixel] = filter.apply(image)

    assert(result.getHeight == 2)
    assert(result.getWidth == 3)
    assert(result.getPixel(0, 0).scale == 30)
    assert(result.getPixel(1, 0).scale == 40)
    assert(result.getPixel(2, 0).scale == 50)
    assert(result.getPixel(0, 1).scale == 60)
    assert(result.getPixel(1, 1).scale == 70)
    assert(result.getPixel(2, 1).scale == 80)
  }
