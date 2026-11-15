package filters

import image.*

class FlipXImageFilter[T] extends ImageFilter[T]:
  override def apply(image: Image[T]): Image[T] =
    Image(
      Vector.tabulate(image.getHeight, image.getWidth) { (y, x) =>
        image.getPixel(x, image.getHeight - 1 - y)
      }
    )
