package kava.tty

val ESC = 27.toChar()
val CSI = ESC + "["

data class Coordinates(
  val row: Int,
  val column: Int
)

class Ansi {
  
  fun csi(contents: String): String =
    CSI + contents
  
  fun saveCursor() {
    val 
  }
  
  fun dimensions(): Coordinates {
    val moveTo = csi("999;999H")
    TODO()
  }
}