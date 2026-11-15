package filters

import image.*

class InvertGreyscaleImageFilter extends GreyscaleImageFilter:
  override def apply(image: Image[GreyscalePixel]): Image[GreyscalePixel] =
    Image(
      Vector.tabulate(image.getHeight, image.getWidth) { (y, x) =>
        GreyscalePixel(255 - image.getPixel(x, y).scale)
      }
    )
