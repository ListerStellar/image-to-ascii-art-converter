package image

case class RGBPixel(red: Int, green: Int, blue: Int):
  require(
    red >= 0 && red <= 255,
    s"Red channel must be in 0..255, got $red"
  )
  require(
    green >= 0 && green <= 255,
    s"Green channel must be in 0..255, got $green"
  )
  require(
    blue >= 0 && blue <= 255,
    s"Blue channel must be in 0..255, got $blue"
  )
