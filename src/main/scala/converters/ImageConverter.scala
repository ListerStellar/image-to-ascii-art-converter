package converters

import image.*

trait ImageConverter[T, E]:
  def convert(image: Image[T]): Image[E]
