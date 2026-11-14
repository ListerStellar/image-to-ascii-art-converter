package converters

import image.*

class GreyscaleToASCIIImageConverter(
  private val pixelConverter: PixelConverter[GreyscalePixel, Char]
) extends ImageConverter[GreyscalePixel, Char]:

  override def convert(image: Image[GreyscalePixel]): Image[Char] =
    val height: Int = image.getHeight
    val width: Int = image.getWidth
    val grid: Vector[Vector[Char]] =
      Vector.tabulate(height, width) { (y, x) =>
        pixelConverter.convert(image.getPixel(x, y))
      }
    Image(grid)
