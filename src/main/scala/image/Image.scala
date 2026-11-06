package image

class Image[T](private val pixels: Vector[Vector[T]]):
  require(
    pixels.nonEmpty,
    "Pixels grid must not be empty"
  )
  require(
    pixels.forall(_.nonEmpty),
    "Pixels rows must not be empty"
  )

  private val firstRowWidth: Int = pixels.head.length
  require(
    pixels.forall(_.length == firstRowWidth),
    "All rows must have the same width"
  )

  private val height: Int = pixels.length
  private val width: Int = firstRowWidth

  def getHeight: Int = height

  def getWidth: Int = width

  def getPixel(x: Int, y: Int): T =
    if x < 0 || x >= width || y < 0 || y >= height then
      throw IndexOutOfBoundsException(
        s"Coordinates (x=$x, y=$y) are out of bounds for image with bounds (${width}x$height)"
      )
    pixels(y)(x)

  override def equals(other: Any): Boolean = other match
    case that: Image[?] =>
      width == that.getWidth && height == that.getHeight && pixels == that.pixels
    case _ => false

  override def hashCode: Int =
    pixels.hashCode
