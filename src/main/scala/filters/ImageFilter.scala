package filters

import image.*

trait ImageFilter[T]:
  def apply(image: Image[T]): Image[T]
