package image

import org.scalatest.funsuite.AnyFunSuite

class ImageTest extends AnyFunSuite:
  private def createSampleGrid[T](rows: Int, cols: Int)(
    cellSupplier: (Int, Int) => T
  ): Vector[Vector[T]] =
    Vector.tabulate(rows, cols)(cellSupplier)

  private def createSampleImage[T](rows: Int, cols: Int)(
    cellSupplier: (Int, Int) => T
  ): Image[T] =
    Image(createSampleGrid(rows, cols)(cellSupplier))

  test("Image dimensions and pixel retrieval function correctly") {
    val grid = Vector(
      Vector(1, 2, 3),
      Vector(4, 5, 6)
    )
    val image = Image(grid)

    assert(image.getHeight == 2)
    assert(image.getWidth == 3)
    assert(image.getPixel(0, 0) == 1)
    assert(image.getPixel(1, 0) == 2)
    assert(image.getPixel(2, 0) == 3)
    assert(image.getPixel(0, 1) == 4)
    assert(image.getPixel(1, 1) == 5)
    assert(image.getPixel(2, 1) == 6)
  }

  test("Image throws IllegalArgumentException for empty pixels grid") {
    assertThrows[IllegalArgumentException](Image(Vector.empty))
  }

  test("Image throws IllegalArgumentException for grid with empty rows") {
    assertThrows[IllegalArgumentException](Image(Vector(Vector.empty)))
  }

  test("Image throws IllegalArgumentException for non-rectangular grid") {
    val jaggedGrid = Vector(
      Vector(1, 2, 3),
      Vector(4, 5)
    )
    assertThrows[IllegalArgumentException](Image(jaggedGrid))
  }

  test(
    "Image throws IndexOutOfBoundsException when coordinates exceed bounds"
  ) {
    val image = createSampleImage(2, 3)((row, col) => row * 3 + col)

    assertThrows[IndexOutOfBoundsException](image.getPixel(-1, 0))
    assertThrows[IndexOutOfBoundsException](image.getPixel(0, -1))
    assertThrows[IndexOutOfBoundsException](image.getPixel(3, 0))
    assertThrows[IndexOutOfBoundsException](image.getPixel(0, 2))
  }

  test("Image works with RGBPixel and GreyscalePixel types") {
    val rgbGrid = Vector(
      Vector(RGBPixel(0, 0, 0), RGBPixel(255, 255, 255)),
      Vector(RGBPixel(10, 20, 30), RGBPixel(40, 50, 60))
    )
    val rgbImage = Image(rgbGrid)
    assert(rgbImage.getPixel(1, 0) == RGBPixel(255, 255, 255))

    val greyGrid = Vector(
      Vector(GreyscalePixel(0), GreyscalePixel(255))
    )
    val greyImage = Image(greyGrid)
    assert(greyImage.getPixel(0, 0) == GreyscalePixel(0))
  }
