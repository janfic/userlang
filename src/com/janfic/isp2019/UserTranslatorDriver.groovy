package com.janfic.isp2019

import com.janfic.isp2019.tst.*
import com.badlogic.ashley.core.Component
import com.badlogic.ashley.core.Entity

class UserTranslatorDriver {
    
    private static Scanner userInput
    private static def files
    
    /**
     *   Starts the program, reads if there are any arguments. 
     *   If there are not ask user for some. After that, determine if input is directory or file and compile accordingly. 
     **/
    static void main(String[] args) {
        files = []
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
                println compileFile(it)
        })
    }
    
    static void compileFile(File file) {
        if(file.isDirectory()) {
            for(f in file.listFiles(new USRFileFilter())) {
                compileFile(f)
            }
        }
        else {
            Writer.setFile(file)
            Translator.setFile(file)
            Translator.compile()
            Writer.write(Translator.getTranslation())
        }
    }
    
    static class USRFileFilter implements FileFilter {
        @Override
        boolean accept(File file) {
            return file.name.endsWith(".usr") || !file.name.contains(".")
        }
    }
}