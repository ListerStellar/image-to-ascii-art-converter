package loaders

import java.nio.file.Path

class JpgFileImageLoader(imagePath: Path) extends FileImageLoader(imagePath):
  require(
    fileNameLowerCase.exists(name =>
      name.endsWith(".jpg") || name.endsWith(".jpeg")
    ),
    s"File path does not have a valid JPG extension (.jpg, .jpeg): $imagePath"
  )
