package object stackoverflow34112892 {

  import scala.collection.JavaConversions._

  def myJavaFun(in: java.util.Map[Object, java.lang.Double]): Unit = {}

  val myMap = new java.util.HashMap[Object, Double]()

  myJavaFun(mapAsJavaMap(myMap.mapValues(x => x)))
}
