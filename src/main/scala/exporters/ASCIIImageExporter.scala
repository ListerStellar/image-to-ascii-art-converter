package exporters

import image.*

trait ASCIIImageExporter:
  def exportImage(image: Image[Char]): Unit
