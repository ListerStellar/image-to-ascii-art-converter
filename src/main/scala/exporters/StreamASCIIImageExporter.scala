package exporters

import image.*
import java.io.OutputStream
import java.nio.charset.StandardCharsets

class StreamASCIIImageExporter(private val outputStream: OutputStream)
    extends ASCIIImageExporter:
  override def exportImage(image: Image[Char]): Unit =
    for y <- 0 until image.getHeight do
      val rowBuilder: StringBuilder = new StringBuilder
      for x <- 0 until image.getWidth do rowBuilder.append(image.getPixel(x, y))
      rowBuilder.append('\n')
      outputStream.write(rowBuilder.toString.getBytes(StandardCharsets.UTF_8))
    outputStream.flush()

  protected def close(): Unit =
    outputStream.close()
