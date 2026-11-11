package image

import org.scalatest.funsuite.AnyFunSuite

class RGBPixelTest extends AnyFunSuite:
  test("RGBPixel creation with valid boundaries succeeds") {
    val pixelMin = RGBPixel(0, 0, 0)
    assert(pixelMin.red == 0)
    assert(pixelMin.green == 0)
    assert(pixelMin.blue == 0)

    val pixelMax = RGBPixel(255, 255, 255)
    assert(pixelMax.red == 255)
    assert(pixelMax.green == 255)
    assert(pixelMax.blue == 255)

    val pixelMid = RGBPixel(10, 20, 30)
    assert(pixelMid.red == 10)
    assert(pixelMid.green == 20)
    assert(pixelMid.blue == 30)
  }

  test("RGBPixel throws IllegalArgumentException when red is out of bounds") {
    assertThrows[IllegalArgumentException](RGBPixel(-1, 100, 100))
    assertThrows[IllegalArgumentException](RGBPixel(256, 100, 100))
  }

  test("RGBPixel throws IllegalArgumentException when green is out of bounds") {
    assertThrows[IllegalArgumentException](RGBPixel(100, -1, 100))
    assertThrows[IllegalArgumentException](RGBPixel(100, 256, 100))
  }

  test("RGBPixel throws IllegalArgumentException when blue is out of bounds") {
    assertThrows[IllegalArgumentException](RGBPixel(100, 100, -1))
    assertThrows[IllegalArgumentException](RGBPixel(100, 100, 256))
  }
