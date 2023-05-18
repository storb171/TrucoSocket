@echo off
start cmd /k "chcp 65001>nul & javac -g -cp gson-2.8.0.jar *.java && java -cp gson-2.8.0.jar;. Server"
start cmd /k "chcp 65001>nul & java -cp gson-2.8.0.jar;. Client"
start cmd /k "chcp 65001>nul & java -cp gson-2.8.0.jar;. Client"
start cmd /k "chcp 65001>nul & java -cp gson-2.8.0.jar;. Client"
start cmd /k "chcp 65001>nul & java -cp gson-2.8.0.jar;. Client"