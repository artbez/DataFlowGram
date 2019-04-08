package se.iimetra.dataflowgram

fun main(args: Array<String>) {
    println("Enter git repository")
    val repo = readLine()!!
    val gitHelper = GitHelper(repo)
}