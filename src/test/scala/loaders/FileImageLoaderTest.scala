package loaders

import image.*
import org.scalatest.funsuite.AnyFunSuite

import java.awt.image.BufferedImage
import java.nio.file.{DirectoryStream, Files, Path}
import javax.imageio.ImageIO

class FileImageLoaderTest extends AnyFunSuite:

  private def withTempFile[T](prefix: String, suffix: String)(
    testBlock: Path => T
  ): T =
    val path: Path = Files.createTempFile(prefix, suffix)
    try testBlock(path)
    finally Files.deleteIfExists(path)

  private def withTempDirectory[T](prefix: String)(
    testBlock: Path => T
  ): T =
    val dir: Path = Files.createTempDirectory(prefix)
    try testBlock(dir)
    finally
      val files: DirectoryStream[Path] = Files.newDirectoryStream(dir)
      try
        val iterator: java.util.Iterator[Path] = files.iterator()
        while iterator.hasNext do Files.deleteIfExists(iterator.next())
      finally files.close()
      Files.deleteIfExists(dir)

  test(
    "PngFileImageLoader loads valid PNG image with correct dimensions, coordinates, and pixel values"
  ) {
    withTempFile("test_png_loader", ".png") { path =>
      val width: Int = 3
      val height: Int = 2
      val bufferedImage: BufferedImage =
        BufferedImage(width, height, BufferedImage.TYPE_INT_RGB)
      bufferedImage.setRGB(0, 0, (10 << 16) | (20 << 8) | 30)
      bufferedImage.setRGB(1, 0, (40 << 16) | (50 << 8) | 60)
      bufferedImage.setRGB(2, 0, (70 << 16) | (80 << 8) | 90)
      bufferedImage.setRGB(0, 1, (100 << 16) | (110 << 8) | 120)
      bufferedImage.setRGB(1, 1, (130 << 16) | (140 << 8) | 150)
      bufferedImage.setRGB(2, 1, (160 << 16) | (170 << 8) | 180)
      ImageIO.write(bufferedImage, "png", path.toFile)

      val loader: ImageLoader = PngFileImageLoader(path)
      val loadedImage: Image[RGBPixel] = loader.load()

      assert(loadedImage.getWidth == 3)
      assert(loadedImage.getHeight == 2)
      assert(loadedImage.getPixel(0, 0) == RGBPixel(10, 20, 30))
      assert(loadedImage.getPixel(1, 0) == RGBPixel(40, 50, 60))
      assert(loadedImage.getPixel(2, 0) == RGBPixel(70, 80, 90))
      assert(loadedImage.getPixel(0, 1) == RGBPixel(100, 110, 120))
      assert(loadedImage.getPixel(1, 1) == RGBPixel(130, 140, 150))
      assert(loadedImage.getPixel(2, 1) == RGBPixel(160, 170, 180))
    }
  }

  test("PngFileImageLoader accepts case-insensitive uppercase .PNG extension") {
    withTempFile("test_uppercase", ".PNG") { path =>
      val bufferedImage: BufferedImage =
        BufferedImage(4, 4, BufferedImage.TYPE_INT_RGB)
      ImageIO.write(bufferedImage, "png", path.toFile)

      val loader: ImageLoader = PngFileImageLoader(path)
      val loadedImage: Image[RGBPixel] = loader.load()
      assert(loadedImage.getWidth == 4)
      assert(loadedImage.getHeight == 4)
    }
  }

  test("JpgFileImageLoader loads valid JPG image with .jpg extension") {
    withTempFile("test_jpg_loader", ".jpg") { path =>
      val bufferedImage: BufferedImage =
        BufferedImage(12, 18, BufferedImage.TYPE_INT_RGB)
      ImageIO.write(bufferedImage, "jpg", path.toFile)

      val loader: ImageLoader = JpgFileImageLoader(path)
      val loadedImage: Image[RGBPixel] = loader.load()
      assert(loadedImage.getWidth == 12)
      assert(loadedImage.getHeight == 18)
    }
  }

  test("JpgFileImageLoader loads valid JPG image with .jpeg extension") {
    withTempFile("test_jpeg_loader", ".jpeg") { path =>
      val bufferedImage: BufferedImage =
        BufferedImage(20, 25, BufferedImage.TYPE_INT_RGB)
      ImageIO.write(bufferedImage, "jpg", path.toFile)

      val loader: ImageLoader = JpgFileImageLoader(path)
      val loadedImage: Image[RGBPixel] = loader.load()
      assert(loadedImage.getWidth == 20)
      assert(loadedImage.getHeight == 25)
    }
  }

  test("JpgFileImageLoader accepts case-insensitive uppercase extensions") {
    withTempFile("test_upper_jpg", ".JPG") { path =>
      val bufferedImage: BufferedImage =
        BufferedImage(8, 8, BufferedImage.TYPE_INT_RGB)
      ImageIO.write(bufferedImage, "jpg", path.toFile)

      val loader: ImageLoader = JpgFileImageLoader(path)
      assert(loader.load().getWidth == 8)
    }
    withTempFile("test_upper_jpeg", ".JPEG") { path =>
      val bufferedImage: BufferedImage =
        BufferedImage(6, 6, BufferedImage.TYPE_INT_RGB)
      ImageIO.write(bufferedImage, "jpg", path.toFile)

      val loader: ImageLoader = JpgFileImageLoader(path)
      assert(loader.load().getWidth == 6)
    }
  }

  test(
    "PngFileImageLoader throws IllegalArgumentException for non-existent file on load"
  ) {
    val nonExistentPath: Path = Path.of("target/non_existent_image_file.png")
    val loader: PngFileImageLoader = PngFileImageLoader(nonExistentPath)
    assertThrows[IllegalArgumentException](loader.load())
  }

  test(
    "JpgFileImageLoader throws IllegalArgumentException for non-existent file on load"
  ) {
    val nonExistentPath: Path = Path.of("target/non_existent_image_file.jpg")
    val loader: JpgFileImageLoader = JpgFileImageLoader(nonExistentPath)
    assertThrows[IllegalArgumentException](loader.load())
  }

  test(
    "File loaders throw IllegalArgumentException when target path is a directory"
  ) {
    withTempDirectory("dir_test") { baseDir =>
      val pngDir: Path = Files.createDirectory(baseDir.resolve("subfolder.png"))
      val pngLoader: PngFileImageLoader = PngFileImageLoader(pngDir)
      assertThrows[IllegalArgumentException](pngLoader.load())

      val jpgDir: Path = Files.createDirectory(baseDir.resolve("subfolder.jpg"))
      val jpgLoader: JpgFileImageLoader = JpgFileImageLoader(jpgDir)
      assertThrows[IllegalArgumentException](jpgLoader.load())
    }
  }

  test(
    "PngFileImageLoader throws IllegalArgumentException for invalid extensions at construction"
  ) {
    assertThrows[IllegalArgumentException](
      PngFileImageLoader(Path.of("image.jpg"))
    )
    assertThrows[IllegalArgumentException](
      PngFileImageLoader(Path.of("image.jpeg"))
    )
    assertThrows[IllegalArgumentException](
      PngFileImageLoader(Path.of("image.gif"))
    )
    assertThrows[IllegalArgumentException](
      PngFileImageLoader(Path.of("image.txt"))
    )
    assertThrows[IllegalArgumentException](PngFileImageLoader(Path.of("image")))
    assertThrows[IllegalArgumentException](
      PngFileImageLoader(Path.of("image.png.bak"))
    )
  }

  test(
    "JpgFileImageLoader throws IllegalArgumentException for invalid extensions at construction"
  ) {
    assertThrows[IllegalArgumentException](
      JpgFileImageLoader(Path.of("image.png"))
    )
    assertThrows[IllegalArgumentException](
      JpgFileImageLoader(Path.of("image.gif"))
    )
    assertThrows[IllegalArgumentException](
      JpgFileImageLoader(Path.of("image.txt"))
    )
    assertThrows[IllegalArgumentException](JpgFileImageLoader(Path.of("image")))
    assertThrows[IllegalArgumentException](
      JpgFileImageLoader(Path.of("image.jpg.bak"))
    )
  }

  test(
    "PngFileImageLoader throws IllegalArgumentException when loading corrupted or non-image file"
  ) {
    withTempFile("corrupted_png", ".png") { path =>
      Files.writeString(path, "not a png image file")
      val loader: PngFileImageLoader = PngFileImageLoader(path)
      assertThrows[IllegalArgumentException](loader.load())
    }
  }

  test(
    "JpgFileImageLoader throws IllegalArgumentException when loading corrupted or non-image file"
  ) {
    withTempFile("corrupted_jpg", ".jpg") { path =>
      Files.writeString(path, "not a jpg image file")
      val loader: JpgFileImageLoader = JpgFileImageLoader(path)
      assertThrows[IllegalArgumentException](loader.load())
    }
  }

  test(
    "PngFileImageLoader throws IllegalArgumentException when loading empty file"
  ) {
    withTempFile("empty_png", ".png") { path =>
      val loader: PngFileImageLoader = PngFileImageLoader(path)
      assertThrows[IllegalArgumentException](loader.load())
    }
  }
