package converters

import image.*

class NonLinearASCIIPixelConverter(
  private val symbols: String,
  private val gamma: Double
) extends GreyscaleToASCIIPixelConverter:
  def this() = this(" .:-=+*#%@", 2.0)
  def this(symbols: String) = this(symbols, 2.0)

  require(symbols.nonEmpty, "Symbols must not be empty")
  require(
    gamma > 0.0 && !gamma.isNaN && !gamma.isInfinite,
    s"Gamma must be positive and finite, got $gamma"
  )

  override def convert(pixel: GreyscalePixel): Char =
    val normalized: Double = pixel.scale.toDouble / 255.0
    val powered: Double = Math.pow(normalized, gamma)
    val index: Int =
      math.min(symbols.length - 1, (powered * symbols.length).toInt)
    symbols(index)
