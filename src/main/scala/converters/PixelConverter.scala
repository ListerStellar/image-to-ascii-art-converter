package converters

import image.*

trait PixelConverter[T, E]:
  def convert(pixel: T): E
