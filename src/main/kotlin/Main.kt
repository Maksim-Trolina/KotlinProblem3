import kotlin.math.pow
import kotlin.math.sqrt

interface Shape{
    fun calcArea(): Double
    fun calcPerimeter(): Double
}

class Circle : Shape{
    private val radius: Double

    override fun calcArea() : Double = Math.PI * radius.pow(2)
    override fun calcPerimeter(): Double = 2 * Math.PI * radius

    override fun toString(): String {
        return "Circle\nradius: $radius"
    }

    constructor(radius: Double){
        if(radius < 0.0)
            throw Exception()
        this.radius = radius
    }
}
class Square : Shape{
    private val side: Double

    override fun calcArea(): Double = side.pow(2)
    override fun calcPerimeter(): Double = 4 * side

    override fun toString(): String {
        return "Square\nside: $side"
    }

    constructor(side: Double){
        if(side < 0.0)
            throw Exception()
        this.side = side
    }
}
class Rectangle : Shape{
    private val height: Double
    private val width: Double

    override fun calcArea(): Double = height * width
    override fun calcPerimeter(): Double = 2 * (height + width)

    override fun toString(): String {
        return "Rectangle\nheight: $height\nwidth: $width"
    }

    constructor(height: Double, width: Double){
        if(height < 0.0 || width < 0.0)
            throw Exception()
        this.height = height
        this.width = width
    }
}
class Triangle : Shape{
    private val side1: Double
    private val side2: Double
    private val side3: Double

    override fun calcArea(): Double {
        val semiPerimeter = calcPerimeter() / 2
        val differenceNumber1 = semiPerimeter - side1
        val differenceNumber2 = semiPerimeter - side2
        val differenceNumber3 = semiPerimeter - side3

        return sqrt(semiPerimeter * differenceNumber1 * differenceNumber2 * differenceNumber3)
    }

    override fun calcPerimeter(): Double = side1 + side2 + side3

    override fun toString(): String {
        return "Triangle\nside1: $side1\nside2: $side2\nside3: $side3"
    }

    constructor(side1: Double, side2: Double, side3: Double){
        if(side1 < 0.0 || side2 < 0.0 || side3 < 0.0)
            throw Exception()
        if(side1 + side2 <= side3 || side1 + side3 <= side2 || side2 + side3 <= side1)
            throw Exception()
        this.side1 = side1
        this.side2 = side2
        this.side3 = side3
    }
}

interface ShapeFactory {
    fun createCircle(radius: Double): Circle
    fun createSquare(side: Double): Square
    fun createRectangle(height: Double, width: Double): Rectangle
    fun createTriangle(side1: Double, side2: Double, side3: Double): Triangle

    fun createRandomCircle(): Circle
    fun createRandomSquare(): Square
    fun createRandomRectangle(): Rectangle
    fun createRandomTriangle(): Triangle

    fun createRandomShape(): Shape
}

class ShapeFactorImpl : ShapeFactory {
    override fun createCircle(radius: Double): Circle = Circle(radius)

    override fun createSquare(side: Double): Square = Square(side)

    override fun createRectangle(height: Double, width: Double): Rectangle = Rectangle(height, width)

    override fun createTriangle(side1: Double, side2: Double, side3: Double): Triangle = Triangle(side1, side2, side3)

    override fun createRandomSquare(): Square {
        val side = (1..100).random()
        return Square(side.toDouble())
    }

    override fun createRandomCircle(): Circle {
        val radius = (1..100).random()
        return Circle(radius.toDouble())
    }

    override fun createRandomRectangle(): Rectangle {
        val height = (1..100).random()
        val width = (1..100).random()
        return Rectangle(height.toDouble(), width.toDouble())
    }

    override fun createRandomTriangle(): Triangle {
        val side1 = (1..100).random()
        val side2 = (1..100).random()
        val side3 = side1 + side2 - 1
        return Triangle(side1.toDouble(), side2.toDouble(), side3.toDouble())
    }

    override fun createRandomShape(): Shape {
        val figureNumber = (1..4).random()
        if(figureNumber == 1)
            return createRandomCircle()
        if(figureNumber == 2)
            return createRandomSquare()
        if(figureNumber == 3)
            return createRandomRectangle()
        return createRandomTriangle()
    }
}

fun main() {

    val factoryShape = ShapeFactorImpl()
    val listOfShapes = crateListOfShapes(factoryShape)
    val shapeMinArea = getShapeWithMinArea(listOfShapes)
    val shapeMaxArea = getShapeWithMaxArea(listOfShapes)
    val shapeMinPerimeter = getShapeWithMinPerimeter(listOfShapes)
    val shapeMaxPerimeter = getShapeWithMaxPerimeter(listOfShapes)
    val sumArea = getSumAreaOfShapes(listOfShapes)
    val sumPerimeter = getSumPerimeterOfShapes(listOfShapes)

    printAllShapes(listOfShapes)

    println("Sum area: $sumArea")
    println("Sum perimeter: $sumPerimeter")
    println("Shape with max area: $shapeMaxArea")
    println("Shape with min area: $shapeMinArea")
    println("Shape with max perimeter: $shapeMaxPerimeter")
    println("Shape with min perimeter: $shapeMinPerimeter")
}

fun crateListOfShapes(factoryShape: ShapeFactory) : ArrayList<Shape> {
    val listOfShapes = arrayListOf<Shape>()
    for(i in 1..8){
        val shape = factoryShape.createRandomShape()
        listOfShapes.add(shape)
    }
    return listOfShapes
}

fun printAllShapes(listOfShapes: ArrayList<Shape>){
    for(shape in listOfShapes)
        println(shape)
}

fun getSumAreaOfShapes(listOfShapes: ArrayList<Shape>) : Double{
    var sumArea = 0.0
    for(shape in listOfShapes)
        sumArea += shape.calcArea()
    return sumArea
}

fun getSumPerimeterOfShapes(listOfShapes: ArrayList<Shape>) : Double{
    var sumPerimeter = 0.0
    for(shape in listOfShapes)
        sumPerimeter += shape.calcPerimeter()
    return sumPerimeter
}

fun getShapeWithMinArea(listOfShapes: ArrayList<Shape>) : Shape? {
    var shapeWithMinArea: Shape? = null
    for(shape in listOfShapes)
        if(shapeWithMinArea == null || shapeWithMinArea.calcArea() > shape.calcArea())
            shapeWithMinArea = shape
    return shapeWithMinArea
}

fun getShapeWithMaxArea(listOfShapes: ArrayList<Shape>) : Shape? {
    var shapeWithMaxArea: Shape? = null
    for(shape in listOfShapes)
        if(shapeWithMaxArea == null || shapeWithMaxArea.calcArea() < shape.calcArea())
            shapeWithMaxArea = shape
    return shapeWithMaxArea
}

fun getShapeWithMinPerimeter(listOfShapes: ArrayList<Shape>) : Shape? {
    var shapeWithMinPerimeter: Shape? = null
    for(shape in listOfShapes)
        if(shapeWithMinPerimeter == null || shapeWithMinPerimeter.calcPerimeter() > shape.calcPerimeter())
            shapeWithMinPerimeter = shape
    return shapeWithMinPerimeter
}

fun getShapeWithMaxPerimeter(listOfShapes: ArrayList<Shape>) : Shape? {
    var shapeWithMaxPerimeter: Shape? = null
    for(shape in listOfShapes)
        if(shapeWithMaxPerimeter == null || shapeWithMaxPerimeter.calcPerimeter() < shape.calcPerimeter())
            shapeWithMaxPerimeter = shape
    return shapeWithMaxPerimeter
}





