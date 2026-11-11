package loaders

import image.*
import java.awt.image.BufferedImage
import java.nio.file.{Files, Path}
import javax.imageio.ImageIO

trait FileImageLoader(protected val imagePath: Path) extends ImageLoader:
  require(Option(imagePath).isDefined, "Image path must not be null")

  override def load(): Image[RGBPixel] =
    validateFile()
    val bufferedImage: BufferedImage = readBufferedImage()
    convertToImage(bufferedImage)

  protected def fileNameLowerCase: Option[String] =
    Option(imagePath)
      .flatMap(p => Option(p.getFileName))
      .map(_.toString.toLowerCase)

  private def validateFile(): Unit =
    if !Files.exists(imagePath) then
      throw IllegalArgumentException(s"File does not exist: $imagePath")
    if !Files.isRegularFile(imagePath) then
      throw IllegalArgumentException(s"Path is not a regular file: $imagePath")
    if !Files.isReadable(imagePath) then
      throw IllegalArgumentException(s"File is not readable: $imagePath")

  private def readBufferedImage(): BufferedImage =
    val maybeImage: Option[BufferedImage] =
      try Option(ImageIO.read(imagePath.toFile))
      catch
        case e: IllegalArgumentException => throw e
        case e: Exception                =>
          throw IllegalArgumentException(
            s"Failed to read image file: $imagePath",
            e
          )
    maybeImage match
      case Some(image) => image
      case None        =>
        throw IllegalArgumentException(
          s"Unsupported or corrupted image file: $imagePath"
        )

  private def convertToImage(bufferedImage: BufferedImage): Image[RGBPixel] =
    val width: Int = bufferedImage.getWidth
    val height: Int = bufferedImage.getHeight
    require(
      width > 0 && height > 0,
      s"Image dimensions must be positive, got ${width}x$height"
    )
    val grid: Vector[Vector[RGBPixel]] =
      Vector.tabulate(height, width) { (y, x) =>
        val rgb: Int = bufferedImage.getRGB(x, y)
        val red: Int = (rgb >> 16) & 0xff
        val green: Int = (rgb >> 8) & 0xff
        val blue: Int = rgb & 0xff
        RGBPixel(red, green, blue)
      }
    Image(grid)
