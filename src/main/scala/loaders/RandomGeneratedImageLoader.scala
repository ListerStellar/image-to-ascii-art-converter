package loaders

import image.*
import scala.util.Random

class RandomGeneratedImageLoader(private val random: Random)
    extends ImageLoader:
  require(Option(random).isDefined, "Random instance must not be null")

  override def load(): Image[RGBPixel] =
    val width: Int = random.between(10, 51)
    val height: Int = random.between(10, 51)
    val grid: Vector[Vector[RGBPixel]] =
      Vector.tabulate(height, width) { (_, _) =>
        val red: Int = random.between(0, 256)
        val green: Int = random.between(0, 256)
        val blue: Int = random.between(0, 256)
        RGBPixel(red, green, blue)
      }
    Image(grid)
