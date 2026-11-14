package converters

import image.*

trait GreyscaleToASCIIPixelConverter
    extends PixelConverter[GreyscalePixel, Char]:
  def convert(pixel: GreyscalePixel): Char
