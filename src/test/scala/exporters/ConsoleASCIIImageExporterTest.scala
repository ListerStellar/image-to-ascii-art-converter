package exporters

import image.*
import java.io.ByteArrayOutputStream
import java.io.PrintStream
import java.nio.charset.StandardCharsets
import org.scalatest.funsuite.AnyFunSuite

class ConsoleASCIIImageExporterTest extends AnyFunSuite:

  test("exportImage writes to standard output correctly") {
    val originalOut: PrintStream = System.out
    val baos: ByteArrayOutputStream = new ByteArrayOutputStream()
    val redirectedOut: PrintStream = new PrintStream(baos)
    try
      System.setOut(redirectedOut)
      val exporter: ConsoleASCIIImageExporter = new ConsoleASCIIImageExporter()
      val image: Image[Char] =
        Image(Vector(Vector('H', 'i'), Vector('!', '?')))
      exporter.exportImage(image)
      assert(baos.toString(StandardCharsets.UTF_8) == "Hi\n!?\n")
    finally System.setOut(originalOut)
  }

  test("exportImage does not close standard output") {
    val originalOut: PrintStream = System.out
    val baos: ByteArrayOutputStream = new ByteArrayOutputStream()
    val redirectedOut: PrintStream = new PrintStream(baos)
    try
      System.setOut(redirectedOut)
      val exporter: ConsoleASCIIImageExporter = new ConsoleASCIIImageExporter()
      val image: Image[Char] = Image(Vector(Vector('O', 'K')))
      exporter.exportImage(image)
      System.out.print("NextLine\n")
      assert(baos.toString(StandardCharsets.UTF_8) == "OK\nNextLine\n")
    finally System.setOut(originalOut)
  }

  test("exportImage writes 2x3 image to standard output correctly") {
    val originalOut: PrintStream = System.out
    val baos: ByteArrayOutputStream = new ByteArrayOutputStream()
    val redirectedOut: PrintStream = new PrintStream(baos)
    try
      System.setOut(redirectedOut)
      val exporter: ConsoleASCIIImageExporter = new ConsoleASCIIImageExporter()
      val image: Image[Char] = Image(Vector(Vector('A', ' ', 'C'), Vector('2', '1', 'E')))
      exporter.exportImage(image)
      assert(baos.toString(StandardCharsets.UTF_8) == "A C\n21E\n")
    finally System.setOut(originalOut)
  }
