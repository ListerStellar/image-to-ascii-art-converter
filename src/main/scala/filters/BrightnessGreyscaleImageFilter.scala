package filters

import image.*

class BrightnessGreyscaleImageFilter(private val value: Int)
    extends GreyscaleImageFilter:
  override def apply(image: Image[GreyscalePixel]): Image[GreyscalePixel] =
    Image(
      Vector.tabulate(image.getHeight, image.getWidth) { (y, x) =>
        val brightened: Long = image.getPixel(x, y).scale.toLong + value
        GreyscalePixel(math.max(0L, math.min(255L, brightened)).toInt)
      }
    )
