package filters

import image.*

class FlipYImageFilter[T] extends ImageFilter[T]:
  override def apply(image: Image[T]): Image[T] =
    Image(
      Vector.tabulate(image.getHeight, image.getWidth) { (y, x) =>
        image.getPixel(image.getWidth - 1 - x, y)
      }
    )
