package exporters

import image.*
import java.io.IOException
import java.nio.charset.StandardCharsets
import java.nio.file.Files
import java.nio.file.Path
import org.scalatest.funsuite.AnyFunSuite

class FileASCIIImageExporterTest extends AnyFunSuite:

  test("exportImage writes image content to file and closes stream") {
    val tempFile: Path = Files.createTempFile("exporter_test", ".txt")
    try
      val exporter: FileASCIIImageExporter =
        new FileASCIIImageExporter(tempFile)
      val image: Image[Char] =
        Image(Vector(Vector('A', 'B', 'C'), Vector('D', 'E', 'F')))
      exporter.exportImage(image)
      val content: String = Files.readString(tempFile, StandardCharsets.UTF_8)
      assert(content == "ABC\nDEF\n")
    finally Files.deleteIfExists(tempFile)
  }

  test("exportImage overwrites existing file content") {
    val tempFile: Path =
      Files.createTempFile("exporter_overwrite_test", ".txt")
    try
      Files.writeString(
        tempFile,
        "Initial content that will be overwritten\n",
        StandardCharsets.UTF_8
      )
      val exporter: FileASCIIImageExporter =
        new FileASCIIImageExporter(tempFile)
      val image: Image[Char] = Image(Vector(Vector('X', 'Y')))
      exporter.exportImage(image)
      val content: String = Files.readString(tempFile, StandardCharsets.UTF_8)
      assert(content == "XY\n")
    finally Files.deleteIfExists(tempFile)
  }

  test("instantiation with non-existent parent directory throws IOException") {
    val nonExistentPath: Path =
      Path.of("target/non_existent_exporter_dir_12345/output.txt")
    assertThrows[IOException] {
      new FileASCIIImageExporter(nonExistentPath)
    }
  }

  test("instantiation with directory path throws IOException") {
    val tempDir: Path = Files.createTempDirectory("exporter_dir_test")
    try
      assertThrows[IOException] {
        new FileASCIIImageExporter(tempDir)
      }
    finally
      Files.deleteIfExists(tempDir)
  }

  test("exportImage writes 2x3 image to file") {
    val tempFile: Path =
      Files.createTempFile("exporter_single_pixel_test", ".txt")
    try
      val exporter: FileASCIIImageExporter =
        new FileASCIIImageExporter(tempFile)
      val image: Image[Char] = Image(Vector(Vector('Z', 'S', 'U'), Vector('T', 'O', 'P')))
      exporter.exportImage(image)
      val content: String = Files.readString(tempFile, StandardCharsets.UTF_8)
      assert(content == "ZSU\nTOP\n")
    finally Files.deleteIfExists(tempFile)
  }
