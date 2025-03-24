import java.io.File
import java.security.MessageDigest
import java.util.zip.DeflaterOutputStream
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
        "hash-object" -> hashObject(args)
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
            .substringAfter("\u0000")
            print(output)
        }
        else -> {
            print("Unimplemented : ${args[1]}")
        }
    }
}

fun hashObject(args: Array<String>){
    when(args[1]){
        "-w" -> {
            val file = File(args[2])
            val content = file.readBytes()
            val header = "blob ${content.size}\u0000"
            val blobContent = "$header${content.toString(Charsets.UTF_8)}"
            val hashedContent = blobContent.hash("SHA-1")
            println(hashedContent)

            val outputFile = File(".git/objects/${hashedContent.take(2)}/${hashedContent.drop(2)}")
            outputFile.parentFile.mkdirs()
            DeflaterOutputStream(outputFile.outputStream()).use {
                it.write(blobContent.toByteArray())
            }
        } else -> {
           print("Unimplemented : ${args[1]}")
        }
    }
}

@OptIn(ExperimentalStdlibApi::class)
private fun String.hash(algorithm: String): String {
    return MessageDigest.getInstance(algorithm).digest(this.toByteArray()).toHexString()
}