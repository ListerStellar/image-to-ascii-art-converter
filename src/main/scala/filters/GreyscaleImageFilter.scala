package filters

import image.*

trait GreyscaleImageFilter extends ImageFilter[GreyscalePixel]:
  def apply(image: Image[GreyscalePixel]): Image[GreyscalePixel]
