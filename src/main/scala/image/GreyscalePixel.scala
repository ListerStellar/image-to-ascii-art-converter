package image

case class GreyscalePixel(scale: Int):
  require(scale >= 0 && scale <= 255, s"Scale must be in 0..255, got $scale")
