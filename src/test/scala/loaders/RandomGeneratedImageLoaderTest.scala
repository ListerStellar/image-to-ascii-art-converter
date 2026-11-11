package loaders

import image.*
import org.scalatest.funsuite.AnyFunSuite

import scala.util.Random

class RandomGeneratedImageLoaderTest extends AnyFunSuite:

  test(
    "RandomGeneratedImageLoader generates image dimensions strictly between 10 and 50 inclusive"
  ) {
    val random: Random = Random(52)
    val loader: RandomGeneratedImageLoader = RandomGeneratedImageLoader(random)
    for _ <- 0 until 50 do
      val image: Image[RGBPixel] = loader.load()
      assert(image.getWidth >= 10 && image.getWidth <= 50)
      assert(image.getHeight >= 10 && image.getHeight <= 50)
  }

  test(
    "RandomGeneratedImageLoader generates RGB channels strictly between 0 and 255 inclusive"
  ) {
    val random: Random = Random(887)
    val loader: RandomGeneratedImageLoader = RandomGeneratedImageLoader(random)
    for _ <- 0 until 5 do
      val image: Image[RGBPixel] = loader.load()
      for
        y <- 0 until image.getHeight
        x <- 0 until image.getWidth
      do
        val pixel: RGBPixel = image.getPixel(x, y)
        assert(pixel.red >= 0 && pixel.red <= 255)
        assert(pixel.green >= 0 && pixel.green <= 255)
        assert(pixel.blue >= 0 && pixel.blue <= 255)
  }

  test(
    "RandomGeneratedImageLoader is 100% deterministic when seeded with the same random seed"
  ) {
    val seed: Int = 4
    val loader1: RandomGeneratedImageLoader =
      RandomGeneratedImageLoader(Random(seed))
    val loader2: RandomGeneratedImageLoader =
      RandomGeneratedImageLoader(Random(seed))

    val image1: Image[RGBPixel] = loader1.load()
    val image2: Image[RGBPixel] = loader2.load()

    assert(image1 == image2)
    assert(image1.getWidth == image2.getWidth)
    assert(image1.getHeight == image2.getHeight)
    for
      y <- 0 until image1.getHeight
      x <- 0 until image1.getWidth
    do assert(image1.getPixel(x, y) == image2.getPixel(x, y))

    val nextImage1: Image[RGBPixel] = loader1.load()
    val nextImage2: Image[RGBPixel] = loader2.load()
    assert(nextImage1 == nextImage2)
  }

  test(
    "RandomGeneratedImageLoader produces different images when seeded with different random seeds"
  ) {
    val loader1: RandomGeneratedImageLoader =
      RandomGeneratedImageLoader(Random(6))
    val loader2: RandomGeneratedImageLoader =
      RandomGeneratedImageLoader(Random(7))

    val image1: Image[RGBPixel] = loader1.load()
    val image2: Image[RGBPixel] = loader2.load()

    assert(image1 != image2)
  }

  test("RandomGeneratedImageLoader conforms to ImageLoader trait interface") {
    val loader: ImageLoader = RandomGeneratedImageLoader(Random(228))
    val image: Image[RGBPixel] = loader.load()
    assert(image.getWidth >= 10 && image.getWidth <= 50)
    assert(image.getHeight >= 10 && image.getHeight <= 50)
  }
