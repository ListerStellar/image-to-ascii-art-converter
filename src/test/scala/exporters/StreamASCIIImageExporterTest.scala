package exporters

import image.*
import java.io.ByteArrayOutputStream
import java.io.IOException
import java.io.OutputStream
import java.nio.charset.StandardCharsets
import org.scalatest.funsuite.AnyFunSuite

class StreamASCIIImageExporterTest extends AnyFunSuite:

  class TrackingOutputStream extends OutputStream:
    var flushed: Boolean = false
    var closed: Boolean = false
    override def write(b: Int): Unit = ()
    override def write(b: Array[Byte], off: Int, len: Int): Unit = ()
    override def flush(): Unit =
      flushed = true
    override def close(): Unit =
      closed = true

  class FailingOutputStream extends OutputStream:
    override def write(b: Int): Unit =
      throw new IOException("Write failure")
    override def write(b: Array[Byte], off: Int, len: Int): Unit =
      throw new IOException("Write failure")

  test("exportImage exports 2x3 image correctly") {
    val baos: ByteArrayOutputStream = new ByteArrayOutputStream()
    val exporter: StreamASCIIImageExporter = new StreamASCIIImageExporter(baos)
    val image: Image[Char] =
      Image(Vector(Vector('A', 'B', 'C'), Vector('D', 'E', 'F')))
    exporter.exportImage(image)
    assert(baos.toString(StandardCharsets.UTF_8) == "ABC\nDEF\n")
  }

  test("exportImage exports 3x2 image correctly") {
    val baos: ByteArrayOutputStream = new ByteArrayOutputStream()
    val exporter: StreamASCIIImageExporter = new StreamASCIIImageExporter(baos)
    val image: Image[Char] =
      Image(Vector(Vector('1', '2'), Vector('3', '4'), Vector('5', '6')))
    exporter.exportImage(image)
    assert(baos.toString(StandardCharsets.UTF_8) == "12\n34\n56\n")
  }

  test("exportImage preserves special characters and whitespace") {
    val baos: ByteArrayOutputStream = new ByteArrayOutputStream()
    val exporter: StreamASCIIImageExporter = new StreamASCIIImageExporter(baos)
    val image: Image[Char] =
      Image(Vector(Vector('#', '@', ' '), Vector('~', '%', '*')))
    exporter.exportImage(image)
    assert(baos.toString(StandardCharsets.UTF_8) == "#@ \n~%*\n")
  }

  test("exportImage flushes output stream and does not close it") {
    val tracker: TrackingOutputStream = new TrackingOutputStream()
    val exporter: StreamASCIIImageExporter =
      new StreamASCIIImageExporter(tracker)
    val image: Image[Char] = Image(Vector(Vector('X')))
    exporter.exportImage(image)
    assert(tracker.flushed)
    assert(!tracker.closed)
  }

  test("exportImage propagates IOException on write failure") {
    val failingStream: FailingOutputStream = new FailingOutputStream()
    val exporter: StreamASCIIImageExporter =
      new StreamASCIIImageExporter(failingStream)
    val image: Image[Char] = Image(Vector(Vector('X')))
    assertThrows[IOException] {
      exporter.exportImage(image)
    }
  }
