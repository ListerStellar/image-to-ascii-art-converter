package loaders

import image.*

trait ImageLoader:
  def load(): Image[RGBPixel]
