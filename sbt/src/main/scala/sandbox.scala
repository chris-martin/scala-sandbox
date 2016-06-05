
package object sandbox {

    sealed trait Number {
      type A
      type B
      def underlying: A
      def +(that: B): B
    }

    sealed trait UInt32 extends Number { x =>
      override type A = Long
      override type B = UInt32
      override def +(y: B): B = new UInt32 {
        // todo - naive implementation, doesn't check overflow
        override val underlying = x.underlying + y.underlying
      }
    }

    def main(args: Array[String]) {
      print((
        new UInt32 { def underlying = 3 } +
        new UInt32 { def underlying = 4 }
      ).underlying)
    }

}
