
package object sandbox {

  sealed trait Number {
    type A
    def underlying: A
    def + (num : Number) : Number = ???
    def - (num : Number) : Number = ???
    def * (num : Number) : Number = ???
  }

  sealed trait SignedNumber extends Number

  sealed trait UnsignedNumber extends Number

  sealed trait UInt32 extends UnsignedNumber {
    override type A = Long
  }

  sealed trait UInt64 extends UnsignedNumber {
    override type A = BigInt
  }

  sealed trait Int32 extends SignedNumber {
    override type A = Int
  }

  sealed trait Int64 extends SignedNumber {
    override type A = Long
  }

}
