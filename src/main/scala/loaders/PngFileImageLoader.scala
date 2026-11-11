package loaders

import java.nio.file.Path

class PngFileImageLoader(imagePath: Path) extends FileImageLoader(imagePath):
  require(
    fileNameLowerCase.exists(name => name.endsWith(".png")),
    s"File path does not have a valid PNG extension (.png): $imagePath"
  )
