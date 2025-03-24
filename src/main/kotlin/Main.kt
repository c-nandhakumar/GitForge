import java.io.File
import java.util.zip.InflaterInputStream
import kotlin.system.exitProcess

fun main(args: Array<String>) {
    // You can use print statements as follows for debugging, they'll be visible when running tests.
    System.err.println("Logs from your program will appear here!")

    if (args.isEmpty()) {
        println("Usage: your_program.sh <command> [<args>]")
        exitProcess(1)
    }


    when (args[0]){
        "init" -> gitInit()
        "cat-file" -> catFile(args)
        else -> {
            println("Unknown command: ${args[0]}")
            exitProcess(1)
        }
    }
}


fun gitInit(){
    val gitDir = File(".git")
    gitDir.mkdir()
    File(gitDir, "objects").mkdir()
    File(gitDir, "refs").mkdir()
    File(gitDir, "HEAD").writeText("ref: refs/heads/master\n")

    println("Initialized git directory")
}

fun catFile(args: Array<String>) {
    when(args[1]) {
        "-p" ->{
            val blob = File(".git/objects/${args[2].take(2)}/${args[2].drop(2)}")
            val output = InflaterInputStream(blob.inputStream()).reader().readText()
            .substringAfter("0".toByte().toString(), "")
            print(output)
        }
        else -> {
            print("Unimplemented : ${args[1]}")
        }
    }
}
