package converters

import image.*

class CustomLinearASCIIPixelConverter(private val userDefinedSymbols: String)
    extends GreyscaleToASCIIPixelConverter:
  require(
    userDefinedSymbols.nonEmpty,
    "User defined symbols must not be empty"
  )

  override def convert(pixel: GreyscalePixel): Char =
    val index: Int =
      math.min(
        userDefinedSymbols.length - 1,
        (pixel.scale * userDefinedSymbols.length) / 256
      )
    userDefinedSymbols(index)
