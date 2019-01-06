package com.janfic.isp2019

import com.janfic.isp2019.tst.*

class UserTranslatorDriver {
    
    private static Scanner userInput
    private static def files
    
    /**
     *   Starts the program, reads if there are any arguments. 
     *   If there are not ask user for some. After that, determine if input is directory or file and compile accordingly. 
     **/
    static void main(String[] args) {
        files = []
        println new RandomInteger([min:3, max: 10])()
        if(!args) {
            println "Enter file names which to translate then type 'end':"
            userInput = new Scanner(System.in)
            while({
                    def file = new File(userInput.nextLine())
                    if(!file.name.equals("end")) {
                        if(file.exists()) {
                            files.add(file)
                        }
                        else{
                            println "Could not find file " + file.name
                        }
                        println "Current compile list: $files"
                        true
                    }
                    else {
                        false
                    }
                }()) continue
            println "Translating the files:"
            files.each({println it})
        }
        else {
            def file = new File(args[0])
            if(file.exists()) {
                files.add(file)
            }
            else {
                println "Cannot not find file " + file.name
                return
            }
        }
        files.each({
                println "Compiling  $it.name.."
                Writer.setFile(it)
                Translator.setFile(it)
                Translator.compile()
                println "..Completed compilation"
                println "Translating.."
                Writer.write(Translator.getTranslation())
                println "..translation found in $it.name"
            })
    }
}