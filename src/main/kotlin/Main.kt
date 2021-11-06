import java.util.*
import kotlin.math.pow
import kotlin.Exception

enum class OperationName {
    LeftBracket,
    RightBracket,
    Minus,
    Plus,
    Multiply,
    Divide,
    Pow,
    UnaryMinus,
    UnaryPlus,
}

interface Operation<T>{
    val name: OperationName
    val priority: Int
    fun process(operands: Stack<T>)
}

class Addition(override val name: OperationName, override val priority: Int) : Operation<Double>{

    override fun process(operands: Stack<Double>) {
        if (operands.size < 2)
            throw Exception("Not enough operands")
        val operand1 = operands.pop()
        val operand2 = operands.pop()
        operands.push(operand2 + operand1)
    }

}

class Subtraction(override val name: OperationName, override val priority: Int) : Operation<Double>{

    override fun process(operands: Stack<Double>) {
        if (operands.size < 2)
            throw Exception("")
        val operand1 = operands.pop()
        val operand2 = operands.pop()
        operands.push(operand2 - operand1)
    }

}

class Multiplication(override val name: OperationName, override val priority: Int) : Operation<Double>{

    override fun process(operands: Stack<Double>) {
        if (operands.size < 2)
            throw Exception("Not enough operands")
        val operand1 = operands.pop()
        val operand2 = operands.pop()
        operands.push(operand2 * operand1)
    }

}

class Division(override val name: OperationName, override val priority: Int) : Operation<Double>{

    override fun process(operands: Stack<Double>) {
        if (operands.size < 2)
            throw Exception("Not enough operands")
        val operand1 = operands.pop()
        if (operand1 == 0.0)
            throw Exception("Trying to divide by zero")
        val operand2 = operands.pop()
        operands.push(operand2 / operand1)
    }

}

class Exponentiation(override val name: OperationName, override val priority: Int) : Operation<Double>{

    override fun process(operands: Stack<Double>) {
        if (operands.size < 2)
            throw Exception("Not enough operands")
        val operand1 = operands.pop()
        val operand2 = operands.pop()
        if (operand2 == 0.0 && operand1 < 0.0)
            throw Exception("Trying to raise zero to a negative degree")
        operands.push(operand2.pow(operand1))
    }

}

class UnaryMinus(override val name: OperationName, override val priority: Int) : Operation<Double>{

    override fun process(operands: Stack<Double>) {
        if(operands.size < 1)
            throw Exception("Not enough operands")
        val operand1 = operands.pop()
        operands.push(-operand1)
    }

}

class UnaryPlus(override val name: OperationName, override val priority: Int) : Operation<Double>{

    override fun process(operands: Stack<Double>) {
        if(operands.size < 1)
            throw Exception("Not enough operands")
        val operand1 = operands.pop()
        operands.push(operand1)
    }

}

class LeftBracket(override val name: OperationName, override val priority: Int) : Operation<Double>{

    override fun process(operands: Stack<Double>) {
        TODO("Not yet implemented")
    }

}

class RightBracket(override val name: OperationName, override val priority: Int) : Operation<Double>{

    override fun process(operands: Stack<Double>) {
        TODO("Not yet implemented")
    }

}



fun main() {
    try {
        print("Enter expression: ")
        val expression = readLine() ?: return
        val result = calculateExpression(expression)
        println("Result: $result")
    } catch (e: Exception) {
        println(e.message)
    }
}

fun calculateExpression(expression: String): Double {
    val operations = Stack<Operation<Double>>()
    val result = Stack<Double>()

    parseLexemes(expression, operations, result)
    processRemainingOperations(operations, result)

    return result.pop()
}

fun processRemainingOperations(operations: Stack<Operation<Double>>, result: Stack<Double>){
    while (!operations.isEmpty()){
        val currentOperation = operations.pop()
        currentOperation.process(result)
    }
    if(result.size != 1)
        throw Exception("Not enough operands")
}

fun parseLexemes(expression: String, operations: Stack<Operation<Double>>, result: Stack<Double>){
    val lexemes = getLexemes(expression)
    var mayUnary = true

    for (lexeme in lexemes) {
        val operation = convertStringToOperation(lexeme, mayUnary)
        if (operation != null) {
            if (operation.name == OperationName.LeftBracket) {
                processLeftBracket(operations, operation)
                mayUnary = true
            } else if (operation.name == OperationName.RightBracket) {
                processRightBracket(operations, result)
                mayUnary = false
            } else {
                processOtherOperation(operations, result, operation)
                mayUnary = true
            }
        } else {
            val operand = convertLexemeToOperand(lexeme)
            result.add(operand)
            mayUnary = false
        }
    }
}


fun processLeftBracket(operations: Stack<Operation<Double>>, operation: Operation<Double>){
    operations.push(operation)
}

fun processRightBracket(operations: Stack<Operation<Double>>, result: Stack<Double>){
    while (!operations.isEmpty() && operations.peek().name != OperationName.LeftBracket) {
        val operation = operations.pop()
        operation.process(result)
    }
    if (operations.isEmpty())
        throw Exception("Mismatch brackets")
    operations.pop()
}

fun processOtherOperation(operations: Stack<Operation<Double>>, result: Stack<Double>, operation: Operation<Double>){
    while (!operations.isEmpty() && operations.peek().priority >= operation.priority) {
        val currentOperation = operations.pop()
        currentOperation.process(result)
    }
    operations.push(operation)
}

fun convertLexemeToOperand(lexeme: String) : Double{
    if(lexeme == "pi")
        return Math.PI
    if(lexeme == "e")
        return Math.E
    return lexeme.toDouble()
}

fun getLexemes(expression: String): List<String> {
    var newExpression = ""
    val expressionWithoutSpace = expression.replace(" ", "")
    for (i in expressionWithoutSpace) {
        if (convertStringToOperation(i.toString()) != null)
            newExpression += " $i "
        else
            newExpression += i.toString()
    }
    return newExpression.trim().split(Regex(" +"))
}

fun convertStringToOperation(lexeme: String, mayUnary: Boolean = false): Operation<Double>? {
    if (lexeme == "-" && !mayUnary)
        return Subtraction(OperationName.Minus, 0)
    if (lexeme == "+" && !mayUnary)
        return Addition(OperationName.Plus, 0)
    if (lexeme == "*")
        return Multiplication(OperationName.Multiply, 1)
    if (lexeme == "/")
        return Division(OperationName.Divide, 1)
    if (lexeme == "^")
        return Exponentiation(OperationName.Pow, 1)
    if (lexeme == "(")
        return LeftBracket(OperationName.LeftBracket, -1)
    if (lexeme == ")")
        return RightBracket(OperationName.RightBracket, -1)
    if (lexeme == "-" && mayUnary)
        return UnaryMinus(OperationName.UnaryMinus, 2)
    if (lexeme == "+" && mayUnary)
        return UnaryPlus(OperationName.UnaryPlus, 2)
    return null
}



