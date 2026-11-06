package image

import org.scalatest.funsuite.AnyFunSuite

class GreyscalePixelTest extends AnyFunSuite:
  test("GreyscalePixel creation with valid boundaries succeeds") {
    val pixelMin = GreyscalePixel(0)
    assert(pixelMin.scale == 0)

    val pixelMax = GreyscalePixel(255)
    assert(pixelMax.scale == 255)

    val pixelMid = GreyscalePixel(128)
    assert(pixelMid.scale == 128)
  }

  test(
    "GreyscalePixel throws IllegalArgumentException when scale is out of bounds"
  ) {
    assertThrows[IllegalArgumentException](GreyscalePixel(-1))
    assertThrows[IllegalArgumentException](GreyscalePixel(256))
  }
