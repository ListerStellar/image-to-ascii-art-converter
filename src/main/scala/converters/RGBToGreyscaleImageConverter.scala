package converters

import image.*

class RGBToGreyscaleImageConverter(
  private val pixelConverter: PixelConverter[RGBPixel, GreyscalePixel]
) extends ImageConverter[RGBPixel, GreyscalePixel]:

  override def convert(image: Image[RGBPixel]): Image[GreyscalePixel] =
    val height: Int = image.getHeight
    val width: Int = image.getWidth
    val grid: Vector[Vector[GreyscalePixel]] =
      Vector.tabulate(height, width) { (y, x) =>
        pixelConverter.convert(image.getPixel(x, y))
      }
    Image(grid)
