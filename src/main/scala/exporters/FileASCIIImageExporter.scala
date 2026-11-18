package exporters

import image.*
import java.io.FileOutputStream
import java.nio.file.Path

class FileASCIIImageExporter(private val exportPath: Path)
    extends StreamASCIIImageExporter(new FileOutputStream(exportPath.toFile)):
  override def exportImage(image: Image[Char]): Unit =
    try super.exportImage(image)
    finally close()
