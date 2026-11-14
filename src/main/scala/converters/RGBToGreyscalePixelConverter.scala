package converters

import image.*

class RGBToGreyscalePixelConverter
    extends PixelConverter[RGBPixel, GreyscalePixel]:
  override def convert(pixel: RGBPixel): GreyscalePixel =
    val intensity: Int =
      ((0.3 * pixel.red) + (0.59 * pixel.green) + (0.11 * pixel.blue)).round.toInt
    val clamped: Int = math.max(0, math.min(255, intensity))
    GreyscalePixel(clamped)
